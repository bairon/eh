package com.eldritch.controller;

import com.eldritch.service.StaticDataService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@RestController
@RequestMapping("/api/static")
public class StaticDataController {

    @Autowired
    private StaticDataService staticDataService;

    @GetMapping("/data")
    public ResponseEntity<?> data(HttpSession session) {
        Locale locale = (Locale) session.getAttribute("locale");
        if (locale == null) {
            locale = Locale.ENGLISH;
        }
        return ResponseEntity.ok(staticDataService.getAllStaticData(locale));
    }

}
