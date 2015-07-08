package org.mitre.sfrederiksen;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class DataTester {
	static ArrayList<Float> data = new ArrayList<Float>();
	static final String dataPath = "C:\\Users\\sfrederiksen\\Desktop\\data.txt";
	static int valNum[];

	private static void loadData() throws IOException{
		BufferedReader in = new BufferedReader(new FileReader(dataPath));
		String str;
		while((str=in.readLine()) != null){
			process(str);
		}
		in.close();
		
	}

	private static void process(String str) {
		String st[] = str.split(",");
		if(!st[0].equals("null")&&!st[0].equals("")){
			Float me = Float.parseFloat(st[0]);
			me = (Float.valueOf((float)(Math.round(me))));
			data.add(me);
		}
	}

	private static void crunchData() {
		Collections.sort(data);
		int maxValue = getMaxValue(data);
		valNum = new int[maxValue+1];
		for(Float i:data){
			valNum[(int)((float)i)]++;
		}
	}

	private static int getMaxValue(ArrayList<Float> d) {
		float m = 0;
		for(Float i:d){
			m = Math.max(((float)(m)), ((float)(i)));
		}
		System.out.println("Max value is: " + (int)m);
		return ((int)(m));
	}

	private static void dispData() throws IOException{
		for(Float fl:data){
			System.out.println(Math.round(fl));
		}
		
		for(int i = 0; i < valNum.length; i++){
			System.out.println(i + " occured " + valNum[i] + " times.");
		}
	}
	public static void main(String... args){
		try {
			loadData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		crunchData();
		try {
			dispData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
