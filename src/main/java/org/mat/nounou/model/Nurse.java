package org.mat.nounou.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

/**
 * UserVO: mlecoutre
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


    @ManyToMany(mappedBy="nurses")
    private Set<Account> employers;

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

    public Set<Account> getEmployers() {
        return employers;
    }

    public void setEmployers(Set<Account> employers) {
        this.employers = employers;
    }

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
        return "NurseVO{" +
                "nurseId=" + nurseId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}
