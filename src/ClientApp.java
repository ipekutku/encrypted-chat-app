import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;

public class ClientApp extends JFrame {

        private static final long serialVersionUID = 1L;
        private Client client;
        private String input_message;
        private String cipher_text;

        /**
         * Creates new form ClientApp
         */
        public ClientApp() {
                initComponents();
        }

        public static void main(String args[]) {
                try {
                        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                                if ("Nimbus".equals(info.getName())) {
                                UIManager.setLookAndFeel(info.getClassName());
                                break;
                                }
                }
                } catch (ClassNotFoundException ex) {
                        java.util.logging.Logger.getLogger(ClientApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (InstantiationException ex) {
                        java.util.logging.Logger.getLogger(ClientApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                        java.util.logging.Logger.getLogger(ClientApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                        java.util.logging.Logger.getLogger(ClientApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                /* Create and display the form */
                EventQueue.invokeLater(new Runnable() {
                public void run() {
                        new ClientApp().setVisible(true);
                }
                });
        }

        private void disconnectButtonActionPerformed(ActionEvent evt) {// GEN-FIRST:event_disconnectButtonActionPerformed
                client.setConnectionInfo(false);
                client.getQueue().offer("!exit");
                disconnectButton.setEnabled(false);
                EncryptButton.setEnabled(false);
                connectButton.setEnabled(true);
                userInputMessage.setText(null);
                cryptedTextArea.setText(null);
                serverDisplayTextArea.setText(null);
                connectionStatus.setText("Not Connected: ---");
        }// GEN-LAST:event_disconnectButtonActionPerformed

        private void connectButtonActionPerformed(ActionEvent evt) throws Exception {// GEN-FIRST:event_connectButtonActionPerformed

                String userName = JOptionPane.showInputDialog(this, "Enter User Name:");
                if (userName != null) {
                        client = new Client();
                        client.setConnectionInfo(true);
                        client.enterUsername(userName);
                        connectionStatus.setText(" Connected: " + userName);
                        connectButton.setEnabled(false);
                        disconnectButton.setEnabled(true);
                        EncryptButton.setEnabled(true);
                        client.sendMessage();
                        client.receiveMessage(serverDisplayTextArea);
                }

        }// GEN-LAST:event_connectButtonActionPerformed

        private void EncryptButtonActionPerformed(ActionEvent evt) throws Exception {// GEN-FIRST:event_EncryptButtonActionPerformed
                input_message = userInputMessage.getText();
                if (input_message != null) {
                if (aesRadioButton.isSelected() && cbcRadioButton.isSelected()) {
                        client.setMethod(true);
                        client.setMode(true);
                } else if (aesRadioButton.isSelected() && ofbRadioButton.isSelected()) {
                        client.setMethod(true);
                        client.setMode(false);
                } else if (desRadioButton.isSelected() && cbcRadioButton.isSelected()) {
                        client.setMethod(false);
                        client.setMode(true);
                } else if (desRadioButton.isSelected() && ofbRadioButton.isSelected()) {
                        client.setMethod(false);
                        client.setMode(false);
                } else {
                        JOptionPane.showMessageDialog(this, "Encryption Method or Mode is not selected!");
                        return;
                }
                cipher_text = client.encryptMessage(input_message);
                cryptedTextArea.setText(cipher_text);
                SendButton.setEnabled(true);
                }
        }// GEN-LAST:event_EncryptButtonActionPerformed

        private void SendButtonActionPerformed(ActionEvent evt) {
                client.getQueue().offer(cipher_text);
                userInputMessage.setText(null);
                cryptedTextArea.setText(null);
                SendButton.setEnabled(false);
        }

        private void initComponents() {

                methods = new ButtonGroup();
                modes = new ButtonGroup();
                jPanel1 = new JPanel();
                jPanel3 = new JPanel();
                jSeparator1 = new JSeparator();
                cbcRadioButton = new JRadioButton();
                ofbRadioButton = new JRadioButton();
                jPanel2 = new JPanel();
                aesRadioButton = new JRadioButton();
                desRadioButton = new JRadioButton();
                jSeparator2 = new JSeparator();
                connectButton = new JButton();
                disconnectButton = new JButton();
                jLabel1 = new JLabel();
                jSeparator3 = new JSeparator();
                TextBottom = new JPanel();
                jScrollPane2 = new JScrollPane();
                userInputMessage = new JTextArea();
                CryptedPanel = new JPanel();
                jScrollPane3 = new JScrollPane();
                cryptedTextArea = new JTextArea();
                EncryptButton = new JButton();
                SendButton = new JButton();
                connectionStatus = new JTextField();
                showMessageField = new JPanel();
                jLabel2 = new JLabel();
                jScrollPane1 = new JScrollPane();
                serverDisplayTextArea = new JTextArea();

                methods.add(aesRadioButton);
                methods.add(desRadioButton);

                modes.add(ofbRadioButton);
                modes.add(cbcRadioButton);

                //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                setBackground(new Color(255, 255, 255));
                addWindowListener(new java.awt.event.WindowAdapter(){
                        @Override
                        public void windowClosing(java.awt.event.WindowEvent e) {
                            if(client!=null){
                                client.setConnectionInfo(false);
                                client.getQueue().offer("!exit");
                            }
                            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                        }
                });


                jPanel3.setBorder(BorderFactory.createTitledBorder(null, "Mode", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));

                jSeparator1.setOrientation(SwingConstants.VERTICAL);

                cbcRadioButton.setText("CBC");
                ofbRadioButton.setText("OFB");

                GroupLayout jPanel3Layout = new GroupLayout(jPanel3);
                jPanel3.setLayout(jPanel3Layout);
                jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addContainerGap(12, Short.MAX_VALUE).addComponent(ofbRadioButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jSeparator1, GroupLayout.PREFERRED_SIZE,GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cbcRadioButton).addGap(16, 16, 16)));
                jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator1)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(cbcRadioButton).addComponent(ofbRadioButton))
                                .addContainerGap(13, Short.MAX_VALUE)));

                jPanel2.setBorder(BorderFactory.createTitledBorder(null, "Method",
                        TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION));
                aesRadioButton.setText("AES");
                desRadioButton.setText("DES");
                jSeparator2.setOrientation(SwingConstants.VERTICAL);

                GroupLayout jPanel2Layout = new GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup().addGap(19, 19, 19).addComponent(aesRadioButton)
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator2, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                                .addComponent(desRadioButton).addContainerGap()));
                jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(aesRadioButton).addComponent(desRadioButton))
                                .addContainerGap(13, Short.MAX_VALUE))
                                .addComponent(jSeparator2));

                connectButton.setBackground(new Color(255, 255, 255));
                connectButton.setText("Connect");
                connectButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                                try {
                                connectButtonActionPerformed(evt);
                                } catch (Exception e) {
                                e.printStackTrace();
                                }
                        }
                });

                disconnectButton.setBackground(new Color(255, 255, 255));
                disconnectButton.setText("Disconnect");
                disconnectButton.setEnabled(false);
                disconnectButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                                disconnectButtonActionPerformed(evt);
                        }
                });

                jLabel1.setDisplayedMnemonic('S');
                jLabel1.setFont(new Font("Tahoma", 1, 13)); // NOI18N
                jLabel1.setLabelFor(this);
                jLabel1.setText("Server");

                GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup().addGap(37, 37, 37)
                                .addComponent(connectButton, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18).addComponent(disconnectButton, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel1))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(33, 33, 33))
                                .addComponent(jSeparator3));

                jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout
                                .createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11))
                                .addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel1).addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(disconnectButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
                                .addComponent(connectButton, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)))
                                .addComponent(jSeparator3, GroupLayout.DEFAULT_SIZE, 11, Short.MAX_VALUE)));

                TextBottom.setBorder(BorderFactory.createTitledBorder(null, "Text", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));

                userInputMessage.setColumns(20);
                userInputMessage.setRows(5);
                userInputMessage.setLineWrap(true);

                jScrollPane2.setViewportView(userInputMessage);

                GroupLayout TextBottomLayout = new GroupLayout(TextBottom);
                TextBottom.setLayout(TextBottomLayout);
                TextBottomLayout.setHorizontalGroup(TextBottomLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane2, GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE));
                TextBottomLayout.setVerticalGroup(TextBottomLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(GroupLayout.Alignment.TRAILING, TextBottomLayout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)));

                CryptedPanel.setBorder(BorderFactory.createTitledBorder(null, "Crypted Text", TitledBorder.LEADING, TitledBorder.DEFAULT_POSITION));

                cryptedTextArea.setColumns(20);
                cryptedTextArea.setRows(5);
                jScrollPane3.setViewportView(cryptedTextArea);
                cryptedTextArea.setEditable(false);
                cryptedTextArea.setLineWrap(true);

                GroupLayout CryptedPanelLayout = new GroupLayout(CryptedPanel);
                CryptedPanel.setLayout(CryptedPanelLayout);

                CryptedPanelLayout.setHorizontalGroup(CryptedPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane3, GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE));
                CryptedPanelLayout.setVerticalGroup(CryptedPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(CryptedPanelLayout.createSequentialGroup()
                                .addComponent(jScrollPane3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));

                EncryptButton.setBackground(new Color(230, 230, 230));
                EncryptButton.setText("Encrypt");
                EncryptButton.setEnabled(false);
                EncryptButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                                try {
                                        EncryptButtonActionPerformed(evt);
                                }catch (Exception e) {
                                        e.printStackTrace();
                                }
                        }
                });

                SendButton.setBackground(new Color(237, 231, 231));
                SendButton.setText("Send");
                SendButton.setEnabled(false);
                SendButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent evt) {
                                SendButtonActionPerformed(evt);
                        }
                });

                connectionStatus.setBackground(new Color(240, 240, 240));
                connectionStatus.setFont(new Font("Tahoma", 1, 13)); // NOI18N
                connectionStatus.setText("Not Connected ---");
                connectionStatus.setEditable(false);
                connectionStatus.setBorder(null);
                connectionStatus.setDisabledTextColor(new Color(255, 255, 255));

                showMessageField.setBackground(new Color(255, 255, 255));

                serverDisplayTextArea.setColumns(20);
                serverDisplayTextArea.setRows(5);
                jScrollPane1.setViewportView(serverDisplayTextArea);
                serverDisplayTextArea.setText("MESSAGES:");
                serverDisplayTextArea.setEditable(false);

                GroupLayout showMessageFieldLayout = new GroupLayout(showMessageField);
                showMessageField.setLayout(showMessageFieldLayout);
                showMessageFieldLayout.setHorizontalGroup(showMessageFieldLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(showMessageFieldLayout.createSequentialGroup().addGap(576, 576, 576).addComponent(jLabel2)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(GroupLayout.Alignment.TRAILING,showMessageFieldLayout.createSequentialGroup().addComponent(jScrollPane1).addContainerGap()));
                showMessageFieldLayout.setVerticalGroup(showMessageFieldLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(showMessageFieldLayout.createSequentialGroup().addContainerGap().addComponent(jLabel2)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 502, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap()));

                GroupLayout layout = new GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,Short.MAX_VALUE)
                                .addComponent(showMessageField, GroupLayout.Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                .addComponent(TextBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(CryptedPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18).addComponent(EncryptButton)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(SendButton, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                                .addComponent(connectionStatus)).addContainerGap()));

                layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(showMessageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout
                                .createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addComponent(TextBottom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(CryptedPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18))
                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(EncryptButton).addComponent(SendButton))
                                .addGap(29, 29, 29)))
                                .addComponent(connectionStatus, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)));

                pack();
        }// </editor-fold>//GEN-END:initComponents

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private JPanel CryptedPanel;
        private JButton EncryptButton;
        private JButton SendButton;
        private JPanel TextBottom;
        private JRadioButton aesRadioButton;
        private JRadioButton cbcRadioButton;
        private JButton connectButton;
        private JTextArea cryptedTextArea;
        private JRadioButton desRadioButton;
        private JButton disconnectButton;
        private JLabel jLabel1;
        private JLabel jLabel2;
        private JPanel jPanel1;
        private JPanel jPanel2;
        private JPanel jPanel3;
        private JScrollPane jScrollPane1;
        private JScrollPane jScrollPane2;
        private JScrollPane jScrollPane3;
        private JSeparator jSeparator1;
        private JSeparator jSeparator2;
        private JSeparator jSeparator3;
        private JTextField connectionStatus;
        private ButtonGroup methods;
        private ButtonGroup modes;
        private JRadioButton ofbRadioButton;
        private JTextArea serverDisplayTextArea;
        private JPanel showMessageField;
        private JTextArea userInputMessage;
        // End of variables declaration//GEN-END:variables
}
