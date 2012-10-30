package org.mat.nounou.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.security.PrivateKey;
import java.util.Date;
import java.util.List;

/**
 * User: mlecoutre
 * Date: 28/10/12
 * Time: 12:25
 */
@XmlRootElement
@Entity
public class Child {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer childId;

    private String firstName;
    private String lastName;
    private Date birthday;

    @ManyToOne
    @JoinColumn(name="nurseId", nullable=false)
    private Nurse nurse;

    @ManyToOne(optional=true)
    @JoinColumn(name = "userId", unique = false, nullable = false, updatable = true)
    private User accountUser;

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Integer getChildId() {
        return childId;
    }

    public void setChildId(Integer childId) {
        this.childId = childId;
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

    public Nurse getNurse() {
        return nurse;
    }

    public void setNurse(Nurse nurse) {
        this.nurse = nurse;
    }

    public User getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(User accountUser) {
        this.accountUser = accountUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Child child = (Child) o;

        if (childId != null ? !childId.equals(child.childId) : child.childId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return childId != null ? childId.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Child{" +
                "childId=" + childId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthday=" + birthday +
                ", nurse=" + nurse +
                '}';
    }
}
