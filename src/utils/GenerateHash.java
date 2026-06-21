package utils;

public class GenerateHash {
    public static void main(String[] args) {
        String hashedPassword = PasswordUtil.hashPassword("Admin123");
        System.out.println("UPDATE staff SET password_hash = '" + hashedPassword + "' WHERE username = 'admin';");
    }
}