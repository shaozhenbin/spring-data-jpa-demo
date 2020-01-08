package com.szb.jpa.service;

import com.szb.jpa.domain.Address;
import com.szb.jpa.domain.Person;
import com.szb.jpa.repository.PersonRepository;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName PersonService
 * @Description TODO
 * @Author szb
 * @Date 2020/1/8 16:00
 * @Version 1.0
 **/
@Component
@Slf4j
public class PersonService {

    private PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void getPersonAddress(String personCode){
        log.debug("--------getPersonAddress----------");
        Person person = personRepository.findByCode(personCode).orElseThrow(
                () -> new RuntimeException("person code is not exists")
        );
        person.getAddressSet();
        log.debug("--------getPersonAddress----------success");
    }
}
