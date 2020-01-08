package com.szb.jpa;

import com.szb.jpa.config.AppConfig;
import com.szb.jpa.domain.Address;
import com.szb.jpa.domain.Person;
import com.szb.jpa.repository.AddressRepository;
import com.szb.jpa.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.FetchType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName OneToOneTest
 * @Description TODO
 * @Author szb
 * @Date 2020/1/5 22:25
 * @Version 1.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class OneToOneTest {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AddressRepository addressRepository;

//    1.谁拥有外键谁就是关系维护端，拿这个例子说，Address就是关系维护端，而Person是关系被维护端；
//    2.惰性加载，一般来说，关系维护端配置为fetch=FetchType.LAZY。
//    3.拥有mappedBy注解的实体类为关系被维护端，另外的实体类为关系维护端的。顾名思意，
//    关系的维护端对关系（在多对多为中间关联表）的CRUD做操作。关系的被维护端没有该操作，不能维护关系。

    /**
     * @Title 实体之间一对一关系配置如下，假如一个人对应一个地址：
     * @Author szb
     * @Description
     * @Date 22:44 2020/1/5
     * @param
     * @return void
     **/
    @Test
    public void 保存用户() {

        Person person = Person.builder()
                .code("002")
                .name("zhangsan")
                .build();
        personRepository.save(person);
    }

    @Test
    public void 保存用户_同时绑定地址() {

        Address address = Address.builder()
                .city("广东茂名")
                .build();

        Person person = Person.builder()
                .code("001")
                .name("szb")
                .address(address)
                .build();

        //双向绑定,不然报错
        address.setPerson(person);
        personRepository.save(person);
    }

    @Test
    public void 绑定用户地址() {

        Person person = personRepository.findByCode("002").orElseThrow(
                () -> new RuntimeException("person code is not exists")
        );

        //双向绑定,不然报错
        Address address = Address.builder()
                .city("广东茂名")
                .person(person)
                .build();

        person.setAddress(address);

        personRepository.save(person);
    }

    @Test
    public void 查询用户信息() {

        Person person = personRepository.findByCode("002").orElseThrow(
                () -> new RuntimeException("person code is not exists")
        );
        person.getAddress();
        logger.info("person -------> {}", person.toString());

    }

    @Test
    public void 修改用户地址() {

        Person person = personRepository.findByCode("001").orElseThrow(
                () -> new RuntimeException("person code is not exists")
        );

        person.getAddress().setCity("广东深圳");

        personRepository.save(person);
    }

    @Test
    public void 删除用户地址() {

        Person person = personRepository.findByCode("001").orElseThrow(
                () -> new RuntimeException("person code is not exists")
        );

        addressRepository.delete(person.getAddress());
    }

    @Test
    public void 查询地址信息() {

        List<Address> list =  addressRepository.findAll().stream().collect(Collectors.toList());
        Person person ;
//        for (Address address : list) {
//            logger.debug("-----------获取用户信息---------------");
//            person = address.getPerson();
//            logger.debug("person -------------> {}", person.toString());
//        }
    }
}
