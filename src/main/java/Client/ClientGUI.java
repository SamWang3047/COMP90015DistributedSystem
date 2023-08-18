package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ClientGUI {
    private JFrame frame;
    private JTextArea meaningPane;
    private JTextField wordField;
    private Client client;

    public JFrame getFrame() {
        return frame;
    }

    public ClientGUI(Client client) {
        this.client = client;
        init();
    }

    private boolean isValid(String word, String meaning, int command) {
        if (word.equals("")) {
            JOptionPane.showMessageDialog(frame, "Please Enter a word.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (!isOnlyChar(word)) {
            JOptionPane.showMessageDialog(frame, "Invalid character! Please check.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (command == StateLib.ADD && meaning.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please Enter the word's meaning.", "Warning", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean isOnlyChar(String str) {
        return str.matches("^([a-zA-Z][\\-]?)*[a-zA-Z]+$");
    }

    private void init() {
        frame = new JFrame();
        frame.setMinimumSize(new Dimension(450, 340));
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        wordField = new JTextField();
        wordField.setColumns(10);

        JButton btnAdd = new JButton("ADD");
        btnAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String word = wordField.getText();
                String meaning = meaningPane.getText();
                if (isValid(word, meaning, StateLib.ADD)) {
                    int confirm = JOptionPane.showConfirmDialog(frame,  "Confirm to Add a new word?", "Confirm Window", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        int state = client.add(word, meaning);
                        if(state == StateLib.UNKNOWN_HOST) {
                            JOptionPane.showMessageDialog(frame, "Unknown Host!\nPlease restart with a correct Address and IP.",
                                    "Warning", JOptionPane.ERROR_MESSAGE);
                        } else if (state == StateLib.FAIL) {
                            JOptionPane.showMessageDialog(frame, "Word Exist!", "Warning", JOptionPane.WARNING_MESSAGE);
                        } else if (state == StateLib.TIMEOUT) {
                            JOptionPane.showMessageDialog(frame, "Timeout!\nPlease check the server or restart with a correct Address and IP.",
                                    "Warning", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Add Success!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });

        JButton btnQuery = new JButton("Query");
        btnQuery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String word = wordField.getText();
                if (isValid(word, "", StateLib.QUERY)) {
                    String[] resultArr = client.query(word);
                    int state = Integer.parseInt(resultArr[0]);
                    if (state == StateLib.UNKNOWN_HOST) {
                        JOptionPane.showMessageDialog(frame, "Unknown Host!\nPlease restart with a correct Address and IP.", "Warning", JOptionPane.ERROR_MESSAGE);
                    } else if (state == StateLib.FAIL) {
                        JOptionPane.showMessageDialog(frame, "Query Fail\nWord Not Exist!", "Warning", JOptionPane.WARNING_MESSAGE);
                    } else if (state == StateLib.TIMEOUT) {
                        JOptionPane.showMessageDialog(frame, "Timeout!\nPlease check the server or restart with a correct Address and IP.",
                                "Warning", JOptionPane.ERROR_MESSAGE);
                    } else {
                        meaningPane.setText(resultArr[1]);
                    }
                }
            }
        });

        JButton btnRemove = new JButton("Remove");
        btnRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String word = wordField.getText();
                if (isValid(word, "", StateLib.REMOVE)) {
                    int confirm = JOptionPane.showConfirmDialog(frame,  "Confirm to Remove a new word?", "Confirm Window", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        int state = client.remove(word);
                        if(state == StateLib.UNKNOWN_HOST) {
                            JOptionPane.showMessageDialog(frame, "Unknown Host!\nPlease restart with a correct Address and IP.", "Warning", JOptionPane.ERROR_MESSAGE);
                        }
                        else if (state == StateLib.FAIL) {
                            JOptionPane.showMessageDialog(frame, "Remove Fail\nWord Not Exist!", "Warning", JOptionPane.WARNING_MESSAGE);
                        } else if (state == StateLib.TIMEOUT) {
                            JOptionPane.showMessageDialog(frame, "Timeout!\nPlease check the server or restart with a correct Address and IP.",
                                    "Warning", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Remove Success!", "Tips", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });

        JLabel lblMeaning = new JLabel("The Meaning(s) of the word: ");

        JScrollPane scrollPane = new JScrollPane();

        JLabel lblWord = new JLabel("Word:");

        meaningPane = new JTextArea();
        scrollPane.setViewportView(meaningPane);
        meaningPane.setLineWrap(true);
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(5)
                                .addComponent(lblMeaning, GroupLayout.PREFERRED_SIZE, 244, GroupLayout.PREFERRED_SIZE))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(5)
                                .addComponent(lblWord, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
                        .addComponent(wordField, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(5)
                                .addComponent(btnAdd, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                .addGap(25)
                                .addComponent(btnQuery, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                .addGap(25)
                                .addComponent(btnRemove, GroupLayout.DEFAULT_SIZE, 130, Short.MAX_VALUE)
                                .addGap(5))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(5)
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 439, Short.MAX_VALUE)
                                .addContainerGap())
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(5)
                                .addComponent(lblMeaning, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                .addGap(5)
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                                .addGap(5)
                                .addComponent(lblWord, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                .addComponent(wordField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                .addGap(5)
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnQuery, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnRemove, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE))
                                .addGap(8))
        );
        frame.getContentPane().setLayout(groupLayout);
    }
}
