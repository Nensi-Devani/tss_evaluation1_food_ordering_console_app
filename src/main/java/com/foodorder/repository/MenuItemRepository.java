package com.foodorder.repository;

import com.foodorder.constants.FileConstants;
import com.foodorder.constants.IdConstants;
import com.foodorder.constants.MessageConstants;
import com.foodorder.database.DatabaseConnection;
import com.foodorder.enums.FoodCategory;
import com.foodorder.enums.FoodType;
import com.foodorder.enums.Status;
import com.foodorder.exception.MenuItemNotFoundException;
import com.foodorder.model.MenuItem;
import com.foodorder.util.FileUtil;
import com.foodorder.util.IdGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuItemRepository {
    Connection connection;
    PreparedStatement preparedStatement;

    public MenuItemRepository(){
        connection = DatabaseConnection
                        .getInstance()
                        .getConnection();
    }

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

        String query = "SELECT * FROM menu_items WHERE restaurant_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Integer.parseInt(restaurantId));
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                MenuItem menuItem = new MenuItem();

                menuItem.setId(String.valueOf(resultSet.getInt("menu_item_id")));
                menuItem.setRestaurantId(String.valueOf(resultSet.getInt("restaurant_id")));
                menuItem.setName(resultSet.getString("name"));
                menuItem.setPrice(resultSet.getDouble("price"));
                menuItem.setDiscount(resultSet.getDouble("discount"));
                menuItem.setFoodType(FoodType.valueOf(resultSet.getString("food_type")));
                menuItem.setFoodCategory(FoodCategory.valueOf(resultSet.getString("food_category")));
                menuItem.setStatus(Status.valueOf(resultSet.getString("status")));

                restaurantMenuItems.add(menuItem);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
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
