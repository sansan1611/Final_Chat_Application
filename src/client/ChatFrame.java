package client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
//import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;


/**
 * view
 *
 * @Created by DELL - StudentID: 18120652
 * @Date Jul 14, 2020 - 8:36:36 PM
 * @Description ...
 */
public class ChatFrame extends JFrame {

//    send button
    private JButton btnSend;
//    ? scroll for chat panel
    private JScrollPane chatPanel;
//    name of receiver
    private JLabel lbReceiver = new JLabel(" ");
//    ?
    private JPanel contentPane;
//    input text
    private JTextField txtMessage;
//    chat panel for display messages?
    private JTextPane chatWindow;
    JButton btnSelectAll;
//    Combo list of online users
    JComboBox<String> onlineUsers = new JComboBox<String>();
//    List<String> group = new ArrayList<String>();

//    myself who just logged in
    private String username;
    private DataInputStream dis;
    private DataOutputStream dos;

//    variable stores receiver name and jpanel which store messages?
    private HashMap<String, JTextPane> chatWindows = new HashMap<String, JTextPane>();

    Thread receiver;

    StyledDocument messageDoc;
    Style userStyleSend;

//    make scroll for chat panel
    private void autoScroll() {
        chatPanel.getVerticalScrollBar().setValue(chatPanel.getVerticalScrollBar().getMaximum());
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Insert a emoji into chat pane.
     */
    private void newEmoji(String username, String emoji, Boolean yourMessage) {

        StyledDocument doc;
        if (username.equals(this.username)) {
            doc = chatWindows.get(lbReceiver.getText()).getStyledDocument();
        } else {
            doc = chatWindows.get(username).getStyledDocument();
        }

        Style userStyle = doc.getStyle("User style");
        if (userStyle == null) {
            userStyle = doc.addStyle("User style", null);
            StyleConstants.setBold(userStyle, true);
        }

        if (yourMessage == true) {
            StyleConstants.setForeground(userStyle, Color.red);
        } else {
            StyleConstants.setForeground(userStyle, Color.BLUE);
        }

        // In ra màn hình tên người gửi
        try {
            doc.insertString(doc.getLength(), username + ": ", userStyle);
        } catch (BadLocationException e) {
        }

        Style iconStyle = doc.getStyle("Icon style");
        if (iconStyle == null) {
            iconStyle = doc.addStyle("Icon style", null);
        }
        String emojiPath = emoji.substring(emoji.lastIndexOf("/icon"));
        StyleConstants.setIcon(iconStyle, new ImageIcon(getClass().getResource(emojiPath)));

        // In ra màn hình Emoji
        try {
            doc.insertString(doc.getLength(), "invisible text", iconStyle);
        } catch (BadLocationException e) {
        }

        // Xuống dòng
        try {
            doc.insertString(doc.getLength(), "\n", userStyle);
        } catch (BadLocationException e) {
        }

        autoScroll();
    }

    /**
     * Insert a new message into chat pane.
     */
    private void newMessage(String username, String message, Boolean yourMessage) {

        StyledDocument doc;
        if (username.equals(this.username)) {
            doc = chatWindows.get(lbReceiver.getText()).getStyledDocument();
        } else if (username.equals(" ")) {
            doc = chatWindows.get(" ").getStyledDocument();
        } else {
            doc = chatWindows.get(username).getStyledDocument();
        }

        Style userStyle = doc.getStyle("User style");
        if (userStyle == null) {
            userStyle = doc.addStyle("User style", null);
            StyleConstants.setBold(userStyle, true);
        }

        if (yourMessage == true) {
            StyleConstants.setForeground(userStyle, Color.red);
        } else {
            StyleConstants.setForeground(userStyle, Color.BLUE);
        }

        // In ra tên người gửi
        try {
            doc.insertString(doc.getLength(), username + ": ", userStyle);
        } catch (BadLocationException e) {
        }

        Style messageStyle = doc.getStyle("Message style");
        if (messageStyle == null) {
            messageStyle = doc.addStyle("Message style", null);
            StyleConstants.setForeground(messageStyle, Color.BLACK);
            StyleConstants.setBold(messageStyle, false);
        }

        // In ra nội dung tin nhắn
        try {
            doc.insertString(doc.getLength(), message + "\n", messageStyle);
        } catch (BadLocationException e) {
        }

        autoScroll();
    }

    /**
     * Chat text with group
     */
    private void groupMessage(String username, String message, Boolean yourMessage) {

        StyledDocument doc;
        if (username.equals(this.username)) {
            doc = chatWindows.get(lbReceiver.getText()).getStyledDocument();
        } else if (username.equals(" ")) {
            doc = chatWindows.get(" ").getStyledDocument();
        } else {
            doc = chatWindows.get(username).getStyledDocument();
        }

        Style userStyle = doc.getStyle("User style");
        if (userStyle == null) {
            userStyle = doc.addStyle("User style", null);
            StyleConstants.setBold(userStyle, true);
        }

        if (yourMessage == true) {
            StyleConstants.setForeground(userStyle, Color.red);
        } else {
            StyleConstants.setForeground(userStyle, Color.BLUE);
        }

        userStyleSend = userStyle;
        Style messageStyle = doc.getStyle("Message style");
        if (messageStyle == null) {
            messageStyle = doc.addStyle("Message style", null);
            StyleConstants.setForeground(messageStyle, Color.BLACK);
            StyleConstants.setBold(messageStyle, false);
        }
        messageDoc = doc;

        autoScroll();
    }

    /**
     * Create the frame.
     */
    public ChatFrame(String username, DataInputStream dis, DataOutputStream dos) {
        setTitle("MANGO CHAT");
        this.username = username;
        this.dis = dis;
        this.dos = dos;
        receiver = new Thread(new Receiver(dis));
        receiver.start();

        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 586, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(230, 240, 247));
        setContentPane(contentPane);

        JPanel header = new JPanel();
        header.setBackground(new Color(160, 190, 223));

        txtMessage = new JTextField();
//        txtMessage.setEnabled(false);
        txtMessage.setColumns(10);

        btnSend = new JButton("");
        btnSend.setEnabled(false);
//        btnSend.setVisible(true);
        btnSend.setIcon(new ImageIcon(getClass().getResource("/icon/component/send.png")));

        chatPanel = new JScrollPane();
        chatPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(230, 240, 247));

        JPanel emojis = new JPanel();
        emojis.setBackground(new Color(230, 240, 247));
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addComponent(header, GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(leftPanel, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                        .addComponent(emojis, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addComponent(txtMessage, GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED))
                                        .addComponent(chatPanel, GroupLayout.DEFAULT_SIZE, 440, Short.MAX_VALUE)))
        );
        gl_contentPane.setVerticalGroup(
                gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                                .addComponent(header, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                        .addGroup(gl_contentPane.createSequentialGroup()
                                                .addComponent(chatPanel, GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addComponent(emojis, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(ComponentPlacement.RELATED)
                                                .addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
                                                        .addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtMessage, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, 355, Short.MAX_VALUE)))
        );

        JLabel smileIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/smile.png")));
		smileIcon.addMouseListener(new IconListener(smileIcon.getIcon().toString()));
		emojis.add(smileIcon);
		
		
		JLabel bigSmileIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/big-smile.png")));
		bigSmileIcon.addMouseListener(new IconListener(bigSmileIcon.getIcon().toString()));
		emojis.add(bigSmileIcon);
		
		JLabel happyIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/happy.png")));
		happyIcon.addMouseListener(new IconListener(happyIcon.getIcon().toString()));
		emojis.add(happyIcon);
		
		JLabel loveIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/love.png")));
		loveIcon.addMouseListener(new IconListener(loveIcon.getIcon().toString()));
		emojis.add(loveIcon);
		
		JLabel sadIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/sad.png")));
		sadIcon.addMouseListener(new IconListener(sadIcon.getIcon().toString()));
		emojis.add(sadIcon);
		
		JLabel madIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/mad.png")));
		madIcon.addMouseListener(new IconListener(madIcon.getIcon().toString()));
		emojis.add(madIcon);
		
		JLabel suspiciousIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/suspicious.png")));
		suspiciousIcon.addMouseListener(new IconListener(suspiciousIcon.getIcon().toString()));
		emojis.add(suspiciousIcon);
		
		JLabel angryIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/angry.png")));
		angryIcon.addMouseListener(new IconListener(angryIcon.getIcon().toString()));
		emojis.add(angryIcon);
		
		JLabel confusedIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/confused.png")));
		confusedIcon.addMouseListener(new IconListener(confusedIcon.getIcon().toString()));
		emojis.add(confusedIcon);
		
		JLabel unhappyIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/unhappy.png")));
		unhappyIcon.addMouseListener(new IconListener(unhappyIcon.getIcon().toString()));
		emojis.add(unhappyIcon);
		
		JLabel appleIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/apple.png")));
		appleIcon.addMouseListener(new IconListener(appleIcon.getIcon().toString()));
		emojis.add(appleIcon);
		
		JLabel orangeIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/orange.png")));
		String test = "" + ChatFrame.class.getResource("/icon/emoji/orange.png");
		orangeIcon.addMouseListener(new IconListener(test));
		emojis.add(orangeIcon);
		
		JLabel cherryIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/cherry.png")));
		cherryIcon.addMouseListener(new IconListener(cherryIcon.getIcon().toString()));
		emojis.add(cherryIcon);
		
		JLabel cakeIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/cake.png")));
		cakeIcon.addMouseListener(new IconListener(cakeIcon.getIcon().toString()));
		emojis.add(cakeIcon);
		
		JLabel vietnamIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/vietnam.png")));
		vietnamIcon.addMouseListener(new IconListener(vietnamIcon.getIcon().toString()));
		emojis.add(vietnamIcon);
		
		JLabel usIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/us.png")));
		usIcon.addMouseListener(new IconListener(usIcon.getIcon().toString()));
		emojis.add(usIcon);
		
		JLabel ukIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/uk.png")));
		ukIcon.addMouseListener(new IconListener(ukIcon.getIcon().toString()));
		emojis.add(ukIcon);
		
		JLabel canadaIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/canada.png")));
		canadaIcon.addMouseListener(new IconListener(canadaIcon.getIcon().toString()));
		emojis.add(canadaIcon);
		
		JLabel italyIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/italy.png")));
		italyIcon.addMouseListener(new IconListener(italyIcon.getIcon().toString()));
		emojis.add(italyIcon);
		
		JLabel spainIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/spain.png")));
		spainIcon.addMouseListener(new IconListener(spainIcon.getIcon().toString()));
		emojis.add(spainIcon);
		
		JLabel egyptIcon = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/emoji/egypt.png")));
		egyptIcon.addMouseListener(new IconListener(egyptIcon.getIcon().toString()));
		emojis.add(egyptIcon);

        JLabel userImage = new JLabel(new ImageIcon(getClass().getResource("/icon/component/user.png")));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(230, 240, 247));
        JLabel lblNewLabel_1 = new JLabel("CHAT WITH");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));

        btnSelectAll = new JButton("Select All");
        btnSelectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtMessage.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "You need to text something!");
                } else {

                    lbReceiver.setText((String) onlineUsers.getSelectedItem());
                    int size = onlineUsers.getItemCount();
                    for (int i = 0; i < size; i++) {
                        String member = onlineUsers.getItemAt(i);
                        try {
                            dos.writeUTF("Text");
                            dos.writeUTF(member);
                            dos.writeUTF("@" + txtMessage.getText());
                            dos.flush();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                            newMessage("ERROR", "Network error!", true);
                        }

                        // In ra tin nhắn lên màn hình chat với người nhận
//                        groupMessage(username, txtMessage.getText(), true);
                    }

                    if (chatWindows.get(" ") != null) {
                        lbReceiver.setText(" ");
                        groupMessage(username, txtMessage.getText(), true);
                        Style messageStyle = messageDoc.getStyle("Message style");
                        try {
                            // In ra tên người gửi
                            messageDoc.insertString(messageDoc.getLength(), username + ": ", userStyleSend);
                            messageDoc.insertString(messageDoc.getLength(), txtMessage.getText() + "\n", messageStyle);
                            txtMessage.setText("");
                        } catch (BadLocationException ex) {
                            Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        JTextPane temp = new JTextPane();
                        temp.setFont(new Font("Arial", Font.PLAIN, 14));
                        temp.setEditable(false);
                        chatWindows.put(" ", temp);
                        lbReceiver.setText(" ");
                        // In tin nhắn lên màn hình chat với người gửi
                        groupMessage(username, txtMessage.getText(), true);
                        Style messageStyle = messageDoc.getStyle("Message style");
                        try {
                            // In ra tên người gửi
                            messageDoc.insertString(messageDoc.getLength(), username + ": ", userStyleSend);
                            messageDoc.insertString(messageDoc.getLength(), txtMessage.getText() + "\n", messageStyle);
                            txtMessage.setText("");
                        } catch (BadLocationException ex) {
                            Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }

        });

        GroupLayout gl_leftPanel = new GroupLayout(leftPanel);
        gl_leftPanel.setHorizontalGroup(
                gl_leftPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_leftPanel.createSequentialGroup()
                                .addGap(25)
                                .addComponent(userImage, GroupLayout.DEFAULT_SIZE, 71, Short.MAX_VALUE)
                                .addGap(25))
                        .addGroup(gl_leftPanel.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panel, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(gl_leftPanel.createSequentialGroup()
                                .addGap(28)
                                .addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(29))
                        .addGroup(Alignment.TRAILING, gl_leftPanel.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(onlineUsers, 0, 101, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(Alignment.LEADING, gl_leftPanel.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnSelectAll)
                                .addContainerGap(22, Short.MAX_VALUE))
        );
        onlineUsers.addItemListener(new ItemListener() {
//            need to change at here
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    lbReceiver.setText((String) onlineUsers.getSelectedItem());
                    if (chatWindow != chatWindows.get(lbReceiver.getText())) {
                        txtMessage.setText("");
                        chatWindow = chatWindows.get(lbReceiver.getText());
                        chatPanel.setViewportView(chatWindow);
                        chatPanel.validate();
                    }

                    if (lbReceiver.getText().isEmpty()) {
                        btnSend.setEnabled(false);

                    } else {
                        btnSend.setEnabled(true);
                    }
                } else {
                    lbReceiver.setText(" ");
                    if (chatWindow != chatWindows.get(lbReceiver.getText())) {
                        txtMessage.setText("");
                        chatWindow = chatWindows.get(lbReceiver.getText());
                        chatPanel.setViewportView(chatWindow);
                        chatPanel.validate();
                        btnSend.setEnabled(true);
                    }
                }

            }
        });

        gl_leftPanel.setVerticalGroup(
                gl_leftPanel.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_leftPanel.createSequentialGroup()
                                .addGap(5)
                                .addComponent(userImage)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(panel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                .addGap(41)
                                .addComponent(lblNewLabel_1)
                                .addPreferredGap(ComponentPlacement.RELATED)
                                .addComponent(onlineUsers, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(btnSelectAll)
                                .addContainerGap(181, Short.MAX_VALUE))
        );

        JLabel lbUsername = new JLabel(this.username);
        lbUsername.setFont(new Font("Arial", Font.BOLD, 15));
        panel.add(lbUsername);
        leftPanel.setLayout(gl_leftPanel);

        JLabel headerContent = new JLabel("MANGO CHAT");
        headerContent.setFont(new Font("Poor Richard", Font.BOLD, 24));
        header.add(headerContent);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setBackground(new Color(230, 240, 247));
        chatPanel.setColumnHeaderView(usernamePanel);

        lbReceiver.setFont(new Font("Arial", Font.BOLD, 16));
        usernamePanel.add(lbReceiver);

        chatWindows.put(" ", new JTextPane());
        chatWindow = chatWindows.get(" ");
        chatWindow.setFont(new Font("Arial", Font.PLAIN, 14));
        chatWindow.setEditable(false);

        chatPanel.setViewportView(chatWindow);
        contentPane.setLayout(gl_contentPane);

        txtMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (txtMessage.getText().isEmpty() || lbReceiver.getText().isEmpty()) {
                    btnSend.setEnabled(false);
                } else {
                    btnSend.setEnabled(true);
                }
            }
        });

        // Set action perform to send button.
        btnSend.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                try {
                    dos.writeUTF("Text");
                    dos.writeUTF(lbReceiver.getText());
                    dos.writeUTF(txtMessage.getText());
                    dos.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    newMessage("ERROR", "Network error!", true);
                }

                // In ra tin nhắn lên màn hình chat với người nhận
                newMessage(username, txtMessage.getText(), true);
                txtMessage.setText("");
            }
        });

        this.getRootPane().setDefaultButton(btnSend);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {

                try {
                    dos.writeUTF("Log out");
                    dos.flush();

                    try {
                        receiver.join();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }

                    if (dos != null) {
                        dos.close();
                    }
                    if (dis != null) {
                        dis.close();
                    }
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }

    /**
     * Luồng nhận tin nhắn từ server của mỗi client
     */
    class Receiver implements Runnable {

        private DataInputStream dis;

        public Receiver(DataInputStream dis) {
            this.dis = dis;
        }

        @Override
        public void run() {
            try {

                while (true) {
                    // Chờ tin nhắn từ server
                    String method = dis.readUTF();

                    if (method.equals("Text")) {
                        // Nhận một tin nhắn văn bản
                        String sender = dis.readUTF();
                        String message = dis.readUTF();

                        if (message.contains("@")) {
                            if (chatWindows.get(" ") != null) {
                                lbReceiver.setText(" ");
                                groupMessage(lbReceiver.getText(), message.substring(1), false);
                                Style messageStyle = messageDoc.getStyle("Message style");
                                try {
                                    // In ra tên người gửi
                                    messageDoc.insertString(messageDoc.getLength(), sender + ": ", userStyleSend);
                                    messageDoc.insertString(messageDoc.getLength(), message.substring(1) + "\n", messageStyle);
                                    txtMessage.setText("");
                                } catch (BadLocationException ex) {
                                    Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {
                                JTextPane temp = new JTextPane();
                                temp.setFont(new Font("Arial", Font.PLAIN, 14));
                                temp.setEditable(false);
                                chatWindows.put(" ", temp);
                                lbReceiver.setText(" ");
                                // In tin nhắn lên màn hình chat với người gửi
                                groupMessage(lbReceiver.getText(), message.substring(1), false);
                                Style messageStyle = messageDoc.getStyle("Message style");
                                try {
                                    // In ra tên người gửi
                                    messageDoc.insertString(messageDoc.getLength(), sender + ": ", userStyleSend);
                                    messageDoc.insertString(messageDoc.getLength(), message.substring(1) + "\n", messageStyle);
                                    txtMessage.setText("");
                                } catch (BadLocationException ex) {
                                    Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        } else {
                            // In tin nhắn lên màn hình chat với người gửi
                            newMessage(sender, message, false);
                        }
                    } else if (method.equals("Emoji")) {
                        // Nhận một tin nhắn Emoji
                        String sender = dis.readUTF();
                        String emoji = dis.readUTF();

                        // In tin nhắn lên màn hình chat với người gửi
                        newEmoji(sender, emoji, false);
                    } else if (method.equals("Online users")) {
                        // Nhận yêu cầu cập nhật danh sách người dùng trực tuyến
                        String[] users = dis.readUTF().split(",");
                        onlineUsers.removeAllItems();

                        String chatting = lbReceiver.getText();

                        boolean isChattingOnline = false;

                        for (String user : users) {
//                            group.add(user);
                            if (user.equals(username) == false) {
                                // Cập nhật danh sách các người dùng trực tuyến vào ComboBox onlineUsers (trừ bản thân)
                                onlineUsers.addItem(user);
                                if (chatWindows.get(user) == null) {
                                    JTextPane temp = new JTextPane();
                                    temp.setFont(new Font("Arial", Font.PLAIN, 14));
                                    temp.setEditable(false);
                                    chatWindows.put(user, temp);
                                }
                            }
                            if (chatting.equals(user)) {
                                isChattingOnline = true;
                            }
                            if (chatting.equals("All")) {
                                isChattingOnline = true;
                            }
                        }

                        if (isChattingOnline == false) {
                            // Nếu người đang chat không online thì chuyển hướng về màn hình mặc định và thông báo cho người dùng
                            onlineUsers.setSelectedItem(" ");
                            JOptionPane.showMessageDialog(null, chatting + " is offline!\nYou will be redirect to default chat window");
                        } else {
                            onlineUsers.setSelectedItem(chatting);
                        }

                        onlineUsers.validate();
                    } else if (method.equals("Safe to leave")) {
                        // Thông báo có thể thoát
                        break;
                    }

                }

            } catch (IOException ex) {
                System.err.println(ex);
            } finally {
                try {
                    if (dis != null) {
                        dis.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * MouseListener cho các đường dẫn tải file.
     */
    class HyberlinkListener extends AbstractAction {

        String filename;
        byte[] file;

        public HyberlinkListener(String filename, byte[] file) {
            this.filename = filename;
            this.file = Arrays.copyOf(file, file.length);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            execute();
        }

        public void execute() {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(filename));
            int rVal = fileChooser.showSaveDialog(contentPane.getParent());
            if (rVal == JFileChooser.APPROVE_OPTION) {

                // Mở file đã chọn sau đó lưu thông tin xuống file đó
                File saveFile = fileChooser.getSelectedFile();
                BufferedOutputStream bos = null;
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(saveFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // Hiển thị JOptionPane cho người dùng có muốn mở file vừa tải về không
                int nextAction = JOptionPane.showConfirmDialog(null, "Saved file to " + saveFile.getAbsolutePath() + "\nDo you want to open this file?", "Successful", JOptionPane.YES_NO_OPTION);
                if (nextAction == JOptionPane.YES_OPTION) {
                    try {
                        Desktop.getDesktop().open(saveFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (bos != null) {
                    try {
                        bos.write(this.file);
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * MouseAdapter cho các Emoji.
     */
    class IconListener extends MouseAdapter {

        String emoji;

        public IconListener(String emoji) {
            this.emoji = emoji;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (txtMessage.isEnabled() == true) {

                try {
                    dos.writeUTF("Emoji");
                    dos.writeUTF(lbReceiver.getText());
                    dos.writeUTF(this.emoji);
                    dos.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    newMessage("ERROR", "Network error!", true);
                }

                // In Emoji lên màn hình chat với người nhận
                newEmoji(username, this.emoji, true);
            }
        }
    }
}
