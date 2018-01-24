package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import web.WebManager;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JTextField;

public class MainWindow extends JFrame {

	private JPanel contentPane;
	private JTextField tfTotal;
	private JTextField tfDividido;
	private JTextField tfMulta;

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
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNovaConta = new JButton("Nova Conta");
		btnNovaConta.setBounds(6, 6, 103, 29);
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
		
		btnNovaConta.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				WebManager w = new WebManager();
				String[] totals = w.getTotals();
				
				tfTotal.setText(totals[0]);
				tfMulta.setText(totals[1]);
				tfDividido.setText(totals[2]);
			}
		});
	}
}
