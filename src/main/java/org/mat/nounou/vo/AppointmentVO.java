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
    private UserVO arrivalUser;
    private UserVO departureUser;
    private String arrivalDate;
    private String departureDate;
    private List<Integer> kidIds; //availableKid Ids
    private String notes;
    private String declarationType;
    private String date;

    private String duration;

    private List<UserVO> users;
    private List<ChildVO> children;


    public UserVO getArrivalUser() {
        return arrivalUser;
    }

    public void setArrivalUser(UserVO arrivalUser) {
        this.arrivalUser = arrivalUser;
    }

    public UserVO getDepartureUser() {
        return departureUser;
    }

    public void setDepartureUser(UserVO departureUser) {
        this.departureUser = departureUser;
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

    public List<Integer> getKidIds() {
        return kidIds;
    }

    public void setKidIds(List<Integer> kidIds) {
        this.kidIds = kidIds;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
                ", arrivalUser=" + arrivalUser +
                ", departureUser=" + departureUser +
                ", arrivalDate='" + arrivalDate + '\'' +
                ", departureDate='" + departureDate + '\'' +
                ", kidIds=" + kidIds +
                ", notes='" + notes + '\'' +
                ", declarationType='" + declarationType + '\'' +
                ", date='" + date + '\'' +
                ", duration='" + duration + '\'' +
                ", users=" + users +
                ", children=" + children +
                '}';
    }
}
