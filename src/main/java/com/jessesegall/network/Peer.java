package com.jessesegall.network;

import com.jessesegall.blockchain.Block;
import com.jessesegall.blockchain.Blockchain;

import java.io.IOException;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Peer {
    private Socket socket;
    private Blockchain blockchain;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;

    public Peer(Socket socket, Blockchain blockchain) {
        this.socket = socket;
        this.blockchain = blockchain;
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Error initializing streams", e);
        }
    }

    public void sendBlock(Block block) {
        try {
            outputStream.writeObject(block);
        } catch (IOException e) {
            throw new RuntimeException("Error sending block", e);
        }
    }

    public Block receiveBlock() {
        try {
            return (Block) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error receiving block", e);
        }
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }
}