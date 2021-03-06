package client;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


import javax.swing.GroupLayout.*;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.awt.*;

public class LoginFrame extends JFrame {
	
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;

	private String host = "localhost";
	private int port = 9999;
	private Socket socket;
	
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private String username;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginFrame frame = new LoginFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	public LoginFrame() {
		setTitle("FIT CHAT");
		
		setDefaultLookAndFeelDecorated(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 517, 343);
		contentPane = new JPanel();
		contentPane.setBackground(UIManager.getColor("InternalFrame.paletteBackground"));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel headerPanel = new JPanel();
		headerPanel.setBackground(UIManager.getColor("InternalFrame.paletteBackground"));
		
		JLabel lbUsername = new JLabel("Username");
		lbUsername.setFont(new Font("Papyrus", Font.PLAIN, 14));
		
		JLabel lbPassword = new JLabel("Password");
		lbPassword.setFont(new Font("Papyrus", Font.PLAIN, 14));
		
		txtUsername = new JTextField();
		txtUsername.setColumns(10);
		
		txtPassword = new JPasswordField();
		
		JPanel buttons = new JPanel();
		buttons.setBackground(new Color(230, 240, 247));
		JPanel notificationContainer = new JPanel();
		notificationContainer.setBackground(UIManager.getColor("InternalFrame.paletteBackground"));
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(LoginFrame.class.getResource("/icon/component/Login/hello.png")));
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(18)
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, 327, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_contentPane.createSequentialGroup()
								.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
									.addComponent(lbUsername)
									.addComponent(lbPassword))
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
									.addComponent(txtPassword)
									.addComponent(txtUsername, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)))
							.addComponent(buttons, GroupLayout.PREFERRED_SIZE, 323, GroupLayout.PREFERRED_SIZE)
							.addComponent(notificationContainer, GroupLayout.DEFAULT_SIZE, 327, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(30)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 262, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(headerPanel, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(notificationContainer, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lbUsername))
							.addGap(30)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lbPassword))
							.addGap(31)
							.addComponent(buttons, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(19, Short.MAX_VALUE))
		);
		
		JLabel headerContent = new JLabel("LOGIN");
		headerPanel.add(headerContent);
		headerContent.setFont(new Font("Papyrus", Font.BOLD, 24));
		
		JLabel notification = new JLabel("");
		notification.setForeground(Color.RED);
		notification.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		notificationContainer.add(notification);
		
		JButton login = new JButton("Sign in");
		login.setFont(new Font("Papyrus", Font.PLAIN, 12));
		login.setIcon(new ImageIcon(LoginFrame.class.getResource("/icon/component/Login/038-login.png")));
		JButton signup = new JButton("Register");
		signup.setFont(new Font("Papyrus", Font.PLAIN, 12));
		signup.setIcon(new ImageIcon(LoginFrame.class.getResource("/icon/component/Login/065-sign up.png")));
		
		login.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String response = Login(txtUsername.getText(), String.copyValueOf(txtPassword.getPassword()));
				
				if ( response.equals("Log in successful") ) {
					username = txtUsername.getText();
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								ChatFrame frame = new ChatFrame(username, dis, dos);
								frame.setVisible(true);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
					dispose();
				} else {
					login.setEnabled(false);
					signup.setEnabled(false);
					txtPassword.setText("");
					notification.setText(response);
				}
			}
		});
		login.setEnabled(false);
		buttons.add(login);
		
		signup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JPasswordField confirm = new JPasswordField();
				
				
			    int action = JOptionPane.showConfirmDialog(null, confirm,"Comfirm your password",JOptionPane.OK_CANCEL_OPTION);
			    if (action == JOptionPane.OK_OPTION) {
			    	if (String.copyValueOf(confirm.getPassword()).equals(String.copyValueOf(txtPassword.getPassword()))) {
			    		String response = Signup(txtUsername.getText(), String.copyValueOf(txtPassword.getPassword()));
					
						if ( response.equals("Sign up successful") ) {
							username = txtUsername.getText();
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										int confirm = JOptionPane.showConfirmDialog(null, "Sign up successful\nWelcome to FIT CHAT", "Sign up successful", JOptionPane.DEFAULT_OPTION);
										 
										ChatFrame frame = new ChatFrame(username, dis, dos);
										frame.setVisible(true);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
							dispose();
						} else {
							login.setEnabled(false);
							signup.setEnabled(false);
							txtPassword.setText("");
							notification.setText(response);
						}
					} else {
			    		notification.setText("Confirm password does not match");
			    	}
			    }
			}
		});
		signup.setEnabled(false);
		buttons.add(signup);
		contentPane.setLayout(gl_contentPane);
		
		txtUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtUsername.getText().isEmpty() || String.copyValueOf(txtPassword.getPassword()).isEmpty()) {
					login.setEnabled(false);
					signup.setEnabled(false);
				} else {
					login.setEnabled(true);
					signup.setEnabled(true);
				}
			}
		});
		
		txtPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (txtUsername.getText().isEmpty() || String.copyValueOf(txtPassword.getPassword()).isEmpty()) {
					login.setEnabled(false);
					signup.setEnabled(false);
				} else {
					login.setEnabled(true);
					signup.setEnabled(true);
				}
			}
		});
		
		this.getRootPane().setDefaultButton(login);
	}
	

	public String Login(String username, String password) {
		try {
			Connect();
			
			dos.writeUTF("Log in");
			dos.writeUTF(username);
			dos.writeUTF(password);
			dos.flush();
			
			String response = dis.readUTF();
			return response;
			
		} catch (IOException e) {
			e.printStackTrace();
			return "Network error: Log in fail";
		}
	}
	

	public String Signup(String username, String password) {
		try {
			Connect();
			
			dos.writeUTF("Sign up");
			dos.writeUTF(username);
			dos.writeUTF(password);
			dos.flush();
			
			String response = dis.readUTF();
			return response;
			
		} catch (IOException e) {
			e.printStackTrace();
			return "Network error: Sign up fail";
		}
	}
	

	public void Connect() {
		try {
			if (socket != null) {
				socket.close();
			}
			socket = new Socket(host, port);
			this.dis = new DataInputStream(socket.getInputStream());
			this.dos = new DataOutputStream(socket.getOutputStream());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public String getUsername() {
		return this.username;
	}
}
