package server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.Color;

public class SoftwareGui extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SoftwareGui dialog = new SoftwareGui();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public SoftwareGui() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblIntroImage = new JLabel("");
			lblIntroImage.setIcon(new ImageIcon(SoftwareGui.class.getResource("/icon/component/Server/Software/laugh.png")));
			
			JLabel lblNewLabel_1 = new JLabel("New label");
			lblNewLabel_1.setIcon(new ImageIcon(SoftwareGui.class.getResource("/icon/component/Server/Software/laugh.png")));
			contentPanel.add(lblNewLabel_1);
//			ImageIcon imageIcon = new ImageIcon(new ImageIcon(SoftwareGui.class.getResource("/image/crewmate02.png")).getImage().getScaledInstance(70, 80, java.awt.Image.SCALE_SMOOTH));
//			
			JTextArea txtrIntro = new JTextArea();
			txtrIntro.setBackground(Color.WHITE);
			txtrIntro.setWrapStyleWord(true);
			txtrIntro.setText("Join with us!\n\r\nConect with friends!\n\r\nChat privately and in group!\r\n\r\nCome and have fun!!!");
			txtrIntro.setTabSize(4);
			txtrIntro.setRows(3);
			txtrIntro.setLineWrap(true);
			txtrIntro.setEditable(false);
			txtrIntro.setBounds(232, 11, 185, 155);
			contentPanel.add(txtrIntro);
			//lblIntroImage.setIcon(imageIcon);
			lblIntroImage.setBounds(334, 173, 83, 77);
			contentPanel.add(lblIntroImage);
		}
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 218, 185));
		panel.setBounds(0, 0, 222, 261);
		contentPanel.add(panel);
		
		JLabel lblCrewmates = new JLabel("FIT CHAT");
		lblCrewmates.setBounds(55, 205, 120, 17);
		lblCrewmates.setHorizontalAlignment(SwingConstants.CENTER);
		lblCrewmates.setFont(new Font("OCR A Extended", Font.PLAIN, 16));
		
		JLabel lblImage = new JLabel("");
		lblImage.setIcon(new ImageIcon(SoftwareGui.class.getResource("/icon/component/Server/Software/chat.png")));
		lblImage.setBounds(0, 0, 234, 141);
		//ImageIcon imageIcon = new ImageIcon(new ImageIcon(SoftwareGui.class.getResource("/image/crewmates-team.jpg")).getImage().getScaledInstance(241, 141, java.awt.Image.SCALE_SMOOTH));
		//lblImage.setIcon(imageIcon);
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblWeAre = new JLabel("Welcome to");
		lblWeAre.setBounds(47, 176, 145, 17);
		lblWeAre.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeAre.setFont(new Font("OCR A Extended", Font.PLAIN, 16));
		panel.setLayout(null);
		panel.add(lblCrewmates);
		panel.add(lblImage);
		panel.add(lblWeAre);
	}
}
