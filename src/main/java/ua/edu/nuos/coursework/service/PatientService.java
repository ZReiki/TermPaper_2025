package ua.edu.nuos.coursework.service;

import org.springframework.stereotype.Service;
import ua.edu.nuos.coursework.logic.Patient;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PatientService {
    private final String fileName = "patients.json";

    private List<Patient> patients = new ArrayList<>();
    private final PatientRepoJSONImpl repo = new PatientRepoJSONImpl();

    public List<Patient> getAllPatients() {
        return new ArrayList<>(patients);
    }

    public Patient getById(Integer id) {
        return patients.stream()
                .filter(p -> p.getPatientId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public void addPatient(Patient patient) {
        int newId = patients.stream().mapToInt(Patient::getPatientId).max().orElse(0) + 1;
        patient.setPatientId(newId);
        patients.add(patient);
    }

    public void updatePatient(Patient updatedPatient) {
        patients.replaceAll(p -> {
            if (p.getPatientId().equals(updatedPatient.getPatientId())) {
                p.setPatientFullName(updatedPatient.getPatientFullName());
                p.setDutyDoctor(updatedPatient.getDutyDoctor());
                p.setDiagnose(updatedPatient.getDiagnose());
                p.setMedicationName(updatedPatient.getMedicationName());
                p.setDosage(updatedPatient.getDosage());
                if (updatedPatient.getStartDate() == null) {
                    updatedPatient.setStartDate(p.getStartDate());
                }
                if (updatedPatient.getEndDate() == null) {
                    updatedPatient.setEndDate(p.getEndDate());
                }
                return p;
            }
            return p;
        });
    }

    public void updateInstructions(Integer id, String instructions) {
        Patient p = getById(id);
        if (p != null) p.setInstructions(instructions);
    }

    public void deleteById(int id) {
        patients.removeIf(p -> p.getPatientId() == id);
    }

    public List<Patient> searchPatients(Integer id, String name) {
        Set<Patient> results = new LinkedHashSet<>();

        if (id != null) {
            Patient p = getById(id);
            if (p != null) results.add(p);
        }

        if (name != null && !name.trim().isEmpty()) {
            results.addAll(patients.stream()
                    .filter(p -> p.getPatientFullName().toLowerCase().contains(name.toLowerCase()))
                    .toList());
        }

        if(results.isEmpty()) results.addAll(patients);

        return new ArrayList<>(results);
    }

    public List<Patient> filterPatients(String diagnose, String medication, LocalDate start, LocalDate end) {
        return patients.stream()
                .filter(p -> (diagnose == null || diagnose.isBlank() || p.getDiagnose().equalsIgnoreCase(diagnose)) &&
                        (medication == null || medication.isBlank() || p.getMedicationName().equalsIgnoreCase(medication)) &&
                        (start == null || !p.getStartDate().isBefore(start)) &&
                        (end == null || !p.getEndDate().isAfter(end)))
                .collect(Collectors.toList());
    }

    public void loadFromFile() {
        patients = repo.readList(fileName);
    }

    public void saveToFile() {
        repo.outputList(patients, fileName);
    }
}
