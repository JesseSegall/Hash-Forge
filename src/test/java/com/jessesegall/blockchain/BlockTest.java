package com.jessesegall.blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BlockTest {

    private Block block;
    private List<Transaction> transactions;

    @BeforeEach
    void setUp() {
        transactions = new ArrayList<>();
        // Add mock transactions to the list
        // transactions.add(new Transaction(...));
        block = new Block("0000", transactions);
    }

    @Test
    void testCalculateHash() {
        String calculatedHash = block.calculateHash();
        assertNotNull(calculatedHash);
        // You can add more assertions to validate the structure or length of the hash
    }

    @Test
    void testIncrementNonce() {
        int initialNonce = block.getNonce();
        block.incrementNonce();
        assertEquals(initialNonce + 1, block.getNonce());
    }

    // Test getters and setters
    @Test
    void testGettersAndSetters() {
        block.setPreviousHash("1234");
        assertEquals("1234", block.getPreviousHash());

        block.setTimeStamp(100000L);
        assertEquals(100000L, block.getTimeStamp());

        block.setNonce(5);
        assertEquals(5, block.getNonce());


        String newHash = "abcd";
        block.setHash(newHash);
        assertEquals(newHash, block.getHash());
    }

    @Test
    void testGetTransactions() {
        List<Transaction> newTransactions = new ArrayList<>();


        block.setTransactions(newTransactions);
        assertEquals(newTransactions.toString(), block.getTransactions());
    }


    // Add more tests for other functionalities as needed
}
