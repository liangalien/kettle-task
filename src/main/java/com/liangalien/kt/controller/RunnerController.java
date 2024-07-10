package com.liangalien.kt.controller;


import com.github.pagehelper.PageInfo;
import com.liangalien.kt.dto.RunnerDTO;
import com.liangalien.kt.service.RunnerService;
import com.liangalien.kt.util.reqeust.RequestUtil;
import com.liangalien.kt.util.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/runner")
public class RunnerController {

    @Autowired
    RunnerService runnerService;


    @PostMapping("/list")
    public Response list(@RequestBody Map<String, Object> body) throws Exception {
        RequestUtil.setPageHelper(body, "id desc");
        List<RunnerDTO> results = runnerService.getAll(body);
        PageInfo<RunnerDTO> pageInfo = new PageInfo<>(results);
        return Response.success(new ArrayList<>(pageInfo.getList()), (int) pageInfo.getTotal());
    }


    @GetMapping("/log")
    public Response log(@RequestParam("id") String id) throws Exception {
        return Response.success(runnerService.log(new BigInteger(id)));
    }
}
