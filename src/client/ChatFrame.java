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
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;


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

        // In ra mÃ n hÃ¬nh tÃªn ngÆ°á»�i gá»­i
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

        // In ra mÃ n hÃ¬nh Emoji
        try {
            doc.insertString(doc.getLength(), "invisible text", iconStyle);
        } catch (BadLocationException e) {
        }

        // Xuá»‘ng dÃ²ng
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

        // In ra tÃªn ngÆ°á»�i gá»­i
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

        // In ra ná»™i dung tin nháº¯n
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
        setTitle("FIT CHAT");
        this.username = username;
        this.dis = dis;
        this.dos = dos;
        receiver = new Thread(new Receiver(dis));
        receiver.start();

        setDefaultLookAndFeelDecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 620, 491);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setBackground(new Color(245, 222, 179));
        setContentPane(contentPane);

        txtMessage = new JTextField();
        txtMessage.setFont(new Font("Papyrus", Font.PLAIN, 13));
//        txtMessage.setEnabled(false);
        txtMessage.setColumns(10);

        btnSend = new JButton("");
        btnSend.setToolTipText("Send to this one");
        btnSend.setBackground(new Color(255, 222, 173));
        btnSend.setForeground(new Color(255, 228, 181));
        btnSend.setEnabled(false);
//        btnSend.setVisible(true);
        btnSend.setIcon(new ImageIcon(ChatFrame.class.getResource("/icon/component/ChatFrame/paper-plane.png")));

        chatPanel = new JScrollPane();
        chatPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(240, 248, 255)));
        leftPanel.setBackground(new Color(250, 128, 114));
        
                btnSelectAll = new JButton("");
                btnSelectAll.setIcon(new ImageIcon(ChatFrame.class.getResource("/icon/component/ChatFrame/024-discussion.png")));
                btnSelectAll.setToolTipText("Send message to all ");
                btnSelectAll.setFont(new Font("OCR A Extended", Font.PLAIN, 11));
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

                                // In ra tin nháº¯n lÃªn mÃ n hÃ¬nh chat vá»›i ngÆ°á»�i nháº­n
//                        groupMessage(username, txtMessage.getText(), true);
                            }

                            if (chatWindows.get(" ") != null) {
                                lbReceiver.setText(" ");
                                groupMessage(username, txtMessage.getText(), true);
                                Style messageStyle = messageDoc.getStyle("Message style");
                                try {
                                    // In ra tÃªn ngÆ°á»�i gá»­i
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
                                // In tin nháº¯n lÃªn mÃ n hÃ¬nh chat vá»›i ngÆ°á»�i gá»­i
                                groupMessage(username, txtMessage.getText(), true);
                                Style messageStyle = messageDoc.getStyle("Message style");
                                try {
                                    // In ra tÃªn ngÆ°á»�i gá»­i
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
        
        JPanel leftPanel_1 = new JPanel();
        leftPanel_1.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(240, 248, 255)));
        leftPanel_1.setBackground(new Color(250, 128, 114));
        
        JLabel userImage_1 = new JLabel((Icon) null);
        
                JLabel userImage = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/component/ChatFrame/santa-claus.png")));
        
                JPanel panel = new JPanel();
                panel.setBackground(new Color(255, 239, 213));
                
                        JLabel lbUsername = new JLabel(this.username);
                        lbUsername.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
                        panel.add(lbUsername);
        GroupLayout gl_leftPanel_1 = new GroupLayout(leftPanel_1);
        gl_leftPanel_1.setHorizontalGroup(
        	gl_leftPanel_1.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_leftPanel_1.createSequentialGroup()
        			.addContainerGap(413, Short.MAX_VALUE)
        			.addGroup(gl_leftPanel_1.createParallelGroup(Alignment.TRAILING)
        				.addComponent(userImage_1)
        				.addComponent(panel, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(userImage)
        			.addContainerGap())
        );
        gl_leftPanel_1.setVerticalGroup(
        	gl_leftPanel_1.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_leftPanel_1.createSequentialGroup()
        			.addGroup(gl_leftPanel_1.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_leftPanel_1.createSequentialGroup()
        					.addContainerGap()
        					.addGroup(gl_leftPanel_1.createParallelGroup(Alignment.LEADING)
        						.addComponent(userImage)
        						.addComponent(userImage_1)))
        				.addGroup(gl_leftPanel_1.createSequentialGroup()
        					.addGap(12)
        					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        leftPanel_1.setLayout(gl_leftPanel_1);
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
        	gl_contentPane.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addComponent(leftPanel_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addGap(6))
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addComponent(btnSelectAll)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(txtMessage, GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(btnSend)
        					.addContainerGap())
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addComponent(leftPanel, GroupLayout.PREFERRED_SIZE, 107, GroupLayout.PREFERRED_SIZE)
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(chatPanel, GroupLayout.DEFAULT_SIZE, 485, Short.MAX_VALUE)
        					.addContainerGap())))
        );
        gl_contentPane.setVerticalGroup(
        	gl_contentPane.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addComponent(leftPanel_1, GroupLayout.PREFERRED_SIZE, 64, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
        				.addComponent(chatPanel)
        				.addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, 331, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        				.addComponent(txtMessage, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
        				.addComponent(btnSelectAll, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        				.addComponent(btnSend, GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE))
        			.addContainerGap())
        );
		String test = "" + ChatFrame.class.getResource("/icon/emoji/orange.png");
        JLabel lblNewLabel_1 = new JLabel("Active Users");
        lblNewLabel_1.setFont(new Font("PT Serif Caption", Font.BOLD, 11));

        GroupLayout gl_leftPanel = new GroupLayout(leftPanel);
        gl_leftPanel.setHorizontalGroup(
        	gl_leftPanel.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_leftPanel.createSequentialGroup()
        			.addGroup(gl_leftPanel.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_leftPanel.createSequentialGroup()
        					.addContainerGap()
        					.addComponent(onlineUsers, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE))
        				.addGroup(gl_leftPanel.createSequentialGroup()
        					.addGap(17)
        					.addComponent(lblNewLabel_1)))
        			.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        gl_leftPanel.setVerticalGroup(
        	gl_leftPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_leftPanel.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblNewLabel_1)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(onlineUsers, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(273, Short.MAX_VALUE))
        );
        onlineUsers.setBackground(new Color(245, 245, 220));
        onlineUsers.setFont(new Font("PT Sans Caption", Font.PLAIN, 11));
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
        leftPanel.setLayout(gl_leftPanel);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setBackground(new Color(255, 250, 205));
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

                // In ra tin nháº¯n lÃªn mÃ n hÃ¬nh chat vá»›i ngÆ°á»�i nháº­n
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
     * Luá»“ng nháº­n tin nháº¯n tá»« server cá»§a má»—i client
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
                    // Chá»� tin nháº¯n tá»« server
                    String method = dis.readUTF();

                    if (method.equals("Text")) {
                        // Nháº­n má»™t tin nháº¯n vÄƒn báº£n
                        String sender = dis.readUTF();
                        String message = dis.readUTF();

                        if (message.contains("@")) {
                            if (chatWindows.get(" ") != null) {
                                lbReceiver.setText(" ");
                                groupMessage(lbReceiver.getText(), message.substring(1), false);
                                Style messageStyle = messageDoc.getStyle("Message style");
                                try {
                                    // In ra tÃªn ngÆ°á»�i gá»­i
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
                                // In tin nháº¯n lÃªn mÃ n hÃ¬nh chat vá»›i ngÆ°á»�i gá»­i
                                groupMessage(lbReceiver.getText(), message.substring(1), false);
                                Style messageStyle = messageDoc.getStyle("Message style");
                                try {
                                    // In ra tÃªn ngÆ°á»�i gá»­i
                                    messageDoc.insertString(messageDoc.getLength(), sender + ": ", userStyleSend);
                                    messageDoc.insertString(messageDoc.getLength(), message.substring(1) + "\n", messageStyle);
                                    txtMessage.setText("");
                                } catch (BadLocationException ex) {
                                    Logger.getLogger(ChatFrame.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }

                        } else {
                            // In tin nháº¯n lÃªn mÃ n hÃ¬nh chat vá»›i ngÆ°á»�i gá»­i
                            newMessage(sender, message, false);
                        }
                    } else if (method.equals("Emoji")) {
                        // Nháº­n má»™t tin nháº¯n Emoji
                        String sender = dis.readUTF();
                        String emoji = dis.readUTF();

                        // In tin nháº¯n lÃªn mÃ n hÃ¬nh chat vá»›i ngÆ°á»�i gá»­i
                        newEmoji(sender, emoji, false);
                    } else if (method.equals("Online users")) {
                        // Nháº­n yÃªu cáº§u cáº­p nháº­t danh sÃ¡ch ngÆ°á»�i dÃ¹ng trá»±c tuyáº¿n
                        String[] users = dis.readUTF().split(",");
                        onlineUsers.removeAllItems();

                        String chatting = lbReceiver.getText();

                        boolean isChattingOnline = false;

                        for (String user : users) {
//                            group.add(user);
                            if (user.equals(username) == false) {
                                // Cáº­p nháº­t danh sÃ¡ch cÃ¡c ngÆ°á»�i dÃ¹ng trá»±c tuyáº¿n vÃ o ComboBox onlineUsers (trá»« báº£n thÃ¢n)
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
                            // Náº¿u ngÆ°á»�i Ä‘ang chat khÃ´ng online thÃ¬ chuyá»ƒn hÆ°á»›ng vá»� mÃ n hÃ¬nh máº·c Ä‘á»‹nh vÃ  thÃ´ng bÃ¡o cho ngÆ°á»�i dÃ¹ng
                            onlineUsers.setSelectedItem(" ");
                            JOptionPane.showMessageDialog(null, chatting + " is offline!\nYou will be redirect to default chat window");
                        } else {
                            onlineUsers.setSelectedItem(chatting);
                        }

                        onlineUsers.validate();
                    } else if (method.equals("Safe to leave")) {
                        // ThÃ´ng bÃ¡o cÃ³ thá»ƒ thoÃ¡t
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
     * MouseListener cho cÃ¡c Ä‘Æ°á»�ng dáº«n táº£i file.
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

                // Má»Ÿ file Ä‘Ã£ chá»�n sau Ä‘Ã³ lÆ°u thÃ´ng tin xuá»‘ng file Ä‘Ã³
                File saveFile = fileChooser.getSelectedFile();
                BufferedOutputStream bos = null;
                try {
                    bos = new BufferedOutputStream(new FileOutputStream(saveFile));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                // Hiá»ƒn thá»‹ JOptionPane cho ngÆ°á»�i dÃ¹ng cÃ³ muá»‘n má»Ÿ file vá»«a táº£i vá»� khÃ´ng
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
}
