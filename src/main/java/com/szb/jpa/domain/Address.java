package com.szb.jpa.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName Address
 * @Description TODO
 * @Author szb
 * @Date 2020/1/5 22:22
 * @Version 1.0
 **/
@Entity
@Table(name = "address")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "city")
public class Address {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    private String id;

    @Column(name = "CITY", length = 20, nullable = false, unique = true)
    private String city;

    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable (//关联表
            name = "person_address" , //关联表名
            inverseJoinColumns = @JoinColumn (name = "PERSON_ID" ),//被维护端外键
            joinColumns = @JoinColumn (name = "ADDRESS_ID" ))//维护端外键
    private Set<Person> personSet = new HashSet<>();

    public Set<Person> add(Person person) {
        this.personSet.add(person);
        return this.personSet;
    }

}
