package com.app.main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

import javax.swing.JLabel;

import org.apache.log4j.Logger;

import com.app.util.PropertyFileReader;
/**
 * 
 * @author intakhabalam.s
 *
 */
public class FTPMonitor extends javax.swing.JFrame {
	private static Logger logger = Logger.getLogger(FTPMonitor.class);
	private static String filePath = System.getProperty("user.dir") + PropertyFileReader.prop.getProperty("logs.path");
	Thread thread;
	StringBuilder inputAppend = new StringBuilder();
	private static final long serialVersionUID = 1L;
	private JLabel flexiLogLabel;
	private javax.swing.JScrollPane scrollPane;
	boolean stop = false;

	public FTPMonitor() {
		init();
	}


	public void init() {
		Container contentPane = getContentPane();
		contentPane.setLayout(new FlowLayout());
		flexiLogLabel = new JLabel("Logged In");
		scrollPane = new javax.swing.JScrollPane();
		flexiLogLabel.setBackground(new java.awt.Color(0, 0, 0));
		flexiLogLabel.setFont(new java.awt.Font("Calibri", 1, 12)); // NOI18N
		flexiLogLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		flexiLogLabel.setForeground(new java.awt.Color(51, 255, 0));
		flexiLogLabel.setAutoscrolls(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource( PropertyFileReader.prop.getProperty("icon.name"))));
		flexiLogLabel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				javax.swing.BorderFactory.createTitledBorder(""), "FTP Dowloading",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Calibri", 1, 14), new java.awt.Color(0, 51, 255))); // NOI18N
		flexiLogLabel.setDoubleBuffered(true);
		flexiLogLabel.setOpaque(true);
		scrollPane.setViewportView(flexiLogLabel);
		// contentPane.add(flexiLogLabel);
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 380, Short.MAX_VALUE)
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
				javax.swing.GroupLayout.Alignment.TRAILING,
				layout.createSequentialGroup().addContainerGap()
						.addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
						.addContainerGap()));
		scrollPane.setViewportView(flexiLogLabel);

		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				try {
					System.out.println("Stopping Thread.....");
					stop = true;
					setDefaultCloseOperation(DISPOSE_ON_CLOSE);
					thread.interrupt();
				} catch (Exception ex) {
				}
			}
		});

		scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}
		});

		try {
			downloadLogs();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	 public static String[] tailLines(String filename, int nLinesToRead) throws FileNotFoundException, IOException  {
		 String[] lines=null;
		 String line=null;
	     try(   RandomAccessFile randomAccessFile = new RandomAccessFile(filename, "r")){
	        long lengthOfFile = randomAccessFile.length();
	        long counterFromEnd = 1L;
	        long newlineCounterGoal = nLinesToRead;
	        int newlineCounter = 0;
	        long tailPosition = 0L; // start of the end ;-)

	        // If you want to get the last 10 lines,
	        // and the last line ends with a newline, then you need to count back 11 newlines
	        // if there is no trailing newline, then you only need to count back 10
	        randomAccessFile.seek(lengthOfFile - 1L);
	        char currentChar = (char) randomAccessFile.readByte();
	        if(currentChar == '\n') {
	            newlineCounterGoal++;
	        }

	        while(counterFromEnd <= lengthOfFile) {
	            randomAccessFile.seek(lengthOfFile - counterFromEnd);
	            if(randomAccessFile.readByte() == '\n') {
	                newlineCounter++;
	            }
	            if(newlineCounter == newlineCounterGoal) {
	                tailPosition = randomAccessFile.getFilePointer();
	                break;
	            }
	            counterFromEnd++;
	        }
	        randomAccessFile.seek(tailPosition);

	       
	        lines = new String[nLinesToRead];
	        int nLine = 0;
	        while((line = randomAccessFile.readLine()) != null) {
	            lines[nLine++] = line;
	        }
	      }
	       return lines;
	    }
	 
	 //**
	 
	private void downloadLogs() {
		int line2Read=Integer.parseInt(PropertyFileReader.prop.getProperty("line.read"));
		thread = new Thread() {
			public void run() {
				int counter = 1;
				while (!stop) {
					try {
						flexiLogLabel.setAlignmentX(0);
						flexiLogLabel.setAlignmentY(0);
						
						String[] lines = tailLines(filePath, line2Read);
				        for(String line : lines) {
				            if(line != null) {
				            	inputAppend.append(line).append("\n").append("<br/>");
				            }
				        }
				        flexiLogLabel.setText("<html><body>" + inputAppend.toString() + "<body><html>");
						flexiLogLabel.repaint();
				        if (counter == 10) {
							Thread.sleep(10000);
							flexiLogLabel.setText("");
							flexiLogLabel.repaint();
							inputAppend.setLength(0);
						}
							
						int time=Integer.parseInt(PropertyFileReader.prop.getProperty("thread.time"));
						Thread.sleep(time);
						counter++;
					} catch (InterruptedException ex) {
						logger.error(ex.getMessage());
						
					} catch (Exception ex) {
						logger.error(ex.getMessage());

					}
				}
			}
		};
		thread.start();
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				FTPMonitor ft = new FTPMonitor();
				ft.setVisible(true);
				Dimension frameSize = new Dimension(Integer.parseInt("600"), Integer.parseInt("600"));
				ft.setSize(frameSize);
				ft.setMinimumSize(frameSize);
				ft.setLocationRelativeTo(null); // Center main frame

			}
		});
	}
}
