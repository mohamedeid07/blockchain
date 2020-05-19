package blockchain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

public class Monitor {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {

		Monitor monitor = new Monitor();
		try {
			System.out.println(monitor.getCurrentChain());
		} catch (RemoteException ex) {
			System.err.println("Cannot connect to miner...");
			System.exit(1);
		}
	}

	/**
	 *
	 * @return
	 * @throws java.rmi.RemoteException
	 *             if we cannot get chain from miner
	 */
	public ChainLink getCurrentChain() throws RemoteException {
		try {
			List<String> participants = getParticipants();
			RemoteInterface remote = (RemoteInterface) Naming.lookup(participants.get(0));

			ChainLink chain = remote.getCurrentChain();
			return chain;

		} catch (NotBoundException | MalformedURLException ex) {
			throw new RuntimeException(ex);
		}
	}

	private List<String> getParticipants() {
		try {
			FileInputStream file = new FileInputStream(RMIBasedP2P.PARTICIPANTS_FILE);
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

}
