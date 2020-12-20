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
        setTitle("DISCUSSION ");
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
        contentPane.setBackground(new Color(255, 250, 250));
        setContentPane(contentPane);

        JPanel header = new JPanel();
        header.setBackground(new Color(230, 230, 250));

        txtMessage = new JTextField();
        txtMessage.setFont(new Font("OCR A Extended", Font.PLAIN, 11));
//        txtMessage.setEnabled(false);
        txtMessage.setColumns(10);

        btnSend = new JButton("");
        btnSend.setBackground(new Color(0, 0, 0));
        btnSend.setForeground(new Color(245, 222, 179));
        btnSend.setEnabled(false);
//        btnSend.setVisible(true);
        btnSend.setIcon(new ImageIcon(ChatFrame.class.getResource("/icon/component/send.png")));

        chatPanel = new JScrollPane();
        chatPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(new MatteBorder(2, 2, 2, 2, (Color) new Color(240, 248, 255)));
        leftPanel.setBackground(new Color(230, 230, 250));
        
                btnSelectAll = new JButton("Select All");
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
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
        	gl_contentPane.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        						.addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
        						.addGroup(gl_contentPane.createSequentialGroup()
        							.addGap(21)
        							.addComponent(btnSelectAll)))
        					.addPreferredGap(ComponentPlacement.UNRELATED)
        					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
        							.addComponent(txtMessage, GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
        							.addGap(18)
        							.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
        							.addGap(6))
        						.addComponent(chatPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)))
        				.addComponent(header, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 550, Short.MAX_VALUE)))
        );
        gl_contentPane.setVerticalGroup(
        	gl_contentPane.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_contentPane.createSequentialGroup()
        			.addComponent(header, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addComponent(chatPanel, GroupLayout.DEFAULT_SIZE, 301, Short.MAX_VALUE)
        					.addGap(11))
        				.addGroup(gl_contentPane.createSequentialGroup()
        					.addComponent(leftPanel, GroupLayout.DEFAULT_SIZE, 306, Short.MAX_VALUE)
        					.addPreferredGap(ComponentPlacement.RELATED)))
        			.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
        				.addComponent(txtMessage, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
        				.addComponent(btnSend, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
        				.addComponent(btnSelectAll))
        			.addContainerGap())
        );
		String test = "" + ChatFrame.class.getResource("/icon/emoji/orange.png");

        JLabel userImage = new JLabel(new ImageIcon(ChatFrame.class.getResource("/icon/component/user.png")));

        JPanel panel = new JPanel();
        panel.setBackground(new Color(230, 240, 247));
        JLabel lblNewLabel_1 = new JLabel("CHOOSE PARTNER ");
        lblNewLabel_1.setFont(new Font("OCR A Extended", Font.BOLD, 11));

        GroupLayout gl_leftPanel = new GroupLayout(leftPanel);
        gl_leftPanel.setHorizontalGroup(
        	gl_leftPanel.createParallelGroup(Alignment.TRAILING)
        		.addGroup(gl_leftPanel.createSequentialGroup()
        			.addGap(25)
        			.addComponent(userImage, GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
        			.addGap(25))
        		.addGroup(gl_leftPanel.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(panel, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
        			.addContainerGap())
        		.addGroup(gl_leftPanel.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
        			.addContainerGap())
        		.addGroup(Alignment.LEADING, gl_leftPanel.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(onlineUsers, 0, 180, Short.MAX_VALUE)
        			.addContainerGap())
        );
        gl_leftPanel.setVerticalGroup(
        	gl_leftPanel.createParallelGroup(Alignment.LEADING)
        		.addGroup(gl_leftPanel.createSequentialGroup()
        			.addGap(5)
        			.addComponent(userImage)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(panel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
        			.addGap(36)
        			.addComponent(lblNewLabel_1)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(onlineUsers, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(145, Short.MAX_VALUE))
        );
        onlineUsers.setFont(new Font("OCR A Extended", Font.PLAIN, 11));
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

        JLabel lbUsername = new JLabel(this.username);
        lbUsername.setFont(new Font("Comic Sans MS", Font.BOLD, 15));
        panel.add(lbUsername);
        leftPanel.setLayout(gl_leftPanel);

        JLabel headerContent = new JLabel("CONVERSATION BOX");
        headerContent.setFont(new Font("OCR A Extended", Font.BOLD, 24));
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
