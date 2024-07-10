package com.liangalien.kt.service;

import com.liangalien.kt.dto.QuartzDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class QuartzService {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ApplicationContext applicationContext;

    public void addOrUpdate(QuartzDetailDTO dto) {
        try {
            Class<? extends Job> jobBean = (Class<? extends Job>) applicationContext.getBean(dto.getBean()).getClass();

            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(dto.getName(), dto.getGroupName())
                    .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.SECOND))
                    .withSchedule(CronScheduleBuilder.cronSchedule(dto.getCron())).startNow().build();


            JobDataMap jobDataMap = new JobDataMap();
            if (dto.getParams() != null) {
                jobDataMap.putAll(dto.getParams());
            }

            if (has(dto.getName(), dto.getGroupName())) {
                remove(dto.getName(), dto.getGroupName());
            }

            JobDetail jobDetail = JobBuilder.newJob(jobBean).withIdentity(dto.getName(), dto.getGroupName()
            ).setJobData(jobDataMap).build();

            scheduler.scheduleJob(jobDetail, trigger);
            if (!scheduler.isShutdown()) {
                scheduler.start();
            }
            log.info("创建定时任务成功：{}", dto);
        } catch (SchedulerException e) {
            log.error("创建或更新定时任务失败", e);
            e.printStackTrace();
        }
    }

    public void remove(String name, String groupName) {
        try {
            scheduler.pauseTrigger(TriggerKey.triggerKey(name, groupName));
            scheduler.unscheduleJob(TriggerKey.triggerKey(name, groupName));
            scheduler.deleteJob(JobKey.jobKey(name));
            log.info("移除定时任务成功：{} {}", groupName, name);
        } catch (SchedulerException e) {
            log.error("移除定时任务失败：{} {}", groupName, name, e);
            throw new RuntimeException(e);
        }
    }


    public boolean has(TriggerKey triggerKey) {
        try {
            return scheduler.getTrigger(triggerKey) != null;
        } catch (SchedulerException e) {

        }
        return false;
    }

    public boolean has(String name, String groupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(name, groupName);
        return has(triggerKey);
    }

    public Trigger queryTrigger(String name, String groupName) {
        TriggerKey triggerKey = TriggerKey.triggerKey(name, groupName);
        try {
            return scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            return null;
        }
    }
}
