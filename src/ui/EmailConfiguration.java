package ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import utils.Utils;

import javax.swing.JButton;

public class EmailConfiguration {

	private JFrame frame;
	private JTextField tfSender;
	private JTextField tfPassword;
	private JTextField tfReceiver;

	/**
	 * Launch the application.
	 */
	public EmailConfiguration() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		String []strings = null;
		try {
			strings = Utils.emailFile(new File("emailconfig"));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		String sender = strings[0], receiver = strings[1], password = strings[3];
		
		frame = new JFrame();
		frame.setBounds(100, 100, 431, 221);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblSender = new JLabel("Email remetente:");
		lblSender.setBounds(20, 23, 111, 14);
		frame.getContentPane().add(lblSender);
		
		tfSender = new JTextField();
		tfSender.setBounds(128, 20, 243, 20);
		frame.getContentPane().add(tfSender);
		tfSender.setColumns(10);
		tfSender.setText(sender);
		
		tfPassword = new JTextField();
		tfPassword.setColumns(10);
		tfPassword.setBounds(128, 59, 152, 20);
		frame.getContentPane().add(tfPassword);
		tfPassword.setText(password);
		
		JLabel lblPassword = new JLabel("Senha:");
		lblPassword.setBounds(77, 62, 41, 14);
		frame.getContentPane().add(lblPassword);
		
		tfReceiver = new JTextField();
		tfReceiver.setColumns(10);
		tfReceiver.setBounds(128, 102, 243, 20);
		frame.getContentPane().add(tfReceiver);
		tfReceiver.setText(receiver);
		
		JLabel lblReceiver = new JLabel("Email destinat\u00E1rio:");
		lblReceiver.setBounds(10, 105, 121, 14);
		frame.getContentPane().add(lblReceiver);
		
		JButton btnSave = new JButton("Salvar");
		btnSave.setBounds(207, 148, 89, 23);
		frame.getContentPane().add(btnSave);
		
		JButton btnCancel = new JButton("Cancelar");
		btnCancel.setBounds(77, 148, 89, 23);
		frame.getContentPane().add(btnCancel);
		
		btnCancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		btnSave.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				StringBuilder sb = new StringBuilder();
				char separator = ';';
				sb.append(tfSender.getText());
				sb.append(separator);
				sb.append(tfReceiver.getText());
				sb.append(separator);
				sb.append(tfSender.getText());
				sb.append(separator);
				sb.append(tfPassword.getText());
				sb.append(separator);
				
				File f = new File("emailconfig");
				try {
					FileWriter fw = new FileWriter(f);
					fw.write(sb.toString());
					fw.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				frame.dispose();
			}
		});
	}
}
