package mpp2025.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mpp2025.domain.Question;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;
    private String questionText;
    private String answer;
    private int difficulty;

    public Question toQuestion() {
        return Question.builder()
                .id(this.id)
                .questionText(this.questionText)
                .answer(this.answer)
                .difficulty(this.difficulty)
                .build();
    }

    public static QuestionDTO fromQuestion(Question question) {
        return new QuestionDTO(
                question.getId(),
                question.getQuestionText(),
                question.getAnswer(),
                question.getDifficulty()
        );
    }
}
