import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 * Group project for project 5
 *
 * @author Parth Thakre, Anthony Rodriguez, Will Greenwood, Marcelo Moreno, Ji Bing Ni
 * @version 2023-04-12
 */


public class MessagingApp extends JComponent implements Runnable {
    private MessagingApp messagingApp;
    boolean isCustomer;
    boolean isSeller;
    private JFrame frame;
    private JPanel welcomeScreen;
    private JPanel sellerLogin;
    private JPanel customerLogin;
    private JPanel customerNewAccount;
    private JPanel sellerNewAccount;
    private JPanel messagingPanel;
    private JPanel messagingLog;
    private String currentUser;
    private JButton exportToCsv;
    private JButton cStatisticsReceived;
    private JButton cStatisticsSent;
    private JTextArea username;
    private JTextArea sUsername;
    private JTextArea password;
    private JTextArea sPassword;
    private JTextArea newUsername;
    private JTextArea newPassword;
    private JTextArea newEmail;
    private JTextArea messageBox;
    private JTextArea sNewUsername;
    private JTextArea sNewPassword;
    private JTextArea sNewEmail;
    private JTextArea sNewStore;
    private Customer customer;
    private Seller seller;
    private Socket socket;
    private JButton sellerStatisticsSent;
    private JButton viewStatsButton;
    private ArrayList<Customer> customers;
    private ArrayList<Seller> sellers;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private BufferedReader reader;
    private PrintWriter writer;
    private JComboBox<String> messageList;
    private JComboBox<String> newMessageList;
    private String[] listOfUsers = new String[0];
    private String[] newChatOptions = new String[0];
    private JTextPane messagesPane;
    private boolean startingNew;
    private String sendMessageTo;
    private String total;
    private String cSentStats;
    private String sellerSentStats;
    private String unsorted;
    private JButton importFile;

    private JButton refreshButton;



    /* action listener for buttons */
    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            //System.out.println(actionCommand);
            if (actionCommand.equalsIgnoreCase("Customer")) {
                isCustomer = true;
                setCurrentPanel(customerLogin);
            }
            if (actionCommand.equalsIgnoreCase("Seller")) {
                isSeller = true;
                setCurrentPanel(sellerLogin);
            }
            if (actionCommand.equalsIgnoreCase("Login")) {
                String pw;
                JLabel messagesWith;
                JLabel newMessagesOption;
                JLabel statsDashboard;
                exportToCsv.setVisible(false);
                importFile.setVisible(false);
                refreshButton.setVisible(false);
                if (isCustomer) {
                    cStatisticsReceived.setVisible(true);
                    cStatisticsSent.setVisible(true);
                    sellerStatisticsSent.setVisible(false);
                    viewStatsButton.setVisible(false);
                    currentUser = username.getText();
                    pw = password.getText();
                    writer.println("Login Customer:" + currentUser + "," + pw);
                    writer.flush();
                    try {
                        customer = (Customer) ois.readObject();
                        listOfUsers = (String[]) ois.readObject();
                        newChatOptions = (String[]) ois.readObject();
                        if (customer == null) {
                            closeProgram();
                            JOptionPane.showMessageDialog(null, "Wrong Login!", "Error! Wrong login! Try again later."
                                    , JOptionPane.ERROR_MESSAGE);
                        } else {
                            setCurrentPanel(messagingPanel);
                        }
                    } catch (Exception y) {
                        y.printStackTrace();
                    }
                    messagesWith = new JLabel("Select Seller to Message:");
                    messagesWith.setBounds(10, 0, 180, 20);
                    newMessagesOption = new JLabel("Start Conversation with Seller: ");
                    newMessagesOption.setBounds(10, 70, 180, 20);
                    statsDashboard = new JLabel("View conversation statistics:");
                    statsDashboard.setBounds(10, 140, 180, 20);
                } else {
                    cStatisticsSent.setVisible(false);
                    cStatisticsReceived.setVisible(false);
                    sellerStatisticsSent.setVisible(true);
                    viewStatsButton.setVisible(true);
                    currentUser = sUsername.getText();
                    pw = sPassword.getText();
                    writer.println("Login Seller:" + currentUser + "," + pw);
                    writer.flush();
                    try {
                        seller = (Seller) ois.readObject();
                        listOfUsers = (String[]) ois.readObject();
                        newChatOptions = (String[]) ois.readObject();
                        if (seller == null) {
                            closeProgram();
                            JOptionPane.showMessageDialog(null, "Wrong Login!", "Error! Wrong login! Try again later."
                                    , JOptionPane.ERROR_MESSAGE);
                        } else {
                            setCurrentPanel(messagingPanel);
                        }
                    } catch (Exception y) {
                        y.printStackTrace();
                    }
                    messagesWith = new JLabel("Select Customer to Message:");
                    messagesWith.setBounds(10, 0, 180, 20);
                    newMessagesOption = new JLabel("Start Conversation with Customer: ");
                    newMessagesOption.setBounds(10, 70, 180, 20);
                    statsDashboard = new JLabel("View conversation statistics:");
                    statsDashboard.setBounds(10, 140, 180, 20);

                    JButton addStore = new JButton("Add Store");
                    addStore.setBounds(10, 421, 230, 30);
                    buttonActivation(addStore);
                    messagingPanel.add(addStore);
                }
                messagingPanel.add(messagesWith);
                setupListOfMessages(listOfUsers);
                messagingPanel.add(newMessagesOption);
                setupNewMessageOption(newChatOptions);
                messagingPanel.add(statsDashboard);
            }
            if (actionCommand.equalsIgnoreCase("Create New Account")) {
                if (isCustomer) {
                    setCurrentPanel(customerNewAccount);
                } else {
                    setCurrentPanel(sellerNewAccount);
                }
            }
            if (actionCommand.equalsIgnoreCase("Create")) {
                String pw;
                String email;
                JLabel messagesWith;
                JLabel newMessagesOption;
                JLabel statsDashboard;
                exportToCsv.setVisible(false);
                importFile.setVisible(false);
                refreshButton.setVisible(false);
                if (isCustomer) {
                    currentUser = newUsername.getText();
                    pw = newPassword.getText();
                    email = newEmail.getText();
                    writer.println("Create Customer:" + currentUser + "," + pw + "," + email);
                    writer.flush();
                    cStatisticsReceived.setVisible(true);
                    cStatisticsSent.setVisible(true);
                    sellerStatisticsSent.setVisible(false);
                    viewStatsButton.setVisible(false);
                    try {
                        customer = (Customer) ois.readObject();
                        listOfUsers = (String[]) ois.readObject();
                        newChatOptions = (String[]) ois.readObject();
                        if (customer == null) {
                            closeProgram();
                            JOptionPane.showMessageDialog(null, "Error!", "Error! Login taken! Try again later."
                                    , JOptionPane.ERROR_MESSAGE);
                        } else {
                            setCurrentPanel(messagingPanel);
                        }
                    } catch (Exception y) {
                        y.printStackTrace();
                    }
                    messagesWith = new JLabel("Select Seller to Message:");
                    messagesWith.setBounds(10, 0, 180, 20);
                    newMessagesOption = new JLabel("Start Conversation with Seller: ");
                    newMessagesOption.setBounds(10, 70, 180, 20);
                    statsDashboard = new JLabel("View conversation statistics:");
                    statsDashboard.setBounds(10, 140, 180, 20);
                } else {
                    cStatisticsReceived.setVisible(false);
                    cStatisticsSent.setVisible(false);
                    sellerStatisticsSent.setVisible(true);
                    viewStatsButton.setVisible(true);
                    currentUser = sNewUsername.getText();
                    pw = sNewPassword.getText();
                    email = sNewEmail.getText();
                    String store = sNewStore.getText();
                    writer.println("Create Seller:" + currentUser + "," + pw + "," + email + "," + store);
                    writer.flush();
                    try {
                        seller = (Seller) ois.readObject();
                        listOfUsers = (String[]) ois.readObject();
                        newChatOptions = (String[]) ois.readObject();
                        if (seller == null) {
                            closeProgram();
                            JOptionPane.showMessageDialog(null, "Error!", "Error! Login taken! Try again later."
                                    , JOptionPane.ERROR_MESSAGE);
                        } else {
                            setCurrentPanel(messagingPanel);
                        }
                    } catch (Exception y) {
                        y.printStackTrace();
                    }
                    messagesWith = new JLabel("Select Customer to Message:");
                    messagesWith.setBounds(10, 0, 180, 20);
                    newMessagesOption = new JLabel("Start Conversation with Customer: ");
                    newMessagesOption.setBounds(10, 70, 180, 20);
                    statsDashboard = new JLabel("View conversation statistics:");
                    statsDashboard.setBounds(10, 140, 180, 20);
                    JButton addStore = new JButton("Add Store");
                    addStore.setBounds(10, 421, 230, 30);
                    buttonActivation(addStore);
                    messagingPanel.add(addStore);
                }
                messagingPanel.add(messagesWith);
                setupListOfMessages(listOfUsers);
                messagingPanel.add(newMessagesOption);
                setupNewMessageOption(newChatOptions);
                messagingPanel.add(statsDashboard);
            }
            if (actionCommand.equalsIgnoreCase("Logout")) {
                closeProgram();
            }
            if (e.getSource().equals(messageList)) {
                if (messageList.getSelectedItem() != null) {
                    String line = ((String) messageList.getSelectedItem()).split(",")[0];
                    exportToCsv.setVisible(true);
                    importFile.setVisible(true);
                    refreshButton.setVisible(true);

                    if (isCustomer) {

                        if (!line.equalsIgnoreCase("No messages with sellers")) {
                            exportToCsv.setBounds(10, 421, 230, 30);
                            cStatisticsReceived.setBounds(10, 170, 230, 30);
                            cStatisticsSent.setBounds(10, 207, 230, 30);

                            setupSendingFeature();
                            sendMessageTo = line;
                            startingNew = false;
                            writer.println("Load Messages:" + line);
                            writer.flush();
                            try {
                                String messageLog = (String) ois.readObject();
                                messagesPane.setText(messageLog);
                            } catch (Exception y) {
                                y.printStackTrace();
                            }
                        }
                    } else {
                        if (!line.equalsIgnoreCase("No messages with customers")) {
                            setupSendingFeature();
                            sendMessageTo = line;
                            startingNew = false;
                            writer.println("Load Messages:" + line);
                            writer.flush();
                            try {
                                String messageLog = (String) ois.readObject();
                                messagesPane.setText(messageLog);
                            } catch (Exception y) {
                                y.printStackTrace();
                            }
                        }
                    }
                }
            }
            if (e.getSource().equals(newMessageList)) {
                if (newMessageList.getSelectedItem() != null) {
                    exportToCsv.setVisible(false);
                    importFile.setVisible(false);
                    refreshButton.setVisible(false);
                    String line = ((String) newMessageList.getSelectedItem()).split(",")[0];
                    messagesPane.setText("");
                    if (isCustomer) {
                        if (!line.equalsIgnoreCase("No new sellers to be messaged")) {
                            startingNew = true;
                            sendMessageTo = line;
                            setupSendingFeature();
                        }
                    } else {
                        if (!line.equalsIgnoreCase("No new customers to be messaged")) {
                            startingNew = true;
                            sendMessageTo = line;
                            setupSendingFeature();
                        }
                    }
                }
            }
            if (actionCommand.equalsIgnoreCase("Send")) {
                String messageToSend = messageBox.getText();
                if (startingNew) {
                    writer.println("Start New:" + messageToSend);
                } else writer.println("Send Message:" + messageToSend);
                String updatedConversation = "";
                if (isCustomer) {
                    writer.println("Seller" + "," + sendMessageTo);
                    writer.flush();
                    try {
                        updatedConversation = (String) ois.readObject();
                        if (startingNew) {
                            listOfUsers = (String[]) ois.readObject();
                            newChatOptions = (String[]) ois.readObject();
                        }
                    } catch (Exception y) {
                        y.printStackTrace();
                    }
                } else {
                    writer.println("Customer" + "," + sendMessageTo);
                    writer.flush();
                    try {
                        updatedConversation = (String) ois.readObject();
                        if (startingNew) {
                            listOfUsers = (String[]) ois.readObject();
                            newChatOptions = (String[]) ois.readObject();
                        }
                    } catch (Exception y) {
                        y.printStackTrace();
                    }
                }
                messagesPane.setText(updatedConversation);
                if (startingNew) {
                    messageList.removeAllItems();
                    for (int y = 0; y < listOfUsers.length; y++)
                        messageList.addItem(listOfUsers[y]);
                    //newMessageList.setVisible(false);
                    newMessageList.removeAllItems();
                    for (int y = 0; y < newChatOptions.length; y++)
                        newMessageList.addItem(newChatOptions[y]);
                    messagingPanel.add(newMessageList);
                    messageList.setSelectedItem(messageList.getItemAt(listOfUsers.length - 1));
                    startingNew = false;
                }
            }
            if (actionCommand.equalsIgnoreCase("Change E-mail")) {
                String newEmail = JOptionPane.showInputDialog(null,
                        "Enter new email:", "Change Email", JOptionPane.QUESTION_MESSAGE);
                if (isCustomer) {
                    writer.println("Change E-mail Customer:" + newEmail);
                    writer.flush();
                    try {
                        customer = (Customer) ois.readObject();
                    } catch (Exception y) {
                        y.printStackTrace();
                    }
                } else {
                    writer.println("Change E-mail Seller:" + newEmail);
                    writer.flush();
                    try {
                        seller = (Seller) ois.readObject();
                    } catch (Exception y) {
                        y.printStackTrace();
                    }
                }
            }
            if (actionCommand.equalsIgnoreCase("Add Store")) {
                String newStore = JOptionPane.showInputDialog(null,
                        "Store name:", "Add new Store", JOptionPane.QUESTION_MESSAGE);
                writer.println("Add Store:" + newStore);
                writer.flush();
                try {
                    seller = (Seller) ois.readObject();
                } catch (Exception y) {
                    y.printStackTrace();
                }
            }
            if (actionCommand.equalsIgnoreCase("Export to CSV")) {
                String fileName = "";
                if (isCustomer)
                    fileName = currentUser + "&" + sendMessageTo + ".txt";
                else fileName = sendMessageTo + "&" + currentUser + ".txt";
                writer.println("Export to CSV:" + fileName);
                writer.flush();
                JOptionPane.showMessageDialog(null,
                        "Exported Successfully!", "Exported Done!", JOptionPane.PLAIN_MESSAGE);
            }
            //Statistics for customer - sorted by num messages received
            if (actionCommand.equalsIgnoreCase("Sort Stats by Messages Received")) {
                if (isCustomer) {
                    writer.println("Sort Stats by Messages Received:c");
                    writer.flush();
                    try {
                        total = (String) ois.readObject();
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(null,
                            total, "Statistics", JOptionPane.PLAIN_MESSAGE);
                }
            }
            if (actionCommand.equalsIgnoreCase("Sort Stats by Messages Sent")) {
                if (isCustomer) {
                    writer.println("Sort Stats by Messages Sent: ");
                    writer.flush();
                    try {
                        cSentStats = (String) ois.readObject();
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(null,
                            cSentStats, "Statistics", JOptionPane.PLAIN_MESSAGE);
                }
            }
            if (actionCommand.equalsIgnoreCase("Edit")) {
                boolean successful = false;
                do {
                    String lineToEdit = JOptionPane.showInputDialog(null,
                            "Enter message to edit:", "Edit message", JOptionPane.QUESTION_MESSAGE);
                    String newEditedLine = JOptionPane.showInputDialog(null,
                            "Enter new message:", "Edit message", JOptionPane.QUESTION_MESSAGE);
                    writer.println("Edit Message:" + lineToEdit + "," + newEditedLine);
                    writer.println(sendMessageTo);
                    if (isCustomer)
                        writer.println("Seller");
                    else
                        writer.println("Customer");
                    writer.flush();
                    try {
                        successful = Boolean.parseBoolean(reader.readLine());
                    } catch (Exception y) {
                        y.printStackTrace();
                    }
                    if (!successful)
                        JOptionPane.showMessageDialog(null,
                                "Message can't be edited!", "Error!", JOptionPane.PLAIN_MESSAGE);
                } while (!successful);
                JOptionPane.showMessageDialog(null,
                        "Message Edited!", "Edited!", JOptionPane.PLAIN_MESSAGE);
                try {
                    String messages = (String) ois.readObject();
                    messagesPane.setText(messages);
                } catch (Exception y) {
                    y.printStackTrace();
                }
            }
            if (actionCommand.equalsIgnoreCase("Delete")) {
                boolean successful = false;
                do {
                    String lineToDelete = JOptionPane.showInputDialog(null,
                            "Enter message to delete:", "Delete message", JOptionPane.QUESTION_MESSAGE);
                    writer.println("Delete Message:" + lineToDelete);
                    writer.println(sendMessageTo);
                    if (isCustomer)
                        writer.println("Seller");
                    else
                        writer.println("Customer");
                    writer.flush();
                    try {
                        successful = Boolean.parseBoolean(reader.readLine());
                    } catch (Exception y) {
                        y.printStackTrace();
                    }
                    if (!successful)
                        JOptionPane.showMessageDialog(null,
                                "Message can't be Deleted!", "Error!", JOptionPane.PLAIN_MESSAGE);
                } while (!successful);
                JOptionPane.showMessageDialog(null,
                        "Message Deleted!", "Deleted!", JOptionPane.PLAIN_MESSAGE);
                try {
                    String messages = (String) ois.readObject();
                    messagesPane.setText(messages);
                } catch (Exception y) {
                    y.printStackTrace();
                }
            }
            if (actionCommand.equalsIgnoreCase("View Sorted Stats")) { //seller
                if (isSeller) {
                    writer.println("View Sorted Stats1: ");
                    writer.flush();
                    try {
                        sellerSentStats = (String) ois.readObject();
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(null,
                            sellerSentStats, "Statistics", JOptionPane.PLAIN_MESSAGE);
                }
            }
            if (actionCommand.equalsIgnoreCase("Stats of Messages Received")) {
                if (isSeller) {
                    writer.println("Stats of Messages Received: ");
                    writer.flush();
                    try {
                        unsorted = (String) ois.readObject();
                    } catch (IOException | ClassNotFoundException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(null,
                            unsorted, "Statistics", JOptionPane.PLAIN_MESSAGE);
                }
            }
            if (actionCommand.equalsIgnoreCase("Import and Send file")) {
                String fileName = JOptionPane.showInputDialog(frame, "Please type the " +
                        "file name to read and send:", "Import file", JOptionPane.QUESTION_MESSAGE);

                if (fileName != null && !fileName.isEmpty()) {
                    File selectedFile = new File(fileName);

                    if (selectedFile.exists()) {
                        try {
                            StringBuilder messageContent = new StringBuilder();
                            String messageLine;

                            BufferedReader bfr2 = new BufferedReader(new FileReader(selectedFile));

                            while ((messageLine = bfr2.readLine()) != null) {
                                messageContent.append(messageLine).append(" ");
                            }
                            bfr2.close();
                            writer.println("Send Message:" + messageContent);
                            if (isCustomer) {
                                writer.println("Seller" + "," + sendMessageTo);
                                writer.flush();
                                try {
                                    messagesPane.setText((String) ois.readObject());
                                } catch (Exception y) {
                                    y.printStackTrace();
                                }
                            } else {
                                writer.println("Customer" + "," + sendMessageTo);
                                writer.flush();
                                try {
                                    messagesPane.setText((String) ois.readObject());
                                } catch (Exception y) {
                                    y.printStackTrace();
                                }
                            }

                        } catch (IOException exception) {
                            exception.printStackTrace();
                            JOptionPane.showMessageDialog(frame, "There has been" +
                                    "an error when importing the file!", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "The file does not exist please type a " +
                                "valid file name.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
            if(actionCommand.equalsIgnoreCase("Refresh Chat")){
                if(isCustomer)
                    writer.println("Refresh:" + sendMessageTo + "," + "Seller");
                else
                    writer.println("Refresh:" + sendMessageTo + "," + "Customer");
                writer.flush();
                try{
                    messagesPane.setText((String) ois.readObject());
                }catch(Exception y){
                    y.printStackTrace();
                }
            }
            if(actionCommand.equalsIgnoreCase("Block User")){
                String result = "";
                do {
                    String userToBlock = JOptionPane.showInputDialog(null,
                            "Enter User you want to block:", "BLock user", JOptionPane.QUESTION_MESSAGE);
                    if (isCustomer)
                        writer.println("Block:" + userToBlock + "," + "Seller");
                    else
                        writer.println("Block:" + userToBlock + "," + "Customer");
                    writer.flush();
                    try{
                        result = reader.readLine();
                    }catch(Exception y){
                        y.printStackTrace();
                    }
                }while(result.equalsIgnoreCase("Failed"));

                if(result.equalsIgnoreCase("Done Blocking"))
                    JOptionPane.showMessageDialog(null,
                            "Blocked User!", "Blocked!", JOptionPane.PLAIN_MESSAGE);
                else JOptionPane.showMessageDialog(null,
                        "Unblocked User!", "Unblocked!", JOptionPane.PLAIN_MESSAGE);
                try{
                    listOfUsers = (String[]) ois.readObject();
                    newChatOptions = (String[]) ois.readObject();
                }catch(Exception y){
                    y.printStackTrace();
                }
                messageList.removeAllItems();
                for (int y = 0; y < listOfUsers.length; y++)
                    messageList.addItem(listOfUsers[y]);
                //newMessageList.setVisible(false);
                newMessageList.removeAllItems();
                for (int y = 0; y < newChatOptions.length; y++)
                    newMessageList.addItem(newChatOptions[y]);
                messagingPanel.add(newMessageList);
                messageList.setSelectedItem(messageList.getItemAt(listOfUsers.length - 1));
            }
        }
    };

    public MessagingApp() {
        currentUser = "";
        isCustomer = false;
        isSeller = false;
        welcomeScreen = new JPanel();
        welcomeScreen.setBounds(0, 0, 720, 590);
        welcomeScreen.setLayout(null);
        sellerLogin = new JPanel();
        sellerLogin.setBounds(0, 0, 720, 590);
        sellerLogin.setLayout(null);
        customerLogin = new JPanel();
        customerLogin.setBounds(0, 0, 720, 590);
        customerLogin.setLayout(null);
        customerNewAccount = new JPanel();
        customerNewAccount.setBounds(0, 0, 720, 590);
        customerNewAccount.setLayout(null);
        sellerNewAccount = new JPanel();
        sellerNewAccount.setBounds(0, 0, 720, 590);
        sellerNewAccount.setLayout(null);
        messagingPanel = new JPanel();
        messagingPanel.setBounds(0, 0, 590, 720);
        messagingPanel.setLayout(null);
        messagingLog = new JPanel();
        messagingLog.setLayout(null);
        messagingLog.setBounds(250, 0, 520, 590);
        messagingLog.setBackground(Color.WHITE);
        frame = new JFrame("Messaging App");
        username = new JTextArea("Username");
        password = new JTextArea("Password");
        sUsername = new JTextArea("Username");
        sPassword = new JTextArea("Password");
        newUsername = new JTextArea("Username");
        newPassword = new JTextArea("Password");
        newEmail = new JTextArea("Email");
        sNewUsername = new JTextArea("Username");
        sNewPassword = new JTextArea("Password");
        sNewEmail = new JTextArea("Email");
        sNewStore = new JTextArea("Store Name");
        messageBox = new JTextArea("Type a message to send.");
        messageBox.setBackground(Color.BLACK);
        messageBox.setForeground(Color.WHITE);
        messageBox.setBounds(10, 505, 350, 25);
        messageBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        startingNew = false;
        exportToCsv = new JButton("Export to CSV");
        exportToCsv.setBounds(10, 381, 230, 30);
        messagingPanel.add(exportToCsv);
        buttonActivation(exportToCsv);
        cStatisticsReceived = new JButton("Sort Stats by Messages Received");
        cStatisticsReceived.setBounds(10, 170, 230, 30);
        buttonActivation(cStatisticsReceived);
        messagingPanel.add(cStatisticsReceived);
        cStatisticsSent = new JButton("Sort Stats by Messages Sent");
        cStatisticsSent.setBounds(10, 207, 230, 30);
        buttonActivation(cStatisticsSent);
        messagingPanel.add(cStatisticsSent);
        sellerStatisticsSent = new JButton("View Sorted Stats");
        sellerStatisticsSent.setBounds(10, 207, 230, 30);
        buttonActivation(sellerStatisticsSent);
        messagingPanel.add(sellerStatisticsSent);
        viewStatsButton = new JButton("Stats of Messages Received");
        viewStatsButton.setBounds(10, 170, 230, 30);
        buttonActivation(viewStatsButton);
        messagingPanel.add(viewStatsButton);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new MessagingApp());
    }

    public void run() {
        try {
            establishConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame.setSize(720, 590);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        JLabel welcomeText = new JLabel("Welcome to the messaging app! Are you a Customer or a Seller?");
        welcomeText.setBounds(180, 140, 400, 50);
        JButton customerButton = new JButton("Customer");
        buttonActivation(customerButton);
        JButton sellerButton = new JButton("Seller");
        buttonActivation(sellerButton);
        customerButton.setBounds(280, 220, 150, 50);
        sellerButton.setBounds(280, 290, 150, 50);

        welcomeScreen.add(welcomeText);
        welcomeScreen.add(customerButton);
        welcomeScreen.add(sellerButton);
        frame.setContentPane(welcomeScreen);

        //customer login panel
        JButton login = new JButton("Login");
        login.setBounds(285, 250, 150, 20);
        buttonActivation(login);
        JButton newAccount = new JButton("Create New Account");
        newAccount.setBounds(285, 280, 150, 20);
        buttonActivation(newAccount);

        JLabel welcomeCustomer = new JLabel("Welcome Customer! Login or Create new account.");
        welcomeCustomer.setBounds(230, 140, 300, 50);

        username.setBounds(285, 190, 150, 20);
        password.setBounds(285, 220, 150, 20);
        customerLogin.add(welcomeCustomer);
        customerLogin.add(username);
        customerLogin.add(password);
        customerLogin.add(login);
        customerLogin.add(newAccount);

        // seller login panel
        JButton sLogin = new JButton("Login");
        sLogin.setBounds(285, 250, 150, 20);
        buttonActivation(sLogin);

        JButton sNewAccount = new JButton("Create New Account");
        buttonActivation(sNewAccount);
        sNewAccount.setBounds(285, 280, 150, 20);

        JLabel welcomeSeller = new JLabel("Welcome Seller! Login or Create new account.");
        welcomeSeller.setBounds(230, 140, 300, 50);

        sUsername.setBounds(285, 190, 150, 20);
        sPassword.setBounds(285, 220, 150, 20);
        sellerLogin.add(welcomeSeller);
        sellerLogin.add(sUsername);
        sellerLogin.add(sPassword);
        sellerLogin.add(sLogin);
        sellerLogin.add(sNewAccount);

        // new account panel for customer
        JLabel customerPrompt = new JLabel("Enter information for new account.");
        customerPrompt.setBounds(265, 140, 300, 50);
        newUsername.setBounds(285, 190, 150, 20);
        newPassword.setBounds(285, 220, 150, 20);
        newEmail.setBounds(285, 250, 150, 20);
        JButton create = new JButton("Create");
        buttonActivation(create);
        create.setBounds(285, 280, 150, 20);

        customerNewAccount.add(customerPrompt);
        customerNewAccount.add(newUsername);
        customerNewAccount.add(newPassword);
        customerNewAccount.add(newEmail);
        customerNewAccount.add(create);

        // new account panel for seller
        JLabel sellerPrompt = new JLabel("Enter information for new account.");
        sellerPrompt.setBounds(265, 140, 300, 50);
        sNewUsername.setBounds(285, 190, 150, 20);
        sNewPassword.setBounds(285, 220, 150, 20);
        sNewEmail.setBounds(285, 250, 150, 20);
        sNewStore.setBounds(285, 280, 150, 20);
        JButton sCreate = new JButton("Create");
        sCreate.setBounds(285, 310, 150, 20);
        buttonActivation(sCreate);


        sellerNewAccount.add(sellerPrompt);
        sellerNewAccount.add(sNewUsername);
        sellerNewAccount.add(sNewPassword);
        sellerNewAccount.add(sNewEmail);
        sellerNewAccount.add(sNewStore);
        sellerNewAccount.add(sCreate);

        JButton logout = new JButton("Logout");
        logout.setBounds(10, 501, 230, 30);
        buttonActivation(logout);
        messagingPanel.add(logout);
        JButton editEmail = new JButton("Change E-mail");
        editEmail.setBounds(10, 461, 230, 30);
        buttonActivation(editEmail);
        messagingPanel.add(editEmail);
        messagingPanel.add(messagingLog);

        messagesPane = new JTextPane();
        messagesPane.setEditable(false);
        messagesPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        JScrollPane scrollPane = new JScrollPane(messagesPane);
        scrollPane.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        messagingLog.add(messagesPane);
        messagingLog.add(scrollPane);
        messagesPane.setBounds(0, 0, 445, 476);
        scrollPane.setBounds(445, 0, 10, 476);
        messagingLog.add(messageBox);
        messageBox.setVisible(false);

        importFile = new JButton("Import and Send file");
        importFile.setBounds(10, 381 - 40, 230, 30);
        buttonActivation(importFile);
        messagingPanel.add(importFile);
        importFile.setVisible(false);

        refreshButton = new JButton("Refresh Chat");
        refreshButton.setBounds(10, 381 - 80, 230, 30);
        buttonActivation(refreshButton);
        messagingPanel.add(refreshButton);
        refreshButton.setVisible(false);

        JButton blockButton = new JButton("Block User");
        blockButton.setBounds(10, 381 - 120, 230, 30);
        buttonActivation(blockButton);
        messagingPanel.add(blockButton);
        blockButton.setVisible(true);
    }

    public void establishConnection() throws UnknownHostException, IOException, ClassNotFoundException {
        socket = new Socket("localhost", 4242);
        oos = new ObjectOutputStream(socket.getOutputStream());
        ois = new ObjectInputStream(socket.getInputStream());
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new PrintWriter(socket.getOutputStream());
    }

    public void closeProgram() {
        try {
            writer.println("Logout:");
            writer.flush();
            frame.setVisible(false);
            frame.dispose();
            writer.close();
            reader.close();
            oos.close();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentPanel(JPanel currentPanel) {
        frame.setContentPane(currentPanel);
        frame.invalidate();
        frame.validate();
    }

    public void buttonActivation(JButton b) {
        b.setBackground(Color.BLACK);
        b.setForeground(Color.WHITE);
        b.addActionListener(actionListener);
    }

    public void setupListOfMessages(String[] listOfUsers) {
        messageList = new JComboBox<String>(listOfUsers);
        messageList.addActionListener(actionListener);
        messageList.setBounds(10, 30, 230, 25);
        messagingPanel.add(messageList);
    }

    public void setupNewMessageOption(String[] listOfUsers) {
        newMessageList = new JComboBox<String>(listOfUsers);
        newMessageList.addActionListener(actionListener);
        newMessageList.setBounds(10, 100, 230, 25);
        messagingPanel.add(newMessageList);
    }

    public void setupSendingFeature() {
        messageBox.setVisible(true);
        JButton sendButton = new JButton("Send");
        buttonActivation(sendButton);
        sendButton.setBounds(370, 480, 80, 20);
        messagingLog.add(sendButton);
        JButton editButton = new JButton("Edit");
        buttonActivation(editButton);
        messagingLog.add(editButton);
        editButton.setBounds(370, 505, 80, 20);
        JButton deleteButton = new JButton("Delete");
        buttonActivation(deleteButton);
        messagingLog.add(deleteButton);
        deleteButton.setBounds(370, 530, 80, 20);
    }
}
