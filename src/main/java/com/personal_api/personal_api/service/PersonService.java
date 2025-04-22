package com.personal_api.personal_api.service;

import com.personal_api.personal_api.model.Person;
import com.personal_api.personal_api.repo.PersonRepo;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponseException;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    final PersonRepo personRepo;

    public PersonService(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    public Person save(Person person) {
        return personRepo.save(person);
    }
    public Person update(int id, Person updatedPerson) {
        Optional<Person> optionalPerson = personRepo.findById(id);
        if (optionalPerson.isPresent()) {
            Person oldPerson = optionalPerson.get();
            oldPerson.setFirst_name(updatedPerson.getFirst_name());
            oldPerson.setLast_name(updatedPerson.getLast_name());
            oldPerson.setEmail(updatedPerson.getEmail());
            oldPerson.setPhone(updatedPerson.getPhone());
            return personRepo.save(oldPerson);
        } else {
            throw new RuntimeException("Cant Find Person!");
        }
    }
    public Person findById(int id) {
        Optional<Person> optionalPerson = personRepo.findById(id);
        if (optionalPerson.isPresent()) {
            return optionalPerson.get();
        } else {
            throw new RuntimeException("Cant Find Person!");
        }
    }
    public List<Person> getAll() {
        return personRepo.findAll();
    }
    public String delete(int id) {
        personRepo.deleteById(id);
        return "Person deleted";
    }
}
