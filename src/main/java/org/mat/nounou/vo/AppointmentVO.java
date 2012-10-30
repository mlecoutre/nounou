package org.mat.nounou.vo;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * UserVO: E010925
 * Date: 30/10/12
 * Time: 11:49
 */
@XmlRootElement
public class AppointmentVO {

    private Integer existingAppointmentId;
    private Integer userId;   //will be accountId
    private String currentUserName;
    private String arrivalUserName;
    private String arrivalDate;
    private String departureUserName;
    private String departureDate;
    private Integer kidId;
    private String kidName;
    private String notes;
    private String declarationType;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDeclarationType() {
        return declarationType;
    }

    public void setDeclarationType(String declarationType) {
        this.declarationType = declarationType;
    }

    public Integer getExistingAppointmentId() {
        return existingAppointmentId;
    }

    public void setExistingAppointmentId(Integer existingAppointmentId) {
        this.existingAppointmentId = existingAppointmentId;
    }

    public String getKidName() {
        return kidName;
    }

    public void setKidName(String kidName) {
        this.kidName = kidName;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(String arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public Integer getKidId() {
        return kidId;
    }

    public void setKidId(Integer kidId) {
        this.kidId = kidId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getArrivalUserName() {
        return arrivalUserName;
    }

    public void setArrivalUserName(String arrivalUserName) {
        this.arrivalUserName = arrivalUserName;
    }

    public String getDepartureUserName() {
        return departureUserName;
    }

    public void setDepartureUserName(String departureUserName) {
        this.departureUserName = departureUserName;
    }

    @Override
    public String toString() {
        return "AppointmentVO{" +
                "existingAppointmentId=" + existingAppointmentId +
                ", userId=" + userId +
                ", arrivalUserName='" + arrivalUserName + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", departureUserName='" + departureUserName + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", kidId=" + kidId +
                ", kidName='" + kidName + '\'' +
                ", notes='" + notes + '\'' +
                ", declarationType='" + declarationType + '\'' +
                '}';
    }
}
