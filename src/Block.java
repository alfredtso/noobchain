import java.util.Date;

public class Block {

    public int index;
    public long timestamp;
    public String hash;
    public String prevHash;
    public String data;
    public int difficulty;
    public int nonce;

    public Block(String prevHash, String data, int index) {
        this.index = index;
        this.data = data;
        this.prevHash = prevHash;
        this.timestamp = new Date().getTime()/1000;
        this.hash = calculateHash();
        this.difficulty = Noobchain.getDifficulty();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                prevHash + timestamp + nonce + data + index + difficulty);

    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!this.hash.substring(0, difficulty).equals(target)) {
            nonce++;
            this.hash = calculateHash();
        }

        System.out.println("New Block is generated : " + hash);
    }

    @Override
    public String toString() {
        StringBuilder myString = new StringBuilder();
        myString.append("--- Block Info ---");
        myString.append("\n");
        myString.append("Index: ").append(index);
        myString.append("\n");
        myString.append("Timestamp: ").append(timestamp);
        myString.append("\n");
        myString.append("Hash: ").append(hash);
        myString.append("\n");
        myString.append("Previous Hash: ").append(prevHash);
        myString.append("\n");

        return myString.toString();
    }

    public static void main(String[] args) {
        Block myBlock = new Block("0", "diu", 0);
        System.out.println(myBlock);
    }
}
