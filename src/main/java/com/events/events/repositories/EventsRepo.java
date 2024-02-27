package com.events.events.repositories;

import com.events.events.models.Events;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventsRepo extends JpaRepository<Events, Long> {

}
