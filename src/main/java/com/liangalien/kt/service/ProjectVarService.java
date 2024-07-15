package com.liangalien.kt.service;

import com.liangalien.kt.dao.ProjectVarDAO;
import com.liangalien.kt.dto.ProjectVarDTO;
import com.liangalien.kt.util.CryptoUtil;
import com.liangalien.kt.util.reqeust.RequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectVarService {

    @Autowired
    private ProjectVarDAO projectVarDao;

    @Autowired
    private CryptoUtil cryptoUtil;

    private final String encodeTextReplace = "******";

    public List<ProjectVarDTO> getAll(BigInteger projectId) {
        List<ProjectVarDTO> vars = projectVarDao.selectAll(projectId, true);
        vars.forEach(var -> {
            if (var.getIsEncode() == 1) {
                var.setValue(encodeTextReplace);  // 使用6个*代替密文
            }
        });
        return vars;
    }


    public void addOrUpdate(BigInteger projectId, List<ProjectVarDTO> dots) {
        List<String> existNames = projectVarDao.selectAll(projectId, false).stream().map(
                ProjectVarDTO::getName).collect(Collectors.toList());

        projectVarDao.remove(projectId);

        List<ProjectVarDTO> news = new ArrayList<>();
        dots.forEach(dot -> {
            dot.setProjectId(projectId);
            if (dot.getIsEncode() == 1)
                if (!encodeTextReplace.equals(dot.getValue())) {
                    try {
                        dot.setValue(cryptoUtil.encrypt(dot.getValue()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    dot.setValue(null);  // 值是6个*，不去更新密文
                }

            if (existNames.contains(dot.getName())) {
                dot.setUpdateBy(RequestUtil.getUserName());
                dot.setIsDeleted(0);
                projectVarDao.update(dot);
            } else {
                dot.setCreateBy(RequestUtil.getUserName());
                news.add(dot);
            }
        });

        if (news.size() > 0) {
            projectVarDao.inserts(news);
        }
    }
}
