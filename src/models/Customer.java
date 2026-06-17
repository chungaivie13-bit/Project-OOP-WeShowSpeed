package models;

public class Customer {
    private int customerId;
    private String companyName;
    private String contactPerson;
    private String phone;
    private String email;
    private String shippingAddress;
    private String status;

    // Constructor for creating a new customer
    public Customer(String companyName, String contactPerson, String phone, String email, String shippingAddress, String status) {
        this.companyName = companyName;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.shippingAddress = shippingAddress;
        this.status = status;
    }

    // Constructor for retrieving from the database
    public Customer(int customerId, String companyName, String contactPerson, String phone, String email, String shippingAddress, String status) {
        this.customerId = customerId;
        this.companyName = companyName;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.email = email;
        this.shippingAddress = shippingAddress;
        this.status = status;
    }

    // Getters
    public int getCustomerId() { return customerId; }
    public String getCompanyName() { return companyName; }
    public String getContactPerson() { return contactPerson; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getShippingAddress() { return shippingAddress; }
    public String getStatus() { return status; }

    // Setters
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }
    public void setStatus(String status) { this.status = status; }
}