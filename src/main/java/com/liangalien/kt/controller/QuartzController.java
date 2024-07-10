package com.liangalien.kt.controller;


import com.liangalien.kt.dto.QuartzDetailDTO;
import com.liangalien.kt.service.QuartzService;
import com.liangalien.kt.util.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/qrtz")
public class QuartzController {

    @Autowired
    private QuartzService quartzService;

    @PostMapping("/set")
    public Response add(@RequestBody QuartzDetailDTO body) {
        quartzService.addOrUpdate(body);
        return Response.success();
    }

    @DeleteMapping("/remove")
    public Response add(@RequestParam("name") String name, @RequestParam("group") String group) {
        quartzService.remove(name, group);
        return Response.success();
    }
}