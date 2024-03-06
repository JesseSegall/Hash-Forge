package com.jessesegall.blockchain;

import com.jessesegall.consensus.ProofOfWork;

import java.util.ArrayList;
import java.util.List;

// Manages the chain of blocks.
public class Blockchain {
    private static Blockchain instance;
    private List<Block> chain;
    private ProofOfWork proofOfWork;
    private UTXOManager utxoManager;
    private TransactionPool transactionPool;

    public Blockchain(int difficulty) {
        this.chain = new ArrayList<>();
        this.proofOfWork = new ProofOfWork(difficulty);
        this.utxoManager = new UTXOManager();
        this.transactionPool = new TransactionPool();
        createGenesisBlock();
    }

    public static Blockchain getInstance(int difficulty) {
        if (instance == null) {
            instance = new Blockchain(difficulty);
        }
        return instance;
    }

    public static Blockchain getInstance() {
        return instance;
    }

    public static void setInstance(Blockchain instance) {
        Blockchain.instance = instance;
    }

    private void createGenesisBlock() {
        Block genesis = new Block("0", new ArrayList<>());
        proofOfWork.mine(genesis);
        chain.add(genesis);
    }

    public List<Block> getChain() {
        return chain;
    }

    public void setChain(List<Block> chain) {
        this.chain = chain;
        utxoManager.updateUTXOSet(this); // Update UTXO set after setting a new chain
    }

    public ProofOfWork getProofOfWork() {
        return proofOfWork;
    }

    public void setProofOfWork(ProofOfWork proofOfWork) {
        this.proofOfWork = proofOfWork;
    }

    public UTXOManager getUtxoManager() {
        return utxoManager;
    }

    public TransactionPool getTransactionPool() {
        return transactionPool;
    }

    public boolean addBlock(Block block) {
        if (validateNewBlock(block, getLastBlock())) {
            chain.add(block);
            utxoManager.updateUTXOSet(this); // Update UTXO set after adding a block
            return true;
        }
        return false;
    }

    Block getLastBlock() {
        return !chain.isEmpty() ? chain.get(chain.size() - 1) : null;
    }

    private boolean validateNewBlock(Block newBlock, Block previousBlock) {
        if (previousBlock != null && !newBlock.getPreviousHash().equals(previousBlock.getHash())) {
            return false;
        }
        return newBlock.validateTransactions(utxoManager) && proofOfWork.validate(newBlock);
    }

    public boolean validateBlockchain() {
        Block currentBlock;
        Block previousBlock;
        for (int i = 1; i < chain.size(); i++) {
            currentBlock = chain.get(i);
            previousBlock = chain.get(i - 1);
            if (!currentBlock.getHash().equals(currentBlock.calculateHash())) {
                return false;
            }
            if (!previousBlock.getHash().equals(currentBlock.getPreviousHash())) {
                return false;
            }
            if (!proofOfWork.validate(currentBlock)) {
                return false;
            }
        }
        return true;
    }
}