package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Table(name = "review")
public class Review {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "ReviewID")
    private long reviewID;

    @ManyToOne
    @JoinColumn(name = "productID", referencedColumnName = "productID")
    private Product productID;

    @ManyToOne
    @JoinColumn(name = "usertableid", referencedColumnName = "usertableid")
    private UserTable userTableID;

    @NotBlank(message = "Rating is mandatory")
    @Column(name = "Rating")
    private int rating;

    @Size(max = 2000, message = "Review comment description must be less than 2000")
    @NotBlank(message = "Review comment description is mandatory")
    @Column(name = "reviewcomment")
    private String reviewComment;

    @NotBlank(message = "Review date is mandatory")
    @Column(name = "reviewdate")
    private LocalDateTime reviewDate;
}
