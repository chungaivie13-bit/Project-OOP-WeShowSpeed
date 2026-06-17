package main;

import controllers.*;
import models.*;
import java.util.Scanner;
import java.util.List;

public class Main {
    // Initialize Controllers for all 4 Modules
    private static StaffController staffController = new StaffController();
    private static MaterialController materialController = new MaterialController();
    private static CustomerController customerController = new CustomerController();
    private static SupplierController supplierController = new SupplierController();
    private static OrderController orderController = new OrderController();

    private static Scanner scanner = new Scanner(System.in);
    private static Staff loggedInUser = null;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("  CONSTRUCTION MATERIAL SUPPLY MANAGEMENT SYSTEM  ");
        System.out.println("=================================================");

        // MODULE 1: AIVIE'S LOGIN GATEWAY
        while (loggedInUser == null) {
            System.out.println("\n--- SYSTEM LOGIN ---");
            System.out.print("Username: ");
            String username = scanner.nextLine();
            System.out.print("Password: ");
            String password = scanner.nextLine();

            loggedInUser = staffController.authenticateLogin(username, password);

            if (loggedInUser == null) {
                System.out.println(">> Error: Invalid credentials or inactive account. Try again.");
            } else {
                System.out.println(">> Login Successful! Welcome, " + loggedInUser.getFullName() + " (" + loggedInUser.getRole() + ")");
            }
        }

        // MAIN DASHBOARD ROUTING
        boolean isRunning = true;
        while (isRunning) {
            System.out.println("\n================ MAIN DASHBOARD ================");
            System.out.println("1. System & Staff Module (Aivie)");
            System.out.println("2. Material & Inventory Module (Danish)");
            System.out.println("3. Customer & Supplier Module (Umie)");
            System.out.println("4. Order & Calculation Module (Harith)");
            System.out.println("5. Logout & Exit");
            System.out.println("================================================");
            System.out.print("Select a module to test (1-5): ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    testStaffModule();
                    break;
                case 2:
                    testMaterialModule();
                    break;
                case 3:
                    testCustomerSupplierModule();
                    break;
                case 4:
                    testOrderModule();
                    break;
                case 5:
                    System.out.println("Logging out... Goodbye!");
                    isRunning = false;
                    break;
                default:
                    System.out.println(">> Invalid choice. Please select 1-5.");
            }
        }
        scanner.close();
    }

    // ==========================================
    // MODULE TESTING MENUS
    // ==========================================

    private static void testStaffModule() {
        System.out.println("\n--- STAFF MODULE ---");
        System.out.println("Fetching all active staff...");
        List<Staff> staffList = staffController.getAllActiveStaff();
        for (Staff s : staffList) {
            System.out.println("ID: " + s.getStaffId() + " | Name: " + s.getFullName() + " | Role: " + s.getRole());
        }
    }

    private static void testMaterialModule() {
        System.out.println("\n--- MATERIAL MODULE ---");
        System.out.println("Fetching available inventory...");
        List<Material> materials = materialController.getAllAvailableMaterials();
        for (Material m : materials) {
            System.out.println("Item: " + m.getName() + " | Stock: " + m.getStockQuantity() + " | Price: RM" + m.getUnitPrice());
        }
    }

    private static void testCustomerSupplierModule() {
        System.out.println("\n--- CUSTOMER & SUPPLIER MODULE ---");
        System.out.println("1. View Active Customers");
        System.out.println("2. View Active Suppliers");
        System.out.print("Choice: ");
        int choice = scanner.nextInt();
        
        if (choice == 1) {
            List<Customer> customers = customerController.getAllActiveCustomers();
            for (Customer c : customers) {
                System.out.println("Company: " + c.getCompanyName() + " | Contact: " + c.getContactPerson());
            }
        } else if (choice == 2) {
            List<Supplier> suppliers = supplierController.getAllActiveSuppliers();
            for (Supplier s : suppliers) {
                System.out.println("Supplier: " + s.getSupplierName() + " | Phone: " + s.getContactNumber());
            }
        }
    }

    private static void testOrderModule() {
        System.out.println("\n--- ORDER & CALCULATION MODULE ---");
        System.out.println("Testing Calculation Component (Rubric Requirement)...");
        System.out.print("Enter a test subtotal amount (RM): ");
        double subtotal = scanner.nextDouble();
        
        double tax = orderController.calculateTax(subtotal);
        double grandTotal = orderController.calculateGrandTotal(subtotal, tax, 50.00); // Assuming flat RM50 delivery fee
        
        System.out.println(">> Subtotal: RM " + String.format("%.2f", subtotal));
        System.out.println(">> Tax (6%): RM " + String.format("%.2f", tax));
        System.out.println(">> Delivery: RM 50.00");
        System.out.println(">> GRAND TOTAL: RM " + String.format("%.2f", grandTotal));
    }
}