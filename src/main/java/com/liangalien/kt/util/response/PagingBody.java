package com.liangalien.kt.util.response;

import lombok.Data;
import java.util.List;


@Data
public class PagingBody {
    private List<Object> rows;
    private Integer total;
}
