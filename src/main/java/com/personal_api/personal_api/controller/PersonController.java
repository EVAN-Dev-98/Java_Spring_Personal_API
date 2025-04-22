package com.personal_api.personal_api.controller;

import com.personal_api.personal_api.model.Person;
import com.personal_api.personal_api.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/User")
public class PersonController {
    final PersonService personService;
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/Save")
    public Person save(@RequestBody Person person) {
        return personService.save(person);
    }
    @GetMapping("/GetAll")
    public List<Person> getAll() {
        return personService.getAll();
    }
    @GetMapping("/FindById")
    public Person findById(@RequestParam("id") int id) {
        return personService.findById(id);
    }
    @PutMapping("/Update")
    public Person update(@RequestParam("id") int id, @RequestBody Person person) {
        return personService.update(id, person);
    }
    @DeleteMapping("/Delete")
    public String delete(@RequestBody int id) {
        return personService.delete(id);
    }
}
