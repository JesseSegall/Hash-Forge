package com.jessesegall.blockchain;

import com.jessesegall.network.P2PNetwork;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Blockchain blockchain;
    private P2PNetwork p2pNetwork;

    public Wallet(Blockchain blockchain, P2PNetwork p2pNetwork) {
        this.blockchain = blockchain;
        this.p2pNetwork = p2pNetwork;
        generateKeyPair();
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

            // Initialize key generator and generate key pair
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            // Set public and private keys from key pair
            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> entry : blockchain.getUtxoManager().getAllUTXOs().entrySet()) {
            TransactionOutput utxo = entry.getValue();
            if (utxo.isMine(publicKey)) {
                total += utxo.getValue();
            }
        }
        return total;
    }

    public Transaction sendFunds(PublicKey recipient, float value) {
        if (getBalance() < value) {
            System.out.println("Not enough funds to send transaction. Transaction discarded.");
            return null;
        }

        List<TransactionInput> inputs = new ArrayList<>();
        float total = 0;

        for (Map.Entry<String, TransactionOutput> entry : blockchain.getUtxoManager().getAllUTXOs().entrySet()) {
            TransactionOutput utxo = entry.getValue();
            if (utxo.isMine(publicKey) && total < value) {
                total += utxo.getValue();
                inputs.add(new TransactionInput(utxo.getId()));
            }
        }

        Transaction newTransaction = new Transaction(publicKey, recipient, value, inputs);
        newTransaction.generateSignature(privateKey);

        for (TransactionInput input : inputs) {
            blockchain.getUtxoManager().removeUTXO(input.getTransactionOutputId());
        }

        // Add the transaction to the transaction pool
        blockchain.getTransactionPool().addTransaction(newTransaction);

        // Broadcast the transaction to the network
        p2pNetwork.broadcastTransaction(newTransaction);

        return newTransaction;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}