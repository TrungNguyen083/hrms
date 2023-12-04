package com.hrms.careerpathmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TreeSimpleData {
    private Integer id;
    private String name;
    private List<TreeSimpleData> children;
}
