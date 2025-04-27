package com.personal_api.personal_api.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class PersonDTO {
    private int id;
    private String first_name;
    private String last_name;
    private String email;
    private String phone;
    private Date birth_date;
}
