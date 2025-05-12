create database order_management;
use order_management;
-- Users Table
CREATE TABLE Users (
    userId INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) CHECK (role IN ('Admin', 'User')) NOT NULL
);

-- Products Table
CREATE TABLE Products (
    productId INT PRIMARY KEY AUTO_INCREMENT,
    productName VARCHAR(255) NOT NULL,
    description TEXT,
    price DOUBLE NOT NULL,
    quantityInStock INT NOT NULL,
    type VARCHAR(50) CHECK (type IN ('Electronics', 'Clothing')) NOT NULL
);

-- Electronics Table
CREATE TABLE Electronics (
    productId INT PRIMARY KEY,
    brand VARCHAR(255) NOT NULL,
    warrantyPeriod INT NOT NULL,
    FOREIGN KEY (productId) REFERENCES Products(productId)
);

-- Clothing Table
CREATE TABLE Clothing (
    productId INT PRIMARY KEY,
    size VARCHAR(50) NOT NULL,
    color VARCHAR(50) NOT NULL,
    FOREIGN KEY (productId) REFERENCES Products(productId)
);

-- Orders Table
CREATE TABLE Orders (
    orderId INT PRIMARY KEY AUTO_INCREMENT,
    userId INT,
    orderDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (userId) REFERENCES Users(userId)
);

-- OrderProducts Table
CREATE TABLE OrderProducts (
    orderId INT,
    productId INT,
    quantity INT NOT NULL,
    PRIMARY KEY (orderId, productId),
    FOREIGN KEY (orderId) REFERENCES Orders(orderId),
    FOREIGN KEY (productId) REFERENCES Products(productId)
);

-- Insert into Users Table
INSERT INTO Users (username, password, role) 
VALUES 
('adminUser', 'adminPass2025', 'Admin'),
('janeDoe', 'janePass123', 'User'),
('johnSmith', 'johnPass456', 'User'),
('aliceJohnson', 'alicePass789', 'Admin');

-- Insert into Products Table
INSERT INTO Products (productName, description, price, quantityInStock, type) 
VALUES 
('iPhone 14', 'Latest model with 128GB storage, 5G capability', 999.99, 500, 'Electronics'),
('Samsung Galaxy S23', 'Flagship model with 256GB storage, AMOLED display', 849.99, 400, 'Electronics'),
('Nike Air Max 270', 'Comfortable and stylish running shoes', 130.00, 150, 'Clothing'),
('Adidas Hoodie', 'Soft fleece hoodie with adjustable drawstrings', 55.00, 300, 'Clothing'),
('Sony WH-1000XM5', 'Noise-cancelling over-ear headphones', 348.00, 200, 'Electronics');

-- Insert into Electronics Table
INSERT INTO Electronics (productId, brand, warrantyPeriod) 
VALUES 
(1, 'Apple', 1),
(2, 'Samsung', 2),
(5, 'Sony', 2);

-- Insert into Clothing Table
INSERT INTO Clothing (productId, size, color) 
VALUES 
(3, '10', 'White'),
(4, 'L', 'Black');

-- Insert into Orders Table
INSERT INTO Orders (userId) 
VALUES 
(2),
(3),
(4);

-- Insert into OrderProducts Table
INSERT INTO OrderProducts (orderId, productId, quantity) 
VALUES 
(1, 1, 1),
(1, 4, 1),
(2, 2, 2),
(2, 3, 1),
(3, 5, 1);

select * from users;