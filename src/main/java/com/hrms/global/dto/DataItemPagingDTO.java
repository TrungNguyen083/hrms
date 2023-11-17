package com.hrms.global.dto;

import com.hrms.global.paging.Pagination;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataItemPagingDTO {
    private List<DataItemDTO> data;
    private Pagination pagination;
}
