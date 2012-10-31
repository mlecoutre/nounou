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

    @OneToOne(optional = true)
    @JoinColumn(name = "accountId", unique = false, nullable = true)
    private Account account;

    @OneToOne(optional = true)
    @JoinColumn(name = "kidId", unique = false)
    private Child child;

    private Date arrivalDate;
    @OneToOne(optional = true)
    @JoinColumn(name = "arrivalUser", unique = false, nullable = true)
    private User arrivalUser;
    private Date arrivalPlannedDate;
    @OneToOne(optional = true)
    @JoinColumn(name = "plannedArrivalUser", unique = false, nullable = true)
    private User plannedArrivalUser;

    private Date departureDate;

    @OneToOne(optional = true)
    @JoinColumn(name = "departureUser", unique = false, nullable = true)
    private User departureUser;
    private Date departurePlannedDate;
    @OneToOne(optional = true)
    @JoinColumn(name = "plannedDepartureUser", unique = false, nullable = true)
    private User plannedDepartureUser;

    private String notes;


    public Appointment() {

    }

    public Appointment(User user, boolean isFuture, Date arrival, Date departure){
           if (isFuture){
               this.departurePlannedDate = departure;
               this.arrivalPlannedDate = arrival;
           }else{
              this.departureDate=departure;
               this.arrivalDate=arrival;
           }
    }

    public Integer getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(Integer appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Date getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(Date arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public User getArrivalUser() {
        return arrivalUser;
    }

    public void setArrivalUser(User arrivalUser) {
        this.arrivalUser = arrivalUser;
    }

    public Date getArrivalPlannedDate() {
        return arrivalPlannedDate;
    }

    public void setArrivalPlannedDate(Date arrivalPlannedDate) {
        this.arrivalPlannedDate = arrivalPlannedDate;
    }

    public User getPlannedArrivalUser() {
        return plannedArrivalUser;
    }

    public void setPlannedArrivalUser(User plannedArrivalUser) {
        this.plannedArrivalUser = plannedArrivalUser;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public User getDepartureUser() {
        return departureUser;
    }

    public void setDepartureUser(User departureUser) {
        this.departureUser = departureUser;
    }

    public Date getDeparturePlannedDate() {
        return departurePlannedDate;
    }

    public void setDeparturePlannedDate(Date departurePlannedDate) {
        this.departurePlannedDate = departurePlannedDate;
    }

    public User getPlannedDepartureUser() {
        return plannedDepartureUser;
    }

    public void setPlannedDepartureUser(User plannedDepartureUser) {
        this.plannedDepartureUser = plannedDepartureUser;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Appointment that = (Appointment) o;

        if (appointmentId != null ? !appointmentId.equals(that.appointmentId) : that.appointmentId != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return appointmentId != null ? appointmentId.hashCode() : 0;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Child getChild() {
        return child;
    }

    public void setChild(Child child) {
        this.child = child;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", account=" + account +
                ", child=" + child +
                ", arrivalDate=" + arrivalDate +
                ", arrivalUser=" + arrivalUser +
                ", arrivalPlannedDate=" + arrivalPlannedDate +
                ", plannedArrivalUser=" + plannedArrivalUser +
                ", departureDate=" + departureDate +
                ", departureUser=" + departureUser +
                ", departurePlannedDate=" + departurePlannedDate +
                ", plannedDepartureUser=" + plannedDepartureUser +
                ", notes='" + notes + '\'' +
                '}';
    }
}
