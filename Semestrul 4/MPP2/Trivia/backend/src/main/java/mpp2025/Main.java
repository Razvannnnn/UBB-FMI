package mpp2025;

import mpp2025.repo.GameRepo;
import mpp2025.repo.QuestionRepo;
import mpp2025.repo.UserRepo;
import mpp2025.service.AppService;
import mpp2025.service.Server;

public class Main {
    public static void main(String[] args) {
        var userRepo = new UserRepo();
        var gameRepo = new GameRepo();
        var questionRepo = new QuestionRepo();
        AppService service = new AppService(userRepo, gameRepo, questionRepo);

        Server server = new Server(service, 12353);
        new Thread(() -> server.run()).start();
    }
}