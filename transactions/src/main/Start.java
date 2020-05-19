package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import blockchain.Block;
import blockchain.RemoteInterface;
import transactions.*;

public class Start {
	
	public static final String PARTICIPANTS_FILE = "./system.properties";
	
	private List<String> getParticipants() {
		try {
			FileInputStream file = new FileInputStream(PARTICIPANTS_FILE);
			ObjectInputStream objectIn = new ObjectInputStream(file);
			List<String> participants = (List<String>) objectIn.readObject();
			objectIn.close();
			return participants;

		} catch (FileNotFoundException ex) {
			// file does not exist => I'm the first participant
			return new LinkedList<>();

		} catch (IOException | ClassNotFoundException ex) {
			throw new RuntimeException(ex);

		}
	}
	
	public void broadcastBlock(HashMap<String, String> transaction) {

		List<String> participants = getParticipants();
		System.out.println("Broadcast <" + transaction + "> to " + participants);

		for (String participant : participants) {
			try {
				RemoteInterface remote = (RemoteInterface) Naming.lookup(participant);
				remote.addTransaction(transaction);
			} catch (RemoteException ex) {
				System.err.println("Failed to send to " + participant + "! Moving on...");
			} catch (NotBoundException | MalformedURLException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	public static void main(String[] args) {
		Validator validator = new Validator();
		HashMap<Integer, Transaction> transactions = validator.readFile();

		HashMap<Integer, Client> clients = new HashMap<Integer, Client>();

		for (int i = 1; i < 51; i++) {
			validator.printTransaction(transactions.get(i));
		}
		// Creating Clients
		for (int i = 0; i < 50; i++) {
			clients.put(i, new Client(i));
		}
		// Issuing transactions
		for (int i = 50; i < transactions.size(); i++) {
			if (transactions.containsKey(i)) {
				Transaction t = transactions.get(i);
				int input = t.getInput();
				Client payer = clients.get(input);
				int previoustx = t.getPrevioustx();
				HashMap<String, String> obj = payer.newTransaction(transactions.get(previoustx));
				// Validating by the payee
				Client payee = clients.get(transactions.get(i).getOutput()[0]);
				System.out.println(payee.validate(obj));
			}
		}
	}

}
