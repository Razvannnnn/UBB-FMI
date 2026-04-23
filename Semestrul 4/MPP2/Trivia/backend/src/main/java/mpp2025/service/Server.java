package mpp2025.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import mpp2025.utils.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final AppService service;
    private final ObjectMapper mapper = new ObjectMapper();
    private final int port;

    public Server(AppService service, int port) {
        this.service = service;
        this.port = port;
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from " + clientSocket.getRemoteSocketAddress());
                new Thread(() -> {
                    try {
                        doWork(clientSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doWork(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

        IObserver observer = event -> {
            try {
                System.out.println("Sending update to client - server observer");
                String json = mapper.writeValueAsString(event);
                writer.println(json);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        synchronized (service) {
            service.addObserver(observer);
        }

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    RequestBase request = mapper.readValue(line, RequestBase.class);
                    if (request == null) {
                        System.out.println("Received null request");
                        continue;
                    }

                    System.out.println("Received request, looking for response");

                    ResponseBase response = null;
                    synchronized (service) {
                        System.out.println("Locked service");
                        if (request instanceof LoginRequest req) {
                            response = service.login(req);
                        }
                        if (request instanceof SubmitGameRequest req) {
                            response = service.submitGame(req);
                        }
                        if (request instanceof GetAllGamesRequest req) {
                            response = service.getAllGames(req);
                        }
                        if (request instanceof ModifyGameConfigRequest req) {
                            response = service.addGameConfig(req);
                        }
                        if( request instanceof GetAllQuestionsRequest req) {
                            response = service.getAllQuestions(req);
                        }
                        if (request instanceof ModifyQuestionConfigRequest req) {
                            response = service.addQuestionConfig(req);
                        }
                        System.out.println("Unlocked service");
                    }

                    String json = mapper.writeValueAsString(response);
                    System.out.println("Sending response: " + json);
                    writer.println(json);
                } catch (IOException e) {
                    System.out.println("Error processing request: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } finally {
            synchronized (service) {
                service.removeObserver(observer);
            }
            clientSocket.close();
            System.out.println("Client disconnected, server handler stopped.");
        }
    }
}
