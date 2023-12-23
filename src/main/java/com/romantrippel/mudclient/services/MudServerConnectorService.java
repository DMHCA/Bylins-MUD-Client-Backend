package com.romantrippel.mudclient.services;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.net.telnet.TelnetClient;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
@Service
public class MudServerConnectorService {

    private final Environment env;

    public MudServerConnectorService(Environment env) {
        this.env = env;
    }

    @PostConstruct
    public void init() {
        connectToMudServer();
    }
    private void connectToMudServer() {
        final String server = env.getProperty("bylins.server");
        final int port = Integer.parseInt(Objects.requireNonNull(env.getProperty("bylins.port")));
        final AtomicBoolean isDisconnect = new AtomicBoolean(false);
        final TelnetClient telnetClient = new TelnetClient();

        try {
            telnetClient.connect(server, port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(telnetClient.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(telnetClient.getOutputStream()));
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))
        )
        {
            System.out.println("Connected to the MUD server \"Bilyns.su\".");

            readMudServerThread(reader, isDisconnect);

            // Handling user input and sanding commands to the server
                String userInputLine;
                try {
                    //todo test account
                    loginTestAccount(writer);

                    while ((userInputLine = userInput.readLine()) != null) {
                        if (!isDisconnect.get()) {
                            writer.write(userInputLine + "\n");
                            writer.flush();
                        } else break;
                    }
                } catch (IOException e) {
                    log.error("An error occurred in write process", e);
                }

            } catch (IOException e) {
                log.error("An error occurred in Connection to MUD", e);
            }
        System.out.println("The END");
    }

    private void loginTestAccount(BufferedWriter writer) throws IOException {
        writer.write(env.getProperty("sec.encoding") +"\n");
        writer.flush();
        writer.write(String.format("%s %s\n", env.getProperty("sec.username"), env.getProperty("sec.password")));
        writer.flush();
        writer.write(" \n");
        writer.flush();
    }

    private static void readMudServerThread(BufferedReader reader, AtomicBoolean disconnect) {
        Thread readerThread = new Thread(() -> {
            try {
                int character;
                while ((character = reader.read()) != -1) {
                    System.out.print((char) character);
                }
                disconnect.set(true);
            } catch (IOException e) {
                log.error("An error occurred --- in readerThread", e);
            }
        });
        readerThread.start();
    }
}
