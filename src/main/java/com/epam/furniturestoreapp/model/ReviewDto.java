package com.epam.furniturestoreapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ReviewDto {
    private Long reviewID;
    private Long productID;
    private Long userTableID;
    private Integer rating;
    private String reviewComment;
    private LocalDateTime reviewDate;
}
