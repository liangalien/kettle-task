package com.liangalien.kt.bean;

import com.liangalien.kt.dto.TaskDTO;
import com.liangalien.kt.kettle.Runner;
import com.liangalien.kt.service.TaskService;
import com.liangalien.kt.util.RunnerStatus;
import com.liangalien.kt.util.RunnerThread;
import com.liangalien.kt.util.RunnerTrigger;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.math.BigInteger;


@Slf4j
@Component
public class KettleQuartzJob extends QuartzJobBean {

    @Autowired
    private TaskService taskService;

    @Autowired
    Runner runner;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info("开始执行Kettle定时任务");
        Object taskId = jobExecutionContext.getJobDetail().getJobDataMap().get("id");
        if (taskId == null) {
            log.error("Kettle定时任务缺失参数：id");
        } else {
            try {
                BigInteger id = new BigInteger(taskId.toString());

                TaskDTO dto = taskService.checkExists(id);
                RunnerThread.create(dto.getKey(), () -> {
                    runner.execute(dto, RunnerTrigger.CRON, "admin");
                });
            } catch (Exception e) {
                log.error("Kettle定时任务失败", e);
                throw new RuntimeException(e);
            }
        }
    }
}
