package ua.edu.nuos.coursework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.edu.nuos.coursework.logic.Patient;
import ua.edu.nuos.coursework.service.PatientService;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/")
public class PatientController {
    private final PatientService patientService = new PatientService();

    @GetMapping("")
    public String showStartPage() {
        return "start-page";
    }

    @GetMapping("/main-page")
    public String showMainPage(Model model) {
        if (!model.containsAttribute("patients")) {
            List<Patient> patients = patientService.getAllPatients();
            model.addAttribute("patients", patients);
        }
        return "main-page";
    }

    @GetMapping("/details/{id}")
    public String showPatientDetails(@PathVariable Integer id, Model model) {
        Patient patient = patientService.getById(id);
        if (patient == null) return "redirect:/main-page";
        model.addAttribute("patient", patient);
        return "patient-details";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) Integer id,
                         @RequestParam(required = false) String name,
                         RedirectAttributes redirectAttributes) {
        List<Patient> result = patientService.searchPatients(id, name);
        redirectAttributes.addFlashAttribute("patients", result);
        return "redirect:/main-page";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam(required = false) String diagnose,
                         @RequestParam(required = false) String medication,
                         @RequestParam(required = false) LocalDate start,
                         @RequestParam(required = false) LocalDate end,
                         RedirectAttributes redirectAttributes) {
        List<Patient> filtered = patientService.filterPatients(diagnose, medication, start, end);
        redirectAttributes.addFlashAttribute("patients", filtered);
        return "redirect:/main-page";
    }

    // --- Додавання ---
    @GetMapping("/main-page/add-patient")
    public String addPatientForm(Model model) {
        model.addAttribute("patient", new Patient());
        return "add-patient";
    }

    @PostMapping("/main-page/add-patient")
    public String addPatient(@ModelAttribute Patient patient) {
        patientService.addPatient(patient);
        return "redirect:/main-page";
    }

    // --- Редагування ---
    @GetMapping("/main-page/edit-patient/{id}")
    public String editPatientForm(@PathVariable Integer id, Model model) {
        Patient patient = patientService.getById(id);
        if (patient == null) return "redirect:/main-page";
        model.addAttribute("patient", patient);
        return "edit-patient";
    }

    @PostMapping("/main-page/edit-patient")
    public String updatePatient(@ModelAttribute Patient patient) {
        patientService.updatePatient(patient);
        return "redirect:/main-page";
    }

    // --- Інструкції ---
    @GetMapping("/edit-instruction/{id}")
    public String editInstructionForm(@PathVariable Integer id, Model model) {
        Patient patient = patientService.getById(id);
        if (patient == null) return "redirect:/main-page";
        model.addAttribute("patient", patient);
        return "edit-instruction";
    }

    @PostMapping("/edit-instruction/{id}")
    public String updateInstruction(@PathVariable Integer id, @ModelAttribute Patient updatedPatient) {
        patientService.updateInstructions(id, updatedPatient.getInstructions());
        return "redirect:/details/" + id;
    }

    // --- Видалення ---
    @PostMapping("/main-page/delete-patient/{id}")
    public String deletePatient(@PathVariable int id) {
        patientService.deleteById(id);
        return "redirect:/main-page";
    }

    // --- Файли ---
    @GetMapping("/load-from-json-file")
    public String loadFromFile() {
        patientService.loadFromFile();
        return "redirect:/main-page";
    }

    @GetMapping("/save-to-json-file")
    public String saveToFile() {
        patientService.saveToFile();
        return "redirect:/main-page";
    }
}
