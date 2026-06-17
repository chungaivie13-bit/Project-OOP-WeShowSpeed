package controllers;

import db.DatabaseConnection;
import models.Supplier;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierController {

    // 1. CREATE
    public boolean addSupplier(Supplier s) {
        String query = "INSERT INTO suppliers (supplier_name, contact_number, email, address, status) VALUES (?, ?, ?, ?, 'Active')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, s.getSupplierName());
            stmt.setString(2, s.getContactNumber());
            stmt.setString(3, s.getEmail());
            stmt.setString(4, s.getAddress());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. READ (Active only)
    public List<Supplier> getAllActiveSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM suppliers WHERE status = 'Active'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                suppliers.add(new Supplier(
                    rs.getInt("supplier_id"), rs.getString("supplier_name"),
                    rs.getString("contact_number"), rs.getString("email"),
                    rs.getString("address"), rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    // 3. UPDATE
    public boolean updateSupplier(int supplierId, String newContact, String newEmail) {
        String query = "UPDATE suppliers SET contact_number = ?, email = ? WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, newContact);
            stmt.setString(2, newEmail);
            stmt.setInt(3, supplierId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. SOFT DELETE
    public boolean softDeleteSupplier(int supplierId) {
        String query = "UPDATE suppliers SET status = 'Inactive' WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, supplierId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. SEARCH
    public List<Supplier> searchSupplierByName(String keyword) {
        List<Supplier> suppliers = new ArrayList<>();
        String query = "SELECT * FROM suppliers WHERE supplier_name LIKE ? AND status = 'Active'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                suppliers.add(new Supplier(
                    rs.getInt("supplier_id"), rs.getString("supplier_name"),
                    rs.getString("contact_number"), rs.getString("email"),
                    rs.getString("address"), rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }
}