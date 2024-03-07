package com.events.events.controllers;

import com.events.events.models.EventImage;
import com.events.events.models.Events;
import com.events.events.repositories.EventImageRepo;
import com.events.events.repositories.EventsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EventsRestController {
    @Autowired
    private EventsRepo repo;
    public EventsRestController (EventsRepo repo) {
        this.repo = repo;
    }

    @Autowired
    private EventImageRepo eventImageRepo;

    @GetMapping("/allevents")
    Collection<FilteredEvent> events() {
        return repo.findAll().stream()
                .map(event -> {
                    FilteredEvent filteredEvent = new FilteredEvent();
                    filteredEvent.setId(event.getId());
                    filteredEvent.setTitle(event.getTitle());
                    filteredEvent.setDescription(event.getDescription());
                    filteredEvent.setCity(event.getCity());
                    filteredEvent.setDepartment(event.getDepartment());
                    filteredEvent.setPrice(event.getPrice());
                    filteredEvent.setStartDate(event.getStartDate());
                    filteredEvent.setEndDate(event.getEndDate());

                    List<String> imageFileNames = event.getImages().stream()
                            .map(EventImage::getImageFileName)
                            .collect(Collectors.toList());
                    filteredEvent.setImages(imageFileNames);

                    return filteredEvent;
                })
                .collect(Collectors.toList());
    }


    @PostMapping("/allevents")
    Events newEvent(@RequestBody Events newEventRequest) {
        return repo.save(newEventRequest);
    }


    @PutMapping("/allevents/{id}")
    Events replaceEvent(@RequestBody Events newEvent, @PathVariable Long id) {

        return repo.findById(id)
                .map(event -> {
                    event.setTitle(newEvent.getTitle());
                    event.setDescription(newEvent.getDescription());
                    event.setCity(newEvent.getCity());
                    event.setDepartment(newEvent.getDepartment());
                    event.setPrice(newEvent.getPrice());
                    event.setStartDate(newEvent.getStartDate());
                    event.setEndDate(newEvent.getEndDate());
                    return repo.save(event);
                })
                .orElseGet(() -> {
                    newEvent.setId(id);
                    return repo.save(newEvent);
                });
    }

    @DeleteMapping("/allevents/{id}")
    void deleteEvent(@PathVariable Long id) {
        repo.deleteById(id);
    }

}



class FilteredEvent {

    private Long id;
    private String title;
    private String description;
    private String city;
    private String department;
    private double price;
    private Date startDate;
    private Date endDate;
    private List<String> images;

    // Constructors, Getters, and Setters

    public FilteredEvent() {
    }

    public FilteredEvent(Long id, String title, String description, String city, String department, double price, Date startDate, Date endDate, List<String> images) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.city = city;
        this.department = department;
        this.price = price;
        this.startDate = startDate;
        this.endDate = endDate;
        this.images = images;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}

