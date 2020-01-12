package com.szb.jpa.service;

import com.szb.jpa.domain.Person;

public interface PersonService {

    void save(Person person);

    Person findByCode(String code);

}
