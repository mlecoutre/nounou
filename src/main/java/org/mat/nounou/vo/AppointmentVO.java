package org.mat.nounou.vo;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * UserVO: E010925
 * Date: 30/10/12
 * Time: 11:49
 */
@XmlRootElement
public class AppointmentVO {

    private Integer appointmentId; //may be null if not exist
    private Integer accountId;   //will be accountId
    private String currentUserName;
    private Integer currentUserId;
    private Integer arrivalUserId;
    private Integer departureUserId;
    private String arrivalUserName;
    private String arrivalDate;
    private String departureUserName;
    private String departureDate;
    private Integer kidId;
    private String kidName;
    private String notes;
    private String declarationType;
    private String date;

    private String duration;

    private List<UserVO> users;
    private List<ChildVO> children;


    public Integer getArrivalUserId() {
        return arrivalUserId;
    }

    public void setArrivalUserId(Integer arrivalUserId) {
        this.arrivalUserId = arrivalUserId;
    }

    public Integer getDepartureUserId() {
        return departureUserId;
    }

    public void setDepartureUserId(Integer departureUserId) {
        this.departureUserId = departureUserId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getDeclarationType() {
        return declarationType;
    }

    public void setDeclarationType(String declarationType) {
        this.declarationType = declarationType;
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
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

    public List<UserVO> getUsers() {
        return users;
    }

    public void setUsers(List<UserVO> users) {
        this.users = users;
    }

    public List<ChildVO> getChildren() {
        return children;
    }

    public void setChildren(List<ChildVO> children) {
        this.children = children;
    }



    @Override
    public String toString() {
        return "AppointmentVO{" +
                "appointmentId=" + appointmentId +
                ", accountId=" + accountId +
                ", currentUserName='" + currentUserName + '\'' +
                ", currentUserId=" + currentUserId +
                ", arrivalUserId=" + arrivalUserId +
                ", departureUserId=" + departureUserId +
                ", arrivalUserName='" + arrivalUserName + '\'' +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", departureUserName='" + departureUserName + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", kidId=" + kidId +
                ", kidName='" + kidName + '\'' +
                ", notes='" + notes + '\'' +
                ", declarationType='" + declarationType + '\'' +
                ", date='" + date + '\'' +
                ", duration='" + duration + '\'' +
                ", users=" + users +
                ", children=" + children +
                '}';
    }
}
