package com.jessesegall.blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

class TransactionInputTest {

    private TransactionOutput transactionOutput;
    private TransactionInput transactionInput;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(256);
        KeyPair keyPair = keyGen.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        transactionOutput = new TransactionOutput(publicKey, 10, "parentTransactionId");
        transactionInput = new TransactionInput("outputId");
        transactionInput.setUTXO(transactionOutput);
    }

    @Test
    void testGetUTXO() {
        assertEquals(transactionOutput, transactionInput.getUTXO(), "GetUTXO should return the correct UTXO.");
    }


}
