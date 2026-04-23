package mpp2025.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import mpp2025.dtos.GameDTO;

public class SubmitGameRequest extends RequestBase{
    @JsonProperty("GameDTO")
    private GameDTO gameDTO;

    public SubmitGameRequest() {}
    public SubmitGameRequest(GameDTO gameDTO) {
        this.gameDTO = gameDTO;
    }
    public GameDTO getGameDTO() {
        return gameDTO;
    }
    public void setGameDTO(GameDTO gameDTO) {
        this.gameDTO = gameDTO;
    }
    @Override
    public String toString() {
        return "SubmitGameRequest{" +
                "gameDTO=" + gameDTO +
                '}';
    }
}
