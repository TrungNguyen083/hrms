package com.hrms.performancemanagement.dto;


import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingDTO {
    Integer employeeId;
    String profileImgUrl;
    Float ratingScore;
}
