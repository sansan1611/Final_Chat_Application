package server;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import java.awt.Color;

public class TeamGui extends JDialog {

	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			TeamGui dialog = new TeamGui();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public TeamGui() {
		setBounds(100, 100, 450, 321);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.WHITE);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
//		{
//			ImageIcon imageIcon = new ImageIcon(new ImageIcon(TeamGui.class.getResource("/image/crewmate.png")).getImage().getScaledInstance(90, 100, java.awt.Image.SCALE_SMOOTH));
//		}
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 218, 185));
		panel.setBounds(0, 0, 222, 282);
		contentPanel.add(panel);
		
		JLabel lblCrewmates = new JLabel("DEVELOPMENT TEAM");
		lblCrewmates.setBounds(6, 236, 194, 17);
		lblCrewmates.setHorizontalAlignment(SwingConstants.CENTER);
		lblCrewmates.setFont(new Font("OCR A Extended", Font.PLAIN, 16));
		
		JLabel lblImage = new JLabel("");
		lblImage.setBounds(0, 6, 234, 184);
		//ImageIcon imageIcon = new ImageIcon(new ImageIcon(TeamGui.class.getResource("/icon/component/Server/Team/community.png")).getImage().getScaledInstance(250, 200, java.awt.Image.SCALE_SMOOTH));
		lblImage.setIcon(new ImageIcon(TeamGui.class.getResource("/icon/component/Server/Team/community.png")));
		lblImage.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel lblWeAre = new JLabel("We are..");
		lblWeAre.setBounds(72, 196, 80, 17);
		lblWeAre.setHorizontalAlignment(SwingConstants.CENTER);
		lblWeAre.setFont(new Font("OCR A Extended", Font.PLAIN, 16));
		panel.setLayout(null);
		panel.add(lblCrewmates);
		panel.add(lblImage);
		panel.add(lblWeAre);
		
		JLabel lblTrngNgcQunh = new JLabel("TRUONG NGOC QUYNH...\r\n\r\n");
		lblTrngNgcQunh.setHorizontalAlignment(SwingConstants.CENTER);
		lblTrngNgcQunh.setFont(new Font("OCR A Extended", Font.PLAIN, 14));
		lblTrngNgcQunh.setBounds(232, 78, 192, 15);
		contentPanel.add(lblTrngNgcQunh);
		
		JLabel lblTranThiMai = new JLabel("TRAN THI MAI HUONG...");
		lblTranThiMai.setHorizontalAlignment(SwingConstants.CENTER);
		lblTranThiMai.setFont(new Font("OCR A Extended", Font.PLAIN, 14));
		lblTranThiMai.setBounds(232, 173, 192, 15);
		contentPanel.add(lblTranThiMai);
		
		JLabel lblLamThiThuong = new JLabel("LAM THI THUONG HUYEN... ");
		lblLamThiThuong.setHorizontalAlignment(SwingConstants.CENTER);
		lblLamThiThuong.setFont(new Font("OCR A Extended", Font.PLAIN, 14));
		lblLamThiThuong.setBounds(232, 256, 192, 15);
		contentPanel.add(lblLamThiThuong);
		
		JLabel lblQuynhImage = new JLabel("");
		//ImageIcon img1 = new ImageIcon(new ImageIcon(TeamGui.class.getResource("/image/crewmate00.png")).getImage().getScaledInstance(50, 60, java.awt.Image.SCALE_SMOOTH));
		lblQuynhImage.setIcon(new ImageIcon(TeamGui.class.getResource("/icon/component/Server/Team/quynh.png")));
		lblQuynhImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblQuynhImage.setBounds(304, 11, 50, 56);
		contentPanel.add(lblQuynhImage);
		
		JLabel lblHuongImage = new JLabel("");
		lblHuongImage.setIcon(new ImageIcon(TeamGui.class.getResource("/icon/component/Server/Team/huong.png")));
		lblHuongImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblHuongImage.setBounds(304, 104, 50, 60);
		contentPanel.add(lblHuongImage);
		
		JLabel lblHuyenImage = new JLabel("");
		lblHuyenImage.setHorizontalAlignment(SwingConstants.CENTER);
		lblHuyenImage.setIcon(new ImageIcon(TeamGui.class.getResource("/icon/component/Server/Team/huyen.png")));
		lblHuyenImage.setBounds(304, 189, 50, 86);
		contentPanel.add(lblHuyenImage);
	}
}

