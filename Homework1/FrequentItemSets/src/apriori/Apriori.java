package apriori;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.ws.AsyncHandler;

public class Apriori {

	public static HashMap<String, Integer> candidateItems = new HashMap<>();
	public static HashMap<String, Integer> supportedItems = new HashMap<>();

	public static HashMap<HashSet<String>, Integer> candidatePairs = new HashMap<>();
	public static HashMap<HashSet<String>, Integer> supportedPairs = new HashMap<>();
	public static HashMap<String, Float> confidenceOfPairs = new HashMap<>();

	public static HashMap<HashSet<String>, Integer> candidateTriples = new HashMap<>();
	public static HashMap<HashSet<String>, Integer> supportedTriples = new HashMap<>();
	public static HashMap<String, Float> confidenceOfTriples = new HashMap<>();

	public static void firstPass(String[] items) {
		for (String item : items) {
			if (candidateItems.containsKey(item)) { // already seen the item
				candidateItems.put(item, candidateItems.get(item) + 1);
			} else {
				candidateItems.put(item, 1);
			}
		}
	}

	public static void secondPass(String[] items) {
		for (int i = 0; i < items.length; i++) {
			if (supportedItems.containsKey(items[i])) {
				// add all pairs of the items with this item
				for (int j = i + 1; j < items.length; j++) {
					if (supportedItems.containsKey(items[j])) {
						HashSet<String> set = new HashSet<>();
						set.add(items[i]);
						set.add(items[j]);
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

		HashSet<String> set = new HashSet<>();
		for (int i = 0; i < items.length; i++) {

			// item is in the supported items
			if (supportedItems.containsKey(items[i])) {
				set.add(items[i]);
				// System.out.println("SET IN THIRD PASS -1 : " + set);
				for (int j = i + 1; j < items.length; j++) {
					set.add(items[j]);
					// System.out.println("Checking :" + items[j]);

					// System.out.println("SET IN THIRD PASS -2 : " + set);
					// System.out.println("Candidate triples - already seen: " +
					// candidateTriples);
					// pair in supported pairs
					if (supportedPairs.keySet().contains(set)) {
						// pair is in the supported pairs
						for (int k = j + 1; k < items.length; k++) {
							if (supportedItems.containsKey(items[k])) {
								set.add(items[k]);
								// System.out.println("SET IN THIRD PASS -3 : "
								// + set);
								// System.out.println("Candidate triples -
								// before check " + candidateTriples);
								if (candidateTriples.containsKey(set)) { // already
																			// seen
									candidateTriples.put(new HashSet<>(set), candidateTriples.get(set) + 1);
									// System.out.println("Candidate triples -
									// already seen: " + candidateTriples);
									break;

								} else {
									candidateTriples.put(new HashSet<>(set), 1);
									// System.out.println("Candidate triples
									// -first seen: " + candidateTriples);
									break;
								}
							}
						}
					} else {
						set.remove(items[j]);
					}
					set.remove(items[j]);
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

	public static void computeConfidencePairs(HashMap<HashSet<String>, Integer> pairs) {
		for (HashSet<String> pair : pairs.keySet()) {
			Iterator<String> itr = pair.iterator();
			String key1 = itr.next().toString();
			String key2 = itr.next().toString();

			confidenceOfPairs.put(key1 + "->" + key2, (float) pairs.get(pair) / supportedItems.get(key1));
			confidenceOfPairs.put(key2 + "->" + key1, (float) pairs.get(pair) / supportedItems.get(key2));
		}
	}

	private static void computeConfidenceTriples(HashMap<HashSet<String>, Integer> triples) {
		for (HashSet<String> triple : triples.keySet()) {
			Iterator<String> itr = triple.iterator();
			String key1 = itr.next().toString();
			String key2 = itr.next().toString();
			String key3 = itr.next().toString();
			HashSet<String> set12 = new HashSet<>();
			set12.add(key1);
			set12.add(key2);

			HashSet<String> set23 = new HashSet<>();
			set23.add(key2);
			set23.add(key3);

			HashSet<String> set13 = new HashSet<>();
			set13.add(key1);
			set13.add(key3);

			confidenceOfTriples.put(set12.toString()+"->"+key3, (float) triples.get(triple) / supportedPairs.get(set12));
			confidenceOfTriples.put(set23.toString()+"->"+key1, (float) triples.get(triple) / supportedPairs.get(set23));
			confidenceOfTriples.put(set13.toString()+"->"+key2, (float) triples.get(triple) / supportedPairs.get(set13));
		}

	}

	public static void main(String[] args) {
		File file = new File("/home/mitro/Github/MassiveDataMining/Homework1/data/browsing.txt");
		BufferedReader reader = null;
		int support = 100;
		// first pass
		try {
			reader = new BufferedReader(new FileReader(file));

			String line;
			while ((line = reader.readLine()) != null) {
				String[] items = line.split(" ");
				firstPass(items);
			}
			// System.out.println(tempItems.size());
			// remove items with value less than 100
			for (String key : candidateItems.keySet()) {
				if (candidateItems.get(key) >= support) {
					supportedItems.put(key, candidateItems.get(key));
				}
			}
			// System.out.println("Supported items: "+supportedItems);

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

			// for (HashSet<String> set : supportedPairs.keySet()) {
			// System.out.println(set + ":" + supportedPairs.get(set));
			// }

			// Compute confidence of supported pairs
			computeConfidencePairs(supportedPairs);
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
			// File file = new
			// File("/home/mitro/Github/MassiveDataMining/Homework1/data/browsing.txt");
			reader = new BufferedReader(new FileReader(file));

			String line;
			while ((line = reader.readLine()) != null) {
				String[] items = line.split(" ");
				thirdPass(items);
			}

			System.out.println("Candidate triples size: " + candidateTriples.size());
			// System.out.println("Candidate triples: " + candidateTriples);
			// remove triples with support less than 100

			for (HashSet<String> set : candidateTriples.keySet()) {
				// System.out.println("Candidate Set: " + set);
				if (candidateTriples.get(set) != null && candidateTriples.get(set) >= support) {
					supportedTriples.put(set, candidateTriples.get(set));
				}
			}

			System.out.println("Supported triples size: " + supportedTriples.size());
			// Print out all triples

			/*
			 * for (HashSet<String> set : supportedTriples.keySet()) {
			 * System.out.println(set + ":" + supportedTriples.get(set)); }
			 */
			// Compute confidence of supported pairs
			computeConfidenceTriples(supportedTriples);
			// TODO: Save the confidenceOfPairs in a csv for picking the top 5

			
			 for(String s: confidenceOfTriples.keySet())
			 { System.out.println(s +": "+confidenceOfTriples.get(s)); }
			 

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
