package controllers;

import db.DatabaseConnection;
import models.Customer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerController {

    // 1. CREATE
    public boolean addCustomer(Customer c) {
        String query = "INSERT INTO customers (company_name, contact_person, phone, email, shipping_address, status) VALUES (?, ?, ?, ?, ?, 'Active')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, c.getCompanyName());
            stmt.setString(2, c.getContactPerson());
            stmt.setString(3, c.getPhone());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getShippingAddress());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. READ (Active only)
    public List<Customer> getAllActiveCustomers() {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers WHERE status = 'Active'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getInt("customer_id"), rs.getString("company_name"),
                    rs.getString("contact_person"), rs.getString("phone"),
                    rs.getString("email"), rs.getString("shipping_address"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    // 3. UPDATE
    public boolean updateCustomer(int customerId, String newContact, String newPhone, String newEmail) {
        String query = "UPDATE customers SET contact_person = ?, phone = ?, email = ? WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, newContact);
            stmt.setString(2, newPhone);
            stmt.setString(3, newEmail);
            stmt.setInt(4, customerId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. SOFT DELETE
    public boolean softDeleteCustomer(int customerId) {
        String query = "UPDATE customers SET status = 'Inactive' WHERE customer_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, customerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. SEARCH
    public List<Customer> searchCustomerByName(String keyword) {
        List<Customer> customers = new ArrayList<>();
        String query = "SELECT * FROM customers WHERE company_name LIKE ? AND status = 'Active'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                customers.add(new Customer(
                    rs.getInt("customer_id"), rs.getString("company_name"),
                    rs.getString("contact_person"), rs.getString("phone"),
                    rs.getString("email"), rs.getString("shipping_address"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }
}