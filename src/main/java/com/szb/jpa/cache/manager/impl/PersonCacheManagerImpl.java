package com.szb.jpa.cache.manager.impl;

import com.szb.jpa.cache.manager.PersonCacheManager;
import com.szb.jpa.domain.Person;
import com.szb.redis.SzbRedisUtil;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName PersonCaheManagerImpl
 * @Description TODO
 * @Author szb
 * @Date 2020/1/11 10:05
 * @Version 1.0
 **/
@Component
public class PersonCacheManagerImpl implements PersonCacheManager {

    public static final String TABLE_PERSON = "TABLE_PERSON";
    public static final String PERSON = "PERSON_";
    private SzbRedisUtil<Person> redisUtilPerson;

    public PersonCacheManagerImpl(SzbRedisUtil<Person> redisUtilPerson) {
        this.redisUtilPerson = redisUtilPerson;
    }

    @Override
    public void cachePersonDetails(Person person) {
        redisUtilPerson.putMap(TABLE_PERSON,PERSON+person.getCode(),person);
        redisUtilPerson.setExpire(TABLE_PERSON,1, TimeUnit.DAYS);
    }
}
