package com.jessesegall.blockchain;
import java.util.HashMap;
import java.util.Map;
public class UTXOManager {
    private Map<String, TransactionOutput> UTXOs = new HashMap<>();

    public void addUTXO(TransactionOutput utxo) {
        UTXOs.put(utxo.getId(), utxo);
    }

    public void removeUTXO(String transactionOutputId){
        UTXOs.remove(transactionOutputId);
    }

    public TransactionOutput getUTXO(String transactionOutputId){
        return UTXOs.get(transactionOutputId);
    }

    public boolean containsUTXO(String transactionOutputId) {
        return UTXOs.containsKey(transactionOutputId);
    }

    // Gets all UTXOs
    public Map<String, TransactionOutput> getAllUTXOs() {
        return new HashMap<>(UTXOs);
    }

}
