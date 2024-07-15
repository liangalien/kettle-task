package com.liangalien.kt.service;

import com.liangalien.kt.dto.CronTriggerDTO;
import com.liangalien.kt.dto.QuartzDetailDTO;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


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
        if (!has(name, groupName)) {
            return;
        }

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

    public List<CronTriggerDTO> cronList() throws SchedulerException {
        List<String> triggerGroupNames = scheduler.getTriggerGroupNames();

        List<CronTriggerDTO> results = new ArrayList<>();
        for (String groupName : triggerGroupNames) {
            for (TriggerKey triggerKey : scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(groupName))) {
                CronTriggerImpl trigger = (CronTriggerImpl) queryTrigger(triggerKey);

                CronTriggerDTO dto = new CronTriggerDTO();
                dto.setKey(trigger.getKey().toString());
                dto.setName(trigger.getName());
                dto.setGroup(trigger.getGroup());
                dto.setCron(trigger.getCronExpression());
                dto.setNextFireTime(trigger.getNextFireTime());

                results.add(dto);
            }
        }

        return results;
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
        return queryTrigger(triggerKey);
    }

    public Trigger queryTrigger(TriggerKey triggerKey) {
        try {
            return scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            return null;
        }
    }
}
