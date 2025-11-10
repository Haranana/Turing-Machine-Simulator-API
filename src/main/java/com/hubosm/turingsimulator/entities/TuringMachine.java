package com.hubosm.turingsimulator.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "turing_machines",
uniqueConstraints = @UniqueConstraint(name = "tm_author_name_constraint" , columnNames = {"author_id" , "name"}))
public class TuringMachine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull
    private User author;

    @Column(name = "name",nullable = false, length = 30)
    private String name;

    @Column(name = "description",nullable = false, length = 255)
    private String description;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "TEXT" , name="program")
    private String program;

    @Column(name = "initial_state" ,nullable = false, length = 32)
    private String initialState;

    @Column(name = "accept_state", length = 32)
    private String acceptState;

    @Column(name = "reject_state", length = 32)
    private String rejectState;

    @Column(name = "blank", nullable = false, length = 10)
    private String blank;

    @Column(name = "sep1", nullable = false, length = 10)
    private String sep1;

    @Column(name = "sep2", nullable = false, length = 10)
    private String sep2;

    @Column(name ="move_right" , nullable = false, length = 10)
    private String moveRight;

    @Column(name ="move_left" , nullable = false, length = 10)
    private String moveLeft;

    @Column(name ="move_stay" , nullable = false, length = 10)
    private String moveStay;

    @Column(name = "tapes_amount", nullable = false)
    @Min(1)
    private Integer tapesAmount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
