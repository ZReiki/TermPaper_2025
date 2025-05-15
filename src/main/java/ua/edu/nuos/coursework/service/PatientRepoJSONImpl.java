package ua.edu.nuos.coursework.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.*;
import org.springframework.stereotype.Service;
import ua.edu.nuos.coursework.logic.Patient;
import ua.edu.nuos.coursework.repository.PatientRepository;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientRepoJSONImpl implements PatientRepository {
    @Override
    public void outputList(List<Patient> patients, File file) {
        try (Writer writer = new FileWriter(file)) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .setPrettyPrinting()
                    .create();
            gson.toJson(patients, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void outputList(List<Patient> patients, String fileName) {
        File file = new File(fileName);
        outputList(patients, file);
    }

    @Override
    public List<Patient> readList(File file) {
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                    .setPrettyPrinting()
                    .create();
            Type listType = new TypeToken<ArrayList<Patient>>() {}.getType();
            return gson.fromJson(new FileReader(file), listType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Patient> readList(String fileName) {
        File file = new File(fileName);
        return readList(file);
    }
}
