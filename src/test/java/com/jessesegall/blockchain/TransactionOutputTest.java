package com.jessesegall.blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

class TransactionOutputTest {

    private PublicKey recipientPublicKey;
    private TransactionOutput transactionOutput;

    @BeforeEach
    void setUp() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(256);
        KeyPair recipientKeyPair = keyGen.generateKeyPair();
        recipientPublicKey = recipientKeyPair.getPublic();

        transactionOutput = new TransactionOutput(recipientPublicKey, 10, "parentTransactionId");
    }

    @Test
    void testIsMine() {
        assertTrue(transactionOutput.isMine(recipientPublicKey), "Output should belong to recipient public key.");
    }


}
