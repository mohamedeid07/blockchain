package transactions;

public class Transaction {
	private int id, input, previoustx, outputindex;
	private double[] values;
	private int[] output;
	
	public Transaction(int id, int input, int previoustx, int outputindex, double[] values, int[] output) {
		this.id = id;
		this.input = input;
		this.previoustx = previoustx;
		this.outputindex = outputindex;
		this.values = values;
		this.output = output;
	}
	public int getId() {
		return id;
	}

	public int getInput() {
		return input;
	}

	public int getPrevioustx() {
		return previoustx;
	}

	public int getOutputindex() {
		return outputindex;
	}

	public double[] getValues() {
		return values;
	}

	public int[] getOutput() {
		return output;
	}
	
}
