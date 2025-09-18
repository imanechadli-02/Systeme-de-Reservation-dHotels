package model;

import java.util.UUID;

public class Client {
    public enum Role { USER, ADMIN }

    private UUID id;
    private String fullName;
    private String email;
    private String password;
    private Role role;

    public Client(String fullName, String email, String password, Role role) {
        this.id = UUID.randomUUID();
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public UUID getId() { return id; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    @Override
    public String toString() {
        return String.format("%s (%s) - %s", fullName, id.toString(), email);
    }
}
