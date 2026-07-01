package com.foodorder.repository;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.enums.Status;
import com.foodorder.exception.MenuItemNotFoundException;
import com.foodorder.model.MenuItem;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

import java.util.ArrayList;
import java.util.List;

public class MenuItemRepository {
    public void save(MenuItem menuItem) {
        List<MenuItem> menuItems = FileUtil.readData(FileConstants.MENU_ITEMS_FILE);

        menuItem.setId(IdGenerator.generateId(
                FileConstants.MENU_ITEMS_FILE,
                IdConstants.MENU_ITEM_ID_PREFIX));

        menuItems.add(menuItem);

        FileUtil.writeData(FileConstants.MENU_ITEMS_FILE, menuItems);
    }

    public void update(MenuItem menuItem) {
        List<MenuItem> menuItems = FileUtil.readData(FileConstants.MENU_ITEMS_FILE);

        for (int i = 0; i < menuItems.size(); i++) {
            if (menuItems.get(i).getId().equals(menuItem.getId())) {
                menuItems.set(i, menuItem);

                FileUtil.writeData(FileConstants.MENU_ITEMS_FILE, menuItems);
                return;
            }
        }

        throw new MenuItemNotFoundException(MessageConstants.MENU_ITEM_NOT_FOUND);
    }

    public MenuItem findById(String id) {
        List<MenuItem> menuItems = FileUtil.readData(FileConstants.MENU_ITEMS_FILE);

        for (MenuItem menuItem : menuItems) {
            if (menuItem.getId().equals(id))
                return menuItem;
        }

        throw new MenuItemNotFoundException(MessageConstants.MENU_ITEM_NOT_FOUND);
    }

    public List<MenuItem> findByRestaurantId(String restaurantId) {
        List<MenuItem> restaurantMenuItems = new ArrayList<>();

        List<MenuItem> menuItems = FileUtil.readData(FileConstants.MENU_ITEMS_FILE);

        for (MenuItem menuItem : menuItems) {
            if (menuItem.getRestaurantId().equals(restaurantId))
                restaurantMenuItems.add(menuItem);
        }

        return restaurantMenuItems;
    }

    public List<MenuItem> findAll() {
        return FileUtil.readData(FileConstants.MENU_ITEMS_FILE);
    }

    public List<MenuItem> findAllActive() {
        List<MenuItem> activeMenuItems = new ArrayList<>();

        List<MenuItem> menuItems = FileUtil.readData(FileConstants.MENU_ITEMS_FILE);

        for (MenuItem menuItem : menuItems) {
            if (menuItem.getStatus() == Status.ACTIVE)
                activeMenuItems.add(menuItem);
        }

        return activeMenuItems;
    }

    public List<MenuItem> findAllInactive() {
        List<MenuItem> inactiveMenuItems = new ArrayList<>();

        List<MenuItem> menuItems = FileUtil.readData(FileConstants.MENU_ITEMS_FILE);

        for (MenuItem menuItem : menuItems)
            if (menuItem.getStatus() == Status.INACTIVE) {
                inactiveMenuItems.add(menuItem);
        }

        return inactiveMenuItems;
    }

    public MenuItem findByRestaurantIdAndName(String restaurantId, String name) {
        List<MenuItem> menuItems = FileUtil.readData(FileConstants.MENU_ITEMS_FILE);

        for (MenuItem menuItem : menuItems) {
            if (menuItem.getRestaurantId().equals(restaurantId) && menuItem.getName().equalsIgnoreCase(name))
                return menuItem;
        }

        return null;
    }
}
