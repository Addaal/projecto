package com.events.events.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "events")
public class Events {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;

    private String city;

    private String department;

    private double price;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;
//

    @Column(columnDefinition = "TEXT" , name = "ImageFile")
    private String imageFileName;
//
//    @Column(name = "event_maker_name")
//    private String eventMakerName;
//
//    @Column(name = "event_maker_phone")
//    private String eventMakerPhone;
//
//    @Column(name = "event_maker_email")
//    private String eventMakerEmail;

    // Constructors
    public Events() {
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getImageFileName() {
        return imageFileName;
    }


    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }
}

//

//
//    public String getEventMakerName() {
//        return eventMakerName;
//    }
//
//    public void setEventMakerName(String eventMakerName) {
//        this.eventMakerName = eventMakerName;
//    }
//
//    public String getEventMakerPhone() {
//        return eventMakerPhone;
//    }
//
//    public void setEventMakerPhone(String eventMakerPhone) {
//        this.eventMakerPhone = eventMakerPhone;
//    }
//
//    public String getEventMakerEmail() {
//        return eventMakerEmail;
//    }
//
//    public void setEventMakerEmail(String eventMakerEmail) {
//        this.eventMakerEmail = eventMakerEmail;
//    }
//}
