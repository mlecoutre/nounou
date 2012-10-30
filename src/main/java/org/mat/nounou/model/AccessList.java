package org.mat.nounou.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * User: E010925
 * Date: 29/10/12
 * Time: 18:33
 */
@XmlRootElement
@Entity
public class AccessList {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Integer recordId;

    @OneToOne(optional = true)
    @JoinColumn(name = "accountUser", unique = false, nullable = false)
    private User accountUser;

    @OneToOne(optional = true)
    @JoinColumn(name = "authorizedUser", unique = false, nullable = true)
    private User authorizedUser;

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public User getAccountUser() {
        return accountUser;
    }

    public void setAccountUser(User accountUser) {
        this.accountUser = accountUser;
    }

    public User getAuthorizedUser() {
        return authorizedUser;
    }

    public void setAuthorizedUser(User authorizedUser) {
        this.authorizedUser = authorizedUser;
    }
}
