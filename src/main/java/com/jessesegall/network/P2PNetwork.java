package com.jessesegall.network;

import com.jessesegall.blockchain.Block;
import com.jessesegall.blockchain.Blockchain;
import com.jessesegall.blockchain.Transaction;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class P2PNetwork {
    private static List<Peer> peers;
    private ServerSocket serverSocket;
    private Blockchain blockchain;
    private List<String> knownNodes;

    public P2PNetwork(int port, Blockchain blockchain, List<String> knownNodes) {
        peers = new ArrayList<>();
        this.blockchain = blockchain;
        this.knownNodes = knownNodes;

        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing server socket", e);
        }
    }

    public static void broadcastBlock(Block block) {
        for (Peer peer : peers) {
            peer.sendBlock(block);
        }
    }

    public void broadcastTransaction(Transaction transaction) {
        for (Peer peer : peers) {
            peer.sendTransaction(transaction);
        }
    }


    public void start() {
        // Connect to known nodes
        for (String nodeAddress : knownNodes) {
            String[] parts = nodeAddress.split(":");
            String host = parts[0];
            int port = Integer.parseInt(parts[1]);
            connectToPeer(host, port);
        }

        // Accept incoming connections
        new Thread(() -> {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    Peer peer = new Peer(socket, blockchain);
                    peers.add(peer);
                    handleNewPeer(peer);
                } catch (IOException e) {
                    throw new RuntimeException("Error accepting connection", e);
                }
            }
        }).start();
    }

    private void connectToPeer(String host, int port) {
        try {
            Socket socket = new Socket(host, port);
            Peer peer = new Peer(socket, blockchain);
            peers.add(peer);
            handleNewPeer(peer);
        } catch (IOException e) {
            // Handle connection error
        }
    }

    private void handleNewPeer(Peer peer) {
        // Send the current blockchain to the new peer
        peer.sendBlockchain(blockchain);
    }

    private void handleReceivedTransaction(Transaction transaction) {
        blockchain.getTransactionPool().addTransaction(transaction);
    }

    public void synchronizeBlockchain() {
        Blockchain longestChain = blockchain;

        for (Peer peer : peers) {
            Blockchain peerBlockchain = peer.requestBlockchain();
            if (peerBlockchain.getChain().size() > longestChain.getChain().size()) {
                longestChain = peerBlockchain;
            }
        }

        if (longestChain != blockchain) {
            blockchain.setChain(longestChain.getChain());
            blockchain.getUtxoManager().updateUTXOSet(blockchain);
        }
    }
}