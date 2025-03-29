package com.eldritch.service;

import com.eldritch.game.ClueService;
import com.eldritch.game.Clue;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
public class ClueServiceTest {

    private ClueService clueService;

    @BeforeMethod
    public void setUp() {
        clueService = new ClueService();
        clueService.setCluesFile("clues");

    }

    @Test
    public void testGetClues() {
        List<Clue> clues = clueService.getClues();
        assertEquals(clues.size(), 36);
    }
}