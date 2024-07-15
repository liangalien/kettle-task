package com.liangalien.kt.service;


import com.liangalien.kt.dao.FileRepoDAO;
import com.liangalien.kt.dao.ProjectDAO;
import com.liangalien.kt.dao.TaskDAO;
import com.liangalien.kt.dto.FileRepoDTO;
import com.liangalien.kt.dto.ProjectDTO;
import com.liangalien.kt.dto.TaskDTO;
import com.liangalien.kt.dto.UserDTO;
import com.liangalien.kt.util.reqeust.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class FileRepoService {

    @Value("${kettle.repo}")
    private String localRepoPath;

    @Autowired
    private FileRepoDAO fileRepoDao;

    @Autowired
    private ProjectDAO projectDao;

    @Autowired
    private TaskDAO taskDao;


    @Autowired
    private TaskService taskService;

    public List<FileRepoDTO> selectAll(Map<String, Object> body) {
        List<FileRepoDTO> fileRepos = fileRepoDao.selectAll(body);
        return fileRepos;
    }

    public void upload(BigInteger projectId, MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new Exception("上传文件内容为空");
        }

        ProjectDTO project = projectDao.selectById(projectId);
        if (project == null) {
            throw new Exception("项目不存在：" + projectId);
        }



        String fileName;
        try {
            fileName = file.getOriginalFilename();
            String fileDirStr = String.format("%s/%s/", localRepoPath, project.getKey());
            File fileDir = new File(fileDirStr);
            if (!fileDir.exists()) {
                if (!fileDir.mkdirs()) {
                    throw new IOException("创建项目文件资源库目录失败");
                } else {
                    log.info("创建项目文件资源库目录成功：{}", fileDirStr);
                }
            }

            File destFile = new File(fileDirStr, fileName);
            file.transferTo(destFile);
        } catch (IOException e) {
            log.error("上传文件失败", e);
            throw new IOException("上传文件失败", e);
        }

        FileRepoDTO dto = fileRepoDao.selectByName(projectId, fileName);
        if (dto == null) {
            dto = new FileRepoDTO();
            dto.setProjectId(projectId);
            dto.setFileName(fileName);
            dto.setFileType(fileName.endsWith(".ktr") ? "trans" : "job");
            dto.setCreateBy(RequestUtil.getUserName());
            fileRepoDao.insert(dto);
        } else {
            dto.setIsDeleted(0);
            dto.setUpdateBy(RequestUtil.getUserName());
            fileRepoDao.update(dto);
        }
    }

    public void remove(BigInteger id) throws Exception {
        FileRepoDTO dto = checkExists(id, true);

        List<TaskDTO> tasks = taskDao.selectByRepoId(id);
        tasks.forEach(task -> {
            try {
                taskService.remove(task.getId());
            } catch (Exception e) {

            }
        });

        File file = new File(String.format("%s/%s/%s", localRepoPath, dto.getProjectKey(), dto.getFileName()));
        if (file.exists()) {
            log.info("删除文件：{}", file.getPath());
            file.delete();
        }

        fileRepoDao.remove(id, RequestUtil.getUserName());
    }


    public FileRepoDTO checkExists(BigInteger id, boolean isThrow) throws Exception {
        FileRepoDTO dto = fileRepoDao.selectById(id);
        if (dto == null && isThrow) {
            throw new Exception("文件不存在：" + id);
        }
        return dto;
    }

}
