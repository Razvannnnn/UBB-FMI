package mpp2025.service;

import mpp2025.domain.User;
import mpp2025.dtos.GameDTO;
import mpp2025.dtos.QuestionDTO;
import mpp2025.repo.GameRepo;
import mpp2025.repo.QuestionRepo;
import mpp2025.repo.UserRepo;
import mpp2025.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class AppService implements IService, Subject{
    private static final Logger logger = LoggerFactory.getLogger(AppService.class);
    private final UserRepo userRepo;
    private final GameRepo gameRepo;
    private final QuestionRepo questionRepo;
    private final List<IObserver> observers = new CopyOnWriteArrayList<>();

    public AppService(UserRepo userRepo, GameRepo gameRepo, QuestionRepo questionRepo) {
        this.userRepo = userRepo;
        this.gameRepo = gameRepo;
        this.questionRepo = questionRepo;
    }

    @Override
    public void addObserver(IObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IObserver obs : observers) {
            try {
                obs.update(new SimpleNotification("Game updated"));
            } catch (Exception e) {
                System.out.println("Failed to update observer: " + e.getMessage());
            }
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        logger.info("Login attempt for username: {}", request.getUsername());
        // in login request avem doar username
        User user = userRepo.findAll().stream()
                .filter(u -> u.getUsername().equals(request.getUsername()))
                .findFirst()
                .orElse(null);
        if(user == null){
            logger.warn("Login failed: no user found for username {}", request.getUsername());
            return new LoginResponse.WrongUsername();
        }
        logger.info("Login successful for username: {}", request.getUsername());
        return new LoginResponse.Success();
    }

    @Override
    public SubmitGameResponse submitGame(SubmitGameRequest request) {
        logger.info("Submit game request" + request);
        var game = request.getGameDTO().toGame();
        System.out.println(game);
        gameRepo.save(game);
        notifyObservers();
        return new SubmitGameResponse.Success();
    }

    @Override
    public GetAllGamesResponse getAllGames(GetAllGamesRequest req) {
        logger.info("Get all games request" + req);
        List<GameDTO> gameDTOs = gameRepo.findAll().stream()
                .map(GameDTO::fromGame)
                .toList();
        return new GetAllGamesResponse.Success(gameDTOs);
    }

    @Override
    public ModifyGameConfigResponse addGameConfig(ModifyGameConfigRequest modifyGameConfigRequest) {
        var game = modifyGameConfigRequest.getGameDTO().toGame();
        gameRepo.modify(game);
        return new ModifyGameConfigResponse.Success();
    }

    @Override
    public GetAllQuestionsResponse getAllQuestions(GetAllQuestionsRequest request) {
        logger.info("Get all questions request" + request);
        List<QuestionDTO> questionDTOs = questionRepo.findAll().stream()
                .map(QuestionDTO::fromQuestion)
                .toList();
        return new GetAllQuestionsResponse.Success(questionDTOs);
    }

    @Override
    public ModifyQuestionConfigResponse addQuestionConfig(ModifyQuestionConfigRequest modifyQuestionConfigRequest) {
        var question = modifyQuestionConfigRequest.getQuestionDTO().toQuestion();
        questionRepo.modify(question);
        return new ModifyQuestionConfigResponse.Success();
    }
}
