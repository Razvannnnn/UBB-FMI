package mpp2025.practic.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import mpp2025.domain.User;
import mpp2025.dtos.GameDTO;
import mpp2025.dtos.QuestionDTO;
import mpp2025.practic.models.Client;
import mpp2025.repo.QuestionRepo;
import mpp2025.service.IObserver;
import mpp2025.utils.*;

import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MainController implements IObserver {
    @FXML
    private Label statusLabel;
    @FXML
    private Label timpLabel;
    @FXML
    private VBox gameVBox;
    @FXML
    private VBox leaderboardVBox;

    private Client client;
    private User currentUser;

    private long startTimeMillis;
    private long durationSeconds;
    private Timeline timeline;
    private int score;
    private int level;
    private int nrIntrebare = 0;
    private int intrebari_level1 = 0;
    private int intrebari_level2 = 0;
    private int intrebari_level3 = 0;

    private int intrebari_gresite_level1 = 0;
    private int intrebari_gresite_level2 = 0;
    private int intrebari_gresite_level3 = 0;

    private GameDTO gameDTO = new GameDTO();

    public void loadAfterLogin() {
        getOneQuestions(1);
        refreshLeaderboard();
        startTimeMillis = System.currentTimeMillis();
        startTimer();
        client.addObserver(this);
    }

    private void refreshLeaderboard() {
        client.getAllGames(new GetAllGamesRequest()).thenAccept(response -> {
            if (response instanceof GetAllGamesResponse.Success success) {
                List<GameDTO> games = success.getGames();
                games.sort(Comparator.comparingInt(GameDTO::getDuration).reversed());

                Platform.runLater(() -> {
                    leaderboardVBox.getChildren().clear();
                    int top = Math.min(games.size(), 5);
                    for (int i = 0; i < top; i++) {
                        GameDTO g = games.get(i);
                        if(!g.getSuccess())continue;
                        Label label = new Label((i + 1) + ". " + g.getUsername() + ": " + g.getNumberPoints() + " pct" + " time: " + g.getDuration());
                        leaderboardVBox.getChildren().add(label);
                    }
                });
            }
        }).exceptionally(ex -> {
            Platform.runLater(() -> statusLabel.setText("Eroare la clasament: " + ex.getMessage()));
            return null;
        });
    }

    public void getOneQuestions(int difficulty) {
        client.getAllQuestions(new GetAllQuestionsRequest()).thenAccept(response -> {
            if (response instanceof GetAllQuestionsResponse.Success success) {
                List<QuestionDTO> questions = success.getQuestions();
                Platform.runLater(() -> {
                    statusLabel.setText("Intrebari incarcate cu succes!");
                    questions.stream()
                            .filter(q -> q.getDifficulty() == difficulty)
                            .toList();

                    if (!questions.isEmpty()) {
                        QuestionDTO randomQuestion = questions.get(new Random().nextInt(questions.size()));
                        Label questionLabel = new Label("Intrebare: " + randomQuestion.getQuestionText());
                        Label scoreLabel = new Label("Scor curent: " + score);
                        TextField answerField = new TextField();
                        answerField.setPromptText("Introdu raspunsul aici...");
                        Button submitButton = new Button("Trimite raspuns");
                        submitButton.setOnAction(event -> handleClick(answerField, randomQuestion));
                        gameVBox.getChildren().clear();
                        gameVBox.getChildren().addAll(questionLabel, scoreLabel, answerField, submitButton);
                    } else {
                        statusLabel.setText("Nu exista intrebari disponibile pentru dificultatea selectata.");
                    }
                });
            }
        }).exceptionally(ex -> {
            Platform.runLater(() -> statusLabel.setText("Eroare la incarcarea intrebarilor: " + ex.getMessage()));
            return null;
        });
    }

    public void handleIntrebare(QuestionDTO question, int scor) {
        if(nrIntrebare == 1) {
            gameDTO.setI1(question.getQuestionText());
            gameDTO.setR1(question.getAnswer());
            gameDTO.setP1(scor);
        } else if(nrIntrebare == 2) {
            gameDTO.setI2(question.getQuestionText());
            gameDTO.setR2(question.getAnswer());
            gameDTO.setP2(scor);
        } else if(nrIntrebare == 3) {
            gameDTO.setI3(question.getQuestionText());
            gameDTO.setR3(question.getAnswer());
            gameDTO.setP3(scor);
        } else if(nrIntrebare == 4) {
            gameDTO.setI4(question.getQuestionText());
            gameDTO.setR4(question.getAnswer());
            gameDTO.setP4(scor);
        } else if(nrIntrebare == 5) {
            gameDTO.setI5(question.getQuestionText());
            gameDTO.setR5(question.getAnswer());
            gameDTO.setP5(scor);
        } else if(nrIntrebare == 6) {
            gameDTO.setI6(question.getQuestionText());
            gameDTO.setR6(question.getAnswer());
            gameDTO.setP6(scor);
        }

    }

    private void handleClick(TextField answerField, QuestionDTO question) {
        String answer = answerField.getText();
        int levelIntrebare = question.getDifficulty();

        if (answer.equalsIgnoreCase(question.getAnswer())) {
            statusLabel.setText("Raspuns corect!");
            score += 4 * levelIntrebare * levelIntrebare;
        } else {
            statusLabel.setText("Raspuns gresit! Raspunsul corect era: " + question.getAnswer());
            score -= 2;
        }

        nrIntrebare++;
        handleIntrebare(question, score);

        if (nrIntrebare == 6) {
            saveGame(true);
            return;
        }

        getOneQuestions(level);
    }


    private void saveGame(boolean success) {
        durationSeconds = (System.currentTimeMillis() - startTimeMillis) / 1000;

        GameDTO gameDTO = new GameDTO();
        gameDTO.setUsername(currentUser.getUsername());
        stopTimer();
        gameDTO.setNumberPoints(score);
        gameDTO.setSuccess(success);
        gameDTO.setDuration((int) durationSeconds);

        SubmitGameRequest request = new SubmitGameRequest(gameDTO);
        client.submitGame(request).thenAccept(response -> {
            if (response instanceof SubmitGameResponse.Success) {
                Platform.runLater(() -> {
                    statusLabel.setText("Joc salvat cu succes!");
                    refreshLeaderboard();
                });
            }
        }).exceptionally(ex -> {
            Platform.runLater(() -> {
                ex.printStackTrace();
                statusLabel.setText("Eroare la salvare joc: " + ex.getMessage());
            });
            return null;
        });
        handleRestart();
    }


    public void setClient(Client client) {
        this.client = client;
    }

    public void setUser(User loggedInUser) {
        this.currentUser = loggedInUser;
    }

    private void startTimer() {
        if (timeline != null) timeline.stop();
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            long elapsed = (System.currentTimeMillis() - startTimeMillis) / 1000;
            timpLabel.setText("Timp: " + elapsed + "s");
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    @FXML
    public void handleRestart() {
        score = 0;
        nrIntrebare = 0;
        intrebari_level1 = 0;
        intrebari_level2 = 0;
        intrebari_level3 = 0;
        intrebari_gresite_level1 = 0;
        intrebari_gresite_level2 = 0;
        intrebari_gresite_level3 = 0;
        gameDTO = new GameDTO();
        gameVBox.getChildren().clear();

        getOneQuestions(1);
        statusLabel.setText("Joc nou!");
        startTimeMillis = System.currentTimeMillis();
        startTimer();
    }

    @Override
    public void update(Object event) {
        Platform.runLater(() -> {
            statusLabel.setText("Joc actualizat!");
            refreshLeaderboard();
        });
    }
}
