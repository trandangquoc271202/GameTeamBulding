package vn.edu.hcmuaf.fit.gameteambulding.Model;

import java.time.LocalDateTime;

public class UserInfo {
    private String address;
    private boolean isAdmin;
    private String birthday;
    private String email;
    private boolean isMale;
    private String password;
    private String phone;
    private String username;
    private String documentId;

    public String getDocumentId() {
        return documentId;
    }


    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    // Constructor
    public UserInfo(String address, boolean isAdmin, String birthday, String email,
                    boolean isMale, String password, String phone, String username) {
        this.address = address;
        this.isAdmin = isAdmin;
        this.birthday = birthday;
        this.email = email;
        this.isMale = isMale;
        this.password = password;
        this.phone = phone;
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter methods (generated automatically or manually)
    public UserInfo() {
    }
}