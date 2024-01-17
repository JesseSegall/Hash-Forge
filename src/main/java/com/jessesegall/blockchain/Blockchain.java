package com.jessesegall.blockchain;
import com.jessesegall.consensus.ProofOfWork;

import java.util.ArrayList;
import java.util.List;
// Manages the chain of blocks.
public class Blockchain {
    private static Blockchain instance;
    private List<Block> chain;
    private ProofOfWork proofOfWork;

    public Blockchain(int difficulty){
        this.chain = new ArrayList<>();
        this.proofOfWork = new ProofOfWork(difficulty);

        createGenesisBlock();
    }

    public static Blockchain getInstance(int difficulty) {
        if (instance == null) {
            instance =  new Blockchain(difficulty);
        }
        return instance;
    }

    private void createGenesisBlock() {
        Block genesis = new Block("0", new ArrayList<>());
        proofOfWork.mine(genesis);
        chain.add(genesis);
    }


}
