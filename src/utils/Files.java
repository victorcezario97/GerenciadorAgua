package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Files {
	
	public static String readFile(String fileName) throws IOException {
		String line;
		StringBuilder sb = new StringBuilder();
		
		BufferedReader bf = new BufferedReader(new FileReader(fileName));
		while((line = bf.readLine()) != null) {
			sb.append(line);
			sb.append(System.getProperty("line.separator"));
		}
		bf.close();
		
		return sb.toString();
	}
}
