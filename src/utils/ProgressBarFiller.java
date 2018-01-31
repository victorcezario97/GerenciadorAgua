package utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import web.ContaGetter;
import javax.swing.JLabel;

public class ProgressBarFiller extends JFrame implements Runnable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JProgressBar progressBar;
	ContaGetter cg;
	private Thread t;
	
	public ProgressBarFiller(ContaGetter cg) {
		this.cg = cg;
		
		setBounds(150, 150, 246, 75);
		JPanel panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(null);
		setVisible(true);
		setAlwaysOnTop(true);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(43, 16, 146, 20);
		panel.add(progressBar);
		
		JLabel lblKwdknaskdnkasnd = new JLabel("kwdknaskdnkasnd");
		lblKwdknaskdnkasnd.setBounds(6, 6, 61, 16);
		panel.add(lblKwdknaskdnkasnd);
		progressBar.setVisible(true);
	}
	
	@Override
	public void run() {
		Timer t = new Timer(100, new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				progressBar.setValue(cg.getProgress());
			}
		});
		t.start();
		
		while(progressBar.getValue() < 100) {
				progressBar.setValue(cg.getProgress());
		}
		
		return;
		
	}
	
	public void start() {
		if(t == null) {
			t = new Thread(this, "ProgressBarFillerThread");
			
			t.start();
		}
	}

}
