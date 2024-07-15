package com.liangalien.kt.kettle;

import com.liangalien.kt.dao.ProjectVarDAO;
import com.liangalien.kt.dao.TaskDAO;
import com.liangalien.kt.dao.RunnerDAO;
import com.liangalien.kt.dto.ProjectVarDTO;
import com.liangalien.kt.dto.TaskDTO;
import com.liangalien.kt.dto.RunnerDTO;
import com.liangalien.kt.util.*;
import lombok.extern.slf4j.Slf4j;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.filerep.KettleFileRepository;
import org.pentaho.di.repository.filerep.KettleFileRepositoryMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Component
public class Runner {
    @Autowired
    private RunnerDAO runnerDao;

    @Autowired
    private TaskDAO taskDao;

    @Autowired
    private ProjectVarDAO projectVarDao;

    @Autowired
    private CryptoUtil cryptoUtil;


    @Value("${kettle.repo}")
    private String localRepoPath;

    @Value("${kettle.plugins}")
    private String pluginsPath;

    private Map<String, KettleFileRepository> projectRepoMap = new HashMap<>();

    private static Map<String, Trans> transMap = new HashMap<>();

    private static Map<String, KettleJob> jobMap = new HashMap<>();

    @PostConstruct
    public void init() {
        try {
            loadPlugins();
            kettleEnvInit();
        } catch (KettleException e) {
            throw new RuntimeException(e);
        }
    }

    public void kettleEnvInit() throws KettleException {
        System.setProperty(Const.KETTLE_DISABLE_CONSOLE_LOGGING, "Y");
        KettleEnvironment.init(true);
        log.info("Kettle environment initialized");
    }


    public void loadPlugins() {
        if (pluginsPath != null) {
            StepPluginType.getInstance().getPluginFolders().add(
                    new PluginFolder(pluginsPath, false, true)
            );
            log.info("Kettle plugin folder loaded");
        }
    }

    public KettleFileRepository connectLocalRepo(BigInteger projectId, String projectKey) throws KettleException {
        KettleFileRepository projectRepo = projectRepoMap.get(projectKey);
        if (projectRepo != null) {
            return projectRepo;
        }

        KettleFileRepositoryMeta fileRepMeta = new KettleFileRepositoryMeta(
                projectId.toString(), projectKey, "",  localRepoPath + "/" + projectKey);
        KettleFileRepository repo = new KettleFileRepository();
        repo.init(fileRepMeta);
        repo.connect("", "");
        log.info("连接项目文件资源库成功：{}", projectKey);

        projectRepoMap.put(projectKey, repo);

        return repo;
    }


    public void execute(TaskDTO taskDto, RunnerTrigger trigger, String createBy) {
        LogListener logListener = null;

        String name = taskDto.getRepoName().replace(".ktr", "").replace(".kjb", "");
        BigInteger runnerId = null, taskId = taskDto.getId();
        String runnerName = null;

        RunnerStatus endStatus = RunnerStatus.STOPPED;
        try {
            KettleFileRepository projectRepo = connectLocalRepo(taskDto.getProjectId(), taskDto.getProjectKey());

            runnerId = whenReady(taskId, trigger, createBy);
            runnerName = getRunnerKey(taskDto.getKey(), runnerId);
            LogListener.setLogName(runnerName);


            updateStatus(taskId, runnerId, RunnerStatus.READY);
            LogListener.writeLog(name + " - 已就绪");

            if (RunnerType.TRANS.getValue().equals(taskDto.getRepoType())) {
                Trans lastTrans = transMap.get(taskDto.getKey());
                if (lastTrans != null && !lastTrans.isFinishedOrStopped()) {
                    LogListener.writeLog(name + " - 上次执行未结束，无法执行", LogLevel.ERROR);
                    return;
                }

                TransMeta transMeta = projectRepo.loadTransformation(name, null, null,
                        true,  null);
                Trans trans = new Trans(transMeta);
                trans.setLogLevel(getLogLevelByValue(taskDto.getLogLevel()));
                trans.setThreadName(runnerName);
                int varSize = setVariable(taskDto.getProjectId(), trans);
                LogListener.writeLog(String.format("%s - 成功设置%s个项目变量", name, varSize));

                logListener = new LogListener(runnerName);
                KettleLogStore.getAppender().addLoggingEventListener(logListener);

                transMap.put(taskDto.getKey(), trans);

                LogListener.writeLog(name + " - 准备执行");
                trans.prepareExecution(null);

                LogListener.writeLog(name + " - 开始执行");
                updateStatus(taskId, runnerId, RunnerStatus.RUNNING);
                trans.startThreads();
                trans.waitUntilFinished();

                if (trans.isFinishedOrStopped()) {
                    // 是否被用户强制停止
                    if (trans.getVariable("stopFlag") != null) {
                        endStatus = RunnerStatus.STOPPED;
                    } else {
                        endStatus = KettleUtil.getStatus(trans.getStatus());
                    }
                } else {
                    endStatus = RunnerStatus.FAILED;
                }
            } else {
                KettleJob lastJob = jobMap.get(taskDto.getKey());
                if (lastJob != null && !(lastJob.isFinished() || lastJob.isStopped())) {
                    LogListener.writeLog(name + " - 上次执行未结束，无法执行", LogLevel.ERROR);
                    return;
                }


                JobMeta jobMeta = projectRepo.loadJob(name, null, null, null);
                jobMeta.setRepositoryDirectory(null);

                KettleJob job  = new KettleJob(projectRepo, jobMeta);
                job.setVariable(Const.INTERNAL_VARIABLE_ENTRY_CURRENT_DIRECTORY, projectRepo.getRepositoryMeta().getBaseDirectory());
                job.setLogLevel(getLogLevelByValue(taskDto.getLogLevel()));
                job.setName(runnerName);
                int varSize = setVariable(taskDto.getProjectId(), job);
                LogListener.writeLog(String.format("%s - 成功设置%s个项目变量", name, varSize));


                logListener = new LogListener(runnerName);
                KettleLogStore.getAppender().addLoggingEventListener(logListener);

                jobMap.put(taskDto.getKey(), job);

                LogListener.writeLog(name + " - 开始执行");
                job.start();

                updateStatus(taskId, runnerId, RunnerStatus.RUNNING);

                job.waitUntilFinished();

                if (job.isStopped() || job.isFinished()) {
                    // 是否被用户强制停止
                    if (job.getVariable("stopFlag") != null) {
                        endStatus = RunnerStatus.STOPPED;
                    } else {
                        endStatus = KettleUtil.getStatus(job.getStatus());
                    }
                } else {
                    endStatus = RunnerStatus.FAILED;
                }
            }
        } catch (Exception e) {
            endStatus = RunnerStatus.ERROR;
            LogListener.writeLog(name + " - 执行异常：" + e.getMessage(), LogLevel.ERROR);

            log.error("Execute trans failed", e);
            throw new RuntimeException(e);
        } finally {
            log.info(String.format("任务%s(id=%s, name=%s)执行结束，状态为：%s",
                    taskDto.getRepoType(), taskDto.getId(), name, endStatus.getDesc()));

            LogListener.writeLog(name + " - 执行结束，状态为：" + endStatus.getDesc());
            if (runnerName != null) {
                LogListener.clearLog(runnerName);
            }

            if (logListener != null) {
                KettleLogStore.getAppender().removeLoggingEventListener(logListener);
            }

            whenEnd(taskId, runnerId, endStatus);
        }
    }

    /**
     * 强制停止转换
     * @param taskKey 任务KEY
     * @return 停止之前是否已经完成了
     */
    public static boolean transStop(String taskKey) {
        Trans trans = transMap.get(taskKey);
        if (trans == null) {
            return true;
        }

        if (!trans.isFinishedOrStopped()) {
            LogListener.setLogName(trans.getThreadName());
            LogListener.writeLog("用户强制停止当前转换！", LogLevel.ERROR);
            LogListener.clearLog(trans.getThreadName());

            trans.setVariable("stopFlag", "Y");
            trans.killAll();

            return false;
        }

        return true;
    }

    /**
     * 强制停止作业
     * @param taskKey 任务KEY
     * @return 停止之前是否已经完成了
     */
    public static boolean jobStop(String taskKey) {
        Job job = jobMap.get(taskKey);
        if (job == null) {
            return true;
        }

        if (!job.isFinished() && !job.isStopped()) {
            LogListener.setLogName(job.getName());
            LogListener.writeLog("用户强制停止当前作业！", LogLevel.ERROR);
            LogListener.clearLog(job.getName());

            job.setVariable("stopFlag", "Y");
            job.stopAll();

            return false;
        }

        return true;
    }

    @Transactional
    public void updateStatus(BigInteger taskId, BigInteger runnerId, RunnerStatus status) {
        TaskDTO taskDto = new TaskDTO();
        RunnerDTO runnerDto = new RunnerDTO();

        taskDto.setId(taskId);
        taskDto.setStatus(status.getValue());
        taskDao.update(taskDto);

        runnerDto.setId(runnerId);
        runnerDto.setStatus(status.getValue());
        runnerDao.update(runnerDto);

    }


    @Transactional
    public BigInteger whenReady(BigInteger taskId, RunnerTrigger trigger, String createBy) {
        TaskDTO taskDto = new TaskDTO();
        RunnerDTO runnerDto = new RunnerDTO();
        LocalDateTime now = LocalDateTime.now();

        taskDto.setId(taskId);
        taskDto.setStatus(RunnerStatus.READY.getValue());
        taskDto.setLastStartTime(now);
        taskDao.update(taskDto);

        runnerDto.setTaskId(taskId);
        runnerDto.setStartTime(now);
        runnerDto.setStatus(RunnerStatus.READY.getValue());
        runnerDto.setTrigger(trigger.getValue());
        runnerDto.setCreateBy(createBy);
        runnerDao.insert(runnerDto);

        return runnerDto.getId();
    }


    @Transactional
    public void whenEnd(BigInteger taskId, BigInteger runnerId, RunnerStatus status) {
        TaskDTO taskDto = new TaskDTO();
        RunnerDTO runnerDto = new RunnerDTO();
        LocalDateTime now = LocalDateTime.now();

        taskDto.setId(taskId);
        taskDto.setStatus(status.getValue());
        taskDto.setLastEndTime(now);
        taskDao.update(taskDto);

        if (runnerId != null) {
            runnerDto.setId(runnerId);
            runnerDto.setStatus(status.getValue());
            runnerDto.setEndTime(now);
            runnerDao.update(runnerDto);
        }
    }

    public String getRunnerKey(String taskKey, BigInteger runnerId) {
        return taskKey + "-" + runnerId;
    }


    public static LogLevel getLogLevelByValue(int value) {
        for (LogLevel level : LogLevel.values()) {
            if (level.getLevel() == value) {
                return level;
            }
        }
        return LogLevel.BASIC;
    }

    public int setVariable(BigInteger projectId, Job job) {
        return setVariable(projectId, job, null);
    }

    public int setVariable(BigInteger projectId, Trans trans) {
        return setVariable(projectId, null, trans);
    }

    public int setVariable(BigInteger projectId, Job job, Trans trans) {
        List<ProjectVarDTO> vars = projectVarDao.selectAll(projectId, true);
        vars.forEach(var -> {
            try {
                if (job != null) {
                    job.setVariable(var.getName(), var.getIsEncode() == 1 ? cryptoUtil.decrypt(var.getValue()) : var.getValue());
                }
                if (trans != null) {
                    trans.setVariable(var.getName(), var.getIsEncode() == 1 ? cryptoUtil.decrypt(var.getValue()) : var.getValue());
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        return vars.size();
    }
}
