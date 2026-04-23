package mpp2025.practic.controllers;

import mpp2025.dtos.GameDTO;
import mpp2025.dtos.QuestionDTO;
import mpp2025.practic.models.Client;
import mpp2025.service.AppService;
import mpp2025.utils.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/app")
public class RestController {
    private AppService appService;
    private Client client;

    public RestController(AppService appService, Client client) {
        this.appService = appService;
        this.client = client;
    }

    @GetMapping("/test")
    public String getAppInfo() {
        return "ceva";
    }

    @GetMapping("/viewgames/{username}/{id}")
    public List<GameDTO> viewUserGames(@PathVariable("username") String username,
                                        @PathVariable("id") Long gameId) {
        try {
            GetAllGamesRequest getAllGamesRequest = new GetAllGamesRequest();
            GetAllGamesResponse response = client.getAllGames(getAllGamesRequest).get();

            if(response instanceof GetAllGamesResponse.Success success) {
                return success.getGames().stream()
                        .filter(game -> game.getUsername().equalsIgnoreCase(username))
                        .filter(game -> game.getId().equals(gameId))
                        .collect(Collectors.toList());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @PutMapping("/game/update/{id}")
    public ResponseEntity<?> updateGame(@PathVariable("id") Long id, @RequestBody GameDTO dto) {
        try {
            dto.setId(id);
            ModifyGameConfigRequest request = new ModifyGameConfigRequest(dto);

            ModifyGameConfigResponse response = client.addGameConfig(request).get();

            if (response instanceof ModifyGameConfigResponse.Success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/question/update/{id}")
    public ResponseEntity<?> updateGame(@PathVariable("id") Long id, @RequestBody QuestionDTO dto) {
        try {
            dto.setId(id);
            ModifyQuestionConfigRequest request = new ModifyQuestionConfigRequest(dto);
            ModifyQuestionConfigResponse response = client.addQuestionConfig(request).get();

            if (response instanceof ModifyQuestionConfigResponse.Success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

}
