package com.personal_api.personal_api.controller;

import com.personal_api.personal_api.dto.LoginRequestDTO;
import com.personal_api.personal_api.dto.PersonDTO;
import com.personal_api.personal_api.model.Person;
import com.personal_api.personal_api.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class PersonController {
    final PersonService personService;
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/register")
    public String  register(@RequestBody Person person) {
        return personService.register(person);
    }
    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO loginRequestDTO) {
        return personService.login(loginRequestDTO);
    }
    @GetMapping("/getAll")
    public List<PersonDTO> getAll() {
        return personService.getAll();
    }
    @GetMapping("/findById")
    public PersonDTO findById(@RequestParam("id") int id) {
        return personService.findById(id);
    }
    @PutMapping("/update")
    public String update(@RequestParam("id") int id, @RequestBody Person person) {
        return personService.update(id, person);
    }
    @DeleteMapping("/delete")
    public String delete(@RequestParam("id") int id) {
        return personService.delete(id);
    }
}
