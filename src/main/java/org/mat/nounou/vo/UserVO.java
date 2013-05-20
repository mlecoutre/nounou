package org.mat.nounou.vo;

import org.mat.nounou.model.User;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserVO {

    private Integer userId;
    private Integer accountId;

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private String type;
    private boolean newUser = false;

    public UserVO() {

    }

    public boolean isNewUser() {
        return newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

    public UserVO(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "userId=" + userId +
                ", accountId=" + accountId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public static UserVO populate(User user){
        UserVO vo = new UserVO();
        vo.setAccountId(user.getAccount().getAccountId());
        vo.setEmail(user.getEmail());
        vo.setFirstName(user.getFirstName());
        vo.setLastName(user.getLastName());
        vo.setPassword(user.getPassword());
        vo.setUserId(user.getUserId());
        vo.setPhoneNumber(user.getPhoneNumber());
        return vo;
    }
}
