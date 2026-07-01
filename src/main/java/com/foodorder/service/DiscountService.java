package com.foodorder.service;

import java.util.List;

import com.foodorder.enums.Status;
import com.foodorder.model.Discount;
import com.foodorder.repository.DiscountRepository;

public class DiscountService {
    private final DiscountRepository discountRepository = new DiscountRepository();

    public Discount createDiscount(Discount discount) {
        discount.setStatus(Status.ACTIVE);

        discountRepository.save(discount);

        return discount;
    }

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public List<Discount> getActiveDiscounts() {
        return discountRepository.findAllActive();
    }

    public Discount getBestDiscount(double amount) {
        List<Discount> discounts = discountRepository.findAllActive();

        Discount best = null;

        for (Discount d : discounts) {
            if (amount >= d.getMinimumAmount()) {
                if (best == null || d.getDiscountPercentage() > best.getDiscountPercentage()) {
                    best = d;
                }
            }
        }

        return best;
    }
}