package org.mat.nounou.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

/**
 * User: mlecoutre
 * Date: 28/10/12
 * Time: 12:25
 */
public class Child {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer childId;

    private String firstName;
    private String lastName;
    private Nurse nurse;


}
