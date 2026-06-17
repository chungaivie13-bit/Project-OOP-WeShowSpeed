package controllers;

import db.DatabaseConnection;
import models.Order;
import models.OrderItem;
import models.Delivery;
import models.Payment;

import java.sql.*;
import java.util.List;

public class OrderController {

    // ==========================================
    // 1. THE CALCULATION COMPONENT (Rubric Requirement)
    // ==========================================
    private static final double TAX_RATE = 0.06; // 6% Standard Tax

    public double calculateLineTotal(int quantity, double unitPrice) {
        return quantity * unitPrice;
    }

    public double calculateTax(double subtotal) {
        return subtotal * TAX_RATE;
    }

    public double calculateGrandTotal(double subtotal, double tax, double deliveryFee) {
        return subtotal + tax + deliveryFee;
    }

    // ==========================================
    // 2. ORDER PROCESSING (Transactions)
    // ==========================================
    
    /**
     * Creates an order and its associated items simultaneously.
     * Uses SQL Transactions to prevent partial data entry.
     */
    public boolean placeOrder(Order order, List<OrderItem> items) {
        String insertOrderQuery = "INSERT INTO orders (customer_id, order_date, subtotal, tax_amount, grand_total, order_status) VALUES (?, CURDATE(), ?, ?, ?, ?)";
        String insertItemQuery = "INSERT INTO order_items (order_id, material_id, quantity, line_total) VALUES (?, ?, ?, ?)";
        String updateStockQuery = "UPDATE materials SET stock_quantity = stock_quantity - ? WHERE material_id = ?";

        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Start Transaction

            // 1. Insert the Main Order
            int generatedOrderId = -1;
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderQuery, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, order.getCustomerId());
                orderStmt.setDouble(2, order.getSubtotal());
                orderStmt.setDouble(3, order.getTaxAmount());
                orderStmt.setDouble(4, order.getGrandTotal());
                orderStmt.setString(5, "Pending");
                orderStmt.executeUpdate();

                ResultSet rs = orderStmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedOrderId = rs.getInt(1);
                }
            }

            if (generatedOrderId == -1) {
                conn.rollback();
                return false;
            }

            // 2. Insert Order Items & Update Inventory Stock
            try (PreparedStatement itemStmt = conn.prepareStatement(insertItemQuery);
                 PreparedStatement stockStmt = conn.prepareStatement(updateStockQuery)) {
                
                for (OrderItem item : items) {
                    // Link item to the new order
                    itemStmt.setInt(1, generatedOrderId);
                    itemStmt.setInt(2, item.getMaterialId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getLineTotal());
                    itemStmt.addBatch();

                    // Deduct stock from materials table
                    stockStmt.setInt(1, item.getQuantity());
                    stockStmt.setInt(2, item.getMaterialId());
                    stockStmt.addBatch();
                }
                itemStmt.executeBatch();
                stockStmt.executeBatch();
            }

            conn.commit(); // Save all changes if no errors occurred
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback(); // Cancel transaction on error
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                if (conn != null) conn.setAutoCommit(true); // Reset connection state
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    // ==========================================
    // 3. DELIVERY & PAYMENT MANAGEMENT
    // ==========================================

    public boolean scheduleDelivery(Delivery delivery) {
        String query = "INSERT INTO deliveries (order_id, delivery_date, delivery_address, delivery_fee, driver_name, delivery_status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, delivery.getOrderId());
            stmt.setDate(2, delivery.getDeliveryDate());
            stmt.setString(3, delivery.getDeliveryAddress());
            stmt.setDouble(4, delivery.getDeliveryFee());
            stmt.setString(5, delivery.getDriverName());
            stmt.setString(6, delivery.getDeliveryStatus());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean recordPayment(Payment payment) {
        String query = "INSERT INTO payments (order_id, payment_date, amount_paid, payment_method, payment_status) VALUES (?, CURDATE(), ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, payment.getOrderId());
            stmt.setDouble(2, payment.getAmountPaid());
            stmt.setString(3, payment.getPaymentMethod());
            stmt.setString(4, payment.getPaymentStatus());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOrderStatus(int orderId, String newStatus) {
        String query = "UPDATE orders SET order_status = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, newStatus);
            stmt.setInt(2, orderId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}