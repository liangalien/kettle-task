package com.liangalien.kt.service;


import com.liangalien.kt.dao.RunnerDAO;
import com.liangalien.kt.dto.RunnerDTO;
import com.liangalien.kt.dto.TaskDTO;
import com.liangalien.kt.util.RunnerThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Duration;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class RunnerService {

    @Autowired
    RunnerDAO runnerDao;

    @Autowired
    TaskService taskService;


    public List<RunnerDTO> getAll(Map<String, Object> body) {
        List<RunnerDTO> runners = runnerDao.selectAll(body);
        runners.forEach(runner -> {
            if (runner.getStartTime() != null && runner.getEndTime() != null) {
                runner.setDurations(Duration.between(runner.getStartTime(), runner.getEndTime()).getSeconds());
            }
        });

        return runners;
    }

    public String log(BigInteger runnerId) throws Exception {
        RunnerDTO runnerDto = checkExists(runnerId);

        String logName = runnerDto.getKey();
        File logFile = new File("logs/runner/" + logName + ".log");
        if (!logFile.exists()) {
            throw new Exception("未找到日志文件：" + logName);
        }

        try {
            byte[] fileContent = FileCopyUtils.copyToByteArray(logFile);
            return new String(fileContent);
        } catch (IOException e) {
            log.error("日志文件读取失败", e);
            throw new IOException("日志文件读取失败：" + e.getMessage());
        }
    }

    public RunnerDTO checkExists(BigInteger id) throws Exception {
        RunnerDTO dto = runnerDao.selectById(id);
        if (dto == null) {
            throw new Exception("任务运行记录不存在：" + id);
        }
        return dto;
    }
}
