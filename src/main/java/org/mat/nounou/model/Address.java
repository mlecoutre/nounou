package org.mat.nounou.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * User: mlecoutre
 * Date: 28/10/12
 * Time: 12:29
 */
@Entity
public class Address {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Integer addressId;
    private String address;


}
