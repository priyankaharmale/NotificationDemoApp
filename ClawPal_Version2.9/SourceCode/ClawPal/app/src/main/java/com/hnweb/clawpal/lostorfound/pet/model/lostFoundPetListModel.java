package com.hnweb.clawpal.lostorfound.pet.model;

import java.io.Serializable;

/**
 * Created by HNWeb-11 on 8/3/2016.
 */
public class lostFoundPetListModel implements Serializable {
    String lost_found_id, title, name, age_range, gender, type_of_animal, breed_type, report_type, description, location, state, image, reporter_name, reporter_email, reporter_contact, report_date_time, address, latitude,
            longitude, country, zipcode;

    public String getReporter_name() {
        return reporter_name;
    }

    public void setReporter_name(String reporter_name) {
        this.reporter_name = reporter_name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }





    public lostFoundPetListModel(String lost_found_id, String title, String name, String age_range, String gender, String type_of_animal,
                                 String breed_type, String report_type, String description, String location, String state, String image, String reporter_name
                              , String reporter_email, String reporter_contact, String report_date_time, String address, String latitude, String longitude
                                 , String country, String zipcode ) {
        this.lost_found_id = lost_found_id;
        this.title = title;
        this.name = name;
        this.age_range = age_range;
        this.gender = gender;
        this.type_of_animal = type_of_animal;
        this.breed_type = breed_type;
        this.report_type = report_type;
        this.description = description;
        this.location = location;
        this.state = state;
        this.reporter_email = reporter_email;
        this.reporter_contact = reporter_contact;
        this.report_date_time = report_date_time;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
        this.reporter_name = reporter_name;

        this.country=country;
        this.zipcode=zipcode;


    }

    public String getLost_found_id() {
        return lost_found_id;
    }

    public void setLost_found_id(String lost_found_id) {
        this.lost_found_id = lost_found_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge_range() {
        return age_range;
    }

    public void setAge_range(String age_range) {
        this.age_range = age_range;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getType_of_animal() {
        return type_of_animal;
    }

    public void setType_of_animal(String type_of_animal) {
        this.type_of_animal = type_of_animal;
    }

    public String getBreed_type() {
        return breed_type;
    }

    public void setBreed_type(String breed_type) {
        this.breed_type = breed_type;
    }

    public String getReport_type() {
        return report_type;
    }

    public void setReport_type(String report_type) {
        this.report_type = report_type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String imagereporter_name) {
        this.image = image;
    }

    public String getreporter_name() {
        return reporter_name;
    }

    public void setreporter_name(String reporter_name) {
        this.reporter_name = reporter_name;
    }

    public String getReporter_email() {
        return reporter_email;
    }

    public void setReporter_email(String reporter_email) {
        this.reporter_email = reporter_email;
    }

    public String getReporter_contact() {
        return reporter_contact;
    }

    public void setReporter_contact(String reporter_contact) {
        this.reporter_contact = reporter_contact;
    }

    public String getReport_date_time() {
        return report_date_time;
    }

    public void setReport_date_time(String report_date_time) {
        this.report_date_time = report_date_time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
