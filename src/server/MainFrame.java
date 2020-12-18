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
import javax.swing.JTextPane;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

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
		setTitle("FIT Chat Server Mangement");
		
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
		start.setIcon(new ImageIcon(MainFrame.class.getResource("/icon/component/rocket.png")));
		start.setFont(new Font("Times New Roman", Font.BOLD, 14));
		
		JPanel panel = new JPanel();
		
		JLabel lblFitIcon = new JLabel("");
		lblFitIcon.setIcon(new ImageIcon(MainFrame.class.getResource("/icon/component/speech-bubble.png")));
		
		JLabel lblNewLabel = new JLabel("FIT Chat Server Management System");
		lblNewLabel.setFont(new Font("Arial Hebrew Scholar", Font.BOLD, 16));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(15, Short.MAX_VALUE)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 555, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(93)
					.addComponent(lblFitIcon, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel)
					.addContainerGap(124, Short.MAX_VALUE))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(210)
					.addComponent(start, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(198, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(30)
							.addComponent(lblFitIcon, GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
							.addGap(18))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblNewLabel)
							.addGap(39)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(start, GroupLayout.PREFERRED_SIZE, 80, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
					.addGap(15))
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
				text.setText("Start server successful!");
				text.setIcon(new ImageIcon(MainFrame.class.getResource("/icon/component/fireworks.png")));
			}
		});
	}
}
