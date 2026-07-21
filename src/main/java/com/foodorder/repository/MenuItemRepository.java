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

import java.sql.*;
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
        String query = "INSERT INTO menu_items (restaurant_id, name, price, discount, food_type, food_category, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, Long.parseLong(menuItem.getRestaurantId()));
            preparedStatement.setString(2, menuItem.getName());
            preparedStatement.setDouble(3, menuItem.getPrice());
            preparedStatement.setDouble(4, menuItem.getDiscount());
            preparedStatement.setString(5, menuItem.getFoodType().name());
            preparedStatement.setString(6, menuItem.getFoodCategory().name());
            preparedStatement.setString(7, menuItem.getStatus().name());

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (resultSet.next()) {
                menuItem.setId(String.valueOf(resultSet.getLong(1)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void update(MenuItem menuItem) {
        String query = "UPDATE menu_items SET restaurant_id=?, name=?, price=?, discount=?, food_type=?, food_category=?, status=? WHERE menu_item_id=?";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, Long.parseLong(menuItem.getRestaurantId()));
            preparedStatement.setString(2, menuItem.getName());
            preparedStatement.setDouble(3, menuItem.getPrice());
            preparedStatement.setDouble(4, menuItem.getDiscount());
            preparedStatement.setString(5, menuItem.getFoodType().name());
            preparedStatement.setString(6, menuItem.getFoodCategory().name());
            preparedStatement.setString(7, menuItem.getStatus().name());
            preparedStatement.setLong(8, Long.parseLong(menuItem.getId()));

            int rows = preparedStatement.executeUpdate();

            if (rows == 0) {
                throw new MenuItemNotFoundException(MessageConstants.MENU_ITEM_NOT_FOUND);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public MenuItem findById(String id) {
        String query = "SELECT * FROM menu_items WHERE menu_item_id = ?";

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, Long.parseLong(id));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                MenuItem menuItem = new MenuItem();

                menuItem.setId(String.valueOf(resultSet.getLong("menu_item_id")));
                menuItem.setRestaurantId(String.valueOf(resultSet.getLong("restaurant_id")));
                menuItem.setName(resultSet.getString("name"));
                menuItem.setPrice(resultSet.getDouble("price"));
                menuItem.setDiscount(resultSet.getDouble("discount"));
                menuItem.setFoodType(FoodType.valueOf(resultSet.getString("food_type")));
                menuItem.setFoodCategory(FoodCategory.valueOf(resultSet.getString("food_category")));
                menuItem.setStatus(Status.valueOf(resultSet.getString("status")));

                return menuItem;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
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
        String query = "SELECT * FROM menu_items WHERE restaurant_id = ? AND LOWER(name) = LOWER(?)";

        try {
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setLong(1, Long.parseLong(restaurantId));
            preparedStatement.setString(2, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                MenuItem menuItem = new MenuItem();

                menuItem.setId(String.valueOf(resultSet.getLong("menu_item_id")));
                menuItem.setRestaurantId(String.valueOf(resultSet.getLong("restaurant_id")));
                menuItem.setName(resultSet.getString("name"));
                menuItem.setPrice(resultSet.getDouble("price"));
                menuItem.setDiscount(resultSet.getDouble("discount"));
                menuItem.setFoodType(FoodType.valueOf(resultSet.getString("food_type")));
                menuItem.setFoodCategory(FoodCategory.valueOf(resultSet.getString("food_category")));
                menuItem.setStatus(Status.valueOf(resultSet.getString("status")));

                return menuItem;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return null;
    }
}
