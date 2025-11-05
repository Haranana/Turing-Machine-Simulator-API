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

    @Column(name = "name")
    @NotNull
    @NotEmpty
    @Size(max = 30)
    private String name;

    @Column(name = "description")
    @Size(max = 255)
    private String description;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "TEXT" , name="program")
    private String program;

    @Column(name = "initial_state")
    @NotNull
    @NotEmpty
    @Size(max = 32)
    private String initialState;

    @Column(name = "accept_state")
    @Size(max = 32)
    private String acceptState;

    @Column(name = "reject_state")
    @Size(max = 32)
    private String rejectState;

    @Column(name = "blank")
    @Size(max = 10)
    @NotNull
    @NotEmpty
    private String blank;

    @Column(name = "sep1")
    @Size(max = 10)
    @NotNull
    @NotEmpty
    private String sep1;

    @Column(name = "sep2")
    @Size(max = 10)
    @NotNull
    @NotEmpty
    private String sep2;

    @Column(name = "tapes_amount")
    @Min(1)
    @NotNull
    private Integer tapesAmount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
