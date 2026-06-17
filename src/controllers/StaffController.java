package controllers;

import db.DatabaseConnection;
import models.Staff;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StaffController {

    // ==========================================
    // 1. AUTHENTICATION (Login Gateway)
    // ==========================================
    
    /**
     * Authenticates a user against the database.
     * Returns the Staff object if successful, or null if credentials fail.
     */
    public Staff authenticateLogin(String username, String password) {
        String query = "SELECT * FROM staff WHERE username = ? AND password_hash = ? AND status = 'Active'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            // In a real-world scenario, you would hash the incoming password here before comparing.
            stmt.setString(2, password); 
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Staff(
                    rs.getInt("staff_id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("full_name"),
                    rs.getString("role"),
                    rs.getString("status")
                );
            }
        } catch (SQLException e) {
            System.err.println("Database error during login: " + e.getMessage());
        }
        return null; // Authentication failed
    }

    // ==========================================
    // 2. STAFF MANAGEMENT (CRUDS)
    // ==========================================

    /**
     * CREATE: Adds a new staff member to the system.
     */
    public boolean addStaff(Staff staff) {
        String query = "INSERT INTO staff (username, password_hash, full_name, role, status) VALUES (?, ?, ?, ?, 'Active')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, staff.getUsername());
            stmt.setString(2, staff.getPasswordHash());
            stmt.setString(3, staff.getFullName());
            stmt.setString(4, staff.getRole());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding staff: " + e.getMessage());
            return false;
        }
    }

    /**
     * READ: Retrieves a list of all active staff members.
     */
    public List<Staff> getAllActiveStaff() {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT * FROM staff WHERE status = 'Active'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                staffList.add(new Staff(
                    rs.getInt("staff_id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("full_name"),
                    rs.getString("role"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving staff list: " + e.getMessage());
        }
        return staffList;
    }

    /**
     * UPDATE: Modifies the role and name of an existing staff member.
     */
    public boolean updateStaffProfile(int staffId, String newFullName, String newRole) {
        String query = "UPDATE staff SET full_name = ?, role = ? WHERE staff_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, newFullName);
            stmt.setString(2, newRole);
            stmt.setInt(3, staffId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating staff: " + e.getMessage());
            return false;
        }
    }

    /**
     * SOFT DELETE: Marks a staff member as 'Inactive' rather than deleting their record.
     */
    public boolean deactivateStaff(int staffId) {
        String query = "UPDATE staff SET status = 'Inactive' WHERE staff_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, staffId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deactivating staff: " + e.getMessage());
            return false;
        }
    }

    /**
     * SEARCH: Finds active staff members by their username or full name.
     */
    public List<Staff> searchStaff(String keyword) {
        List<Staff> staffList = new ArrayList<>();
        String query = "SELECT * FROM staff WHERE (username LIKE ? OR full_name LIKE ?) AND status = 'Active'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                staffList.add(new Staff(
                    rs.getInt("staff_id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getString("full_name"),
                    rs.getString("role"),
                    rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error searching staff: " + e.getMessage());
        }
        return staffList;
    }
}