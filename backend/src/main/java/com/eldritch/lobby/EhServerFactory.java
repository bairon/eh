package com.eldritch.lobby;

import com.eldritch.ancientone.AncientOneService;
import com.eldritch.game.*;
import com.eldritch.investigator.InvestigatorService;
import com.eldritch.logic.EhLogic;
import com.eldritch.logic.EhState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EhServerFactory {
    private final SimpMessagingTemplate messagingTemplate;

    private AncientOneService ancientOneService;
    private ArtifactService artifactService;
    private AssetService assetService;
    private ClueService clueService;
    private ConditionService conditionService;
    private InvestigatorService investigatorService;
    private LocationService locationService;
    private MonsterService monsterService;
    private MysteryService mysteryService;
    private MythosService mythosService;
    private PortalService portalService;
    private SpellService spellService;

    @Autowired
    public EhServerFactory(
            SimpMessagingTemplate messagingTemplate,
            AncientOneService ancientOneService,
            ArtifactService artifactService,
            AssetService assetService,
            ClueService clueService,
            ConditionService conditionService,
            InvestigatorService investigatorService,
            LocationService locationService,
            MonsterService monsterService,
            MysteryService mysteryService,
            MythosService mythosService,
            PortalService portalService,
            SpellService spellService
    ) {
        this.messagingTemplate = messagingTemplate;
        this.ancientOneService = ancientOneService;
        this.artifactService = artifactService;
        this.assetService = assetService;
        this.clueService = clueService;
        this.conditionService = conditionService;
        this.investigatorService = investigatorService;
        this.locationService = locationService;
        this.monsterService = monsterService;
        this.mysteryService = mysteryService;
        this.mythosService = mythosService;
        this.portalService = portalService;
        this.spellService = spellService;
    }

    public EhServer createServer(String lobbyId) {
        EhState state = new EhState();
        List<EhAgent> agents = new ArrayList<>();

        EhLogic ehLogic = new EhLogic(
                null, // server will be set after creation
                state,
                agents,
                ancientOneService,
                artifactService,
                assetService,
                clueService,
                conditionService,
                investigatorService,
                locationService,
                monsterService,
                mysteryService,
                mythosService,
                portalService,
                spellService
        );

        EhServer server = new EhServer(messagingTemplate, lobbyId);
        ehLogic.setServer(server);
        server.setEhLogic(ehLogic);

        return server;
    }
}