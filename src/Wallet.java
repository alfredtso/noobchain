import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
    public PrivateKey privateKey;
    public PublicKey publicKey;

    public Wallet() {
        generateKeypair();
    }

    private void generateKeypair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");

            KeyPair keyPair = keyGen.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();

        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Wallet testing = new Wallet();
        System.out.println(testing.publicKey);
        System.out.println(testing.privateKey);
    }
}
