package com.foodorder.util;

import com.foodorder.common.Identifiable;

import java.util.List;

public final class IdGenerator {
    public static <T extends Identifiable> String generateId(String filePath, String prefix) {
        List<T> data = FileUtil.readData(filePath);

        if (data.isEmpty()) {
            return prefix + "001";
        }

        String lastId = data.get(data.size() - 1).getId(); // get the last object id
        String numericPart = lastId.replaceAll("\\D", ""); // removes everything except digits
        int nextNumber = Integer.parseInt(numericPart) + 1; // increment by 1

        return prefix + String.format("%03d", nextNumber);
    }
}
