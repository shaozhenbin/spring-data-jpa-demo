package com.szb.jpa;

import com.szb.jpa.config.AppConfig;
import com.szb.jpa.domain.Address;
import com.szb.jpa.domain.Person;
import com.szb.jpa.repository.AddressRepository;
import com.szb.jpa.repository.PersonRepository;
import com.szb.jpa.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.FetchType;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ClassName ManyToManyTest
 * @Description TODO
 * @Author szb
 * @Date 2020/1/5 22:25
 * @Version 1.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
@Slf4j
public class ManyToManyTest {

    private static final Logger logger
            = LoggerFactory.getLogger(ManyToManyTest.class);

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private AddressRepository addressRepository;

    // 注意：1.谁拥有外键谁就是关系维护端，拿这个例子说，Student就是关系维护端，而Teacher是关系被维护端；
    // 2.惰性加载，一般来说，关系维护端配置为fetch=FetchType.LAZY。
    //3.拥有mappedBy注解的实体类为关系被维护端，另外的实体类为关系维护端的。
    // 顾名思意，关系的维护端对关系（在多对多为中间关联表）的CRUD做操作。关系的被维护端没有该操作，不能维护关系。
    //4.关系维护端删除时，如果中间表存在些纪录的关联信息，则会删除该关联信息;关系被维护端删除时，
    // 如果中间表存在些纪录的关联信息，则会删除失败 .

    /**
     * @Title 实体之间多对多关系配置如下，假如一个人有多个地址，同一个地址有很多人
     * @Author szb
     * @Description
     * @Date 22:44 2020/1/5
     * @param
     * @return void
     **/
    @Test
    public void 保存用户() {

        Person person = Person.builder()
                .code("005")
                .name("xiaoliu")
                .build();
        personRepository.save(person);
        logger.debug("-----保存用户成功---------");
    }

    @Test
    public void 保存用户_同时绑定地址() {

        Address address1 = Address.builder()
                .city("家乡地址：广东茂名")
                .build();

        Address address2 = Address.builder()
                .city("工作地址：福建厦门")
                .build();

        Set<Address> set = new HashSet<>();
        set.add(address1);
        set.add(address2);

        Person person = Person.builder()
                .code("001")
                .name("szb")
                .addressSet(set)
                .build();

        //双向绑定,不然报错
        Set<Person> personSet = new HashSet<>();
        personSet.add(person);

        address1.setPersonSet(personSet);
        address2.setPersonSet(personSet);

        personRepository.save(person);

    }

    @Test
    public void 绑定用户地址() {

        Person person = personRepository.findByCode("005").orElseThrow(
                () -> new RuntimeException("person code is not exists")
        );

        Set<Person> personSet = new HashSet<>();
        personSet.add(person);

        //双向绑定,不然报错
        Address address1 = Address.builder()
                .city("工作地址：广东深圳")
                .personSet(personSet)
                .build();

        Address address2 = Address.builder()
                .city("大学地址：江西南昌")
                .personSet(personSet)
                .build();
        person.add(address1).add(address2);

        personRepository.save(person);
        log.debug("------------success---------------");
    }

    @Test
    @Transactional
    public void 查询用户信息() {

        Person person = personRepository.findByCode("002").orElseThrow(
                () -> new RuntimeException("person code is not exists")
        );

        logger.debug("person -------> {}", person.toString());

        for (Address address : person.getAddressSet()) {
            logger.debug("address ----------> {}", address.getCity());
        }
    }

    @Test
    public void 修改用户地址() {

        Person person = personRepository.findByCode("001").orElseThrow(
                () -> new RuntimeException("person code is not exists")
        );

        person.getAddressSet().stream().forEach(address -> {
            address.setCity(address.getCity().concat("6666666"));
        });

        personRepository.save(person);
    }

    @Test
    public void 删除用户() {
        Person person = personRepository.findByCode("001").orElseThrow(
                () -> new RuntimeException("person code is not exists")
        );
        personRepository.delete(person);
    }

    @Test
    public void 删除用户地址() {

        Person person = personRepository.findByCode("005").orElseThrow(
                () -> new RuntimeException("person code is not exists")
        );
        //删除关系外键得关系维护端删除
        addressRepository.delete(person.getAddressSet().stream().findFirst().get());

    }

    @Test
    public void 查询地址信息() {

        List<Address> list =  addressRepository.findAll().stream().collect(Collectors.toList());
        Person person ;
        log.debug("-----------获取用户信息---------------");
        for (Address address : list) {
            logger.debug("-----------获取用户信息地址---------------");
//            person = address.getPerson();
//            logger.debug("person -------------> {}", person.toString());
        }

    }

    @Test
    public void testLog(){
        log.trace("This is a TRACE log");
        log.debug("This is a DEBUG log");
        log.info("This is an INFO log");
        log.error("This is an ERROR log");
    }

    @Autowired
    private PersonService personService;

    @Test
    public void 查询用户地址信息() {
        personService.getPersonAddress("001");
    }
}
