package com.romantrippel.mudclient;

import java.io.*;
import java.net.Socket;
public class MudServerConnector {
    public static void main(String[] args) {
        String server = "bylins.su";
        int port = 4000;

        try (Socket socket = new Socket(server, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the MUD server.");

            // Thread for reading data from the server
            Thread readerThread = new Thread(() -> {
                try {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            readerThread.start();

            // Handling user input and sanding commands to the server
            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null) {
                writer.write(userInputLine + "\n");
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
