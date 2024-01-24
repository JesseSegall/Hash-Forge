package com.jessesegall.blockchain;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockTest {

    private Block block;
    private Transaction transaction;
    private PublicKey senderPublicKey;
    private PrivateKey senderPrivateKey; // Added for signing transactions
    private PublicKey recipientPublicKey;
    private UTXOManager utxoManager;
    private List<Transaction> transactions;
    private String previousHash = "00000abcd1234";

    @BeforeAll
    public static void setupClass() {
        Security.addProvider(new BouncyCastleProvider());
    }

    @BeforeEach
    void setUp() throws Exception {
        // Generate mock public/private keys for the sender and recipient
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "BC");
        keyGen.initialize(256);

        KeyPair senderKeyPair = keyGen.generateKeyPair();
        senderPublicKey = senderKeyPair.getPublic();
        senderPrivateKey = senderKeyPair.getPrivate(); // Private key for signing

        KeyPair recipientKeyPair = keyGen.generateKeyPair();
        recipientPublicKey = recipientKeyPair.getPublic();

        // Create a UTXOManager and add a UTXO for testing
        utxoManager = new UTXOManager();
        TransactionOutput utxo = new TransactionOutput(senderPublicKey, 10, "parentTransactionId");
        utxoManager.addUTXO(utxo);

        transactions = new ArrayList<>();
        transaction = new Transaction(senderPublicKey, recipientPublicKey, 5, new ArrayList<>());
        transaction.generateSignature(senderPrivateKey);

        TransactionInput input = new TransactionInput(utxo.getId());
        input.setUTXO(utxo);
        transaction.getInputs().add(input);

        TransactionOutput outputToRecipient = new TransactionOutput(recipientPublicKey, 5, "childTransactionId1");
        TransactionOutput changeBackToSender = new TransactionOutput(senderPublicKey, 5, "childTransactionId2");
        transaction.getOutputs().addAll(List.of(outputToRecipient, changeBackToSender));

        // Process the transaction and assert
        boolean transactionProcessed = transaction.processTransaction(utxoManager);
        System.out.println("Transaction processed: " + transactionProcessed);
        assertTrue(transactionProcessed, "Transaction should process successfully");

        block = new Block(previousHash, transactions);
    }

    @Test
    void testCalculateHash() {
        String calculatedHash = block.calculateHash();
        assertEquals(calculatedHash, block.getHash(), "Calculated hash should match the stored hash");
    }

    @Test
    void testValidateTransactions() {
        System.out.println("UTXOManager state before processing transaction:");
        utxoManager.getAllUTXOs().forEach((id, output) -> System.out.println("UTXO ID: " + id + ", Value: " + output.getValue()));
        assertTrue(block.validateTransactions(utxoManager), "All transactions should be valid");
    }
}
