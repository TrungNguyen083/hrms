package com.hrms.search.dto;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultItemDTO {
    private String id;
    private String displayText;
    private String description;
    private String tag;
    private Float rankingScore;
}