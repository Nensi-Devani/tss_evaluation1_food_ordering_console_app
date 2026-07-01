package com.foodorder.service;

import java.util.List;

import com.foodorder.constants.MessageConstants;
import com.foodorder.enums.Status;
import com.foodorder.exception.MenuItemAlreadyExistsException;
import com.foodorder.exception.MenuItemNotFoundException;
import com.foodorder.model.MenuItem;
import com.foodorder.repository.MenuItemRepository;

public class MenuItemService {

    private final MenuItemRepository menuItemRepository = new MenuItemRepository();

    public void addMenuItem(MenuItem menuItem) {
        MenuItem existing = menuItemRepository.findByRestaurantIdAndName(menuItem.getRestaurantId(), menuItem.getName());

        if (existing != null)
            throw new MenuItemAlreadyExistsException(MessageConstants.MENU_ITEM_ALREADY_EXISTS);

        menuItem.setStatus(Status.ACTIVE);

        menuItemRepository.save(menuItem);
    }

    public MenuItem getMenuItemById(String id) {
        return menuItemRepository.findById(id);
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public List<MenuItem> getMenuItemsByRestaurantId(String restaurantId) {
        return menuItemRepository.findByRestaurantId(restaurantId);
    }

    public List<MenuItem> getActiveMenuItems() {
        return menuItemRepository.findAllActive();
    }

    public List<MenuItem> getInactiveMenuItems() {
        return menuItemRepository.findAllInactive();
    }

    public void updateMenuItem(MenuItem menuItem) {
        menuItemRepository.update(menuItem);
    }

    public void deactivateMenuItem(String id) {
        MenuItem menuItem = menuItemRepository.findById(id);

        menuItem.setStatus(Status.INACTIVE);

        menuItemRepository.update(menuItem);
    }

    public void activateMenuItem(String id) {
        MenuItem menuItem = menuItemRepository.findById(id);

        menuItem.setStatus(Status.ACTIVE);

        menuItemRepository.update(menuItem);
    }
}
