package blockchain;

import java.util.Date;

public class MaliciousUser extends SimpleChain {
	public ChainLink chain;
	private String head;

	private final P2PInterface p2p;

	public MaliciousUser(final int port) {
		super(port);
		p2p = getP2P();
		chain = p2p.getCurrentChain();
		head = chain.head().block.hash;
	}

	@Override
	public void mine() {
		while (true) {
			long start = System.currentTimeMillis();
			Block block = new Block();
			block.data = getData();
			block.timestamp = new Date().getTime();
			block.parent = head;

			block.solve();
			p2p.broadcastBlock(block);
			head = block.hash;
			long end = System.currentTimeMillis();
			System.out.println("mining took " + (end - start) / 1000 + " seconds");
			System.out.println("=================================");
		}
	}

	private String getData() {
		return "blah";
	}
}
