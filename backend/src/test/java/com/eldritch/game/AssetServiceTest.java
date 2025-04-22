package com.eldritch.game;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class AssetServiceTest {
    private AssetService assetService;

    @BeforeMethod
    public void setUp() {
        assetService = new AssetService();

    }

    @Test
    public void testGetAssets() {
        List<Asset> assets = assetService.getAssets();
        assertEquals(assets.size(), 40);
    }

}