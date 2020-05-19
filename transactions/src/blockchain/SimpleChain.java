package blockchain;

import java.util.Date;
import java.util.HashMap;

public class SimpleChain {

    public ChainLink chain;

    private final P2PInterface p2p;

    public SimpleChain(final int port) {
        p2p = new RMIBasedP2P(this, port);
        chain = p2p.getCurrentChain();
    }

    public void mine() {
        while (true) {
        	long start = System.currentTimeMillis();
            Block block = new Block();
            block.data = getData();
            block.timestamp = new Date().getTime();
            block.parent = chain.head().block.hash;

            block.solve();
            p2p.broadcastBlock(block);
            long end = System.currentTimeMillis();
            System.out.println("mining took " + (end - start)/1000 + " seconds");
            System.out.println("=================================");
        }
    }
    
    public P2PInterface getP2P() {
    	return p2p;
    }


    private String getData() {
        return "blah";
    }

    public boolean addBlock(Block block) {
        if (!block.verify()) {
            return false;
        }

        if(chain.addBlock(block)) {
            System.out.println("Added block " + block);
            return true;
        }

        return false;
    }

	public void addTransaction(HashMap<String, String> transaction) {
		// TODO Auto-generated method stub
		
	}
}
