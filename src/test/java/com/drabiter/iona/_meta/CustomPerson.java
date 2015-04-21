package com.drabiter.iona._meta;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.drabiter.iona.annotations.MentalModel;

@Table(name = "customperson")
@MentalModel(endpoint = "custom_endpoint")
public class CustomPerson {

    private long id;

    private long socialNumber;

    @Id
    @GeneratedValue
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "social_number")
    public long getSocialNumber() {
        return socialNumber;
    }

    public void setSocialNumber(long socialNumber) {
        this.socialNumber = socialNumber;
    }

}