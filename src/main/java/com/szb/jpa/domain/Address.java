package com.szb.jpa.domain;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

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
public class Address {

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "uuid")
    private String id;

    @Column(name = "CITY", length = 20, nullable = false)
    private String city;

    @ManyToOne(cascade=CascadeType.PERSIST, optional=false, fetch= FetchType.LAZY)
    @JoinColumn(name="PERSON_ID")
    private Person person;

}
