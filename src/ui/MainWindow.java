package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.ReadOnlyFileSystemException;

import javax.swing.JButton;

import web.WebManager;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;

import utils.Files;
import javax.swing.JTextPane;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField tfTotal;
	private JTextField tfDividido;
	private JTextField tfMulta;
	private JLabel lbUltimo;
	private JTextField tfUltimo;
	private JLabel lblAssunto;
	private JTextField tfAssunto;
	private JLabel lblMensagem;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow frame = new MainWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 681, 396);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNovaConta = new JButton("Nova Conta");
		btnNovaConta.setBounds(32, 141, 103, 29);
		contentPane.add(btnNovaConta);
		
		JLabel lblTotal = new JLabel("Total:");
		lblTotal.setBounds(6, 41, 36, 16);
		contentPane.add(lblTotal);
		
		tfTotal = new JTextField();
		tfTotal.setBounds(68, 36, 90, 26);
		contentPane.add(tfTotal);
		tfTotal.setColumns(10);
		
		JLabel lblMulta = new JLabel("Multa:");
		lblMulta.setBounds(6, 69, 39, 16);
		contentPane.add(lblMulta);
		
		JLabel lblDividido = new JLabel("Dividido:");
		lblDividido.setBounds(6, 97, 62, 16);
		contentPane.add(lblDividido);
		
		tfDividido = new JTextField();
		tfDividido.setColumns(10);
		tfDividido.setBounds(68, 92, 90, 26);
		contentPane.add(tfDividido);
		
		tfMulta = new JTextField();
		tfMulta.setColumns(10);
		tfMulta.setBounds(68, 64, 90, 26);
		contentPane.add(tfMulta);
		
		lbUltimo = new JLabel("Último a pagar:");
		lbUltimo.setBounds(170, 41, 102, 16);
		contentPane.add(lbUltimo);
		
		tfUltimo = new JTextField();
		tfUltimo.setBounds(280, 36, 130, 26);
		contentPane.add(tfUltimo);
		tfUltimo.setColumns(10);
		
		JButton btnEnviarEmail = new JButton("Enviar email");
		btnEnviarEmail.setBounds(280, 306, 117, 29);
		contentPane.add(btnEnviarEmail);
		
		lblAssunto = new JLabel("Assunto:");
		lblAssunto.setBounds(211, 69, 61, 16);
		contentPane.add(lblAssunto);
		
		tfAssunto = new JTextField();
		tfAssunto.setColumns(10);
		tfAssunto.setBounds(280, 64, 130, 26);
		contentPane.add(tfAssunto);
		
		lblMensagem = new JLabel("Mensagem:");
		lblMensagem.setBounds(196, 97, 76, 16);
		contentPane.add(lblMensagem);
		
		JTextPane tpMensagem = new JTextPane();
		tpMensagem.setBounds(280, 100, 261, 178);
		contentPane.add(tpMensagem);
		
		btnNovaConta.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String text = null;
				
				try {
					text = Files.readFile("emailBody.txt");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				WebManager w = new WebManager();
				String[] totals = w.getTotals();
				
				tfTotal.setText(totals[0]);
				tfMulta.setText(totals[1]);
				tfDividido.setText(totals[2]);
				
				text = text.replace("*total*", totals[0]);
				text = text.replace("*dividido*", totals[2]);
				text = text.replace("*multado*", totals[3]);
				text = text.replace("*ultimo*", tfUltimo.getText());
				text = text.replace("*data*", totals[4]);
				
				tpMensagem.setText(text);
			}
		});
		
		btnEnviarEmail.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
