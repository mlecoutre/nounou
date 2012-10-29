package org.mat.nounou.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * User: mlecoutre
 * Date: 28/10/12
 * Time: 12:24
 */
@XmlRootElement
@Entity
public class Nurse {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer nurseId;

    private String firstName;
    private String lastName;
    private String phoneNumber;

   /* @OneToOne(optional = true)
    @JoinColumn(name = "addressId", unique = false, nullable = true, updatable = true)
    private Address address;*/


    public Integer getNurseId() {
        return nurseId;
    }

    public void setNurseId(Integer nurseId) {
        this.nurseId = nurseId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

  /*  public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Nurse nurse = (Nurse) o;

        if (nurseId != null ? !nurseId.equals(nurse.nurseId) : nurse.nurseId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return nurseId != null ? nurseId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Nurse{" +
                "nurseId=" + nurseId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
