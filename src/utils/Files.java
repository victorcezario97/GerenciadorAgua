package utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Files {
	
	public static String readFile(String fileName) throws FileNotFoundException {
		String bodyValues, bodyDate;
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader("emailBody.txt"));
			
			try {
				bodyValues = bf.readLine();
				bodyDate = bf.readLine();
				
				System.out.println(bodyValues + "\n" + bodyDate);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}catch(NullPointerException e) {
			
		}
		
		return "";
	}
}
