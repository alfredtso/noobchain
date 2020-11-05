import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class Noobchain {

    public static int blockGenInterval = 1;
    public static int adjustmentInterval = 2;
    public static int difficulty = 4;
    public static List<Block> blockchain = new ArrayList<>();

    public static Block generateNextBlock(String blockData) {
        if (blockchain.size() == 0) {
           return new Block("0", blockData, 0);
        } else {
            Block prevBlock = blockchain.get(blockchain.size()-1);
            return new Block(prevBlock.calculateHash(), blockData, prevBlock.index+1);
        }
    }

    public static int getDifficulty() {
        if (blockchain.size() == 0) return difficulty;

        Block latestBlock = blockchain.get(blockchain.size()-1);
        if (latestBlock.index % adjustmentInterval == 0 && latestBlock.index != 0) {
//            System.out.println("Adjusting difficulty ...");
            return getAdjustedDifficulty(latestBlock, blockchain);
        } else {
//            System.out.println("Using difficulty: " + latestBlock.difficulty);
            return latestBlock.difficulty;
        }
    }

    private static int getAdjustedDifficulty(Block latestBlock, List<Block> blockchain) {
        Block prevAdjustmentBlock = blockchain.get(blockchain.size() - adjustmentInterval);
        long timeExpected = blockGenInterval * adjustmentInterval;
        long timeTaken = latestBlock.timestamp - prevAdjustmentBlock.timestamp;
        int adjustedDiff;
        if (timeTaken < timeExpected / 2) {
//            System.out.println("Incrementing difficulty by 1 ...");
            adjustedDiff = prevAdjustmentBlock.difficulty + 1;
//            System.out.println("Difficulty : " + prevAdjustmentBlock.difficulty + " -> " + adjustedDiff);
            return adjustedDiff;
        } else if (timeTaken > timeExpected * 2) {
//            System.out.println("Decrementing difficulty by 1 ...");
            adjustedDiff = prevAdjustmentBlock.difficulty - 1;
//            System.out.println("Difficulty : " + prevAdjustmentBlock.difficulty + " -> " + adjustedDiff);
            return adjustedDiff;
        } else {
            return prevAdjustmentBlock.difficulty;
        }
    }

    // cannot use this, needs to debug
    public static Boolean isNewBlockValid(Block curBlock) {

        Block prevBlock;

        if (blockchain.size() == 0) {
            return curBlock.hash.equals(curBlock.calculateHash());
        }

        prevBlock = blockchain.get(blockchain.size()-1);

        // compare the index
        if (curBlock.index != prevBlock.index+1){
            System.out.println("Invalid index");
            return false;
        }

        // compare the current hash
        if (!curBlock.hash.equals(curBlock.calculateHash())) {
            System.out.println("Current block hash value is not valid");
            return false;
        }

        // compare the prevHash
        if (!prevBlock.hash.equals(curBlock.prevHash)) {
            System.out.println("The values for prevHash are not equal");
            return false;
        }

        return true;
    }

    public static Boolean isChainValid() {
        Block curBlock;
        Block prevBlock;
        // Loop the chain to check all hash, prevhash and index are correct
        for (int i = 1; i < blockchain.size(); i++) {
            curBlock = blockchain.get(i);
            prevBlock = blockchain.get(i - 1);

            // compare the index
            if (curBlock.index != prevBlock.index+1){
                System.out.println("Invalid index");
                return false;
            }

            // compare the current hash
            if (!curBlock.hash.equals(curBlock.calculateHash())) {
                System.out.println("Current hash not equal");
                return false;
            }

            if (!prevBlock.hash.equals(curBlock.prevHash)) {
                System.out.println("Prev Hash not equal");
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) {

        for (int i = 0; i < 20; i++) {
            String info = Integer.toString(i);
            System.out.println("Generating New Block for String: " + info);
            Block latestGenBlock = generateNextBlock(info);
            blockchain.add(generateNextBlock(info));
            blockchain.get(i).mineBlock(getDifficulty());
        }

        System.out.println("Checking if the chain is valid : " + isChainValid());
        String blockchainJson = new GsonBuilder().setPrettyPrinting().create().toJson(blockchain);
        System.out.println("\nThe blockchain: ");
        System.out.println(blockchainJson);


}
}
