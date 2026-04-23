package mpp2025.service;

import mpp2025.utils.*;

public interface IService {
    LoginResponse login(LoginRequest loginRequest);
    SubmitGameResponse submitGame(SubmitGameRequest submitGameRequest);
    GetAllGamesResponse getAllGames(GetAllGamesRequest req);
    ModifyGameConfigResponse addGameConfig(ModifyGameConfigRequest modifyGameConfigRequest);
    GetAllQuestionsResponse getAllQuestions(GetAllQuestionsRequest request);
    ModifyQuestionConfigResponse addQuestionConfig(ModifyQuestionConfigRequest modifyQuestionConfigRequest);
}
