package com.events.events;

import com.events.events.controllers.EventsRestController;
import com.events.events.models.EventImage;
import com.events.events.models.Events;
import com.events.events.repositories.EventImageRepo;
import com.events.events.repositories.EventsRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EventControllerRestTest {

    private MockMvc mockMvc;

    @Mock
    private EventsRepo repo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventsRestController eventsController = new EventsRestController(repo);
        mockMvc = MockMvcBuilders.standaloneSetup(eventsController).build();
    }

    @Test
    public void testListEvents() throws Exception {

        Events event1 = new Events();

        event1.setTitle("Event 1");
        event1.setDescription("Test Description");
        event1.setCity("Test City");
        event1.setDepartment("Test Department");
        event1.setPrice(10.0);
        event1.setEndDate(new Date());
        event1.setStartDate(new Date());
        List<EventImage> imageList = new ArrayList<>();
        event1.setImages(imageList);
        repo.save(event1);

        List<Events> eventsList = Arrays.asList(event1);
        when(repo.findAll()).thenReturn(eventsList);

        mockMvc.perform(get("/allevents"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Event 1"));

        verify(repo, times(1)).findAll();

    }

    @Test
    public void testCreateEvent() throws Exception {
        Events event1 = new Events();
        event1.setId(1L);
        event1.setTitle("Test Event");
        event1.setDescription("Test Description");
        event1.setCity("Test City");
        event1.setDepartment("Test Department");
        event1.setPrice(10.0);
        event1.setStartDate(new Date());
        event1.setEndDate(new Date());
        when(repo.save(any(Events.class))).thenReturn(event1);


        mockMvc.perform(post("/allevents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"title\":\"Test Event\",\"description\":\"Test Description\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Event"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.city").value("Test City"))
                .andExpect(jsonPath("$.department").value("Test Department"))
                .andExpect(jsonPath("$.price").value(10.0));

        verify(repo, times(1)).save(any(Events.class));
        verifyNoMoreInteractions(repo);
    }
}
