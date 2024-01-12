package com.jessesegall.consensus;

import com.jessesegall.blockchain.Block;
import com.jessesegall.blockchain.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ProofOfWorkTest {

    private ProofOfWork proofOfWork;
    private Block block;
    private final int difficulty = 4;

    @BeforeEach
    void setUp() {

        proofOfWork = new ProofOfWork(difficulty);


        block = new Block("0000000000000000000000000000000000000000000000000000000000000000", new ArrayList<>());
    }

    @Test
    void mineShouldCreateValidHash() {
        proofOfWork.mine(block);
        String target = new String(new char[difficulty]).replace('\0', '0');
        assertEquals(target, block.getHash().substring(0, difficulty));
    }

    @Test
    void validateShouldReturnTrueForValidHash() {
        proofOfWork.mine(block);
        assertTrue(proofOfWork.validate(block));
    }

    @Test
    void validateShouldReturnFalseForInvalidHash() {
        String invalidHash = "abcd1234"; // Does not have leading zeros required by the difficulty
        block.setHash(invalidHash);
        assertFalse(proofOfWork.validate(block));
    }

    @Test
    void testGettersAndSetters() {
        proofOfWork.setDifficulty(5);
        assertEquals(5, proofOfWork.getDifficulty());
    }
}
