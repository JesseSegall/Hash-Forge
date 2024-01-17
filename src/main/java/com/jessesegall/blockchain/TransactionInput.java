package com.jessesegall.blockchain;

public class TransactionInput {
    private String transactionOutputId;
    private TransactionOutput UTXO; // Unspent transaction output

    public void setUTXO(TransactionOutput UTXO){
        this.UTXO = UTXO;
    }

    public String getTransactionOutputId(){
        return  transactionOutputId;
    }

    public TransactionOutput getUTXO(){
        return UTXO;
    }
}
