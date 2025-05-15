package ua.edu.nuos.coursework.logic;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class Patient implements Serializable {
    private Integer patientId;
    private String patientFullName;
    private String dutyDoctor;
    private String diagnose;
    private String medicationName;
    private Double dosage;
    private LocalDate startDate;
    private LocalDate endDate;
    private String instructions;
}
