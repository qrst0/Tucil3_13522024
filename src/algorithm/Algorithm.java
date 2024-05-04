package algorithm;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import pair.*;


public class Algorithm {
	protected int[][] adjacency_list;
	protected String[] words;
	final int numWord = 370105;
	final int maxAdj = 52;
	public static String fileSeparator = File.separator;
	protected HashMap<String, Integer> hashMap;
	public Algorithm(){
		try {
			adjacency_list = new int[numWord][maxAdj];
			words = new String[numWord];
			hashMap = new HashMap<String, Integer>();
			File adjTxt = new File("." + fileSeparator + "assets" + fileSeparator + "words_alpha.txt");
			Scanner adjReader = new Scanner(adjTxt);
			int index = 0;
			while(index < numWord && adjReader.hasNextLine()) {
				String wordString = adjReader.nextLine();
				words[index] = wordString;
				//System.out.println(wordString);
				hashMap.put(wordString, index);
				index++;
			}
			File adjNumTxtFile = new File("." + fileSeparator + "assets" + fileSeparator + "adjNum.txt");
			Scanner adjNumReaderScanner = new Scanner(adjNumTxtFile);
			index = 0;
			while (adjNumReaderScanner.hasNextLine()) {
				String adjacentNumString = adjNumReaderScanner.nextLine();
				String[] splittedStrings = adjacentNumString.split(",");
				if(adjacentNumString.equals("")) {
					adjacency_list[index][0] = -1;
					index++;
					continue;
				}
				for(int i = 0; i < splittedStrings.length; i++) {
					adjacency_list[index][i] = Integer.parseInt(splittedStrings[i]);
				}
				adjacency_list[index][splittedStrings.length] = -1;
				index++;
			}
			adjNumReaderScanner.close();
			adjReader.close();
		}catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}

	}
	
	public boolean exist(String checkExistString) {
		return hashMap.containsKey(checkExistString);
	}
	
	public static Integer hamiltonianDistance(String s1, String s2) {
		int cnt = 0;
		for(int i = 0; i < s1.length(); ++i) {
			if(s1.charAt(i) != s2.charAt(i)) cnt++;
		}
		return cnt;
	}
	
	public Pair<ArrayList<String>, Pair<Integer, Integer>> Astar(String s1, String s2){
		try {
		PriorityQueue<Pair<Integer, Pair<Integer, Integer>>> pQueue = new PriorityQueue<Pair<Integer,Pair<Integer,Integer>>>((a, b) -> a.first - b.first);
		int a = hashMap.get(s1);
		int b = hashMap.get(s2);
		final long start = System.nanoTime();
		Set<Integer> visitedSet = new HashSet<Integer>();
		HashMap<Integer, Integer> prevHashMap = new HashMap<Integer, Integer>();
		ArrayList<String> resultArrayList = new ArrayList<String>();
		pQueue.add(new Pair<Integer, Pair<Integer,Integer>>(hamiltonianDistance(s1, s2), new Pair<Integer, Integer>(a, -1)));
		while(!pQueue.isEmpty()) {
			Pair<Integer, Pair<Integer, Integer>> topQPair = pQueue.peek();
			pQueue.remove();
			if(visitedSet.contains(topQPair.second.first)) continue;
			topQPair.first -= hamiltonianDistance(words[topQPair.second.first], s2);
			//System.out.println("Value: " + topQPair.first);
			visitedSet.add(topQPair.second.first);
			prevHashMap.put(topQPair.second.first, topQPair.second.second);
			if(topQPair.second.first == b) {
				int back = topQPair.second.first;
				while(back != -1) {
					resultArrayList.add(words[back]);
					back = prevHashMap.get(back);
				}
				final long endtime = System.nanoTime();
				long seconds = TimeUnit.NANOSECONDS.toMillis(endtime - start);
				return new Pair<ArrayList<String>, Pair<Integer,Integer>>(resultArrayList, new Pair<Integer, Integer>((int)seconds, visitedSet.size()));
			}
			for(int i = 0; i < maxAdj && adjacency_list[topQPair.second.first][i] != -1; i++) {
				pQueue.add(new Pair<Integer, Pair<Integer, Integer>>(topQPair.first + 1 + hamiltonianDistance(words[adjacency_list[topQPair.second.first][i]], s2), new Pair<Integer, Integer>(adjacency_list[topQPair.second.first][i], topQPair.second.first)));
			}
		}
		return new Pair<ArrayList<String>, Pair<Integer,Integer>>(resultArrayList, new Pair<Integer, Integer>(-1, -1));
		
		} catch(Exception e) {
			return new Pair<ArrayList<String>, Pair<Integer,Integer>>(new ArrayList<String>(), new Pair<Integer, Integer>(-1, -1));
		}
	}
	
	
	public Pair<ArrayList<String>, Pair<Integer, Integer>> UCS(String s1, String s2){
		try {
		int a = hashMap.get(s1);
		int b = hashMap.get(s2);
		final long start = System.nanoTime();
		Set<Integer> visitedSet = new HashSet<Integer>();
		HashMap<Integer, Integer> prevHashMap = new HashMap<Integer, Integer>();
		ArrayList<String> resultArrayList = new ArrayList<String>();
		Queue<Pair<Integer, Integer>> queue = new LinkedList<>();
		queue.add(new Pair<Integer, Integer>(a, -1));
		while(!queue.isEmpty()){
			Pair<Integer, Integer> topQPair = queue.peek();
			queue.remove();
			if(visitedSet.contains(topQPair.first)) continue;
			visitedSet.add(topQPair.first);
			prevHashMap.put(topQPair.first, topQPair.second);
			if(topQPair.first == b) {
				int back = topQPair.first;
				while(back != -1) {
					resultArrayList.add(words[back]);
					back = prevHashMap.get(back);
				}
				final long endtime = System.nanoTime();
				long seconds = TimeUnit.NANOSECONDS.toMillis(endtime - start);
				return new Pair<ArrayList<String>, Pair<Integer,Integer>>(resultArrayList, new Pair<Integer, Integer>((int)seconds, visitedSet.size()));
			}
			for(int i = 0; i < maxAdj && adjacency_list[topQPair.first][i] != -1; i++) {
				queue.add(new Pair<Integer, Integer>(adjacency_list[topQPair.first][i], topQPair.first));
			}
		}
		return new Pair<ArrayList<String>, Pair<Integer,Integer>>(resultArrayList, new Pair<Integer, Integer>(-1, -1));
		} catch (Exception e) {
			return new Pair<ArrayList<String>, Pair<Integer,Integer>>(new ArrayList<String>(), new Pair<Integer, Integer>(-1, -1));
		}
	}
	
	public Pair<ArrayList<String>, Pair<Integer, Integer>> GBFS(String s1, String s2) {
		try {
		
		int a = hashMap.get(s1);
		int b = hashMap.get(s2);
		int prev = -1;
		ArrayList<String> resultArrayList = new ArrayList<String>();
		long startSeconds = System.nanoTime();
		while(a != b) {
			resultArrayList.add(words[a]);
			//System.out.println(a);
			int dest = -1;
			int maxi = Integer.MAX_VALUE;
			for(int i = 0; i < maxAdj && adjacency_list[a][i] != -1; i++) {
				int c = hamiltonianDistance(words[adjacency_list[a][i]], s2);
				if(c < maxi) {
					dest = adjacency_list[a][i];
					maxi = c;
				}
			}
			if(dest == -1 || dest == prev) {
				return new Pair<ArrayList<String>, Pair<Integer,Integer>>(new ArrayList<String>(), new Pair<Integer, Integer>(-1, -1)); 
			}
			prev = a;
			a = dest;
		}
		resultArrayList.add(words[a]);
		long endSeconds = System.nanoTime();
		long seconds = TimeUnit.NANOSECONDS.toMillis(endSeconds - startSeconds);
		Collections.reverse(resultArrayList);
		return new Pair<ArrayList<String>, Pair<Integer,Integer>>(resultArrayList, new Pair<Integer, Integer>((int)seconds, resultArrayList.size()));
		
		} catch (Exception e) {
			return new Pair<ArrayList<String>, Pair<Integer,Integer>>(new ArrayList<String>(), new Pair<Integer, Integer>(-1, -1));
		}
	}
	
}
