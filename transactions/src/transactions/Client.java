package transactions;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
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

	public HashMap<String, String> newTransaction(Transaction previoustx) {
		//new Validator().printTransaction(previoustx);
		String s = previoustx.getId() + " " + previoustx.getInput() + " " + previoustx.getPrevioustx() + " "
				+ previoustx.getOutputindex();
		int[] output = previoustx.getOutput();
		double[] values = previoustx.getValues();
		for (int i = 0; i < output.length; i++) {
			s = s + " " + output[i] + " " + values[i];
		}
		try {
			// Transaction to Hash
			String Hash = SHA.toHexString(SHA.getSHA(s));
			// Hash to Signature
			HashMap<String, String> obj = EC.sender(Hash);

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

	public boolean validate(HashMap<String, String> obj) {
		try {
			return EC.receiver(obj);
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | UnsupportedEncodingException
				| SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
