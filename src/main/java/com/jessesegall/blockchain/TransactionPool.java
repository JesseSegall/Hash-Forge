package com.jessesegall.blockchain;

import java.util.ArrayList;
import java.util.List;

// Will store unconfirmed transactions.
public class TransactionPool {

    private List<Transaction> transactions;

    public TransactionPool() {
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public void removeTransaction(Transaction transaction) {
        transactions.remove(transaction);
    }

    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions);
    }

    public void clear() {
        transactions.clear();
    }
}
