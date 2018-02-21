package apriori;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Apriori {

	public static HashMap<String, Integer> candidatetems = new HashMap<>();
	public static HashMap<String, Integer> supportedItems = new HashMap<>();
	public static HashMap<HashSet<String>, Integer> candidatePairs = new HashMap<>();
	public static HashMap<HashSet<String>, Integer> supportedPairs = new HashMap<>();
	public static HashMap<String, Float> confidenceOfPairs = new HashMap<>();
	public static HashMap<HashSet<String>, Integer> candidateTriples = new HashMap<>();
	public static HashMap<HashSet<String>, Integer> supportedTriples = new HashMap<>();

	public static void firstPass(String[] items) {
		for (String item : items) {
			if (candidatetems.containsKey(item)) { // already seen the item
				candidatetems.put(item, candidatetems.get(item) + 1);
			} else {
				candidatetems.put(item, 1);
			}
		}
	}

	public static void secondPass(String[] items) {
		for (String item : items) {
			if (supportedItems.containsKey(item)) {
				// add all pairs of the items with this item
				for (int i = 0; i < items.length; i++) {
					if (items[i] != item) {
						HashSet<String> set = new HashSet<>();
						set.add(item);
						set.add(items[i]);
						if (candidatePairs.containsKey(set)) { // already seen
																// the
																// item
							candidatePairs.put(set, candidatePairs.get(set) + 1);
						} else {
							candidatePairs.put(set, 1);
						}
					}

				}

			}
		}
	}

	public static void thirdPass(String[] items) {
		for (String item : items) {
			HashSet<String> set = new HashSet<>();
			if (supportedItems.containsKey(item)) { // item is in the supported
													// items

				set.add(item);
				for (int i = 0; i < items.length; i++) {
					if (!set.contains(items[i])) {
						set.add(items[i]);
						if (supportedPairs.containsKey(set)) { // pair is in the
																// supported
																// pairs

							for (int j = 0; j < items.length; j++) {
								if (!set.contains(items[j])&& supportedItems.containsKey(items[j])) {
									set.add(items[j]);
									if (candidateTriples.containsKey(set)) {
										candidateTriples.put(set, candidateTriples.get(set) + 1);
									} else {
										candidateTriples.put(set, 1);
									}

								}
							}
						} else {
							set.remove(items[i]);
						}
					}

				}
			}
		}
	}

	private static boolean contained(Set<HashSet<String>> sets, String item) {
		for (HashSet<String> set : sets) {
			if (set.contains(item)) {
				return true;
			}
		}
		return false;
	}

	public static void confidence(HashMap<HashSet<String>, Integer> pairs) {
		for (HashSet<String> pair : pairs.keySet()) {
			Iterator<String> itr = pair.iterator();
			String key1 = itr.next().toString();
			String key2 = itr.next().toString();

			confidenceOfPairs.put(key1 + "->" + key2, (float) pairs.get(pair) / supportedItems.get(key1));
			confidenceOfPairs.put(key2 + "->" + key1, (float) pairs.get(pair) / supportedItems.get(key2));
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
			// System.out.println(tempItems.size());
			// remove items with value less than 100
			for (String key : candidatetems.keySet()) {
				if (candidatetems.get(key) >= support) {
					supportedItems.put(key, candidatetems.get(key));
				}
			}
			// System.out.println(supportedItems.size());

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// second pass
		try {
			File file = new File("/home/mitro/Github/MassiveDataMining/Homework1/data/browsing.txt");
			reader = new BufferedReader(new FileReader(file));

			String line;
			while ((line = reader.readLine()) != null) {
				String[] items = line.split(" ");
				secondPass(items);
			}

			// System.out.println("Frequent pairs" + candidatePairs.size());
			// remove pairs with support less than 100
			for (HashSet<String> set : candidatePairs.keySet()) {
				if (candidatePairs.get(set) >= support) {
					supportedPairs.put(set, candidatePairs.get(set));
				}
			}
			System.out.println("Supported pairs size: " + supportedPairs.size());
			// Print out all pairs
			/*
			 * for (HashSet<String> set : supportedPairs.keySet()) {
			 * System.out.println(set + ":"+ supportedPairs.get(set)); }
			 */

			// Compute confidence of supported pairs
			confidence(supportedPairs);
			// TODO: Save the confidenceOfPairs in a csv for picking the top 5

			/*
			 * for(String s: confidenceOfPairs.keySet()){ System.out.println(s
			 * +" "+confidenceOfPairs.get(s)); }
			 */

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// third pass
		try {
			File file = new File("/home/mitro/Github/MassiveDataMining/Homework1/data/browsing.txt");
			reader = new BufferedReader(new FileReader(file));

			String line;
			while ((line = reader.readLine()) != null) {
				String[] items = line.split(" ");
				thirdPass(items);
			}

			System.out.println("Candidate triples size: " + candidateTriples.size());
			// remove triples with support less than 100
			for (HashSet<String> set : candidateTriples.keySet()) {
				if (candidateTriples.get(set) >= support) {
					supportedTriples.put(set, candidateTriples.get(set));
				}
			}
			System.out.println("Supported triples size: " + supportedTriples.size());
			// Print out all triples

			for (HashSet<String> set : supportedTriples.keySet()) {
				System.out.println(set + ":" + supportedTriples.get(set));
			}

			// Compute confidence of supported pairs

			// TODO: Save the confidenceOfPairs in a csv for picking the top 5

			/*
			 * for(String s: confidenceOfPairs.keySet()){ System.out.println(s
			 * +" "+confidenceOfPairs.get(s)); }
			 */

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
