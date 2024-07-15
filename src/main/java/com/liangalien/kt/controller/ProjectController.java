package com.liangalien.kt.controller;


import com.github.pagehelper.PageInfo;
import com.liangalien.kt.dto.ProjectDTO;
import com.liangalien.kt.dto.TaskDTO;
import com.liangalien.kt.service.ProjectService;
import com.liangalien.kt.service.TaskService;
import com.liangalien.kt.util.reqeust.RequestUtil;
import com.liangalien.kt.util.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project")
public class ProjectController {

    @Autowired
    ProjectService projectService;


    @PostMapping("/edit")
    public Response edit(@RequestBody ProjectDTO body) throws Exception {
        projectService.addOrUpdate(body);
        return Response.success();
    }

    @DeleteMapping("/remove")
    public Response remove(@RequestParam("id") String id) throws Exception {
        projectService.remove(new BigInteger(id));
        return Response.success();
    }



    @PostMapping("/list")
    public Response list(@RequestBody Map<String, Object> body) throws Exception {
        RequestUtil.setPageHelper(body, "id desc");
        List<ProjectDTO> results = projectService.getAll(body);
        PageInfo<ProjectDTO> pageInfo = new PageInfo<>(results);
        return Response.success(new ArrayList<>(pageInfo.getList()), (int) pageInfo.getTotal());
    }
}
