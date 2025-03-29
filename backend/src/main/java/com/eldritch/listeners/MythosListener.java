package com.eldritch.listeners;

import com.eldritch.event.MythosEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MythosListener {
    @EventListener
    public void handleMythos(MythosEvent event) {
        System.out.println("Mythos event: " + event.getDescription());
        // Draw a Mythos card, resolve its effects, etc.
    }
}