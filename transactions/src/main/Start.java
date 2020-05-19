package main;

import java.util.HashMap;

import transactions.*;

public class Start {

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
