package com.jessesegall.consensus;

import com.jessesegall.blockchain.Block;
import com.jessesegall.blockchain.BlockchainUtils;

// Proof-of-work algorithm.
public class ProofOfWork {
    private  int difficulty;


    public ProofOfWork(int difficulty){
        this.difficulty=difficulty;
    }

    public void mine(Block block){
        String target = new String(new char[difficulty]).replace('\0', '0'); // Create a string with difficulty * "0"
        while (!block.getHash().substring(0, difficulty).equals(target)) {
            block.incrementNonce(); // Increase nonce to try a new hash
            block.setHash(block.calculateHash()); // Calculate the new hash with the new nonce value
        }
        System.out.println("Block Mined : " + block.getHash());
    }

    //Double hashing
    private String calculatedDoubleHash(Block block){
        String input = block.getPreviousHash() + Long.toString(block.getTimeStamp()) + Integer.toString(block.getNonce()) + block.getTransactions();
        String hashedOnce = BlockchainUtils.applySha256(input);
        return BlockchainUtils.applySha256(hashedOnce);
    }

    // Validates if the block's hash meets the required difficulty level
    public boolean validate(Block block) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        return block.getHash().substring(0, difficulty).equals(target);
    }


    // Getters and Setters
    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
