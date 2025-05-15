package ua.edu.nuos.coursework.repository;

import java.io.File;
import java.util.List;

public interface Repository<T> {
    void outputList(List<T> t, File file);
    void outputList(List<T> t, String fileName);

    List<T> readList(File file);
    List<T> readList(String fileName);
}
