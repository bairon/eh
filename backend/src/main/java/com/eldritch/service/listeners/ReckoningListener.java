package com.eldritch.service.listeners;

import com.eldritch.model.events.ReckoningEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReckoningListener {
    @EventListener
    public void handleReckoning(ReckoningEvent event) {
        System.out.println("Reckoning event: " + event.getDescription());
        // Trigger monster abilities, investigator effects, etc.
    }
}