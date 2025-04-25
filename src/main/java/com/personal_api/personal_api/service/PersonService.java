package com.personal_api.personal_api.service;

import java.util.*;
import com.personal_api.personal_api.model.Person;
import com.personal_api.personal_api.repo.PersonRepo;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PersonService {
    final PersonRepo personRepo;

    public PersonService(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    public String  register(Person person) {
        if (person.getFirst_name().isEmpty())
            return "First Name is Required!";

        if (person.getEmail().isEmpty())
            return "Email is Required!";

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern emailpattern = Pattern.compile(emailRegex);
        Matcher emailmatcher = emailpattern.matcher(person.getEmail());
        if (!emailmatcher.matches())
            return "Invalid email format!";

        if (personRepo.findByEmail(person.getEmail()).isPresent())
            return "The Email is already registered!";

        String passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        Pattern passwordpattern = Pattern.compile(passwordRegex);
        Matcher passwordmatcher = passwordpattern.matcher(person.getPassword());

        if (!passwordmatcher.matches())
            return "Password must be at least 8 characters and contain both letters and numbers!";

        person.setRole("normal");
        String HashedPassword = Password.HashPassword(person.getPassword());
        person.setPassword(HashedPassword);
        personRepo.save(person);
        return "Registered Successfully";
    }
    public String login(LoginRequest loginRequest) {
        Optional<Person> person = personRepo.findByEmail(loginRequest.getEmail());
        if (person.isEmpty())
            return ("Email does not exist! Do Register First!");

        if (!Password.CheckPassword(loginRequest.getPassword(), person.get().getPassword()))
            return ("Incorrect Password!");

        Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        JwtBuilder builder = Jwts.builder();
        builder.claim("email", person.get().getEmail());
        builder.claim("role", person.get().getRole());
        builder.setIssuedAt(new Date());
        builder.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        builder.signWith(SignatureAlgorithm.HS256, secretKey);
        String Token = builder.compact();
        person.get().setToken(Token);
        personRepo.save(person.get());
        return person.get().getToken();
    }
    public String update(int id, Person updatedPerson) {
        Optional<Person> person = personRepo.findById(id);
        if (person.isPresent()) {
            person.get().setFirst_name(updatedPerson.getFirst_name());
            person.get().setLast_name(updatedPerson.getLast_name());
            person.get().setEmail(updatedPerson.getEmail());
            person.get().setPhone(updatedPerson.getPhone());
            person.get().setBirth_date(updatedPerson.getBirth_date());
            personRepo.save(person.get());
            return "Updated Successfully";
        } else
            return "Cant Find Person!";
    }
    public Person findById(int id) {
        Optional<Person> optionalPerson = personRepo.findById(id);
        if (optionalPerson.isPresent())
            return optionalPerson.get();
        else
            throw new RuntimeException("Cant Find Person!");
    }
    public List<Person> getAll() {
        return personRepo.findAll();
    }
    public String delete(int id) {
        personRepo.deleteById(id);
        return "Person deleted";
    }
}
