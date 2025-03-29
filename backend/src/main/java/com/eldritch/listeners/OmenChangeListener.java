package com.eldritch.listeners;

import com.eldritch.event.OmenChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OmenChangeListener {
    @EventListener
    public void handleOmenChange(OmenChangeEvent event) {
        System.out.println("Omen changed: " + event.getDescription());
        // Update game state (e.g., adjust doom track, spawn monsters, etc.)
    }
}