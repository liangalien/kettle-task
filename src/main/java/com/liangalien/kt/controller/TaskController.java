package com.liangalien.kt.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liangalien.kt.dto.TaskDTO;
import com.liangalien.kt.service.TaskService;
import com.liangalien.kt.util.reqeust.RequestUtil;
import com.liangalien.kt.util.response.Response;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    TaskService taskService;


    @PostMapping("/edit")
    public Response edit(@RequestBody TaskDTO body) throws Exception {
        taskService.addOrUpdate(body);
        return Response.success();
    }

    @DeleteMapping("/remove")
    public Response remove(@RequestParam("id") String id) throws Exception {
        taskService.remove(new BigInteger(id));
        return Response.success();
    }

    @PostMapping("/start")
    public Response start(@RequestBody TaskDTO body) throws Exception {
        taskService.start(body.getId());
        return Response.success();
    }

    @DeleteMapping("/stop")
    public Response stop(@RequestParam("id") String id) throws Exception {
        taskService.stop(new BigInteger(id));
        return Response.success();
    }

    @PostMapping("/sched")
    public Response schedule(@RequestBody Map<String, Object> body) throws Exception {
        if (body.get("id") == null || body.get("cron") == null) {
            return Response.fail("参数有误");
        }

        taskService.setSchedule(new BigInteger(body.get("id").toString()), body.get("cron").toString());
        return Response.success();
    }

    @DeleteMapping("/sched/cancel")
    public Response scheduleCancel(@RequestParam("quartz_name") String quartzName) {
        if (quartzName == null || "".equals(quartzName)) {
            return Response.fail("参数有误");
        }

        taskService.removeSchedule(quartzName);
        return Response.success();
    }

    @PostMapping("/list")
    public Response list(@RequestBody Map<String, Object> body) throws Exception {
        RequestUtil.setPageHelper(body, "id desc");
        List<TaskDTO> results = taskService.getAll(body);
        PageInfo<TaskDTO> pageInfo = new PageInfo<>(results);
        return Response.success(new ArrayList<>(pageInfo.getList()), (int) pageInfo.getTotal());
    }
}
