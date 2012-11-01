package org.mat.nounou.vo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * UserVO: mlecoutre
 * Date: 28/10/12
 * Time: 12:24
 */
@XmlRootElement
public class NurseVO {

    private Integer nurseId;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public String getNurseName() {
        return getFirstName().concat(" ").concat(getLastName());
    }


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
