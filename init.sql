CREATE DATABASE IF NOT EXISTS coffee_shop;
USE coffee_shop;

CREATE TABLE ingredients (
	    name VARCHAR(255) PRIMARY KEY,
	    quantity INT NOT NULL
);

CREATE TABLE orders (
	    id INT AUTO_INCREMENT PRIMARY KEY,
	    coffee_type VARCHAR(255) NOT NULL,
	    quantity INT NOT NULL,
	    status VARCHAR(255) NOT NULL,
	    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO ingredients (name, quantity) VALUES
('espressoShot', 1000),
('milk', 50000),
('milkFoam', 20000),
('hotWater', 99999999);
