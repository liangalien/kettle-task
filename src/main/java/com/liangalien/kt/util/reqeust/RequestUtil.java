package com.liangalien.kt.util.reqeust;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Component
public class RequestUtil {
    public static JSONObject getParams(HttpServletRequest request) {
        String query = request.getQueryString();
        if (query == null) return null;
        try {
            query = URLDecoder.decode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("URL转码失败", e);
        }

        JSONObject params = new JSONObject();
        for (String item : query.split("&")) {
            String[] itemArray = item.split("=");
            if (itemArray.length != 2) continue;
            params.put(itemArray[0], itemArray[1]);
        }
        return params;
    }

    public static String getSortBy(String pageSortBy) {
        if (pageSortBy == null || "".equals(pageSortBy.trim())) return null;
        try {
            pageSortBy = URLDecoder.decode(pageSortBy, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        return getSortBy((JSONObject) JSONObject.parse(pageSortBy));
    }

    public static String getSortBy(JSONObject pageSortBy) {
        if (pageSortBy == null) return null;
        List<String> orderByList = new ArrayList<>();
        pageSortBy.getInnerMap().forEach((name,  value) -> {
            if (value != null) {
                value = value.toString().indexOf("desc") != -1 ? "desc" : "asc";
                orderByList.add(name + " " + value);
            }
        });
        return String.join(", ", orderByList);
    }

    public static String getSortBy(Object pageSortBy) {
       if (pageSortBy instanceof String) {
           return getSortBy(pageSortBy + "");
       } else {
           return getSortBy((JSONObject) pageSortBy);
       }
    }

    public static void setPageHelper(Map<String, Object> body, String defaultSortBy) {
        int pageNo = body.get("pageNo") != null ? Integer.parseInt(body.get("pageNo").toString()) : 1;
        int pageSize =  body.get("pageSize") != null ? Integer.parseInt(body.get("pageSize").toString()) : 10;
        String sortBy = body.get("sortBy") != null ? getSortBy(body.get("sortBy")) : defaultSortBy;

        PageHelper.startPage(pageNo, pageSize, sortBy);
    }
}
