package com.drabiter.iona._meta;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.drabiter.iona.annotations.MentalModel;

@Entity(name = "customperson")
@MentalModel(endpoint = "custom_endpoint")
public class CustomPerson {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "social_number")
    private long socialNumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSocialNumber() {
        return socialNumber;
    }

    public void setSocialNumber(long socialNumber) {
        this.socialNumber = socialNumber;
    }

}