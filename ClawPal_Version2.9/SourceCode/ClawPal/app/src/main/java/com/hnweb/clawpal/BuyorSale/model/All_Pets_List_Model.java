package com.hnweb.clawpal.BuyorSale.model;

import java.io.Serializable;

/**
 * Created by HNWeb-11 on 7/23/2016.
 */
public class All_Pets_List_Model implements Serializable{
    String pet_sale_buy_id, type, animal, image, title, gender, description, age_range, price_range, locality, city, breed_type, category, currency,
            email, name, neutered, vaccinated, contact, added_by, date, latitude, longitude, state, country, zipcode;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public All_Pets_List_Model(String pet_sale_buy_id, String type, String animal, String image, String title, String gender, String description, String age_range, String price_range, String locality, String city, String breed_type, String category, String currency,
                               String email, String name, String neutered, String vaccinated, String contact, String added_by, String date, String latitude, String longitude, String state, String country, String zipcode) {
        this.pet_sale_buy_id = pet_sale_buy_id;
        this.type = type;
        this.animal = animal;
        this.image = image;
        this.title = title;
        this.gender = gender;
        this.description = description;
        this.age_range = age_range;
        this.price_range = price_range;
        this.locality = locality;
        this.city = city;
        this.breed_type = breed_type;
        this.category = category;
        this.currency = currency;
        this.email = email;
        this.name = name;
        this.neutered = neutered;
        this.vaccinated = vaccinated;
        this.contact = contact;
        this.added_by = added_by;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.state=state;
        this.city=city;
        this.country=country;
        this.zipcode=zipcode;


    }

    public String getPet_sale_buy_id() {
        return pet_sale_buy_id;
    }

    public void setPet_sale_buy_id(String pet_sale_buy_id) {
        this.pet_sale_buy_id = pet_sale_buy_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnimal() {
        return animal;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBreed_type() {
        return breed_type;
    }

    public void setBreed_type(String breed_type) {
        this.breed_type = breed_type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
