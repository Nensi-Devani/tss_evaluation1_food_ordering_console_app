package com.foodorder.repository;

import java.util.ArrayList;
import java.util.List;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.enums.Status;
import com.foodorder.exception.DiscountNotFoundException;
import com.foodorder.model.Discount;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

public class DiscountRepository {

    public void save(Discount discount) {
        List<Discount> discounts = FileUtil.readData(FileConstants.DISCOUNTS_FILE);

        discount.setId(IdGenerator.generateId(
                FileConstants.DISCOUNTS_FILE,
                IdConstants.DISCOUNT_ID_PREFIX));

        discounts.add(discount);

        FileUtil.writeData(FileConstants.DISCOUNTS_FILE, discounts);
    }

    public void update(Discount discount) {
        List<Discount> discounts = FileUtil.readData(FileConstants.DISCOUNTS_FILE);

        for (int i = 0; i < discounts.size(); i++) {
            if (discounts.get(i).getId().equals(discount.getId())) {
                discounts.set(i, discount);

                FileUtil.writeData(FileConstants.DISCOUNTS_FILE, discounts);
                return;
            }
        }

        throw new DiscountNotFoundException(MessageConstants.DISCOUNT_NOT_FOUND);
    }

    public Discount findById(String id) {
        List<Discount> discounts = FileUtil.readData(FileConstants.DISCOUNTS_FILE);

        for (Discount discount : discounts) {
            if (discount.getId().equals(id))
                return discount;
        }

        throw new DiscountNotFoundException(MessageConstants.DISCOUNT_NOT_FOUND);
    }

    public List<Discount> findAll() {
        return FileUtil.readData(FileConstants.DISCOUNTS_FILE);
    }

    public List<Discount> findAllActive() {
        List<Discount> activeDiscounts = new ArrayList<>();

        List<Discount> discounts = FileUtil.readData(FileConstants.DISCOUNTS_FILE);

        for (Discount discount : discounts) {
            if (discount.getStatus() == Status.ACTIVE)
                activeDiscounts.add(discount);
        }

        return activeDiscounts;
    }

    public List<Discount> findAllInactive() {
        List<Discount> inactiveDiscounts = new ArrayList<>();

        List<Discount> discounts = FileUtil.readData(FileConstants.DISCOUNTS_FILE);

        for (Discount discount : discounts) {
            if (discount.getStatus() == Status.INACTIVE)
                inactiveDiscounts.add(discount);
        }

        return inactiveDiscounts;
    }

}
