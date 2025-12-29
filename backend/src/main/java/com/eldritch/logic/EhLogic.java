package com.eldritch.logic;

import com.eldritch.ancientone.AncientOneService;
import com.eldritch.game.*;
import com.eldritch.investigator.InvestigatorService;
import com.eldritch.lobby.EhAgent;
import com.eldritch.lobby.EhServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class EhLogic {

    private static final Logger logger = LogManager.getLogger(EhLogic.class);

    @Autowired
    private AncientOneService ancientOneService;

    @Autowired
    private ArtifactService artifactService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private InvestigatorService investigatorService;

    @Autowired
    private LocationService locationService;

    @Autowired
    private MonsterService monsterService;

    @Autowired
    private MysteryService mysteryService;

    @Autowired
    private MythosService mythosService;

    @Autowired
    private PortalService portalService;

    @Autowired
    private SpellService spellService;

    private EhServer server;
    private final EhState state;
    private final List<EhAgent> agents;
    private Depo depo;

    public EhLogic(
            EhServer server,
            EhState state,
            List<EhAgent> agents,
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
        this.server = server;
        this.state = state;
        this.agents = agents;

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


        this.state.setStatus(EhStatus.LOBBY);
    }

    public void init() {
        this.depo = new Depo(
                portalService.getPortals(),
                clueService.getClues(),
                assetService.getAssets(),
                spellService.getSpells()
        );
        portalService.getPortals();
        portalService.getPortals();
        portalService.getPortals();
        portalService.getPortals();
        portalService.getPortals();
        portalService.getPortals();

        for (EhAgent agent : agents) {
            addInvestigator(agent.getInvestigatorId());
        }

        state.setStatus(EhStatus.ONGOING);
    }

    private void addInvestigator(String investigatorId) {
        state.getInvestigators().add(investigatorService.create(investigatorId));
    }

    public int runCycle(EhState state) throws ExecutionException {
        logger.debug("Running EH loop");

        return 0;
    }

    public boolean inProgress() {
        return EhStatus.ONGOING == state.getStatus();
    }

    public boolean isFinished() {
        return EhStatus.FINISHED == state.getStatus();
    }

    public EhState getState() {
        return state;
    }

    public void setServer(EhServer server) {
        this.server = server;
    }

    public void setInvestigatorService(InvestigatorService investigatorService) {
        this.investigatorService = investigatorService;
    }

    public void setPortalService(PortalService portalService) {
        this.portalService = portalService;
    }

    public void setClueService(ClueService clueService) {
        this.clueService = clueService;
    }

    public void setAssetService(AssetService assetService) {
        this.assetService = assetService;
    }

    public void setSpellService(SpellService spellService) {
        this.spellService = spellService;
    }
}
