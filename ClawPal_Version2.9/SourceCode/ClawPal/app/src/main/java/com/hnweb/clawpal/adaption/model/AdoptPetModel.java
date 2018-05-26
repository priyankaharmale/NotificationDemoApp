package com.hnweb.clawpal.adaption.model;

import java.io.Serializable;

/**
 * Created by HNWeb-11 on 8/3/2016.
 */
public class AdoptPetModel implements Serializable {
    String pet_adoption_id, user_id, pet_adoption_photo, gender, short_description, location, state, age_range, price_range, breed_type, size_type,
            compatibility, neutered, vaccinated, contact, added_by, adoption_type, type_of_animal, address, latitude, longitude,adaptionDate,email,country,zip;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public AdoptPetModel(String pet_adoption_id, String user_id, String pet_adoption_photo, String gender, String short_description,
                         String location, String state, String age_range, String price_range, String breed_type, String size_type,
                         String compatibility, String neutered, String vaccinated, String contact, String added_by, String adoption_type,
                         String type_of_animal, String address, String latitude, String longitude , String adaptionDate, String email, String country, String zip) {


        this.pet_adoption_id = pet_adoption_id;
        this.user_id = user_id;
        this.pet_adoption_photo = pet_adoption_photo;
        this.gender = gender;
        this.short_description = short_description;
        this.location = location;
        this.state = state;
        this.age_range = age_range;
        this.price_range = price_range;
        this.breed_type = breed_type;
        this.size_type = size_type;
        this.compatibility = compatibility;
        this.neutered = neutered;
        this.vaccinated = vaccinated;
        this.contact = contact;
        this.added_by = added_by;
        this.adoption_type = adoption_type;
        this.type_of_animal = type_of_animal;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adaptionDate = adaptionDate;
        this.address=address;
        this.email=email;
        this.country=country;
        this.zip=zip;




    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getAdaptionDate() {
        return adaptionDate;
    }

    public void setAdaptionDate(String adaptionDate) {
        this.adaptionDate = adaptionDate;
    }
    public String getPet_adoption_id() {
        return pet_adoption_id;
    }

    public void setPet_adoption_id(String pet_adoption_id) {
        this.pet_adoption_id = pet_adoption_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPet_adoption_photo() {
        return pet_adoption_photo;
    }

    public void setPet_adoption_photo(String pet_adoption_photo) {
        this.pet_adoption_photo = pet_adoption_photo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
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

    public String getAge_range() {
        return age_range;
    }

    public void setAge_range(String age_range) {
        this.age_range = age_range;
    }

    public String getPrice_range() {
        return price_range;
    }

    public void setPrice_range(String price_range) {
        this.price_range = price_range;
    }

    public String getBreed_type() {
        return breed_type;
    }

    public void setBreed_type(String breed_type) {
        this.breed_type = breed_type;
    }

    public String getSize_type() {
        return size_type;
    }

    public void setSize_type(String size_type) {
        this.size_type = size_type;
    }

    public String getCompatibility() {
        return compatibility;
    }

    public void setCompatibility(String compatibility) {
        this.compatibility = compatibility;
    }

    public String getNeutered() {
        return neutered;
    }

    public void setNeutered(String neutered) {
        this.neutered = neutered;
    }

    public String getVaccinated() {
        return vaccinated;
    }

    public void setVaccinated(String vaccinated) {
        this.vaccinated = vaccinated;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public String getAdoption_type() {
        return adoption_type;
    }

    public void setAdoption_type(String adoption_type) {
        this.adoption_type = adoption_type;
    }

    public String getType_of_animal() {
        return type_of_animal;
    }

    public void setType_of_animal(String type_of_animal) {
        this.type_of_animal = type_of_animal;
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
