package com.eldritch.lobby;

import com.eldritch.user.UserData;
import com.eldritch.user.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lobby")
public class EhLobbyController {
    private static final Logger logger = LogManager.getLogger(EhLobbyController.class);
    private final EhLobbyManager lobbyManager;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    @Autowired
    public EhLobbyController(EhLobbyManager lobbyManager, SimpMessagingTemplate messagingTemplate, UserService userService) {
        this.lobbyManager = lobbyManager;
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<LobbyInfo>> list() {
        return ResponseEntity.ok(lobbyManager.list());
    }

    @PostMapping("/check")
    public ResponseEntity<LobbyInfo> check(HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        try {
            if (userId != null) {
                EhLobby lobby = lobbyManager.findByUserId(userId);
                if (lobby != null) {
                    return ResponseEntity.ok(lobby.info());
                }
            }
        } catch (RuntimeException e) {
            logger.error("Failed to check game for userId: " + userId, e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create")
    public ResponseEntity<LobbyInfo> create(@RequestBody Map<String, String> request, HttpSession session) {
        logger.info("/create:" + session.getId());
        String gameName = request.get("gameName");
        String userId = (String) session.getAttribute("userId");

        EhLobby newOrExistingLobby = lobbyManager.create(gameName, userId);

        session.setAttribute("lobbyId", newOrExistingLobby.getId());

        return ResponseEntity.ok(newOrExistingLobby.info());
    }

    @PostMapping("/join")
    public ResponseEntity<LobbyInfo> join(@RequestBody Map<String, String> request, HttpSession session) {
        String lobbyId = request.get("lobbyId");
        String userId = session.getAttribute("userId").toString();
        try {
            UserData userData = userService.getUserDataById(userId);
            if (userData == null) {
                return ResponseEntity.badRequest().build();
            }
            LobbyInfo lobbyInfo = lobbyManager.join(lobbyId, userData.getId())
                    .info();
            messagingTemplate.convertAndSend("/topic/ehlobby/" + lobbyInfo, lobbyInfo);
            return ResponseEntity.ok(lobbyInfo);
        } catch (RuntimeException e) {
            logger.error("Failed to join with user id: " + session.getAttribute("userId"), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/leave")
    public ResponseEntity<Void> leave(HttpSession session) {
        String userId = session.getAttribute("userId").toString();
        if (userId == null) {
            return ResponseEntity.notFound().build();
        }

        EhLobby lobby = lobbyManager.leave(userId);
        if (lobby != null)
        messagingTemplate.convertAndSend("/topic/ehlobby/" + lobby.getId(), lobby.info());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/kick")
    public ResponseEntity<LobbyInfo> kick(@RequestBody Map<String, String> request, HttpSession session) {

        String kickUserId = request.get("kickUserId");
        String userId = session.getAttribute("userId").toString();
        try {
            EhLobby lobby = lobbyManager.kick(kickUserId, userId);
            if (lobby != null) {
                LobbyInfo lobbyInfo = lobby.info();
                messagingTemplate.convertAndSend("/topic/ehlobby/" + lobby.getId(), lobbyInfo);
                return ResponseEntity.ok(lobbyInfo);
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (RuntimeException e) {
            logger.error("Failed to kick with user id: " + kickUserId + " with user " + session.getAttribute("userId"), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}