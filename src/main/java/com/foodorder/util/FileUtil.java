package com.foodorder.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class FileUtil {

    public static <T> List<T> readData(String filePath) {
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<T>) ois.readObject();
        } catch (Exception e) {
            throw new RuntimeException("Failed to read data from file: " + filePath, e);
        }
    }

    public static <T> void writeData(String filePath, List<T> data) {
        File file = new File(filePath);

        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(data);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write data to file: " + filePath, e);
        }
    }
}
