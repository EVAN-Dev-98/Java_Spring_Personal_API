package com.personal_api.personal_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.Date;

@Entity
@Data
public class Person {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank(message = "First name is required")
    private String first_name;

    private String last_name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    private String phone;

    private Date birth_date;

    private String token;

    public Person() {
    }
}