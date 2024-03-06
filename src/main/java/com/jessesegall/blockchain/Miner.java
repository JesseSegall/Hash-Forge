package com.jessesegall.blockchain;

import com.jessesegall.network.P2PNetwork;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Miner {
    private Blockchain blockchain;
    private UTXOManager utxoManager;
    private Wallet wallet;
    private TransactionPool transactionPool;
    private P2PNetwork p2pNetwork;

    public Miner(Blockchain blockchain, UTXOManager utxoManager, Wallet wallet, P2PNetwork p2pNetwork) {
        this.blockchain = blockchain;
        this.utxoManager = utxoManager;
        this.wallet = wallet;
        this.p2pNetwork = p2pNetwork;
    }

    public void startMining() {
        // Create a new block
        Block block = new Block(blockchain.getLastBlock().getHash(), new ArrayList<>());

        // Get the transactions from the transaction pool
        List<Transaction> transactions = transactionPool.getTransactions();
        block.getTransactions().addAll(transactions);

        // Create coinbase transaction and add
        Transaction coinbase = createCoinbaseTransaction(wallet.getPublicKey());
        block.getTransactions().add(coinbase);

        blockchain.getProofOfWork().mine(block);

        blockchain.addBlock(block);

        //update UTXO set
        utxoManager.updateUTXOSet(blockchain);

        transactionPool.clear();

        broadcastBlock(block);
    }

    private Transaction createCoinbaseTransaction(PublicKey minerPublicKey) {
        // Create a new transaction output with the miner's reward
        float minerReward = 50;
        TransactionOutput coinbaseOutput = new TransactionOutput(minerPublicKey, minerReward, "Coinbase Transaction");

        // Create a new transaction with an empty input and the coinbase output
        List<TransactionInput> inputs = new ArrayList<>();
        List<TransactionOutput> outputs = new ArrayList<>();
        outputs.add(coinbaseOutput);

        Transaction coinbaseTransaction = new Transaction(null, minerPublicKey, minerReward, inputs);
        coinbaseTransaction.setOutputs(outputs);

        return coinbaseTransaction;
    }


    private void broadcastBlock(Block block) {
        P2PNetwork.broadcastBlock(block);
    }

}
