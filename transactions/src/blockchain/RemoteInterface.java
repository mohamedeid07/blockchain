package blockchain;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashMap;

public interface RemoteInterface extends Remote{
    public void addBlock(Block block) throws RemoteException;
    public ChainLink getCurrentChain() throws RemoteException;
    public void addTransaction(HashMap<String, String> Transaction) throws RemoteException;
}
