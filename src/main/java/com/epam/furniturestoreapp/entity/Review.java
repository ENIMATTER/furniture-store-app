package com.epam.furniturestoreapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
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
    @JoinColumn(name = "userTableID", referencedColumnName = "userTableID")
    private UserTable userTableID;

    @NotBlank(message = "Rating is mandatory")
    @Column(name = "Rating")
    private int rating;

    @Size(max = 2000, message = "Review comment description must be less than 2000")
    @NotBlank(message = "Review comment description is mandatory")
    @Column(name = "ReviewComment")
    private String reviewComment;

    @NotBlank(message = "Review date is mandatory")
    @Column(name = "ReviewDate")
    private LocalDateTime reviewDate;

}
