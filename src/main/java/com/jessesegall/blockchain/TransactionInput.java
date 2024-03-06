package com.jessesegall.blockchain;


public class TransactionInput {

    private String transactionOutputId;   // ID of the TransactionOutput to be used as an input


    private TransactionOutput referencedOutput; // The TransactionOutput that this input references

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    public TransactionOutput getReferencedOutput() {
        return referencedOutput;
    }

    public void setReferencedOutput(TransactionOutput referencedOutput) {
        this.referencedOutput = referencedOutput;
    }
}
