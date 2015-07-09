package org.mitre.sfrederiksen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.codec.binary.Base64;

public class DataTester {
	static ArrayList<Integer> newdata = new ArrayList<Integer>();
	static ArrayList<Float> data = new ArrayList<Float>();
	static final String dataPath = "src/res/data.txt";
	static final String encDataPath = "../data.enc.dat";
	static int valNum[];
	static Scanner in = new Scanner(System.in);
	private static final char[] PASSWORD = getPass();
	private static final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };
	

	private static char[] getPass() {
		System.out.print("Enter in your desired password: ");
		String s = in.nextLine();
		return s.toCharArray();
	}
	private static void loadData() throws IOException{
		System.out.print("Raw or encypted data? (r/e): ");
		String choice = in.nextLine();
		if(choice.toLowerCase().equals("r")){
			BufferedReader in = new BufferedReader(new FileReader(dataPath));
			String str;
			while((str=in.readLine()) != null){
				process(str);
			}
			in.close();
		}else if(choice.toLowerCase().equals("e")){
			Writer w = null; 
			w = new BufferedWriter(new OutputStreamWriter(
						new FileOutputStream("../Output.enc.dat"), "utf-8"));
			BufferedReader in = new BufferedReader(new FileReader(encDataPath));
			String str,data = "";
			while((str=in.readLine()) != null){
				data += str;
			}
			try {
				data = decrypt(data);
				System.out.print(data);
				w.write(data);
			} catch (GeneralSecurityException e) {
				e.printStackTrace();
			}
			w.close();
			in.close();
		}
		
	}

	private static void process(String str) {
		String st[] = str.split(",");
		if(!st[0].equals("null")&&!st[0].equals("")){
			Float me = Float.parseFloat(st[0]);
			data.add(me);
			newdata.add(Math.round(me));
		}
	}

	private static void crunchData() {
		Collections.sort(data);
		Collections.sort(newdata);
		valNum = new int[getMaxValue(newdata)+1];
		for(Float i:data){
			valNum[(int)((float)i)]++;
		}
	}

	private static int getMaxValue(ArrayList<Integer> d) {
		int m = 0;
		for(Integer i:d){
			m = Math.max(m,i);
		}
		return m;
	}

	private static void dispDataHuman() throws IOException{
		Writer w = null; 
		w = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("../Output.dat"), "utf-8"));
		String p = "";
		p += ("Max value is: " + getMaxValue(newdata) + "\n");
		for(int ix = 0; ix < 20; ix++){
			p += "=";
		}
		p += "\n{";
		for(Float fl:data){
			p += (fl +",");
		}
		p += "}\n";
		p += "\n{";
		for(Integer il:newdata){
			p += (il +",");
		}
		p += "}\n";
		for(int ix = 0; ix < 20; ix++){
			p += "=";
		}
		p += "\n";
		for(int i = 0; i < valNum.length; i++){
			p += (i + " occured " + valNum[i] + " times\n");
		}
		for(int ix = 0; ix < 20; ix++){
			p += "=";
		}
		p += "\n";
		for(int i = 0; i < valNum.length; i++){
			p += (i+" | ");
			for(int ix = 0; ix < valNum[i]; ix++){
				p += "-";
			}
			p += "\n--\n";
		} 
		for(int ix = 0; ix < 20; ix++){
			p += "=";
		}
		p += "\n";
		p += "END OF OUTPUT";
		p = p.replace("\n", "\r\n");
		w.write(p);
		System.out.print(p);
		w.close();
	}
	private static void diapDataEnc() throws IOException{
		Writer w = null; 
		w = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream("../data.enc.dat"), "utf-8"));
		String p = "";
		 
		p += ("Max value is: " + getMaxValue(newdata) + "\n");
		for(int ix = 0; ix < 20; ix++){
			p += "=";
		}
		p += "\n{";
		for(Float fl:data){
			p += (fl +",");
		}
		p += "}\n";
		p += "\n{";
		for(Integer il:newdata){
			p += (il +",");
		}
		p += "}\n";
		for(int ix = 0; ix < 20; ix++){
			p += "=";
		}
		p += "\n";
		for(int i = 0; i < valNum.length; i++){
			p += (i + " occured " + valNum[i] + " times\n");
		}
		for(int ix = 0; ix < 20; ix++){
			p += "=";
		}
		p += "\n";
		for(int i = 0; i < valNum.length; i++){
			p += (i+" | ");
			for(int ix = 0; ix < valNum[i]; ix++){
				p += "-";
			}
			p += "\n--\n";
		} 
		for(int ix = 0; ix < 20; ix++){
			p += "=";
		}
		p += "\n";
		p += "END OF OUTPUT";
		p = p.replace("\n", "\r\n");
		try {
			w.write(encrypt(p));
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		w.close();
	}
	public static void main(String... args){
		try {
			loadData();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(data.size()<=1){
			in.close();
			return;
		}
		crunchData();
		try {
			dispDataHuman();
			diapDataEnc();
		} catch (IOException e) {
			e.printStackTrace();
		}
		in.close();
	}
	private static String encrypt(String property) throws GeneralSecurityException, UnsupportedEncodingException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return base64Encode(pbeCipher.doFinal(property.getBytes("UTF-8")));
    }

    private static String base64Encode(byte[] bytes) {
        new Base64();
		// NB: This class is internal, and you probably should use another impl
        return Base64.encodeBase64String(bytes);
    }

    private static String decrypt(String property) throws GeneralSecurityException, IOException {
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
        Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
        pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
        return new String(pbeCipher.doFinal(base64Decode(property)), "UTF-8");
    }

    private static byte[] base64Decode(String property) throws IOException {
        // NB: This class is internal, and you probably should use another impl
        return new Base64().decode(property);
    }
}

	

