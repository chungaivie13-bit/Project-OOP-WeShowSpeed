package controllers;

import db.DatabaseConnection;
import models.Material;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MaterialController {

    /**
     * CREATE: Adds a new construction material and links it to a supplier.
     */
    public boolean addMaterial(Material material) {
        String query = "INSERT INTO materials (supplier_id, name, category, unit_price, stock_quantity, status) VALUES (?, ?, ?, ?, ?, 'Available')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, material.getSupplierId());
            stmt.setString(2, material.getName());
            stmt.setString(3, material.getCategory());
            stmt.setDouble(4, material.getUnitPrice());
            stmt.setInt(5, material.getStockQuantity());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding material: " + e.getMessage());
            return false;
        }
    }

    /**
     * READ: Retrieves all available inventory items.
     */
    public List<Material> getAllAvailableMaterials() {
        List<Material> materials = new ArrayList<>();
        String query = "SELECT * FROM materials WHERE status = 'Available'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                materials.add(new Material(
                    rs.getInt("material_id"),
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("unit_price"),
                    rs.getInt("stock_quantity"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving inventory: " + e.getMessage());
        }
        return materials;
    }

    /**
     * UPDATE: Modifies the stock quantity and unit price of a specific material.
     */
    public boolean updateMaterialStockAndPrice(int materialId, int newStockQuantity, double newUnitPrice) {
        String query = "UPDATE materials SET stock_quantity = ?, unit_price = ? WHERE material_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, newStockQuantity);
            stmt.setDouble(2, newUnitPrice);
            stmt.setInt(3, materialId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating stock/price: " + e.getMessage());
            return false;
        }
    }

    /**
     * SEARCH: Locates inventory items by name or category quickly.
     */
    public List<Material> searchMaterials(String keyword) {
        List<Material> materials = new ArrayList<>();
        // Searches both the material name and the category for maximum utility
        String query = "SELECT * FROM materials WHERE (name LIKE ? OR category LIKE ?) AND status = 'Available'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                materials.add(new Material(
                    rs.getInt("material_id"),
                    rs.getInt("supplier_id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getDouble("unit_price"),
                    rs.getInt("stock_quantity"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching materials: " + e.getMessage());
        }
        return materials;
    }

    /**
     * SOFT DELETE: Marks a material as 'Discontinued' so it no longer shows up in standard searches,
     * but preserves historical order data.
     */
    public boolean discontinueMaterial(int materialId) {
        String query = "UPDATE materials SET status = 'Discontinued' WHERE material_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, materialId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error discontinuing material: " + e.getMessage());
            return false;
        }
    }
}