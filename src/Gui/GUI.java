/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Gui;

import static java.awt.Color.BLACK;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import Control.Clicks;
import Control.Command;
import Control.GameState;
import Control.ICommand;
import Control.IGameState;
import Control.Logic;
import Control.Player;
import Network.SerialClient;

// OK btn + txtfield + testbtn
public class GUI extends JFrame implements IGameState
{
	private JPanel contentPane;
	private MapPanel drawPanel;
	Map wMap = new Map();
	private static final long serialVersionUID = 1L;
	private ICommand ctrl;
	private JTextField textField;
	private Player player;
	
	public GUI()
	{

		super("RISK");
		
		//Default window settigns
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1400, 1000);
	//	/*TEST*/	setBounds(0, 0, 400, 300);
		
		//Menubar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnConnect = new JMenu("Connect");
		menuBar.add(mnConnect);
		
		//Start Client
		JMenuItem mntmClient = new JMenuItem("Client");
		mnConnect.add(mntmClient);
		mntmClient.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				player = new Player(1, Color.ORANGE);
				ctrl = new SerialClient();
			}
		});
		
		//Start Server
		JMenuItem mntmServer = new JMenuItem("Server");
		mnConnect.add(mntmServer);
		mntmServer.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//start server();
				player = new Player(0, Color.BLUE);
				ctrl = new Logic();
			}
		});
		
		//Exit button
		JMenuItem mntmExit = new JMenuItem("Exit");
		menuBar.add(mntmExit);
		mntmExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		//
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{126, 30, 424, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 252, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		//Status bar
		JLabel lblStatus = new JLabel("Status");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.insets = new Insets(0, 0, 5, 0);
		gbc_lblStatus.gridx = 2;
		gbc_lblStatus.gridy = 0;
		contentPane.add(lblStatus, gbc_lblStatus);
		
		JButton btnNewButton_1 = new JButton("OK");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Command cmd = new Command(Clicks.Ok, player.getId());
				ctrl.OnCommand(cmd);
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 1;
		contentPane.add(btnNewButton_1, gbc_btnNewButton_1);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.WEST;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(3);
		
		JButton btnT = new JButton("T1");
		GridBagConstraints gbc_btnT = new GridBagConstraints();
		gbc_btnT.insets = new Insets(0, 0, 5, 5);
		gbc_btnT.gridx = 0;
		gbc_btnT.gridy = 2;
		contentPane.add(btnT, gbc_btnT);
		
		JButton btnT_1 = new JButton("T2");
		GridBagConstraints gbc_btnT_1 = new GridBagConstraints();
		gbc_btnT_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnT_1.gridx = 1;
		gbc_btnT_1.gridy = 2;
		contentPane.add(btnT_1, gbc_btnT_1);
		
		//Text Area
		JTextArea txtrLog = new JTextArea();
		txtrLog.setPreferredSize(new Dimension(150, 16));
		txtrLog.setMinimumSize(new Dimension(100, 16));
		txtrLog.setFont(new Font("Meiryo UI", Font.PLAIN, 12));
		txtrLog.setEditable(false);
		txtrLog.setText("Log");
		GridBagConstraints gbc_txtrLog = new GridBagConstraints();
		gbc_txtrLog.gridwidth = 2;
		gbc_txtrLog.ipadx = 10;
		gbc_txtrLog.insets = new Insets(0, 0, 0, 5);
		gbc_txtrLog.fill = GridBagConstraints.VERTICAL;
		gbc_txtrLog.gridx = 0;
		gbc_txtrLog.gridy = 3;
		contentPane.add(txtrLog, gbc_txtrLog);
		
		//Next button
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
		
		//Map
		JPanel MapPanel = new MapPanel();
		FlowLayout flowLayout = (FlowLayout) MapPanel.getLayout();
		MapPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_MapPanel = new GridBagConstraints();
		gbc_MapPanel.fill = GridBagConstraints.BOTH;
		gbc_MapPanel.gridheight = 3;
		gbc_MapPanel.gridx = 2;
		gbc_MapPanel.gridy = 1;		
		contentPane.add(MapPanel, gbc_MapPanel);
		
		//Click on map
		MapPanel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				String country = "";
				for(Territory t : wMap.getTerritories())
				{
					Shape sh = t.getShape();
					if(sh.contains(e.getPoint())){
						t.setFillColor(player.getColor());
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

	@Override
	public void OnGameState(GameState gs) {
		// TODO Auto-generated method stub
		
	}
}