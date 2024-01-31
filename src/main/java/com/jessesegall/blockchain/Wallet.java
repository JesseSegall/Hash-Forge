package com.jessesegall.blockchain;


import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.List;

// Manages user's keys and transactions.
public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private UTXOManager utxoManager;

    public Wallet(UTXOManager utxoManager) {
        this.utxoManager = utxoManager;
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
        for (TransactionOutput utxo : utxoManager.getAllUTXOs().values()) {
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
        // Create array list of inputs
        List<TransactionInput> inputs = new ArrayList<>();
        float total = 0;

        for (TransactionOutput utxo : utxoManager.getAllUTXOs().values()) {
            if (utxo.isMine(publicKey) && total < value) {
                total += utxo.getValue();
                inputs.add(new TransactionInput(utxo.getId()));

            }
        }

        Transaction newTransaction = new Transaction(publicKey, recipient, value, inputs);
        newTransaction.generateSignature(privateKey);

        // remove the UTXOs from manager after they are spent
        for (TransactionInput input : inputs) {
            utxoManager.removeUTXO(input.getTransactionOutputId());
        }
        return newTransaction;
    }


    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
