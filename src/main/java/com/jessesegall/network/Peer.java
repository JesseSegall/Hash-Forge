package com.jessesegall.network;

import com.jessesegall.blockchain.Block;
import com.jessesegall.blockchain.Blockchain;
import com.jessesegall.blockchain.Transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class Peer {
    private static final String BLOCK = "BLOCK";
    private static final String TRANSACTION = "TRANSACTION";
    private static final String REQUEST_BLOCKCHAIN = "REQUEST_BLOCKCHAIN";
    private static final String REQUEST_KNOWN_NODES = "REQUEST_KNOWN_NODES";
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

        new Thread(this::listenForMessages).start();
    }

    private void listenForMessages() {
        while (true) {
            try {
                String messageType = (String) inputStream.readObject();

                switch (messageType) {
                    case BLOCK:
                        handleBlock((Block) inputStream.readObject());
                        break;
                    case TRANSACTION:
                        handleTransaction((Transaction) inputStream.readObject());
                        break;
                    case REQUEST_BLOCKCHAIN:
                        sendBlockchain(blockchain);
                        break;
                    case REQUEST_KNOWN_NODES:
                        sendKnownNodes();
                        break;
                    default:
                        // Handle unknown message type
                }
            } catch (IOException | ClassNotFoundException e) {
                // Handle error, potentially disconnect peer
                break; // Exit the loop if there's an error
            }
        }
    }

    public void sendBlockchain(Blockchain blockchain) {
        try {
            outputStream.writeObject(blockchain);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error sending blockchain", e);
        }
    }

    private void sendKnownNodes() {
        try {
            List<String> knownNodes = P2PNetwork.getPeers().stream()
                    .map(peer -> peer.socket.getInetAddress().getHostAddress() + ":" + peer.socket.getPort())
                    .collect(Collectors.toList());
            outputStream.writeObject(knownNodes);
            outputStream.flush();
        } catch (IOException e) {
            // TODO
            // Handle error
        }
    }

    public void sendBlock(Block block) {
        try {
            outputStream.writeObject(block);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error sending block", e);
        }
    }

    public Blockchain requestBlockchain() {
        try {
            outputStream.writeObject("REQUEST_BLOCKCHAIN");
            outputStream.flush();
            return (Blockchain) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error requesting blockchain", e);
        }
    }


    public void sendTransaction(Transaction transaction) {
        try {
            outputStream.writeObject(transaction);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error sending transaction", e);
        }
    }

    public Transaction receiveTransaction() {
        try {
            return (Transaction) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error receiving transaction", e);
        }
    }

    public Block receiveBlock() {
        try {
            return (Block) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error receiving block", e);
        }
    }

    public void handleBlockchain(Blockchain receivedBlockchain) {
        if (receivedBlockchain.getChain().size() > blockchain.getChain().size()) {
            blockchain.setChain(receivedBlockchain.getChain());
            blockchain.getUtxoManager().updateUTXOSet(blockchain);
        }
    }

    public void handleBlock(Block block) {
        if (blockchain.addBlock(block)) {
            blockchain.getUtxoManager().updateUTXOSet(blockchain);
        }
    }

    public void handleTransaction(Transaction transaction) {
        blockchain.getTransactionPool().addTransaction(transaction);
    }

    public List<String> requestKnownNodes() {
        try {
            outputStream.writeObject(REQUEST_KNOWN_NODES);
            outputStream.flush();
            return (List<String>) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return null;
        }
    }
}