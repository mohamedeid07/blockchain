package blockchain;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.LinkedList;
import java.util.List;

public class RMIBasedP2P implements P2PInterface {

	public static final String PARTICIPANTS_FILE = "./system.properties";

	private final SimpleChain sc;

	public RMIBasedP2P(SimpleChain sc, int port) {
		this.sc = sc;

		try {
			startRMIHandler(port);
			registerAsParticipant(port);

		} catch (RemoteException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void broadcastBlock(Block block) {

		List<String> participants = getParticipants();
		System.out.println("Broadcast <" + block + "> to " + participants);

		for (String participant : participants) {
			try {
				RemoteInterface remote = (RemoteInterface) Naming.lookup(participant);
				remote.addBlock(block);
			} catch (RemoteException ex) {
				System.err.println("Failed to send to " + participant + "! Moving on...");
			} catch (NotBoundException | MalformedURLException ex) {
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public ChainLink getCurrentChain() {
		try {
			List<String> participants = getParticipants();
			RemoteInterface remote = (RemoteInterface) Naming.lookup(participants.get(0));

			ChainLink chain = remote.getCurrentChain();
			if (chain == null) {
				// The chain has not started yet
				return initialChainLink();
			}

			return chain;

		} catch (NotBoundException | MalformedURLException | RemoteException ex) {
			throw new RuntimeException(ex);
		}
	}

	private ChainLink initialChainLink() {
		Block root = new Block();
		root.hash = new String(new char[64]).replace("\0", "0");
		return new ChainLink(root);
	}

	/**
	 * Start the RMI server and listen on mentioned port.
	 * 
	 * @param port
	 * @throws RemoteException
	 */
	private void startRMIHandler(final int port) throws RemoteException {
		RemoteHandler rh = new RemoteHandler(sc);
		Registry rgsty = LocateRegistry.createRegistry(port);
		rgsty.rebind("simplechain", rh);
	}

	/**
	 * Add myself (my port) to the list of participants.
	 * 
	 * @param port
	 */
	private void registerAsParticipant(int port) {
		String myself = "rmi://127.0.0.1:" + String.valueOf(port) + "/simplechain";
		List<String> participants = getParticipants();
		for (String participant : participants) {
			if (participant.equals(myself)) {
				// I'm already in the list
				return;
			}
		}

		participants.add(myself);
		saveParticipants(participants);
	}

	/**
	 * Read the list of participants from the shared file.
	 * 
	 * @return
	 */
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

	/**
	 * Save the list of participants to the shared file.
	 * 
	 * @param participants
	 */
	private void saveParticipants(List<String> participants) {
		try {
			FileOutputStream fileOut = new FileOutputStream(PARTICIPANTS_FILE);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeObject(participants);
			objectOut.close();

		} catch (FileNotFoundException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}
