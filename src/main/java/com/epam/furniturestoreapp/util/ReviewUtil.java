package com.epam.furniturestoreapp.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ReviewUtil {
    private Long reviewID;
    private Long productID;
    private Long userTableID;
    private Integer rating;
    private String reviewComment;
    private LocalDateTime reviewDate;
}
