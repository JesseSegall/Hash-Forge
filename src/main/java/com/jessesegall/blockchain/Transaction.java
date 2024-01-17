package com.jessesegall.blockchain;
// Represents a single transaction;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
public class Transaction {
    // TODO this is just a placeholder value, constants will be set in Blockchain class
    public static final float minimumTransaction = 0.1f;
    private String transactionId;
    private PublicKey sender;
    private PublicKey recipient;
    private float value;
    private byte[] signature;

    private List<TransactionInput> inputs;
    private List<TransactionOutput> outputs = new ArrayList<>();

    private static int sequence = 0;

    public Transaction(PublicKey from, PublicKey to, float value, List<TransactionInput> inputs){
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    // Calculates transaction hash which will be used for its ID

    private String calculateHash(){
        sequence++;
        return BlockchainUtils.applySha256(
        BlockchainUtils.getStringFromKey(sender) +
                BlockchainUtils.getStringFromKey(recipient)+
                Float.toString(value) + sequence
        );
    }

    //Signs all data that shouldn't be messed with
    public void generateSignature(PrivateKey privateKey){
        String data = BlockchainUtils.getStringFromKey(sender) +
                BlockchainUtils.getStringFromKey(recipient) +
                Float.toString(value);
        signature = BlockchainUtils.applyECDSASig(privateKey, data);
    }

    //Verifies any data that has been signed hasn't been tampered with
    public boolean verifySignature(){
        String data = BlockchainUtils.getStringFromKey(sender) +
                BlockchainUtils.getStringFromKey(recipient) +
                Float.toString(value);
        return BlockchainUtils.verifyECDSASig(sender, data, signature);
    }

    public boolean processTransaction() {
        if (!verifySignature()) {
            System.out.println("#Transaction Signature failed to verify");
            return false;
        }

        // Gather transaction inputs (Make sure they are unspent):
        for (TransactionInput i : inputs) {
            //TODO
            //i.setUTXO(i.transactionOutputID);
        }

        // Check if transaction is valid:
        if (getInputsValue() < minimumTransaction) {
            System.out.println("#Transaction Inputs too small: " + getInputsValue());
            return false;
        }

        // Generate transaction outputs:
        float leftOver = getInputsValue() - value; // get value of inputs then the leftover change
        transactionId = calculateHash();
        outputs.add(new TransactionOutput(this.recipient, value, transactionId)); // send value to recipient
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId)); // send the left over 'change' back to sender

        // Add outputs to Unspent list
        for (TransactionOutput o : outputs) {
            // TODO: add the TransactionOutputs to the UTXO list
        }

        // Remove transaction inputs from UTXO lists as spent:
        for (TransactionInput i : inputs) {
            if (i.getUTXO() != null) {
                // TODO: remove the UTXO from the list of unspent transaction outputs
            }
        }

        return true;
    }
    public float getInputsValue() {
        float total = 0;
        for(TransactionInput i : inputs) {
            if(i.getUTXO() != null) total += i.getUTXO().getValue();
        }
        return total;
    }

    //Getters and Setters
    public String getTransactionID() {
        return transactionId;
    }


    public PublicKey getSender() {
        return sender;
    }



    public PublicKey getRecipient() {
        return recipient;
    }



    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public byte[] getSignature() {
        return signature;
    }


    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }



    public static int getSequence() {
        return sequence;
    }



    }
