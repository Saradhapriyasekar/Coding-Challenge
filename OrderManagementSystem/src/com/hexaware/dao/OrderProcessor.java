package com.hexaware.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.hexaware.entity.Product;
import com.hexaware.entity.User;
import com.hexaware.entity.Electronics;
import com.hexaware.entity.Clothing;
import com.hexaware.exception.OrderNotFoundException;
import com.hexaware.exception.UserNotFoundException;
import com.hexaware.util.DBConnUtil;

public class OrderProcessor implements IOrderManagementRepository {

    private Connection connection;

    public OrderProcessor() {
        this.connection = DBConnUtil.getDBConn("db.properties");
    }

    public void createProduct(Product product) throws Exception {
        String sql = "INSERT INTO products (product_id, name, description, price, quantity, type) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, product.getProductId());
            stmt.setString(2, product.getProductName());
            stmt.setString(3, product.getDescription());
            stmt.setDouble(4, product.getPrice());
            stmt.setInt(5, product.getQuantityInStock());
            stmt.setString(6, product.getType());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error creating product: " + e.getMessage(), e);
        }
    }

    @Override
    public void createProduct(User user, Product product) throws Exception {
        System.out.println("User " + user.getUsername() + " is creating a product.");
        createProduct(product);
    }

    @Override
    public void createUser(User user) throws Exception {
        String sql = "INSERT INTO users (user_id, username, password, role) VALUES (?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, user.getUserId());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new Exception("Error creating user: " + e.getMessage(), e);
        }
    }

    @Override
    public void createOrder(User user, List<Product> productList) throws Exception {
        String sql = "INSERT INTO orders (user_id) VALUES (?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, user.getUserId());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(1);

                String orderProductSql = "INSERT INTO order_products (order_id, product_id) VALUES (?, ?)";
                try (PreparedStatement orderProductStmt = connection.prepareStatement(orderProductSql)) {
                    for (Product product : productList) {
                        orderProductStmt.setInt(1, orderId);
                        orderProductStmt.setInt(2, product.getProductId());
                        orderProductStmt.executeUpdate();
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Error creating order: " + e.getMessage(), e);
        }
    }

    @Override
    public void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException {
        String checkUserSql = "SELECT * FROM users WHERE user_id = ?";

        try (PreparedStatement userStmt = connection.prepareStatement(checkUserSql)) {
            userStmt.setInt(1, userId);
            ResultSet rs = userStmt.executeQuery();

            if (!rs.next()) {
                throw new UserNotFoundException("User not found with ID: " + userId);
            }

            String checkOrderSql = "SELECT * FROM orders WHERE order_id = ? AND user_id = ?";
            try (PreparedStatement orderStmt = connection.prepareStatement(checkOrderSql)) {
                orderStmt.setInt(1, orderId);
                orderStmt.setInt(2, userId);
                ResultSet orderRs = orderStmt.executeQuery();

                if (!orderRs.next()) {
                    throw new OrderNotFoundException("Order not found with ID: " + orderId + " for user ID: " + userId);
                }

                String deleteOrderSql = "DELETE FROM orders WHERE order_id = ?";
                try (PreparedStatement deleteStmt = connection.prepareStatement(deleteOrderSql)) {
                    deleteStmt.setInt(1, orderId);
                    deleteStmt.executeUpdate();
                    System.out.println("Order cancelled successfully.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error cancelling order: " + e.getMessage());
        }
    }

    @Override
    public List<Product> getAllProducts() throws Exception {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String name = rs.getString("name");
                String description = rs.getString("description");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String type = rs.getString("type");

                Product product = null;
                if ("Electronics".equalsIgnoreCase(type)) {
                    product = new Electronics(productId, name, description, price, quantity, type, "", 0);
                } else if ("Clothing".equalsIgnoreCase(type)) {
                    product = new Clothing(productId, name, description, price, quantity, type, "", "");
                }
                products.add(product);
            }
        } catch (Exception e) {
            throw new Exception("Error fetching products: " + e.getMessage(), e);
        }
        return products;
    }

    
    @Override
    
    public List<Product> getOrderByUser(User user) throws UserNotFoundException, Exception {

        List<Product> products = new ArrayList<>();

        String checkUserSql = "SELECT * FROM users WHERE user_id = ?";

        try (PreparedStatement userStmt = connection.prepareStatement(checkUserSql)) {
            userStmt.setInt(1, user.getUserId());
            ResultSet rs = userStmt.executeQuery();

            if (!rs.next()) {
                throw new UserNotFoundException("User not found with ID: " + user.getUserId());
            }

            String sql = "SELECT p.* FROM products p "
                       + "JOIN order_products op ON p.product_id = op.product_id "
                       + "JOIN orders o ON op.order_id = o.order_id "
                       + "WHERE o.user_id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, user.getUserId());
                ResultSet productRs = stmt.executeQuery();

                while (productRs.next()) {
                    int productId = productRs.getInt("product_id");
                    String name = productRs.getString("name");
                    String description = productRs.getString("description");
                    double price = productRs.getDouble("price");
                    int quantity = productRs.getInt("quantity");
                    String type = productRs.getString("type");

                    Product product = null;
                    if ("Electronics".equalsIgnoreCase(type)) {
                        product = new Electronics(productId, name, description, price, quantity, type, "", 0);
                    } else if ("Clothing".equalsIgnoreCase(type)) {
                        product = new Clothing(productId, name, description, price, quantity, type, "", "");
                    }
                    products.add(product);
                }
            }
        } catch (Exception e) {
            throw new Exception("Error fetching orders for user: " + e.getMessage(), e);
        }
        return products;
    }

}
