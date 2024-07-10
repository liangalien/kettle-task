package com.liangalien.kt.bean;

import com.liangalien.kt.service.TaskService;
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

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        log.info("开始执行Kettle定时任务");
        Object taskId = jobExecutionContext.getJobDetail().getJobDataMap().get("id");
        if (taskId == null) {
            log.error("Kettle定时任务缺失参数：id");
        } else {
            try {
                taskService.start(BigInteger.valueOf(Integer.parseInt(taskId + "")));
            } catch (Exception e) {
                log.error("Kettle定时任务失败", e);
                throw new RuntimeException(e);
            }
        }
    }
}
