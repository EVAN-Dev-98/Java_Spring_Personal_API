package com.personal_api.personal_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import java.util.Date;


@Entity
@Data
public class Person {
    @Id
    @GeneratedValue
    int id;
    String first_name;
    String last_name;
    String email;
    String phone;
    Date birth_date;

    public Person() {

    }
}
