package transactions;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;

public class Client {
	private int id;
	private ECDSA EC;
	
	public Client(int id) {
		this.id = id;
		EC = new ECDSA();
	}
	
	public int getId() {
		return id;
	}


	public HashMap<String,String> newTransaction(Transaction T){
		try {
			//Transaction to Hash
			String Hash = SHA.toHexString(SHA.getSHA(T.toString()));
			//Hash to Signature
			HashMap<String,String> obj = EC.sender(Hash);
			
			return obj;
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	

}
