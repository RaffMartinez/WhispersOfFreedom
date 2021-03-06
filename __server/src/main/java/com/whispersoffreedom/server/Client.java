package com.whispersoffreedom.server;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Client {

    @Getter
    private UUID id = UUID.randomUUID();

    @Getter
    private String username;

    @Getter
    private boolean inBattle = false;

    @Getter
    private Battle currentBattle = null;

    @Getter
    private Battle previousBattle = null;

    private TcpConnection connection;

    Logger logger;

    public Client(String newUsername) {
        username = newUsername;
        logger = LoggerFactory.getLogger(String.format("Client [%s]", newUsername));
    }

    public void enterBattle(Battle battle) {
        currentBattle = battle;
        inBattle = true;
    }

    public void leaveBattle() {
        previousBattle = currentBattle;
        currentBattle = null;
        inBattle = false;
    }

    public void broadcast(String message) {
        logger.info("Broadcasting message: " + message);
        if (connection == null) {
            logger.error("No TCP connection for " + username);
            return;
        }
        sendPacket(new WofPacketBroadcast(id.toString(), message));
    }

    public void sendPacket(WofPacket packet) {
        connection.sendPacket(packet);
    }

    public void acceptTcpConnection(TcpConnection conn) {
        logger.info(this.username + " is accepting TCP connection from " + conn.getRemoteAddress());
        connection = conn;
        conn.setClient(this);
    }

    public TcpConnection getConnection() {
        return connection;
    }
}
