package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Data
@NoArgsConstructor
public class Review {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ReviewID")
    private Long reviewID;

    @ManyToOne
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private Product productID;

    @ManyToOne
    @JoinColumn(name = "usertableid", referencedColumnName = "usertableid")
    private UserTable userTableID;

    @NotNull(message = "Rating is mandatory")
    @Column(name = "Rating")
    private Integer rating;

    @Size(max = 2000, message = "Review comment description must be less than 2000")
    @NotBlank(message = "Review comment description is mandatory")
    @Column(name = "reviewcomment")
    private String reviewComment;

    @NotNull(message = "Review date is mandatory")
    @Column(name = "reviewdate")
    private LocalDateTime reviewDate;
}
