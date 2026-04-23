package mpp2025.practic.models;

import mpp2025.utils.*;

import java.util.concurrent.CompletableFuture;

public interface IService {
    CompletableFuture<LoginResponse> login(LoginRequest loginRequest);
    CompletableFuture<SubmitGameResponse> submitGame(SubmitGameRequest submitGameRequest);
    CompletableFuture<GetAllGamesResponse> getAllGames(GetAllGamesRequest getAllGamesRequest);
    CompletableFuture<ModifyGameConfigResponse> addGameConfig(ModifyGameConfigRequest modifyGameConfigRequest);
    CompletableFuture<GetAllQuestionsResponse> getAllQuestions(GetAllQuestionsRequest getAllQuestionsRequest);
    CompletableFuture<ModifyQuestionConfigResponse> addQuestionConfig(ModifyQuestionConfigRequest modifyQuestionConfigRequest);
}
