package com.events.events.controllers;

import com.events.events.models.EventDto;
import com.events.events.models.EventImage;
import com.events.events.models.Events;
import com.events.events.repositories.EventImageRepo;
import com.events.events.repositories.EventsRepo;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventsController {
    @Autowired
    private EventsRepo repo;
    @Autowired
    private EventImageRepo eventImageRepo;

    @GetMapping({"", "/"})
    public String showEventsList (Model model) {
        List<Events> events = repo.findAll();
        model.addAttribute("events", events);
        return "events/index";
    }

    @GetMapping("/create")
    public String createEvent (Model model) {
        EventDto eventDto = new EventDto();
        model.addAttribute("eventDto", eventDto);
        return "events/createEvent";
    }



    @PostMapping("/create")
    public String createEvent(
            @Valid @ModelAttribute EventDto eventDto,
            BindingResult result) {

        if (eventDto.getImageFiles() == null || eventDto.getImageFiles().isEmpty()) {
            result.addError(new FieldError("eventDto", "imageFiles", "Missing Images"));
        } else if (eventDto.getImageFiles().size() > 5) {
            result.addError(new FieldError("eventDto", "imageFiles", "Only a maximum of 5 images are allowed"));
        }


        if (result.hasErrors()) {
            return "events/createEvent";
        }

        Events event = new Events();
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setCity(eventDto.getCity());
        event.setDepartment(eventDto.getDepartment());
        event.setPrice(eventDto.getPrice());
        event.setStartDate(eventDto.getStartDate());
        event.setEndDate(eventDto.getEndDate());

        List<EventImage> images = new ArrayList<>();
        Date createdAt = new Date();
        for (MultipartFile file : eventDto.getImageFiles()) {
            if (!file.isEmpty()) {
                try {
                    String uploadDir = "public/images/";
                    String storageFileName = createdAt.getTime() + "-" + file.getOriginalFilename();
                    Path uploadPath = Paths.get(uploadDir);

                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    try (InputStream inputStream = file.getInputStream()) {
                        Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                    }

                    System.out.println("here");
                    System.out.println(storageFileName);

                    EventImage eventImage = new EventImage();
                    eventImage.setEvent(event);
                    eventImage.setImageFileName(storageFileName);
                    images.add(eventImage);

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    // Handle exception
                }
            }
        }

        event.setImages(images); // Set the list of images to the event
        Events savedEvent = repo.save(event);

        return "redirect:/events";
    }


    @GetMapping("/event")
    public String showEvent (Model model, @RequestParam Long id) {
        try{
            Events event = repo.findById(id).get();
            model.addAttribute("event", event);

            LocalDateTime localDateTimeStart = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime localDateTimeEnd = event.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

            int monthStart = localDateTimeStart.getMonthValue();
            int dayStart = localDateTimeStart.getDayOfMonth();

            int monthEnd = localDateTimeEnd.getMonthValue();
            int dayEnd = localDateTimeEnd.getDayOfMonth();

            String monthnameStart = getMonthName(monthStart);
            String monthnameEnd = getMonthName(monthEnd);

            model.addAttribute("monthStart", monthnameStart);
            model.addAttribute("monthEnd", monthnameEnd);
            model.addAttribute("dayStart", dayStart);
            model.addAttribute("dayEnd", dayEnd);


        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/events";
        }

        return "events/eventDetails";
    }


    public static String getMonthName(int month) {
        String monthName;
        switch (month) {
            case 1:
                monthName = "Ene";
                break;
            case 2:
                monthName = "Feb";
                break;
            case 3:
                monthName = "Mar";
                break;
            case 4:
                monthName = "Abr";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "Jun";
                break;
            case 7:
                monthName = "Jul";
                break;
            case 8:
                monthName = "Agos";
                break;
            case 9:
                monthName = "Sept";
                break;
            case 10:
                monthName = "Oct";
                break;
            case 11:
                monthName = "Nov";
                break;
            case 12:
                monthName = "Dec";
                break;
            default:
                throw new IllegalArgumentException("Invalid month number: " + month);
        }
        return monthName;
    }

    @GetMapping("/edit")
    public String showEditPage(
            Model model,
            @RequestParam Long id
    ) {
        try{
            Events event = repo.findById(id).get();
            model.addAttribute("event", event);

            EventDto eventDto = new EventDto();
            eventDto.setTitle(event.getTitle());
            eventDto.setDescription(event.getDescription());
            eventDto.setCity(event.getCity());
            eventDto.setDepartment(event.getDepartment());
            eventDto.setPrice(event.getPrice());
            eventDto.setStartDate(event.getStartDate());
            eventDto.setEndDate(event.getEndDate());
            model.addAttribute("eventDto", eventDto);

        }catch (Exception e){
            System.out.println(e.getMessage());
            return "redirect:/events";
        }
        return "events/editEvent";
    }

    @PostMapping("/edit")
    public String updateEvent(
            Model model,
            @RequestParam Long id,
            @Valid @ModelAttribute EventDto eventDto,
            BindingResult result

            ){
        try{
            Events event = repo.findById(id).get();
            model.addAttribute("event", event);

            if (eventDto.getImageFiles() == null || eventDto.getImageFiles().isEmpty()) {
                result.addError(new FieldError("eventDto", "imageFiles", "Missing Images"));
            } else if (eventDto.getImageFiles().size() > 5) {
                result.addError(new FieldError("eventDto", "imageFiles", "Only a maximum of 5 images are allowed"));
            }
            if (result.hasErrors()) {
                return "events/createEvent";
            }
            //delete old pictures
            List<EventImage> oldImages = event.getImages();
            System.out.println(oldImages.size());

            for (EventImage image : oldImages) {
                // Delete the image file from the file system
                String filePath = "public/images/" + image.getImageFileName();
                Files.deleteIfExists(Paths.get(filePath));
                image.setEvent(null);
            }
            event.getImages().clear();
            //save new pictures
            List<EventImage> newImages = new ArrayList<>();
            Date createdAt = new Date();
            for (MultipartFile file : eventDto.getImageFiles()) {
                if (!file.isEmpty()) {
                    try {
                        String uploadDir = "public/images/";
                        String storageFileName = createdAt.getTime() + "-" + file.getOriginalFilename();
                        Path uploadPath = Paths.get(uploadDir);

                        if (!Files.exists(uploadPath)) {
                            Files.createDirectories(uploadPath);
                        }

                        try (InputStream inputStream = file.getInputStream()) {
                            Files.copy(inputStream, Paths.get(uploadDir + storageFileName), StandardCopyOption.REPLACE_EXISTING);
                        }

                        EventImage eventImage = new EventImage();
                        eventImage.setEvent(event);
                        eventImage.setImageFileName(storageFileName);
                        newImages.add(eventImage);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        // Handle exception
                    }
                }
            }

            event.setTitle(eventDto.getTitle());
            event.setDescription(eventDto.getDescription());
            event.setCity(eventDto.getCity());
            event.setDepartment(eventDto.getDepartment());
            event.setPrice(eventDto.getPrice());
            event.setStartDate(eventDto.getStartDate());
            event.setEndDate(eventDto.getEndDate());
            event.setImages(newImages);
            repo.save(event);

        }catch (Exception e ){
            System.out.println(e.getMessage());
            return "redirect:/events";
        }
        return "redirect:/events";
    }

    @GetMapping("/delete")
    public String deleteEvent(

            @RequestParam Long id
    ) {
        try {
        Events event = repo.findById(id).get();

        // Delete the image file from the file system
            List<EventImage> oldImages = event.getImages();
            System.out.println(oldImages.size());

            for (EventImage image : oldImages) {
                String filePath = "public/images/" + image.getImageFileName();
                Files.deleteIfExists(Paths.get(filePath));
                image.setEvent(null);
            }

            repo.delete(event);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    return "redirect:/events";
    }

}




