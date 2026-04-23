package mpp2025.utils;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = LoginResponse.WrongUsername.class, name = "1"),
        @JsonSubTypes.Type(value = LoginResponse.Success.class, name = "2"),
        @JsonSubTypes.Type(value = SubmitGameResponse.Success.class, name = "4"),
        @JsonSubTypes.Type(value = GetAllGamesResponse.Success.class, name = "5"),
        @JsonSubTypes.Type(value = ModifyGameConfigResponse.Success.class, name="6"),
        @JsonSubTypes.Type(value = SimpleNotification.class, name = "7"),
        @JsonSubTypes.Type(value = GetAllQuestionsResponse.Success.class, name = "8"),
        @JsonSubTypes.Type(value = ModifyQuestionConfigResponse.class, name = "9"),

})
public class ResponseBase {
}
