package apriori;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Apriori {
	public static HashMap<String, Integer> freqItems = new HashMap<>();
	public static HashMap<String, Integer> supportedItems = new HashMap<>();
	
	public static void firstPass(String[] items) {
		for (String item : items) {
			if (freqItems.containsKey(item)) { // already seen the item ->
				freqItems.put(item, freqItems.get(item) + 1);
			} else {
				freqItems.put(item, 1);
			}
		}
	}

	public static void main(String[] args) {
		// read the file - each line a string
		BufferedReader reader = null;
		int support = 100;
		try {
			File file = new File("/home/mitro/Github/MassiveDataMining/Homework1/data/browsing.txt");
			reader = new BufferedReader(new FileReader(file));

			String line;
			while ((line = reader.readLine()) != null) {
				String[] items = line.split(" ");
				firstPass(items);
			}
			System.out.println(freqItems.size());
			//remove items with value less than 100
			for(String key: freqItems.keySet()){
				if(freqItems.get(key)>=support){
					supportedItems.put(key, freqItems.get(key));
				}
			}
			System.out.println(supportedItems.size());
			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
