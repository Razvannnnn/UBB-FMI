package mpp2025.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import mpp2025.dtos.GameDTO;
import mpp2025.dtos.QuestionDTO;

public class ModifyQuestionConfigRequest extends RequestBase {
    @JsonProperty("GameDTO")
    private QuestionDTO questionDTO;
    public ModifyQuestionConfigRequest() {}
    public ModifyQuestionConfigRequest(QuestionDTO questionDTO) {
        this.questionDTO = questionDTO;
    }
    public QuestionDTO getQuestionDTO() {
        return questionDTO;
    }
    public void setQuestionDTO(QuestionDTO questionDTO1) {
        this.questionDTO = questionDTO1;
    }
    @Override
    public String toString() {
        return "ModifyQuestionConfigRequest{" +
                "questionDTO=" + questionDTO +
                '}';
    }
}
