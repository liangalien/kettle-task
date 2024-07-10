package com.liangalien.kt.service;


import com.alibaba.fastjson.JSONObject;
import com.liangalien.kt.dao.TaskDAO;
import com.liangalien.kt.dto.QuartzDetailDTO;
import com.liangalien.kt.dto.TaskDTO;
import com.liangalien.kt.kettle.Runner;
import com.liangalien.kt.util.RunnerStatus;
import com.liangalien.kt.util.RunnerThread;
import com.liangalien.kt.util.RunnerTrigger;
import com.liangalien.kt.util.RunnerType;
import org.quartz.Trigger;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    @Autowired
    TaskDAO taskDao;

    @Autowired
    Runner runner;

    @Autowired
    QuartzService quartzService;

    private final String KETTLE_GROUP_NAME = "kettle";

    public void start(BigInteger taskId) throws Exception {
        TaskDTO dto = checkExists(taskId);

        // 更新状态
        taskDao.updateStatus(taskId, RunnerStatus.WAITING.getValue());

        RunnerThread.create(dto.getKey(), () -> {
            runner.execute(dto, RunnerTrigger.MANUAL, "admin");
        });
    }

    public void stop(BigInteger taskId) throws Exception {
        TaskDTO dto = checkExists(taskId);

        // 停kettle里的线程
        if (RunnerType.TRANS.getValue().equals(dto.getRepoType())) {
            Runner.transStop(dto.getKey());
        } else {
            Runner.jobStop(dto.getKey());
        }

        /*if (!done
                || dto.getStatus() == RunnerStatus.WAITING.getValue()
                || dto.getStatus() == RunnerStatus.READY.getValue()
                || dto.getStatus() == RunnerStatus.RUNNING.getValue()
        ) {  // 停止前是未完成状态
            // 更新状态
            taskDao.updateStatus(taskId, RunnerStatus.STOPPED.getValue());
        }*/
    }

    public void setSchedule(BigInteger taskId, String cron) throws Exception {
        TaskDTO dto = checkExists(taskId);

        QuartzDetailDTO quartzDetailDto = new QuartzDetailDTO();
        quartzDetailDto.setCron(cron);
        quartzDetailDto.setGroupName(KETTLE_GROUP_NAME);
        quartzDetailDto.setName(dto.getKey());
        quartzDetailDto.setBean("kettleQuartzJob");
        quartzDetailDto.setParams(JSONObject.parseObject(String.format("{\"id\": %s}", taskId)));

        quartzService.addOrUpdate(quartzDetailDto);
    }

    public void removeSchedule(String name) {
        quartzService.remove(name, KETTLE_GROUP_NAME);
    }

    public List<TaskDTO> getAll(Map<String, Object> body) {
        Object search = body.get("search");
        List<TaskDTO> tasks = taskDao.selectAll(search != null ? (Map<String, Object>) search : null);
        tasks.forEach(task -> {
            CronTriggerImpl trigger = (CronTriggerImpl) quartzService.queryTrigger(task.getKey(), KETTLE_GROUP_NAME);
            if (trigger != null) {
                task.setNextRunTime(trigger.getNextFireTime());
                task.setQuartzCron(trigger.getCronExpression());
            }
        });

        return tasks;
    }

    public TaskDTO checkExists(BigInteger taskId) throws Exception {
        TaskDTO dto = taskDao.selectById(taskId);
        if (dto == null) {
            throw new Exception("任务不存在：" + taskId);
        }
        return dto;
    }

    public void addOrUpdate(TaskDTO taskDto) throws Exception {
        if (taskDto.getId() != null) {
            checkExists(taskDto.getId());
            taskDto.setUpdateBy("admin");
            taskDao.update(taskDto);
        } else {
            taskDto.setCreateBy("admin");
            taskDao.insert(taskDto);
        }
    }

    public void remove(BigInteger taskId) throws Exception {
        TaskDTO taskDTO = checkExists(taskId);
        if (taskDTO.getStatus() == RunnerStatus.RUNNING.getValue()) {
            throw new Exception("任务运行中，请先停止后再删除");
        }

        taskDao.remove(taskId, "admin");
    }
}
