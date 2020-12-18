package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class HelpGui extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			HelpGui dialog = new HelpGui();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public HelpGui() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(SystemColor.info);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JTextArea txtrIntro = new JTextArea();
			txtrIntro.setEditable(false);
			txtrIntro.setToolTipText("");
			txtrIntro.setBackground(SystemColor.info);
			txtrIntro.setBounds(232, 11, 192, 138);
			txtrIntro.setWrapStyleWord(true);
			txtrIntro.setText("If there is any problems, please contact crewmate@space.ship.com");
			txtrIntro.setTabSize(4);
			txtrIntro.setRows(3);
			txtrIntro.setLineWrap(true);
			contentPanel.add(txtrIntro);
		}
		{
			JLabel lblIntroImage = new JLabel("");
//			ImageIcon imageIcon = new ImageIcon(new ImageIcon(HelpGui.class.getResource("/image/crewmate00.png")).getImage().getScaledInstance(90, 100, java.awt.Image.SCALE_SMOOTH));
//			lblIntroImage.setIcon(imageIcon);
			lblIntroImage.setBounds(332, 150, 92, 100);
			contentPanel.add(lblIntroImage);
		}
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 218, 185));
		panel.setBounds(0, 0, 222, 261);
		contentPanel.add(panel);
		
		JLabel lblCrewmates = new JLabel("CREWMATEs <3");
		lblCrewmates.setBounds(55, 205, 120, 17);
		lblCrewmates.setHorizontalAlignment(SwingConstants.CENTER);
		lblCrewmates.setFont(new Font("OCR A Extended", Font.PLAIN, 16));
		
		JLabel lblImage = new JLabel("New label");
		lblImage.setBounds(0, 0, 234, 141);
//		ImageIcon imageIcon = new ImageIcon(new ImageIcon(HelpGui.class.getResource("/image/crewmates-team.jpg")).getImage().getScaledInstance(241, 141, java.awt.Image.SCALE_SMOOTH));
//		lblImage.setIcon(imageIcon);
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblWeAre = new JLabel("We are..");
		lblWeAre.setBounds(71, 177, 80, 17);
		lblWeAre.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeAre.setFont(new Font("OCR A Extended", Font.PLAIN, 16));
		panel.setLayout(null);
		panel.add(lblCrewmates);
		panel.add(lblImage);
		panel.add(lblWeAre);
	}
}
