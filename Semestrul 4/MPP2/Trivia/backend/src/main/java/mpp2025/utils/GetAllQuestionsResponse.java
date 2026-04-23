package mpp2025.utils;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import mpp2025.dtos.GameDTO;
import mpp2025.dtos.QuestionDTO;

import java.util.List;

public class GetAllQuestionsResponse extends ResponseBase{
    @Getter
    public static class Success extends GetAllQuestionsResponse {
        @JsonProperty("questionsDTO")
        private List<QuestionDTO> questions;
        public Success(List<QuestionDTO> questionDTO) {
            this.questions = questionDTO;
        }
        public Success() {}
        public List<QuestionDTO> getQuestions() {return questions; }
    }
}
