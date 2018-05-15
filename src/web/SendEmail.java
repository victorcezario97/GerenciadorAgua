package web;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import utils.Utils;

public class SendEmail {
	
	public static void send() throws IOException {
		Process process = new ProcessBuilder("sendEmail.exe", "-o", "tls=yes", "-f", "victorscezario97@gmail.com", "-t", "victorscezario@hotmail.com", "-s", "smtp.gmail.com:587", "-xu", "victorscezario97@gmail.com", "-xp", "kartoffel97", "-u", "\"Hello from sendEmail\"", "-m", "\"How are you? I'm testing sendEmail from the command line.\"").start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		//System.out.printf("Output of running %s is:", Arrays.toString(args));

		while ((line = br.readLine()) != null) {
		  System.out.println(line);
		}
	}
	
	public static void send(String message, String from, String password, String to, String subject) throws IOException {
		Process process = new ProcessBuilder("sendEmail.exe", "-o", "tls=yes", "-f", from, "-t", to, "-s", "smtp.gmail.com:587", "-xu", from, "-xp", password, "-u", subject, "-m", message).start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		//System.out.printf("Output of running %s is:", Arrays.toString(args));

		while ((line = br.readLine()) != null) {
		  System.out.println(line);
		}
	}
	
	public static void send(File filename, String subject, String message) throws IOException {
		String[] strings = Utils.emailFile(filename);
		String from = strings[0], to = strings[1], password = strings[3];
		Process process = new ProcessBuilder("sendEmail.exe", "-o", "tls=yes", "-f", from, "-t", to, "-s", "smtp.gmail.com:587", "-xu", from, "-xp", password, "-u", subject, "-m", message).start();
		InputStream is = process.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String line;

		//System.out.printf("Output of running %s is:", Arrays.toString(args));

		while ((line = br.readLine()) != null) {
		  System.out.println(line);
		}
	}
}
