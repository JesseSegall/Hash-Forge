package com.jessesegall.blockchain;

import java.security.PublicKey;

public class TransactionOutput {
    private String id;
    private PublicKey recipient;
    private float value;

    private String parentTransactionId;

    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = BlockchainUtils.applySha256(
                BlockchainUtils.getStringFromKey(recipient) +
                        Float.toString(value) +
                        parentTransactionId
        );
    }


    public boolean isMine(PublicKey publicKey) {
        return (publicKey.equals(recipient));
    }

    public String getId() {
        return id;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public float getValue() {
        return value;
    }

    public String getParentTransactionId() {
        return parentTransactionId;
    }
}
