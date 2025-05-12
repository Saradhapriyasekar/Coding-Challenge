package com.hexaware.main;

import java.util.List;
import java.util.Scanner;

import com.hexaware.dao.OrderProcessor;
import com.hexaware.entity.Product;
import com.hexaware.entity.User;
import com.hexaware.entity.Clothing;
import com.hexaware.entity.Electronics;
import com.hexaware.exception.OrderNotFoundException;
import com.hexaware.exception.UserNotFoundException;

public class OrderManagement {

    private static OrderProcessor orderProcessor = new OrderProcessor();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nOrder Management System");
            System.out.println("1. Create User");
            System.out.println("2. Create Product");
            System.out.println("3. Cancel Order");
            System.out.println("4. Get All Products");
            System.out.println("5. Get Orders by User");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter username: ");
                    String username = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    System.out.print("Enter role (Admin/User): ");
                    String role = scanner.nextLine();
                    System.out.print("Enter user ID: ");
                    int userId = scanner.nextInt();
                    scanner.nextLine();

                    User newUser = new User(userId, username, password, role);

                    try {
                        orderProcessor.createUser(newUser);
                        System.out.println("User created successfully.");
                    } catch (Exception e) {
                        System.out.println("Error creating user: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    System.out.print("Enter product type (Electronics/Clothing): ");
                    String type = scanner.nextLine();
                    System.out.print("Enter product ID: ");
                    int productId = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter product name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter product description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter product price: ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter quantity in stock: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    Product newProduct;
                    if (type.equalsIgnoreCase("Electronics")) {
                        System.out.print("Enter brand: ");
                        String brand = scanner.nextLine();
                        System.out.print("Enter warranty period: ");
                        int warranty = scanner.nextInt();
                        scanner.nextLine();
                        newProduct = new Electronics(productId, name, description, price, quantity, type, brand, warranty);
                    } else {
                        System.out.print("Enter size: ");
                        String size = scanner.nextLine();
                        System.out.print("Enter color: ");
                        String color = scanner.nextLine();
                        newProduct = new Clothing(productId, name, description, price, quantity, type, size, color);
                    }

                    try {
                        orderProcessor.createProduct(newProduct);
                        System.out.println("Product created successfully.");
                    } catch (Exception e) {
                        System.out.println("Error creating product: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    try {
                        System.out.print("Enter user ID: ");
                        int uId = scanner.nextInt();
                        System.out.print("Enter order ID: ");
                        int oId = scanner.nextInt();
                        scanner.nextLine();

                        orderProcessor.cancelOrder(uId, oId);
                        System.out.println("Order cancelled successfully.");
                    } catch (OrderNotFoundException e) {
                        System.out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Unexpected error: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                case 4:
                    try {
                        System.out.println("All Products:");
                        List<Product> productList = orderProcessor.getAllProducts();
                        if (productList.isEmpty()) {
                            System.out.println("No products found.");
                        } else {
                            for (Product p : productList) {
                                System.out.println("ID: " + p.getProductId() +
                                        ", Name: " + p.getProductName() +
                                        ", Price: " + p.getPrice() +
                                        ", Type: " + p.getType());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error fetching products: " + e.getMessage());
                    }
                    break;

                case 5:
                    System.out.print("Enter user ID: ");
                    int orderUserId = scanner.nextInt();
                    scanner.nextLine();
                    User dummyUser = new User(orderUserId, "", "", "");
                    try {
                        List<Product> userOrders = orderProcessor.getOrderByUser(dummyUser);
                        if (userOrders.isEmpty()) {
                            System.out.println("No orders found for this user.");
                        } else {
                            for (Product p : userOrders) {
                                System.out.println("Product ID: " + p.getProductId() +
                                        ", Name: " + p.getProductName() +
                                        ", Price: " + p.getPrice());
                            }
                        }
                    } catch (UserNotFoundException e) {
                        System.out.println("User not found: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Error fetching user orders: " + e.getMessage());
                        e.printStackTrace();
                    }
                    break;

                case 6:
                    System.out.println("Exiting... Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}
