package org.mat.nounou.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * User: mlecoutre
 * Date: 30/10/12
 * Time: 19:51
 */
@XmlRootElement
@Entity
public class Account {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Integer accountId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name="account_nurses")
    private List<Nurse> nurses;


    public List<Nurse> getNurses() {
        return nurses;
    }

    public void setNurses(List<Nurse> nurses) {
        this.nurses = nurses;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }
}
