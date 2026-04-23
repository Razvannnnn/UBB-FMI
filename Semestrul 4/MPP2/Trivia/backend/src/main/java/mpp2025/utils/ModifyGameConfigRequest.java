package mpp2025.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import mpp2025.dtos.GameDTO;

public class ModifyGameConfigRequest extends RequestBase{
    @JsonProperty("GameDTO")
    private GameDTO gameDTO;
    public ModifyGameConfigRequest() {}
    public ModifyGameConfigRequest(GameDTO gameDTO) {
        this.gameDTO = gameDTO;
    }
    public GameDTO getGameDTO() {
        return gameDTO;
    }
    public void setGameDTO(GameDTO boardDTO) {
        this.gameDTO = boardDTO;
    }
    @Override
    public String toString() {
        return "AddGameConfigRequest{" +
                "gameDTO=" + gameDTO +
                '}';
    }
}
