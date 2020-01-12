package com.szb.jpa.service.impl;

import com.szb.jpa.cache.manager.PersonCacheManager;
import com.szb.jpa.domain.Person;
import com.szb.jpa.domain.QPerson;
import com.szb.jpa.repository.PersonRepository;
import com.szb.jpa.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @ClassName PersonServiceImpl
 * @Description TODO
 * @Author szb
 * @Date 2020/1/11 10:16
 * @Version 1.0
 **/
@Service
@Slf4j
public class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository;
    private PersonCacheManager personCacheManager;

    public PersonServiceImpl(PersonRepository personRepository,
                              PersonCacheManager personCacheManager){
        this.personRepository = personRepository;
        this.personCacheManager = personCacheManager;
    }

    @Override
    public void save(Person person) {
        log.debug("-------save person--------");
        person = personRepository.save(person);
        log.debug("-----------cache person------------");
        personCacheManager.cachePersonDetails(person);
    }

    @Override
    public Person findByCode(String code) {

        QPerson person = QPerson.person;
        return null;
    }
}
