package com.jessesegall.blockchain;

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
        return BlockchainUtils.applySha256(input);
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
