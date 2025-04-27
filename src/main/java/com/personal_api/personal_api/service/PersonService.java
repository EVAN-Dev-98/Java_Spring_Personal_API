package com.personal_api.personal_api.service;

import java.util.*;

import com.personal_api.personal_api.dto.LoginRequestDTO;
import com.personal_api.personal_api.dto.PersonDTO;
import com.personal_api.personal_api.model.Person;
import com.personal_api.personal_api.repo.PersonRepo;
import com.personal_api.personal_api.security.Password;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PersonService {
    final PersonRepo personRepo;
    @Value("${jwt.secret}")
    private String secretKey;
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
    public String login(LoginRequestDTO loginRequestDTO) {
        Optional<Person> person = personRepo.findByEmail(loginRequestDTO.getEmail());
        if (person.isEmpty())
            return ("Email does not exist! Do Register First!");

        if (!Password.CheckPassword(loginRequestDTO.getPassword(), person.get().getPassword()))
            return ("Incorrect Password!");

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
        String currentRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().map(Object::toString).orElse("");
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Person> personOptional = personRepo.findById(id);
        if (personOptional.isEmpty())
            return "Cant Find Person!";
        Person person = personOptional.get();

        if (!person.getEmail().equals(currentEmail) && currentRole.equals("normal"))
            return "Access denied!";

        if (updatedPerson.getFirst_name() != null && !updatedPerson.getFirst_name().isEmpty())
            person.setFirst_name(updatedPerson.getFirst_name());
        if (updatedPerson.getLast_name() != null && !updatedPerson.getLast_name().isEmpty())
            person.setLast_name(updatedPerson.getLast_name());
        if (updatedPerson.getEmail() != null && !updatedPerson.getEmail().isEmpty())
            person.setEmail(updatedPerson.getEmail());
        if (updatedPerson.getPhone() != null && !updatedPerson.getPhone().isEmpty())
            person.setPhone(updatedPerson.getPhone());
        if (updatedPerson.getBirth_date() != null)
            person.setBirth_date(updatedPerson.getBirth_date());
        personRepo.save(person);
        return "Updated Successfully";
    }
    public PersonDTO findById(int id) {
        String currentRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().map(Object::toString).orElse("");
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        Optional<Person> optionalPerson = personRepo.findById(id);
        if (optionalPerson.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cant Find Person!");

        if (!optionalPerson.get().getEmail().equals(currentEmail) && currentRole.equals("normal"))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied!");

        PersonDTO personDTO = new PersonDTO();
        personDTO.setId(optionalPerson.get().getId());
        personDTO.setFirst_name(optionalPerson.get().getFirst_name());
        personDTO.setLast_name(optionalPerson.get().getLast_name());
        personDTO.setEmail(optionalPerson.get().getEmail());
        personDTO.setPhone(optionalPerson.get().getPhone());
        personDTO.setBirth_date(optionalPerson.get().getBirth_date());
        return personDTO;
    }
    public List<PersonDTO> getAll() {
        String currentRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().map(Object::toString).orElse("");
        if (!currentRole.equals("Admin"))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied!");
        List<PersonDTO> List_personDTO = new ArrayList<>();
        for (Person person : personRepo.findAll()) {
            PersonDTO personDTO = new PersonDTO();
            personDTO.setFirst_name(person.getFirst_name());
            personDTO.setLast_name(person.getLast_name());
            personDTO.setEmail(person.getEmail());
            personDTO.setPhone(person.getPhone());
            personDTO.setBirth_date(person.getBirth_date());
            List_personDTO.add(personDTO);
        }
        return List_personDTO;
    }
    public String delete(int id) {
        String currentRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().findFirst().map(Object::toString).orElse("");
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Person> optionalPerson = personRepo.findById(id);
        if (optionalPerson.isEmpty())
            return "Cant Find Person!";
        if (currentRole.equals("normal") && optionalPerson.get().getEmail().equals(currentEmail))
            return "Access denied!";
        personRepo.delete(optionalPerson.get());
        return "Person deleted";
    }
}