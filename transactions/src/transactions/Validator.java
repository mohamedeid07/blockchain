package transactions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Validator {
	
	HashMap<Integer, Transaction> transactions ;

	public HashMap<Integer, Transaction> readFile(){
		
		transactions = new HashMap<Integer, Transaction>();
		
		BufferedReader r ;
		//int g = 0;
		try{
			r = new BufferedReader(new FileReader("txdataset_v2.txt"));
			String line = r.readLine();
			
			while(line != null){
				
				Transaction current = parseLine(line);
				transactions.put(current.getId(), current);
				/*
				if(validateBalance(current)) {
					transactions.put(current.getId(), current);
				} else {
					System.out.println("FALSE---------------"+current.getId()+"  "+ g);
					g++;
				}
				*/
				line = r.readLine();
			}
			
			r.close();
			
		}catch(IOException e){
			e.printStackTrace();
		}
		
		checkDoubleSpend();
		return transactions;
		
	}
	
	public  Transaction parseLine(String s){
		
		int id, input, previoustx, outputindex;
		double[] values;
		int[] output;
		
		String[] parts = s.split("\\s+");
		
		
		id = Integer.parseInt(parts[0].trim());
		
		String inputStr = split(parts[1].trim());
		input = Integer.parseInt(inputStr);
		
		if( parts.length == 4){
			
			values = new double[1];
			output = new int[1];
			
			
			previoustx = -1 ;
			outputindex = -1 ;
			
			String valueStr = split(parts[2].trim());
			values[0] = Double.parseDouble(valueStr);
			
			String outputStr = split(parts[3].trim());
			output[0] = Integer.parseInt(outputStr);
			
		}else{
			
			String previoustxStr = split(parts[2].trim());
			previoustx = Integer.parseInt(previoustxStr);
			
			String outputindexStr = split(parts[3].trim());
			outputindex = Integer.parseInt(outputindexStr);
			
			int numOfOutputs = (parts.length - 4)/2;
			
			values = new double[numOfOutputs];
			output = new int[numOfOutputs];
			
			int z = 0 ;
			
			for(int i = 4 ; i < parts.length ; i += 2 ){
				
				String valueStr = split(parts[i].trim());
				values[z] = Double.parseDouble(valueStr);
				
				String outputStr = split(parts[i+1].trim());
				output[z] = Integer.parseInt(outputStr);
				
				z++;
				
			}
			
		}
		
		
		return new Transaction(id,input,previoustx,outputindex,values,output);
		
	}
	
	public String split(String s){
	
		String[] parts = s.split(":");
		return parts[1];

	}
	
	public void printTransaction( Transaction t){
		System.out.println(t.getId() + " " + t.getInput() + " " + t.getOutputindex() + " " + t.getPrevioustx());
		int[] output = t.getOutput();
		double[] values = t.getValues();
		
		for(int i =0 ; i< output.length ; i++){
			System.out.println(output[i] + " " + values[i]);
		}
		
		System.out.println("+++++++++++++++++++++++++++++++++++");
	}
	
	public boolean validateBalance(Transaction t){
		
		int prevTransactionId = t.getPrevioustx();
		printTransaction(t);
		if(prevTransactionId < 0)
			return true;
		
		int outputindex = t.getOutputindex() - 1 ;
		
		Transaction prev = transactions.get(prevTransactionId);
		
		double outputAmount = 0;
		double[] values = t.getValues();
		
		for(int i = 0 ; i < values.length ; i++ ){
			
			outputAmount += values[i];
		}
		
		double[] prevValues = prev.getValues();
		
		System.out.println(prevValues[outputindex]);
		
		System.out.println(outputAmount);
		
		if(prevValues[outputindex] >= outputAmount) return true;
		
		
		return false;	
	}
	
	public void checkDoubleSpend() {
		HashMap<Integer, Boolean>  flags = new HashMap<Integer, Boolean>();
		Iterator<Integer> iterator = transactions.keySet().iterator();
		
		while(iterator.hasNext()) {
			flags.put((Integer) iterator.next(), false);
		}
		for (int i = 50; i < transactions.size(); i++) {
			if (transactions.containsKey(i)) {
				Transaction t = transactions.get(i);
				for (int j = i+1; j < transactions.size(); j++) {
					if (transactions.containsKey(j)) {
						Transaction p = transactions.get(j);
						if(t.getInput() == p.getInput() && t.getPrevioustx() == p.getPrevioustx() && t.getValues()[0] == p.getValues()[0]) {
							System.out.println(j+"-->"+i);
							transactions.remove(j);
						}
					}
				}
				
			}
		}
	} 
}
