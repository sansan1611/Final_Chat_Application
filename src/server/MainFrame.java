package server;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

/**
 * 
 * @Created by DELL - StudentID: 18120652
 * @Date Jul 16, 2020 - 9:34:38 PM 
 * @Description ...
 */
public class MainFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
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
	public MainFrame() {
		setTitle("FIT Chat Server Management");
		
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 586, 450);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Menu");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmTeam = new JMenuItem("Team");
		mnNewMenu.add(mntmTeam);
		mntmTeam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			new TeamGui().setVisible(true);
				}
			});
		JMenuItem mntmSoftware = new JMenuItem("Software");
		mnNewMenu.add(mntmSoftware);
		mntmSoftware.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			new SoftwareGui().setVisible(true);
				}
			});
		JMenuItem mntmHelp = new JMenuItem("Help");
		mnNewMenu.add(mntmHelp);
		mntmHelp.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			new HelpGui().setVisible(true);
				}
			});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton start = new JButton("");
		start.setIcon(new ImageIcon(MainFrame.class.getResource("/icon/component/Server/rocket.png")));
		start.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JPanel panel = new JPanel();
		
		JLabel lblNewLabel = new JLabel("FIT Chat Server Management System");
		lblNewLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
		lblNewLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/icon/component/Server/speech-bubble.png")));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
					.addGap(121)
					.addComponent(lblNewLabel)
					.addContainerGap(127, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(223, Short.MAX_VALUE)
					.addComponent(start, GroupLayout.PREFERRED_SIZE, 143, GroupLayout.PREFERRED_SIZE)
					.addGap(210))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(61, Short.MAX_VALUE)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 469, GroupLayout.PREFERRED_SIZE)
					.addGap(46))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(47)
					.addComponent(lblNewLabel)
					.addGap(18)
					.addComponent(start, GroupLayout.PREFERRED_SIZE, 68, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(133, Short.MAX_VALUE))
		);
		
		JLabel text = new JLabel("Click here to start server");
		text.setFont(new Font("Tahoma", Font.PLAIN, 14));
		panel.add(text);
		contentPane.setLayout(gl_contentPane);
		
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Thread t = new Thread(){
					public void run() {
						try {
							new Server();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
					}
				};
				t.start();

				start.setEnabled(false);
				text.setText("Start server successful");
				text.setIcon(new ImageIcon(MainFrame.class.getResource("/icon/component/Server/fireworks.png")));
			}
		});
	}
}