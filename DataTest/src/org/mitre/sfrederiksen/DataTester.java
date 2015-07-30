package org.mitre.sfrederiksen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.mitre.sfrederiksen.Entry;

public class DataTester {
	static List<Entry> newdata = new ArrayList<Entry>();
	static Map<Integer,Integer> valNum = new TreeMap<Integer,Integer>();
	static Scanner in = new Scanner(System.in);
	private final static String path = getPath();
  
	private static void loadData() throws IOException{
			BufferedReader in = new BufferedReader(new FileReader(path));
			String str;
			while((str=in.readLine()) != null){
				process(str);
			}
			in.close();
	}

	private static String getPath() {
		String s;File f;
		while(true){
			System.out.print("Enter path to file: ");
			 s = in.nextLine();
			try{
				f = new File(s);
			}catch(Exception e){
				System.out.println("Error, try again");
				continue;
			}
			if(f.isFile()){
				break;
			}
			System.out.println("Not a file, try again");
		}
		return s;
	}

	private static void process(String str) {
		String st[] = str.split(",");
		if(!st[0].equals("null")&&!st[0].equals("")){
			Float me = Float.parseFloat(st[0]);
			//data.add(me);
			newdata.add(new Entry((int)(Math.round((me+5)/10.0)*10.0)));
		}
	}

	private static void crunchData() {
		valNum = newdata.stream()
				.collect(
						Collectors.groupingBy(
								Entry::getNumber,
								Collectors.reducing(0,Entry::getAdd,Integer::sum)
								)
						);
				
	}

	private static void dispDataHuman() throws IOException{
		Writer w = null; 
		w = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("../Output.dat"), "utf-8"));
		String p = "";
		
		SortedSet<Integer> keys = new TreeSet<Integer>(valNum.keySet());
		for (Integer k :keys){
			p+= k + " occurs " + valNum.get(k) + " times\n";
		}
		
		p = p.replace("\n", "\r\n");
		w.write(p);
		System.out.print(p);
		w.close();
	}
	
	public static void main(String... args){
		try {
			loadData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(newdata.size()<=1){
			in.close();
			return;
		}
		crunchData();
		try {
			dispDataHuman();
		} catch (IOException e) {
			e.printStackTrace();
		}
		in.close();
	}
	
}

	

