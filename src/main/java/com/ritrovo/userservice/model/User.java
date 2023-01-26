package com.ritrovo.userservice.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ritrovo.userservice.util.Constants;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "User")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String email;
    private String corporateEmail;
    @Generated(GenerationTime.INSERT)
    @Column(columnDefinition = "ENUM('PENDING', 'ACTIVE', 'INACTIVE')")
    @Enumerated(EnumType.STRING)
    @ColumnDefault(value = "PENDING")
    private Status status;
    private Boolean loginDisabled;
    private String companyName;

    @Column(columnDefinition = "ENUM('MALE', 'FEMALE', 'OTHER')")
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Generated(GenerationTime.INSERT)
    @ColumnDefault(value = Constants.LOCALE_DEFAULT)
    private String locale;

    private Date created_at = new Date();
    private Date last_updated = new Date();
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="dd-MM-yyyy")
    private Date DOB;

    public Date getDOB() {
        return DOB;
    }

    public void setDOB(Date DOB) {
        this.DOB = DOB;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public Date getLast_updated() {
        return last_updated;
    }

    public Integer getId() {
        return id;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCorporateEmail() {
        return corporateEmail;
    }

    public void setCorporateEmail(String corporateEmail) {
        this.corporateEmail = corporateEmail;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(@NonNull Gender gender) {
        this.gender = gender;
    }

    public Boolean getLoginDisabled() {
        return loginDisabled;
    }

    public void setLoginDisabled(Boolean loginDisabled) {
        this.loginDisabled = loginDisabled;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public enum Status {
        PENDING("PENDING"),    // non-corporate verification stage
        ACTIVE("ACTIVE"),     // corporate verified stage.
        INACTIVE("INACTIVE");   // corporate activity suspecious (i.e user have not verified corporate email in the last 3 month )

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Gender{

        FEMALE("FEMALE"),
        MALE("MALE"),
        OTHER("OTHER");
        private final String gender;
        Gender(String value){
            this.gender = value;
        }

        public String getGender(){
            return gender;
        }
    }

}

