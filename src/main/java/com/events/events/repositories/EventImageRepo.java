package com.events.events.repositories;

import com.events.events.models.EventImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventImageRepo extends JpaRepository<EventImage, Long> {
}
