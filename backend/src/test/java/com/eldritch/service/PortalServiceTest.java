package com.eldritch.service;

import com.eldritch.model.Portal;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;
public class PortalServiceTest {

    private PortalService portalService;

    @BeforeMethod
    public void setUp() {
        portalService = new PortalService();
        portalService.setPortalsFile("portals");

    }

    @Test
    public void testGetPortals() {
        List<Portal> portals = portalService.getPortals();
        assertEquals(portals.size(), 9);
    }
}