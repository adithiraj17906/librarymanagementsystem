package com.library.dto;

import com.library.entity.User;

public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    // password included for create/login; never returned by GET
    private String password;

    // No-args constructor
    public UserDTO() {
    }

    // All-args constructor
    public UserDTO(Long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public static UserDTO fromEntity(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO(user.getId(), user.getName(), user.getEmail(), user.getPhone());
        // Do NOT copy password back to response
        return dto;
    }

    public User toEntity() {
        return User.builder()
                .id(this.id)
                .name(this.name)
                .email(this.email)
                .phone(this.phone)
                .password(this.password != null ? this.password : "changeme")
                .build();
    }

    // Manual Builder Pattern
    public static UserDTOBuilder builder() {
        return new UserDTOBuilder();
    }

    public static class UserDTOBuilder {
        private Long id;
        private String name;
        private String email;
        private String phone;

        public UserDTOBuilder id(Long id) { this.id = id; return this; }
        public UserDTOBuilder name(String name) { this.name = name; return this; }
        public UserDTOBuilder email(String email) { this.email = email; return this; }
        public UserDTOBuilder phone(String phone) { this.phone = phone; return this; }

        public UserDTO build() {
            return new UserDTO(id, name, email, phone);
        }
    }
}
