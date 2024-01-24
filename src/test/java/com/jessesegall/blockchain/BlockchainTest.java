package com.jessesegall.blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BlockchainTest {

    private Blockchain blockchain;
    private int difficulty = 1; // Example difficulty, adjust as necessary

    @BeforeEach
    void setUp() {
        // Reset the blockchain instance before each test
        Blockchain.setInstance(null);
        blockchain = Blockchain.getInstance(difficulty);
    }

    @Test
    void blockchainInitTest() {
        // Check that the blockchain is initialized with only the genesis block
        assertEquals(1, blockchain.getChain().size(), "Blockchain should be initialized with only the genesis block");
    }

    @Test
    void addBlockTest() {
        // Attempt to add a new block to the blockchain
        Block newBlock = new Block(blockchain.getChain().get(blockchain.getChain().size() - 1).getHash(), new ArrayList<>());
        blockchain.getProofOfWork().mine(newBlock); // Assuming mining occurs before adding
        assertTrue(blockchain.addBlock(newBlock), "Should be able to add a new block");

        // Check that the blockchain now contains two blocks
        assertEquals(2, blockchain.getChain().size(), "Blockchain should have two blocks after addition");
    }

    @Test
    void validateBlockchainTest() {
        // Validate the blockchain after initialization
        assertTrue(blockchain.validateBlockchain(), "Blockchain should be valid after initialization");

        // Add a new block and re-validate
        Block newBlock = new Block(blockchain.getChain().get(blockchain.getChain().size() - 1).getHash(), new ArrayList<>());
        blockchain.getProofOfWork().mine(newBlock); // Mine the new block
        blockchain.addBlock(newBlock);
        assertTrue(blockchain.validateBlockchain(), "Blockchain should be valid after adding a new block");
    }

   
}
