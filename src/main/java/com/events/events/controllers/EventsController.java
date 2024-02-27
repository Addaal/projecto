package com.events.events.controllers;

import com.events.events.models.EventDto;
import com.events.events.models.Events;
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
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventsController {
    @Autowired
    private EventsRepo repo;

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
    public String createEvent (
            @Valid @ModelAttribute EventDto eventDto,
            BindingResult result)
    {

        if(eventDto.getImageFileName().isEmpty()){
            result.addError(new FieldError( "eventDto","imageFileName", "Missing Image"));
        }

        if(result.hasErrors()){
            return "events/createEvent";
        }

        MultipartFile image = eventDto.getImageFileName();
        Date createdAt = new Date();
        String storageFileName = createdAt.getTime() + "-" + image.getOriginalFilename();

        try{
            String uploadDir = "public/images/";
            Path uploadPath = Paths.get(uploadDir);

            if(!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }
            try(InputStream inputStream = image.getInputStream()){
                Files.copy(inputStream, Paths.get(uploadDir+storageFileName), StandardCopyOption.REPLACE_EXISTING);

            }
        }catch(Exception e) {
            System.out.println(e.getMessage());
        }



        Events event = new Events();
        event.setTitle(eventDto.getTitle());
        event.setDescription(eventDto.getDescription());
        event.setCity(eventDto.getCity());
        event.setDepartment(eventDto.getDepartment());
        event.setPrice(eventDto.getPrice());
        event.setStartDate(eventDto.getStartDate());
        event.setEndDate(eventDto.getEndDate());
        event.setImageFileName(storageFileName);
        repo.save(event);

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
}




