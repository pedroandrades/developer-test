package com.dbserver.developertest.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@Builder
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate voteDate;

    @NotNull
    @OneToOne()
    @JoinColumn(name = "restaurant", referencedColumnName = "id")
    private Restaurant restaurant;

    @NotNull
    @OneToOne()
    @JoinColumn(name = "hungry_professional", referencedColumnName = "id")
    private HungryProfessional hungryProfessional;
}
