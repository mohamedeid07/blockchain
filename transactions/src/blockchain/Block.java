package blockchain;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Block implements Serializable {
	private static final long serialVersionUID = 1L;
	public String hash = "";
    public String parent;
    public String data;
    public long timestamp;
    public int nonce;

    public String hash() {
        return sha256(parent
                + data
                + Long.toString(timestamp)
                + Integer.toString(nonce));
    }

    public boolean verify() {
        return this.hash.equals(this.hash());
    }

    public void solve() {
        do {
            nonce++;
            hash = hash();
        } while (!hash.substring(0, 5).equals("00000"));
    }

    public String sha256(String input) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                    String hex = Integer.toHexString(0xff & hash[i]);
                    if(hex.length() == 1) hexString.append('0');
                    hexString.append(hex);
            }
            return hexString.toString();
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public String toString() {
        return hash.substring(59);
    }
}
