package mpp2025.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import mpp2025.dtos.GameDTO;

import java.util.List;

public class GetAllGamesResponse extends ResponseBase {
    @Getter
    public static class Success extends GetAllGamesResponse {
        @JsonProperty("gamesDTO")
        private List<GameDTO> games;
        public Success(List<GameDTO> gamesDTO) {
            this.games = gamesDTO;
        }
        public Success() {}
        public List<GameDTO> getGames() {return games; }
    }
}
