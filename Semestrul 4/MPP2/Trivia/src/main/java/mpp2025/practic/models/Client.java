package mpp2025.practic.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import mpp2025.service.IObserver;
import mpp2025.service.Subject;
import mpp2025.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.*;

@Component
public class Client implements AutoCloseable, IService, Subject {
    private final String serverAddress;
    private final int port;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final BlockingQueue<CompletableFuture<ResponseBase>> queue = new LinkedBlockingQueue<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private volatile boolean running = true;

    private final List<IObserver> observers = new CopyOnWriteArrayList<>();

    private BufferedWriter writer;
    private BufferedReader reader;

    public Client(
            @Value("${client.server}") String serverAddress,
            @Value("${client.port}") int port
    ) {
        this.serverAddress = serverAddress;
        this.port = port;
    }
    @PostConstruct
    public void init() {
        try {
            start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start Client", e);
        }
    }

    public void start() throws IOException {
        Socket socket = new Socket(serverAddress, port);
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        executor.submit(this::listen);
    }

    private void listen() {
        try {
            while (running) {
                String line = reader.readLine();
                if (line == null) break;

                ResponseBase response = objectMapper.readValue(line, ResponseBase.class);

                if (response instanceof SimpleNotification) {
                    notifyObservers();
                } else {
                    CompletableFuture<ResponseBase> future = queue.poll();
                    if (future != null) {
                        future.complete(response);
                    } else {
                        System.err.println("Unexpected response: " + response.getClass().getSimpleName());
                    }
                }
            }
        } catch (Exception e) {
            if (running) e.printStackTrace();
        }
    }

    private <T extends ResponseBase> CompletableFuture<T> sendRequest(RequestBase request, Class<T> responseType) {
        CompletableFuture<T> future = new CompletableFuture<>();
        try {
            queue.put((CompletableFuture<ResponseBase>) (CompletableFuture<?>) future);
            String json = objectMapper.writeValueAsString(request);
            writer.write(json);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }

    public CompletableFuture<LoginResponse> login(LoginRequest request) {
        return sendRequest(request, LoginResponse.class);
    }

    @Override
    public CompletableFuture<SubmitGameResponse> submitGame(SubmitGameRequest submitGameRequest) {
        return sendRequest(submitGameRequest, SubmitGameResponse.class);
    }

    @Override
    public CompletableFuture<GetAllGamesResponse> getAllGames(GetAllGamesRequest getAllGamesRequest) {
        return sendRequest(getAllGamesRequest, GetAllGamesResponse.class);
    }

    @Override
    public CompletableFuture<ModifyGameConfigResponse> addGameConfig(ModifyGameConfigRequest modifyGameConfigRequest) {
        return sendRequest(modifyGameConfigRequest, ModifyGameConfigResponse.class);
    }

    @Override
    public CompletableFuture<GetAllQuestionsResponse> getAllQuestions(GetAllQuestionsRequest getAllQuestionsRequest) {
        return sendRequest(getAllQuestionsRequest, GetAllQuestionsResponse.class);
    }

    @Override
    public CompletableFuture<ModifyQuestionConfigResponse> addQuestionConfig(ModifyQuestionConfigRequest modifyQuestionConfigRequest) {
        return sendRequest(modifyQuestionConfigRequest, ModifyQuestionConfigResponse.class);
    }


    @Override
    public void close() throws Exception {
        running = false;
        executor.shutdownNow();
        if (writer != null) writer.close();
        if (reader != null) reader.close();
    }

    @Override
    public void addObserver(IObserver o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(IObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (IObserver observer : observers) {
            try {
                observer.update(new SimpleNotification("Game updated"));
            } catch (Exception e) {
                System.err.println("Failed to notify observer: " + e.getMessage());
            }
        }
    }
}