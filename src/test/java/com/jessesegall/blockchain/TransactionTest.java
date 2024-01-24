package com.jessesegall.blockchain;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    private PublicKey senderPublicKey;
    private PrivateKey senderPrivateKey;
    private PublicKey recipientPublicKey;
    private UTXOManager utxoManager;
    private Transaction transaction;
    private TransactionOutput transactionOutput;
    private TransactionInput transactionInput;

    //Need to set provider for test file
    @BeforeAll
    public static void setup() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @BeforeEach
    void setUp() throws Exception {
        // Generate public and private keys for sender and recipient using KeyPairGenerator
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
        keyGen.initialize(256);

        KeyPair senderKeyPair = keyGen.generateKeyPair();
        senderPublicKey = senderKeyPair.getPublic();
        senderPrivateKey = senderKeyPair.getPrivate();

        KeyPair recipientKeyPair = keyGen.generateKeyPair();
        recipientPublicKey = recipientKeyPair.getPublic();

        // Set up UTXOManager and add a UTXO for testing
        utxoManager = new UTXOManager();
        transactionOutput = new TransactionOutput(senderPublicKey, 10, "0");
        utxoManager.addUTXO(transactionOutput);

        // Set up a list of inputs for the transaction
        List<TransactionInput> inputs = new ArrayList<>();
        transactionInput = new TransactionInput(transactionOutput.getId());
        transactionInput.setUTXO(transactionOutput);
        inputs.add(transactionInput);

        // Create a transaction from the sender to the recipient
        transaction = new Transaction(senderPublicKey, recipientPublicKey, 5, inputs);
        transaction.generateSignature(senderPrivateKey);
    }

    @Test
    void testTransactionSignatureVerification() {
        assertTrue(transaction.verifySignature(), "Signature should be valid.");
    }

    @Test
    void testProcessTransaction() {
        assertTrue(transaction.processTransaction(utxoManager), "Transaction should process successfully.");

        // After the transaction, we expect the UTXO set to have one new UTXO for the recipient,
        // and if there's any change, one UTXO representing the change returned to the sender.
        int expectedUTXOCount = transaction.getOutputs().size();
        assertEquals(expectedUTXOCount, utxoManager.getAllUTXOs().size(), "UTXO set should contain all new UTXOs after processing the transaction.");

        // Optionally, verify the actual contents of the UTXO set.
        for (TransactionOutput output : transaction.getOutputs()) {
            assertTrue(utxoManager.containsUTXO(output.getId()), "UTXO set should contain the output with ID: " + output.getId());
        }
    }


}
