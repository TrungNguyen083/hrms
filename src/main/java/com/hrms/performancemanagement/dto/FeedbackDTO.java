package com.hrms.performancemanagement.dto;

import lombok.*;

import java.util.Date;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {
    private Integer id;
    private Integer giverId;
    private String giverName;
    private String giverProfileImage;
    private String feedbackContent;
    private Date createdAt;

    public FeedbackDTO(Integer id, Integer giverId, String giverName, String feedbackContent, Date createdAt) {
        this.id = id;
        this.giverId = giverId;
        this.giverName = giverName;
        this.feedbackContent = feedbackContent;
        this.createdAt = createdAt;
    }
}
