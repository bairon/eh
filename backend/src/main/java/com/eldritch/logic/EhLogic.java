package com.eldritch.logic;

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
    private InvestigatorService investigatorService;

    @Autowired
    private PortalService portalService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private AssetService assetService;

    @Autowired
    private SpellService spellService;

    private final EhServer server;
    private final EhState state;
    private final List<EhAgent> agents;
    private Depo depo;

    public EhLogic(EhServer server, EhState state, List<EhAgent> agents) {
        this.server = server;
        this.state = state;
        this.agents = agents;
        this.state.setStatus(EhStatus.LOBBY);
    }

    public void init() {
        this.depo = new Depo(
                portalService.getPortals(),
                clueService.getClues(),
                assetService.getAssets(),
                spellService.getSpells()
        );
        for (EhAgent agent : agents) {
            addInvestigator(agent.getInvestigatorId());
        }
        state.setStatus(EhStatus.ONGOING);
    }

    private void addInvestigator(String investigatorId) {
        state.getInvestigators().add(investigatorService.create(investigatorId));
    }

    public int runCycle(EhState state) throws ExecutionException {
        logger.debug("Running EH sycle");

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
