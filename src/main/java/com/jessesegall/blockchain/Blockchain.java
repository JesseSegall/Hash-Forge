package com.jessesegall.blockchain;

import com.jessesegall.consensus.ProofOfWork;

import java.util.ArrayList;
import java.util.List;

// Manages the chain of blocks.
public class Blockchain {
    private static Blockchain instance;
    private List<Block> chain;
    private ProofOfWork proofOfWork;
    private UTXOManager utxoManager = new UTXOManager();

    public Blockchain(int difficulty) {
        this.chain = new ArrayList<>();
        this.proofOfWork = new ProofOfWork(difficulty);
        createGenesisBlock();
    }

    public static Blockchain getInstance(int difficulty) {
        if (instance == null) {
            instance = new Blockchain(difficulty);
        }
        return instance;
    }

    private void createGenesisBlock() {
        Block genesis = new Block("0", new ArrayList<>());
        proofOfWork.mine(genesis);
        chain.add(genesis);
    }

    public boolean addBlock(Block block) {
        if (validateNewBlock(block, getLastBlock())) {
            chain.add(block);
            updateUTXOSet(block);
            return true;
        }
        return false;
    }

    private Block getLastBlock() {
        return !chain.isEmpty() ? chain.get(chain.size() - 1) : null;
    }


    private boolean validateNewBlock(Block newBlock, Block previousBlock) {
        if (previousBlock != null && !newBlock.getPreviousHash().equals(previousBlock.getHash())) {
            return false;
        }
        return newBlock.validateTransactions(utxoManager) && proofOfWork.validate(newBlock);
    }

    private void updateUTXOSet(Block block) {
        for (Transaction transaction : block.getTransactions()) {
            for (TransactionInput input : transaction.getInputs()) {
                utxoManager.removeUTXO(input.getUTXO().getId());
            }
            for (TransactionOutput output : transaction.getOutputs()) {
                utxoManager.addUTXO(output);
            }
        }
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
