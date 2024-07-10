package com.liangalien.kt.controller;


import com.github.pagehelper.PageInfo;
import com.liangalien.kt.dto.FileRepoDTO;
import com.liangalien.kt.dto.TaskDTO;
import com.liangalien.kt.service.FileRepoService;
import com.liangalien.kt.service.TaskService;
import com.liangalien.kt.util.reqeust.RequestUtil;
import com.liangalien.kt.util.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/repo/file")
public class FileRepoController {

    @Autowired
    FileRepoService fileRepoService;


    @PostMapping("/list")
    public Response list(@RequestBody Map<String, Object> body) throws Exception {
        RequestUtil.setPageHelper(body, "id desc");
        List<FileRepoDTO> results = fileRepoService.selectAll(body);
        PageInfo<FileRepoDTO> pageInfo = new PageInfo<>(results);
        return Response.success(new ArrayList<>(pageInfo.getList()), (int) pageInfo.getTotal());
    }

    @PostMapping("/upload")
    public Response upload(@RequestParam("project_id") String projectId,
                               @RequestParam("file") MultipartFile file) throws Exception {
        fileRepoService.upload(new BigInteger(projectId), file);
        return Response.success();
    }

    @DeleteMapping("/remove")
    public Response remove(@RequestParam("id") String id) throws Exception {
        fileRepoService.remove(new BigInteger(id));
        return Response.success();
    }
}
