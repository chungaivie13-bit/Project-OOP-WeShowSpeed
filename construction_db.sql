DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS deliveries;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS materials;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS suppliers;
DROP TABLE IF EXISTS staff;

-- 1. STAFF TABLE (For Login/Authentication)
CREATE TABLE staff (
    staff_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'Manager', 'Staff') DEFAULT 'Staff',
    status ENUM('Active', 'Inactive') DEFAULT 'Active'
);

-- 2. SUPPLIERS TABLE
CREATE TABLE suppliers (
    supplier_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_name VARCHAR(255) NOT NULL,
    contact_number VARCHAR(50),
    email VARCHAR(255),
    address TEXT,
    status ENUM('Active', 'Inactive') DEFAULT 'Active'
);

-- 3. CUSTOMERS TABLE
CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(255) NOT NULL,
    contact_person VARCHAR(255),
    phone VARCHAR(50),
    email VARCHAR(255),
    shipping_address TEXT,
    status ENUM('Active', 'Inactive') DEFAULT 'Active'
);

-- 4. MATERIALS TABLE (Links to Suppliers)
CREATE TABLE materials (
    material_id INT AUTO_INCREMENT PRIMARY KEY,
    supplier_id INT,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    unit_price DECIMAL(10,2) NOT NULL,
    stock_quantity INT DEFAULT 0,
    status ENUM('Available', 'Discontinued') DEFAULT 'Available',
    FOREIGN KEY (supplier_id) REFERENCES suppliers(supplier_id) ON DELETE SET NULL
);

-- 5. ORDERS TABLE (Links to Customers)
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    order_date DATE DEFAULT (CURRENT_DATE), -- MySQL 8.0+ automatic date
    subtotal DECIMAL(10,2) DEFAULT 0.00,
    tax_amount DECIMAL(10,2) DEFAULT 0.00,
    grand_total DECIMAL(10,2) DEFAULT 0.00,
    order_status ENUM('Pending', 'Processing', 'Completed', 'Cancelled') DEFAULT 'Pending',
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE RESTRICT
);

-- 6. ORDER ITEMS TABLE (Bridge between Orders and Materials)
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    material_id INT,
    quantity INT NOT NULL,
    line_total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (material_id) REFERENCES materials(material_id) ON DELETE RESTRICT
);

-- 7. DELIVERIES TABLE (Links to Orders)
CREATE TABLE deliveries (
    delivery_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    delivery_date DATE,
    delivery_address TEXT,
    delivery_fee DECIMAL(10,2) DEFAULT 0.00,
    driver_name VARCHAR(255),
    delivery_status ENUM('In Transit', 'Delivered', 'Failed', 'Pending') DEFAULT 'Pending',
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);

-- 8. PAYMENTS TABLE (Links to Orders)
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    payment_date DATE,
    amount_paid DECIMAL(10,2) NOT NULL,
    payment_method ENUM('Credit Card', 'Bank Transfer', 'Cash') NOT NULL,
    payment_status ENUM('Paid', 'Pending', 'Overdue') DEFAULT 'Pending',
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
);


-- Admin data
INSERT INTO staff (username, password_hash, full_name, role, status)
VALUES ('admin', 'admin123', 'System Administrator', 'Admin', 'Active');
