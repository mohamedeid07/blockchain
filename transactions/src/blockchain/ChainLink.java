package blockchain;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ChainLink implements Serializable {
	private static final long serialVersionUID = 1L;
	public Block block;
	public List<ChainLink> children = new LinkedList<>();

	public ChainLink(Block block) {
		this.block = block;
	}

	public ChainLink head() {
		if (children.isEmpty()) {
			return this;
		}

		int max = 0;
		ChainLink current = null;
		for (ChainLink child : children) {
			int depth = child.depth();
			if (depth > max) {
				max = depth;
				current = child.head();
			}
		}
		return current;
	}

	public int depth() {
		if (children.isEmpty()) {
			return 1;
		}

		int max = 0;
		for (ChainLink child : children) {
			int depth = child.depth();
			if (depth > max) {
				max = depth;
			}
		}

		return max + 1;
	}

	public boolean addBlock(Block block) {
		if (this.block.hash.equals(block.parent)) {

			this.children.add(new ChainLink(block));
			return true;
		}

		for (ChainLink child : children) {
			if (child.addBlock(block)) {
				return true;
			}
		}

		return false;
	}

	@Override
	public String toString() {
		return this.print(new StringBuilder(), "", "");
	}

	private String print(StringBuilder buffer, String prefix, String childrenPrefix) {
		buffer.append(prefix);
		buffer.append(block);
		buffer.append('\n');
		for (Iterator<ChainLink> it = children.iterator(); it.hasNext();) {
			ChainLink next = it.next();
			if (it.hasNext()) {
				next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
			} else {
				next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
			}
		}
		return buffer.toString();
	}
}
