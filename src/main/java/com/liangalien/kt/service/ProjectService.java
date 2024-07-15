package com.liangalien.kt.service;

import com.liangalien.kt.dao.ProjectDAO;
import com.liangalien.kt.dto.ProjectDTO;
import com.liangalien.kt.util.reqeust.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    @Autowired
    ProjectDAO projectDao;

    public List<ProjectDTO> getAll(Map<String, Object> body) {
        List<ProjectDTO> projects = projectDao.selectAll(body);
        return projects;
    }

    public ProjectDTO checkExists(BigInteger taskId) throws Exception {
        ProjectDTO dto = projectDao.selectById(taskId);
        if (dto == null) {
            throw new Exception("项目不存在：" + taskId);
        }
        return dto;
    }

    public void addOrUpdate(ProjectDTO projectDto) throws Exception {
        if (projectDto.getId() != null) {
            checkExists(projectDto.getId());
            projectDto.setUpdateBy(RequestUtil.getUserName());
            projectDao.update(projectDto);
        } else {
            projectDto.setCreateBy(RequestUtil.getUserName());
            projectDao.insert(projectDto);
        }
    }

    public void remove(BigInteger projectId) throws Exception {
        checkExists(projectId);
        projectDao.remove(projectId, RequestUtil.getUserName());
    }
}
