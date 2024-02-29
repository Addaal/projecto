package com.events.events.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public class EventDto {
    @Size(min = 3, message = "Minimum characters: 3")
    @Size(max = 50, message = "Maximum characters: 50")
    private String title;

    @Size(min = 10, message = "Minimum characters: 10")
    @Size(max =  1000, message = "Maximum characters: 1000")
    private String description;

    @NotEmpty(message = "City required")
    private String city;

    @NotEmpty(message = "Department required")
    private String department;

    @Min(0)
    private double price;

    @NotNull(message = "Start date required")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Specify the date format here
    private Date startDate;
    @NotNull(message = "End date required")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Specify the date format here
    private Date endDate;

    private List<MultipartFile> imageFiles;

    // Constructors
    public EventDto() {
    }

    // Getters and Setters
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

    public List<MultipartFile> getImageFiles() {
        return imageFiles;
    }

    public void setImageFiles(List<MultipartFile> imageFiles) {
        this.imageFiles = imageFiles;
    }
}
