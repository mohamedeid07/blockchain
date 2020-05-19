package blockchain;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RemoteHandler extends UnicastRemoteObject implements RemoteInterface {

	private static final long serialVersionUID = 1L;
	private final SimpleChain sc;

    public RemoteHandler(SimpleChain sc) throws RemoteException {
        super();
        this.sc = sc;
    }


    @Override
    public void addBlock(Block block) throws RemoteException {
        sc.addBlock(block);
    }

    @Override
    public ChainLink getCurrentChain() throws RemoteException {
        return sc.chain;
    }


	@Override
	public void addTransaction(HashMap<String, String> Transaction) throws RemoteException {
		sc.addTransaction(Transaction);
		
	}

}
