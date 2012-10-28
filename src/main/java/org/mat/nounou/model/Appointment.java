package org.mat.nounou.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
@Entity
@Table( name = "Appointment")
public class Appointment {


    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer appointmentId;

    @Column(name="arrival")
    private Date arrivalDate;

    @Column(name="arrivalPlanned")
    private Date arrivalPlannedDate;

    @Column(name ="PlannedUser")
    private User plannedUser;

    @Column(name ="User")
    private User user;

    private String notes;


    public Appointment() {

    }

}
