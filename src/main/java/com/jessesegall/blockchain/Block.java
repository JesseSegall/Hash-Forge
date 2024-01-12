package com.jessesegall.blockchain;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

// Represents a single block.
public class Block {

    private String hash;
    private String previousHash;
    private long timeStamp;
    private int nonce;

    private List<Transaction> transactions;

    //Block constructor
    public  Block(String previousHash, List<Transaction>transactions){
        this.previousHash = previousHash;
        this.transactions = transactions;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        String input = previousHash + Long.toString(timeStamp) + Integer.toString(nonce) + transactions.toString();
        return applySha256(input);
    }



    // TODO move to util class later
    public static String applySha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Apply hash to input

            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            //Hash as hexadecimal

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }


    // Getters and setters
    public String getHash() {
        return hash;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getTransactions() {
        return transactions.toString();
    }

    public int getNonce() {
        return nonce;
    }

    public void incrementNonce() {
        this.nonce++;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}
