import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

public class GUI {
    static int newAcc;
    Scanner scanner = new Scanner(System.in);
    static String currentUser = "";
    static int userChoice;

    boolean loggedIn = false;
    static String cont;
    static String sendMessageTo;
    static JFrame jf;
    static JLabel welcomeDisplay;
    static JLabel breakoutDisplay;
    static JPanel welcomePanel;
    static JPanel breakoutPanel;
    static JPanel customerOrSellerPanel;
    static JPanel haveAccountPanel;
    static JPanel newCustomerPanel;
    static JLabel newCustomerUserNameLabel;
    static JLabel newCustomerPasswordLabel;
    static JLabel newCustomerEmailLabel;
    static JTextField newCustomerUserNameTextField;
    static JTextField newCustomerPasswordTextField;
    static JTextField newCustomerEmailTextField;

    static JPanel newSellerPanel;

    static JButton continueBtn;
    static JButton yesBtn;
    static JButton noBtn;

    static JButton haveAccountNoBtn;
    static JButton haveAccountYesBtn;

    static JButton customerBtn;
    static JButton sellerBtn;
    static JLabel customerOrSellerLabel;
    static JLabel haveAccountLabel;

    static boolean breakOut;
    public static void main(String[] args) {
        // jFrame
        jf = new JFrame("Messaging System");
        breakOut = false;

        // welcomeDisplay
        welcomePanel = new JPanel();
        welcomeDisplay = new JLabel("Welcome to the messaging system");
        continueBtn = new JButton("Continue");
        continueBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomePanel.setVisible(false);
                breakoutDisplay();
                customerOrUserQuestionDisplay();
                System.out.println("user: " + userChoice);
                // ================= Customer Functionality ================= //
                if (userChoice == 1) {
                    haveAccountDisplay();
                    // login for customer
                    if (newAcc == 1) {
                        loginCustomerDisplay();
                    }
                    // new account
                    if (newAcc == 2) {
                        System.out.println("hit1");
                        newCustomerDisplay();
                    }
                    // ================= Seller Functionality ================= //
                } else {
                    haveAccountDisplay();
                    // login for seller
                    if (newAcc == 1) {
                        loginSellerDisplay();
                    }
                    // new account
                    if (newAcc == 2) {
                        newSellerDisplay();
                    }
                }
            }
        });
        welcomePanel.setLayout(new GridBagLayout());
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        welcomePanel.add(welcomeDisplay);
        welcomePanel.add(continueBtn);

        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setSize(720, 576);
        jf.add(welcomePanel);
        jf.setVisible(true);
        jf.setLocationRelativeTo(null);
    }
    public static void breakoutDisplay() {
        breakoutPanel = new JPanel();
        breakoutPanel.setLayout(new GridBagLayout());
        breakoutDisplay = new JLabel("Would you like to use this system?");
        noBtn = new JButton("No");
        yesBtn = new JButton("Yes");
        yesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                breakoutPanel.setVisible(false);
                customerOrUserQuestionDisplay();
            }
        });
        noBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Quit!");
                breakOut = true;
                jf.dispose();
            }
        });
        breakoutPanel.add(breakoutDisplay);
        breakoutPanel.add(yesBtn);
        breakoutPanel.add(noBtn);
        breakoutPanel.setVisible(true);
        jf.add(breakoutPanel);
        System.out.println("break");
    }
    public static void customerOrUserQuestionDisplay() {
        // customer / user display
        customerOrSellerPanel = new JPanel();
        customerOrSellerLabel = new JLabel("Are you a customer or seller?");
        customerOrSellerPanel.setLayout(new GridBagLayout());
        customerBtn = new JButton("Customer");
        sellerBtn = new JButton("Seller");
        customerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerOrSellerPanel.setVisible(false);
                userChoice = 1;
                System.out.println("user choice: " + userChoice);
                System.out.println("customer");
            }
        });
        sellerBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                customerOrSellerPanel.setVisible(false);
                userChoice = 2;
                System.out.println("user choice: " + userChoice);
                System.out.println("seller");
            }
        });
        customerOrSellerPanel.add(customerOrSellerLabel);
        customerOrSellerPanel.add(customerBtn);
        customerOrSellerPanel.add(sellerBtn);
        customerOrSellerPanel.setVisible(true);
        jf.add(customerOrSellerPanel);
        jf.setVisible(true);
    }

    public static void haveAccountDisplay() {
        haveAccountPanel = new JPanel();
        haveAccountPanel.setLayout(new GridBagLayout());
        haveAccountLabel = new JLabel("Do you already have an account");
        haveAccountYesBtn = new JButton("Yes");
        haveAccountNoBtn = new JButton("No");
        haveAccountNoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("noooo");
                newAcc = 2;
            }
        });
        haveAccountYesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("yesssss");
                newAcc = 1;

            }
        });
        haveAccountPanel.add(haveAccountLabel);
        haveAccountPanel.add(haveAccountYesBtn);
        haveAccountPanel.add(haveAccountNoBtn);
        haveAccountPanel.setVisible(true);
        jf.add(haveAccountPanel);
    }
    public static void newCustomerDisplay() {
        // userName display
        System.out.println("hit!");
        newCustomerPanel = new JPanel();
        newCustomerUserNameLabel = new JLabel("Please enter a username");
        newCustomerUserNameTextField = new JTextField();
        newCustomerPasswordLabel = new JLabel("Please enter a password");
        newCustomerPasswordTextField = new JTextField();
        newCustomerEmailLabel = new JLabel("Please enter an email");
        newCustomerEmailTextField = new JTextField();
        newCustomerPanel.add(newCustomerUserNameLabel);
        newCustomerPanel.add(newCustomerUserNameTextField);
        newCustomerPanel.add(newCustomerPasswordLabel);
        newCustomerPanel.add(newCustomerPasswordTextField);
        newCustomerPanel.add(newCustomerEmailLabel);
        newCustomerPanel.add(newCustomerEmailTextField);
        newCustomerPanel.setVisible(true);
        jf.add(newCustomerPanel);

        currentUser = JOptionPane.showInputDialog(null, "Please enter a username",
                "cUserName", JOptionPane.QUESTION_MESSAGE);

        // password display
        String password  = JOptionPane.showInputDialog(null, "Please enter a password",
                "cPassword", JOptionPane.QUESTION_MESSAGE);

        // email display
        String email  = JOptionPane.showInputDialog(null, "Please enter an email",
                "cEmail", JOptionPane.QUESTION_MESSAGE);
    }
    public static void newSellerDisplay() {
        // userName display
        currentUser = JOptionPane.showInputDialog(null, "Please enter a username",
                "sUserName", JOptionPane.QUESTION_MESSAGE);

        // password display
        String password  = JOptionPane.showInputDialog(null, "Please enter a password",
                "sPassword", JOptionPane.QUESTION_MESSAGE);

        // email display
        String email  = JOptionPane.showInputDialog(null, "Please enter an email",
                "sEmail", JOptionPane.QUESTION_MESSAGE);

        String storeName  = JOptionPane.showInputDialog(null, "Please enter an store name",
                "sStore", JOptionPane.QUESTION_MESSAGE);

    }

    public static void loginCustomerDisplay() {
        String userName = JOptionPane.showInputDialog(null, "Please enter you username",
                "cUserName", JOptionPane.QUESTION_MESSAGE);

        String password = JOptionPane.showInputDialog(null, "Please enter you password",
                "cPassword", JOptionPane.QUESTION_MESSAGE);

    }
    public static void loginSellerDisplay() {
        String userName = JOptionPane.showInputDialog(null, "Please enter you username",
                "sUserName", JOptionPane.QUESTION_MESSAGE);

        String password = JOptionPane.showInputDialog(null, "Please enter you password",
                "sPassword", JOptionPane.QUESTION_MESSAGE);
    }
    public static void errorMessageDisplay() {
        JOptionPane.showInputDialog(null, "Error! Run the program again to try again!",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void choiceOfActionDisplay() {
        userChoice = Integer.parseInt(JOptionPane.showInputDialog(null, "Please enter your choice of action\n" +
                        "1. Message sellers\n" +
                        "2. Block customer/Unblock customer\n" +
                        "3. Log out",
                "choiceOfAction", JOptionPane.QUESTION_MESSAGE));

    }
    public static void choiceOfActionDisplayP2() {
        cont = JOptionPane.showInputDialog(null, "Choose next action:\n" +
                        "1. Sort by messages received\n" +
                        "2. Sort by messages sent\n" +
                        "3. Continue to message sellers",
                "choiceOfActionP2", JOptionPane.QUESTION_MESSAGE);
    }

    public static void typeNameOfSellerDisplay() {
        sendMessageTo = JOptionPane.showInputDialog(null, "Type name of Seller to open chat.\n" +
                        "Or start new conversation Type \"new\" ",
                "choiceOfActionP2", JOptionPane.QUESTION_MESSAGE);
    }
    public static void typeNameOfCustomerDisplay() {
        sendMessageTo = JOptionPane.showInputDialog(null, "Type name of Customer to open chat.\n" +
                        "Or start new conversation Type \"new\" ",
                "choiceOfActionP2", JOptionPane.QUESTION_MESSAGE);
    }
}
