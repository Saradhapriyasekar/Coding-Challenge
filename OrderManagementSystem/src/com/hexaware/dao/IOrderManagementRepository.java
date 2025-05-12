package com.hexaware.dao;

import java.util.List;

import com.hexaware.entity.Product;
import com.hexaware.entity.User;
import com.hexaware.exception.OrderNotFoundException;
import com.hexaware.exception.UserNotFoundException;

public interface IOrderManagementRepository {
	void createUser(User user) throws Exception;

    void createProduct(User user, Product product) throws Exception;

    void createOrder(User user, List<Product> productList) throws Exception;

    void cancelOrder(int userId, int orderId) throws UserNotFoundException, OrderNotFoundException;

    List<Product> getAllProducts() throws Exception;

    List<Product> getOrderByUser(User user) throws UserNotFoundException, Exception;

}
