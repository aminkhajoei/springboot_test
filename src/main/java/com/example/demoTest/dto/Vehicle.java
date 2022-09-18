package com.example.demoTest.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @Column(name = "VIN", nullable = false, length = 17)
    @NonNull
    private String vin;

    @Column(name = "make", nullable = false)
    @NonNull
    @NotEmpty(message = "'make' field was empty")
    private String make;

    @Column(name = "model", nullable = false)
    @NonNull
    @NotEmpty(message = "model' field was empty")
    private String model;

    @Column(name = "year", nullable = false)
    @NonNull
    // Fun fact: VINs were first used until 1954 in the United States
    @DecimalMin(value = "1954", message = "VINs before 1954 are not accepted")
    private Integer year;

    @Column(name = "is_older", nullable = true)
    private Boolean is_older;
}

