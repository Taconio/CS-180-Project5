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
    private String currentUser;

    private JTextArea username;
    private JTextArea sUsername;
    private JTextArea password;
    private JTextArea sPassword;
    private JTextArea newUsername;
    private JTextArea newPassword;
    private JTextArea newEmail;
    private JTextArea sNewUsername;
    private JTextArea sNewPassword;
    private JTextArea sNewEmail;
    private JTextArea sNewStore;
    private Customer customer;
    private Seller seller;
    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private BufferedReader reader;
    private PrintWriter writer;
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
                if (isCustomer) {
                    currentUser = username.getText();
                    pw = password.getText();
                    writer.println("Login Customer:" + currentUser + "," + pw);
                    writer.flush();
                    try {
                        customer = (Customer) ois.readObject();
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
                } else {
                    currentUser = sUsername.getText();
                    pw = sPassword.getText();
                    writer.println("Login Seller:" + currentUser + "," + pw);
                    writer.flush();
                    try {
                        seller = (Seller) ois.readObject();
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
                }
                setupListOfMessages();
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
                if (isCustomer) {
                    currentUser = newUsername.getText();
                    pw = newPassword.getText();
                    email = newEmail.getText();
                    writer.println("Create Customer:" + currentUser + "," + pw + "," + email);
                    writer.flush();
                    try {
                        customer = (Customer) ois.readObject();
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
                } else {
                    currentUser = sNewUsername.getText();
                    pw = sNewPassword.getText();
                    email = sNewEmail.getText();
                    String store = sNewStore.getText();
                    writer.println("Create Seller:" + currentUser + "," + pw + "," + email + "," + store);
                    writer.flush();
                    try {
                        seller = (Seller) ois.readObject();
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
                }
            }
            if (actionCommand.equalsIgnoreCase("Logout")) {
                closeProgram();
            }
        }
    };

    public MessagingApp() {
        currentUser = "";
        isCustomer = false;
        isSeller = false;
        welcomeScreen = new JPanel();
        welcomeScreen.setBounds(0, 0, 720, 576);
        welcomeScreen.setLayout(null);
        sellerLogin = new JPanel();
        sellerLogin.setBounds(0, 0, 720, 576);
        sellerLogin.setLayout(null);
        customerLogin = new JPanel();
        customerLogin.setBounds(0, 0, 720, 576);
        customerLogin.setLayout(null);
        customerNewAccount = new JPanel();
        customerNewAccount.setBounds(0, 0, 720, 576);
        customerNewAccount.setLayout(null);
        sellerNewAccount = new JPanel();
        sellerNewAccount.setBounds(0, 0, 720, 576);
        sellerNewAccount.setLayout(null);
        messagingPanel = new JPanel();
        messagingPanel.setBounds(0, 0, 576, 720);
        messagingPanel.setLayout(null);
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
        /* set up JFrame */
        //messagingApp = new MessagingApp();
        //frame.add(messagingApp, BorderLayout.CENTER);
        //Container content = frame.getContentPane();
        //content.setLayout(new BorderLayout());
        //messagingApp = new MessagingApp();
        //content.add(messagingApp, BorderLayout.CENTER);

        frame.setSize(720, 576);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // welcome screen

        JLabel welcomeText = new JLabel("Welcome to the messaging app! Are you a Customer or a Seller?");
        welcomeText.setBounds(180, 140, 400, 50);
        JButton customerButton = new JButton("Customer");
        buttonActivation(customerButton);
        JButton sellerButton = new JButton("Seller");
        buttonActivation(sellerButton);
        customerButton.setBounds(280, 220, 150, 50);
        sellerButton.setBounds(280, 290, 150, 50);

        //welcomeScreen.setBounds(0,0,720,576);
        //welcomeScreen.add(welcomeText);
        welcomeScreen.add(welcomeText);
        welcomeScreen.add(customerButton);
        welcomeScreen.add(sellerButton);
        frame.setContentPane(welcomeScreen);
        //welcomeScreen.setVisible(true);


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
        logout.setBounds(10, 496, 180, 30);
        buttonActivation(logout);
        messagingPanel.add(logout);
        JPanel messagingLog = new JPanel();
        messagingLog.setBounds(200, 0, 570, 576);
        messagingLog.setBackground(Color.WHITE);
        messagingPanel.add(messagingLog);


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

    public void setupListOfMessages() {
        String[] userMessages;
        JLabel messagesWith;
        if (isCustomer) {
            messagesWith = new JLabel("Select Seller to Message:");
            messagesWith.setBounds(10, 0, 180, 20);
            userMessages = customer.getMessagedSellers();
        } else {
            messagesWith = new JLabel("Select Customer to Message:");
            messagesWith.setBounds(10, 0, 180, 20);
            userMessages = seller.getMessagedCustomers();
        }
        JComboBox<String> messageList = new JComboBox<String>(userMessages);
        messageList.addActionListener(actionListener);
        messageList.setBounds(10, 30, 180, 25);
        messagingPanel.add(messageList);
        messagingPanel.add(messagesWith);
    }
}
