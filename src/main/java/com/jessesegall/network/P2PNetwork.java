package com.jessesegall.network;
//Manages the peer-to-peer network.

import com.jessesegall.blockchain.Block;
import com.jessesegall.blockchain.Blockchain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class P2PNetwork {
    private List<Peer> peers;
    private ServerSocket serverSocket;

    public P2PNetwork(int port) {
        this.peers = new ArrayList<>();
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing server socket", e);
        }
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Blockchain blockchain = new Blockchain(7);
                    Peer peer = new Peer(socket, blockchain);
                    peers.add(peer);
                } catch (IOException e) {
                    throw new RuntimeException("Error accepting connection", e);
                }
            }
        }).start();
    }

    public void broadcast(Block block) {
        for (Peer peer : peers) {
            peer.sendBlock(block);
        }
    }

    public void receiveBlocks() {
        for (Peer peer : peers) {
            Block block = peer.receiveBlock();
            peer.getBlockchain().addBlock(block);
        }
    }
}