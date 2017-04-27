/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package risk;


import java.awt.BasicStroke;
import static java.awt.Color.*;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import java.awt.Dimension;

/**
 *
 * @author 
 */
public class GUI extends JFrame {
	private JPanel contentPane;
	private MapPanel drawPanel;
	Map wMap = new Map();
	private static final long serialVersionUID = 1L;
	private Control ctrl;
	
	
	GUI(Control c) {
		super("RISK");
		ctrl = c;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1400, 1000);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnConnect = new JMenu("Connect");
		menuBar.add(mnConnect);
		
		JMenuItem mntmClient = new JMenuItem("Client");
		mnConnect.add(mntmClient);
		
		JMenuItem mntmServer = new JMenuItem("Server");
		mnConnect.add(mntmServer);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		menuBar.add(mntmExit);
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{150, 424, 0};
		gbl_contentPane.rowHeights = new int[]{0, 252, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		
		JLabel lblStatus = new JLabel("Status");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.insets = new Insets(0, 0, 5, 0);
		gbc_lblStatus.gridx = 1;
		gbc_lblStatus.gridy = 0;
		contentPane.add(lblStatus, gbc_lblStatus);
		
		JTextArea txtrLog = new JTextArea();
		txtrLog.setPreferredSize(new Dimension(150, 16));
		txtrLog.setMinimumSize(new Dimension(100, 16));
		txtrLog.setFont(new Font("Meiryo UI", Font.PLAIN, 12));
		txtrLog.setEditable(false);
		txtrLog.setText("Log");
		GridBagConstraints gbc_txtrLog = new GridBagConstraints();
		gbc_txtrLog.ipadx = 10;
		gbc_txtrLog.insets = new Insets(0, 0, 0, 5);
		gbc_txtrLog.fill = GridBagConstraints.VERTICAL;
		gbc_txtrLog.gridx = 0;
		gbc_txtrLog.gridy = 1;
		contentPane.add(txtrLog, gbc_txtrLog);
		
		JButton btnNewButton = new JButton("Next");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtrLog.setText("");
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.ipadx = 60;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		contentPane.add(btnNewButton, gbc_btnNewButton);
		
		
		JPanel MapPanel = new MapPanel();
		FlowLayout flowLayout = (FlowLayout) MapPanel.getLayout();
		MapPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_MapPanel = new GridBagConstraints();
		gbc_MapPanel.fill = GridBagConstraints.BOTH;
		gbc_MapPanel.gridx = 1;
		gbc_MapPanel.gridy = 1;		
		contentPane.add(MapPanel, gbc_MapPanel);
		MapPanel.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				String country = "";
				for(Territory t : wMap.getTerritories()) {
					Shape sh = t.getShape();
					if(sh.contains(e.getPoint())){
						t.setFillColor(RED);
						country =t.getId() + " " + t.getName() + " - " + e.getX() + ", " + e.getY() ;
					}				
									
				}
				MapPanel.repaint();
				txtrLog.setText(txtrLog.getText() + "\n" +country);
//				ctrl.sendClick(new Point(e.getX(), e.getY()));
			}
		});
		
		setVisible(true);
		
	}

	private class MapPanel extends JPanel {

		private static final long serialVersionUID = 1L;
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			Map.create(g2d);
			
			for(Territory t : wMap.getTerritories()) {
				g2d.setPaint(t.getFillColor());
		        g2d.fill(t.getShape());
		        g2d.setPaint(BLACK);
		        g2d.setStroke(new BasicStroke(1, 0, 0, 4));
		        
				g2d.draw(t.getShape());
			}
			
		}

		@Override
		public void repaint() {
			//super.repaint();
			Graphics2D g2d = (Graphics2D)super.getGraphics();
			
			for(Territory t : wMap.getTerritories()) {
				g2d.setPaint(t.getFillColor());
		        g2d.fill(t.getShape());
		        g2d.setPaint(BLACK);
		        g2d.setStroke(new BasicStroke(1, 0, 0, 4));

//		        String text = t.getName();
				g2d.draw(t.getShape());
			}
			
		}
	
		
	}
	
}

