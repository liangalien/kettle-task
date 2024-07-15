package com.liangalien.kt.controller;

import com.liangalien.kt.dto.ProjectVarDTO;
import com.liangalien.kt.service.ProjectVarService;
import com.liangalien.kt.util.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/project/var")
public class ProjectVarController {

    @Autowired
    ProjectVarService projectVarService;


    @PostMapping("/edit")
    public Response edit(@RequestBody Map<String, Object> body) throws Exception {
        BigInteger projectId = new BigInteger(body.get("project_id").toString());
        List<ProjectVarDTO> data = new ArrayList<>();
        for (Map<String, Object> item : (List<Map<String, Object>>) body.get("data")) {
            ProjectVarDTO dto = new ProjectVarDTO();
            dto.setName(item.get("name").toString());
            dto.setValue(item.get("value").toString());
            dto.setIsEncode(Integer.parseInt(item.get("isEncode").toString()));
            data.add(dto);
        }

        projectVarService.addOrUpdate(projectId, data);
        return Response.success();
    }


    @GetMapping("/get")
    public Response get(@RequestParam("project_id") BigInteger projectId) throws Exception {
        return Response.success(projectVarService.getAll(projectId));
    }
}
