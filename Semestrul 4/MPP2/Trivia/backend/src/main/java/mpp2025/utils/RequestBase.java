package mpp2025.utils;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginRequest.class, name = "1"),
        @JsonSubTypes.Type(value = SubmitGameRequest.class, name = "3"),
        @JsonSubTypes.Type(value = GetAllGamesRequest.class, name = "4"),
        @JsonSubTypes.Type(value = ModifyGameConfigRequest.class, name="5"),
        @JsonSubTypes.Type(value = SimpleNotification.class, name = "7"),
        @JsonSubTypes.Type(value = GetAllQuestionsRequest.class, name = "8"),
        @JsonSubTypes.Type(value = ModifyQuestionConfigRequest.class, name = "9"),

})
public abstract class RequestBase {
}
