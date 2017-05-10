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

import Control.Phases;

public class GUI extends JFrame
{
	private static final long serialVersionUID = -8342265159647402288L;
	
	private JPanel _contentPanel = new JPanel();
	private JTextField _textField;
	private JLabel _status;
	private JTextArea _log;
	
	private MapPanel _mapPanel;
	private Map _map = new Map();
	
	private Controller ctrl;
	
	
	public GUI()
	{
		super("RISK"); //name of the main window
		
		//Default window settings
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1400, 1000);
		
		/*TEST*/	setBounds(0, 0, 800, 600);
		
		
		
		ctrl = new Controller(this, _map);
		
		InitMenuBar();
		InitContentPanel();
		
			setContentPane(_contentPanel);
			
		InitStatusBar();
		InitTestButtons();
		InitTextArea();
		InitNextButton();
		InitMapPanel();
		
		setVisible(true);
	}
	
	private void InitMenuBar()
	{
		//MenuBar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		//Connect menu
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
				ctrl.StartClient();
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
				ctrl.StartServer();
			}
		});
				
		//Exit button
		JMenuItem mntmExit = new JMenuItem("Exit");
		menuBar.add(mntmExit);
		mntmExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ctrl.Exit();
				System.exit(0);
			}
		});
	}

	private void InitContentPanel()
	{
		_contentPanel = new JPanel();
		_contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
			setContentPane(_contentPanel);
			
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{126, 30, 424, 0};
		gbl_contentPane.rowHeights = new int[]{0, 0, 0, 252, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		_contentPanel.setLayout(gbl_contentPane);
	}
	
	private void InitStatusBar()
	{
		//Status bar
		_status = new JLabel("Status");
		GridBagConstraints gbc_lblStatus = new GridBagConstraints();
		gbc_lblStatus.insets = new Insets(0, 0, 5, 0);
		gbc_lblStatus.gridx = 2;
		gbc_lblStatus.gridy = 0;
		_contentPanel.add(_status, gbc_lblStatus);
		
		//OK button
		JButton btnNewButton_1 = new JButton("OK");
		btnNewButton_1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ctrl.BtnOk();
			}
		});
		
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 0;
		gbc_btnNewButton_1.gridy = 1;
		_contentPanel.add(btnNewButton_1, gbc_btnNewButton_1);
		
		_textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.WEST;
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		_contentPanel.add(_textField, gbc_textField);
		_textField.setColumns(3);
	}
	
	private void InitTestButtons()
	{
		//TEST BUTTON 1//
		JButton btnT = new JButton("T1");

		btnT.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				ctrl.Test1();
			}
		});
		
		GridBagConstraints gbc_btnT = new GridBagConstraints();
		gbc_btnT.insets = new Insets(0, 0, 5, 5);
		gbc_btnT.gridx = 0;
		gbc_btnT.gridy = 2;
		_contentPanel.add(btnT, gbc_btnT);
		
		
		//TEST BUTTON 2//
		JButton btnT_1 = new JButton("T2");
		btnT_1.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ctrl.Test2();
			}
		});
		GridBagConstraints gbc_btnT_1 = new GridBagConstraints();
		gbc_btnT_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnT_1.gridx = 1;
		gbc_btnT_1.gridy = 2;
		_contentPanel.add(btnT_1, gbc_btnT_1);
	}
	
	private void InitTextArea()
	{
		//Text Area
		_log = new JTextArea();
		_log.setPreferredSize(new Dimension(150, 16));
		_log.setMinimumSize(new Dimension(100, 16));
		_log.setFont(new Font("Meiryo UI", Font.PLAIN, 12));
		_log.setEditable(false);
		_log.setText("Log");
		
		GridBagConstraints gbc_txtrLog = new GridBagConstraints();
		gbc_txtrLog.gridwidth = 2;
		gbc_txtrLog.ipadx = 10;
		gbc_txtrLog.insets = new Insets(0, 0, 0, 5);
		gbc_txtrLog.fill = GridBagConstraints.VERTICAL;
		gbc_txtrLog.gridx = 0;
		gbc_txtrLog.gridy = 3;
		_contentPanel.add(_log, gbc_txtrLog);
	}
	
	private void InitNextButton()
	{
		//Next button
		JButton btnNewButton = new JButton("Next");
		btnNewButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ctrl.BtnNext();
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.ipadx = 60;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		_contentPanel.add(btnNewButton, gbc_btnNewButton);
	}
	
	private void InitMapPanel()
	{
		//Map
		_mapPanel = new MapPanel();
		_mapPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		GridBagConstraints gbc_MapPanel = new GridBagConstraints();
		gbc_MapPanel.fill = GridBagConstraints.BOTH;
		gbc_MapPanel.gridheight = 3;
		gbc_MapPanel.gridx = 2;
		gbc_MapPanel.gridy = 1;		
		_contentPanel.add(_mapPanel, gbc_MapPanel);
		
		//Click on map
		_mapPanel.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mouseClicked(MouseEvent e)
			{
				for(Territory t : _map.Territories)
				{
					Shape sh = t.getShape();
					if(sh.contains(e.getPoint()))
					{
						ctrl.ClickOnMap(t.getId());
					}							
				}
				
			}
		});
	}

	
	public void ClearLog()
	{
		_log.setText("");
	}
	
	public void AppendLog(String str)
	{
		_log.append(str + "\n");
	}
	
	public void PaintTerritory(int territoryId, Color color)
	{
		_mapPanel.RePaintTerritory(territoryId, color);
	}
	
	public void UpdateStatus(int playerId, Phases phase)
	{
		_status.setText("Player: " + ctrl.GetPlayerId() + "         Current Player: " + playerId + "  Phase: " + phase);
	}

	private class MapPanel extends JPanel
	{

		private static final long serialVersionUID = 1L;
		
		@Override
		protected void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			_map.create(g2d);
			
			for(Territory t : _map.Territories)
			{
				g2d.setPaint(t.getFillColor());
		        g2d.fill(t.getShape());
		        g2d.setPaint(BLACK);
		        g2d.setStroke(new BasicStroke(1, 0, 0, 4));
		        
				g2d.draw(t.getShape());
			}
			
		}

		@Override
		public void repaint()
		{
			//super.repaint();
			Graphics2D g2d = (Graphics2D)super.getGraphics();
			
			for(Territory t : _map.Territories)
			{
				g2d.setPaint(t.getFillColor());
		        g2d.fill(t.getShape());
		        g2d.setPaint(BLACK);
		        g2d.setStroke(new BasicStroke(1, 0, 0, 4));
				g2d.draw(t.getShape());
				g2d.drawString(Integer.toString(t.getUnits()), t.getX(),  t.getY());
			}
			
		}
		
		//TODO check this function!!!
		public void RePaintTerritory(int territoryId, Color color)
		{
			Graphics2D g2d = (Graphics2D)super.getGraphics();
			for(Territory t : _map.Territories)
			{
				if(t.getId() == territoryId)
				{
					g2d.setPaint(color);
					g2d.fill(t.getShape());
					g2d.setPaint(BLACK);
			        g2d.setStroke(new BasicStroke(1, 0, 0, 4));
					g2d.draw(t.getShape());
					g2d.drawString(Integer.toString(t.getUnits()), t.getX(),  t.getY());
				}
			}
		}
	}
}