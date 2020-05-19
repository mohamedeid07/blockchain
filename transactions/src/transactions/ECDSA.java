package transactions;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author metamug.com
 */
public class ECDSA {

    private static final String SPEC = "secp256k1";
    private static final String ALGO = "SHA256withECDSA";
    private PublicKey publicKey;
    private PrivateKey privateKey;
    
	public ECDSA(){
    	ECGenParameterSpec ecSpec = new ECGenParameterSpec(SPEC);
        KeyPairGenerator g;
		try {
			g = KeyPairGenerator.getInstance("EC");
			g.initialize(ecSpec, new SecureRandom());
	        KeyPair keypair = g.generateKeyPair();
	        this.publicKey = keypair.getPublic();
	        this.privateKey = keypair.getPrivate();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    public HashMap<String,String> sender(String plaintext) throws InvalidKeyException, UnsupportedEncodingException, SignatureException, NoSuchAlgorithmException {

        //...... sign
        Signature ecdsaSign = Signature.getInstance(ALGO);
        ecdsaSign.initSign(privateKey);
        ecdsaSign.update(plaintext.getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();
        String pub = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        String sig = Base64.getEncoder().encodeToString(signature);
        //System.out.println(sig);
        //System.out.println(pub);

        HashMap<String,String> obj = new HashMap<String,String>();
        obj.put("publicKey", pub);
        obj.put("signature", sig);
        obj.put("message", plaintext);

        return obj;
    }

    public boolean receiver(HashMap<String,String> obj) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException, SignatureException {

        Signature ecdsaVerify = Signature.getInstance(ALGO);
        KeyFactory kf = KeyFactory.getInstance("EC");

        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(obj.get("publicKey")));

        KeyFactory keyFactory = KeyFactory.getInstance("EC");
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        ecdsaVerify.initVerify(publicKey);
        ecdsaVerify.update(obj.get("message").getBytes("UTF-8"));
        boolean result = ecdsaVerify.verify(Base64.getDecoder().decode(obj.get("signature")));

        return result;
    }

     public static void main(String[] args){
        try {
            ECDSA ECDSA = new ECDSA();
            HashMap<String,String> obj = ECDSA.sender("Hello");
            boolean result = ECDSA.receiver(obj);
            System.out.println(result);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ECDSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(ECDSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ECDSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SignatureException ex) {
            Logger.getLogger(ECDSA.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(ECDSA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
	

}
