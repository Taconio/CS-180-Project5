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

public class MessagingServer {
    private HashMap<String, ArrayList<String>> messages;
    private String currentUser;
    private Customer customer;
    private Seller seller;
    private ArrayList<String> oppositeUsers;
    private ArrayList<Customer> customers;
    private ArrayList<Seller> sellers;
    private ArrayList<Seller> blockedSellers;
    private ArrayList<Customer> blockedCustomers;
    private Scanner scanner;
    public MessagingServer() {
        currentUser = "";
        messages = new HashMap<String, ArrayList<String>>();
        oppositeUsers = new ArrayList<String>();
        customers = new ArrayList<Customer>();
        sellers = new ArrayList<Seller>();
        blockedSellers = new ArrayList<Seller>();
        blockedCustomers = new ArrayList<Customer>();
        scanner = new Scanner(System.in);
        customer = null;
        seller = null;
    }
    public static void main(String [] args){
        MessagingServer ms = new MessagingServer();
        try{
            ms.startProgram();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public void startProgram() throws UnknownHostException, IOException, ClassNotFoundException{
        ServerSocket serverSocket = new ServerSocket(4242); //port 4242 works
        Socket socket = serverSocket.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        while(true){
            String line = reader.readLine();
            String action = line.substring(0, line.indexOf(":"));
            if(action.equalsIgnoreCase("Login Customer")){
                String [] userInfo = line.substring(line.indexOf(":")+1).split(",");
                boolean successful = loginCustomer(userInfo[0], userInfo[1]);
                if(successful == false){
                    Customer x = null;
                }
                oos.writeObject(customer);
                oos.flush();
            }
            if(action.equalsIgnoreCase("Login Seller")){
                String [] userInfo = line.substring(line.indexOf(":")+1).split(",");
                boolean successful = loginSeller(userInfo[0], userInfo[1]);
                if(successful == false){
                    Seller x = null;
                }
                oos.writeObject(seller);
                oos.flush();
            }
            if(action.equalsIgnoreCase("Create Customer")){
                String [] userInfo = line.substring(line.indexOf(":")+1).split(",");
                boolean successful = makeNewCustomer(userInfo[0], userInfo[1], userInfo[2]);
                if(successful == false){
                    Customer x = null;
                }
                oos.writeObject(customer);
                oos.flush();
            }
            if(action.equalsIgnoreCase("Create Seller")){
                String [] userInfo = line.substring(line.indexOf(":")+1).split(",");
                boolean successful = makeNewSeller(userInfo[0], userInfo[1], userInfo[2], userInfo[3]);
                if(successful == false){
                    Seller x = null;
                }
                oos.writeObject(seller);
                oos.flush();
            }
            if(action.equalsIgnoreCase("Logout")){
                if(customer != null && currentUser != null)
                    customer.updateInfo();
                else if(seller != null && currentUser != null)
                    seller.updateInfo();
                break;
            }
        }
        oos.close();
        ois.close();
        writer.close();
        reader.close();
    }
    /*
        Makes new Customer
     */
    public boolean makeNewCustomer(String u, String p, String em) {
        currentUser = u;
        String password = p;
        String email = em;

        File f = new File("UserInfo.txt");
        if (f.exists())
            readUsers("Seller");
        else {
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            readUsers("Seller");
        }
        for (int i = 0; i < customers.size(); i++) {
            if (customers.get(i).getUsername().equals(currentUser)) {
                currentUser = null;
                return false;
            }
        }
        customer = new Customer(currentUser, password, email);
        return true;
    }
    public boolean makeNewSeller(String u, String p, String em, String st) {
        currentUser = u;
        String password = p;
        String email = em;
        String storeName = st;
        File f = new File("UserInfo.txt");
        if (f.exists())
            readUsers("Customer");
        else {
            try {
                f.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            readUsers("Customer");
        }
        for (int i = 0; i < sellers.size(); i++) {
            if (sellers.get(i).getUsername().equals(currentUser)) {
                currentUser = null;
                return false;
            }
        }
        seller = new Seller(currentUser, password, email, storeName);
        return true;
    }

    public boolean loginCustomer(String u, String p) {
        boolean loggedIn = false;
        String username = u;
        String password = p;
        currentUser = username;
        readUsers("Seller");
        for (int i = 0; i < customers.size(); i++) {
            String user = customers.get(i).getUsername();
            String pass = customers.get(i).getPassword();
            String mail = customers.get(i).getEmail();
            if (username.equals(user) && pass.equals(password)) {
                loggedIn = true;
                customer = new Customer(currentUser, password, mail);
                break;
            }
        }
        return loggedIn;
    }
    public boolean loginSeller(String u, String p) {
        boolean loggedIn = false;
        String username = u;
        String password = p;
        currentUser = username;
        readUsers("Customer");
        for (int i = 0; i < sellers.size(); i++) {
            String user = sellers.get(i).getUsername();
            String pass = sellers.get(i).getPassword();
            String mail = sellers.get(i).getEmail();
            String store = sellers.get(i).getStoreName();
            if (username.equals(user) && pass.equals(password)) {
                loggedIn = true;
                seller = new Seller(currentUser, password, mail, store);
                break;
            }
        }
        return loggedIn;
    }
    /*
        Reads all the existing users from the info file in order to check that they exist + calls method to
         import conversations
     */
    public void readUsers(String keyUser) {
        File f = new File("UserInfo.txt");
        try {
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            while (true) {

                String line = bfr.readLine(); // reads first line to check if the person is customer or seller
                if (line == null)
                    break;
                String messageList = bfr.readLine();
                String blockedList = bfr.readLine();
                String[] userMessages;
                String[] blockedUsers;
                if (messageList.equalsIgnoreCase("null"))
                    userMessages = null;
                else userMessages = messageList.split(",");

                if (blockedList.equalsIgnoreCase("null"))
                    blockedUsers = null;
                else blockedUsers = blockedList.split(",");

                String[] userInfo = line.split(","); // splits the line to read type of user

                if (userInfo[3].equals("Seller") && !blockedList.contains(currentUser))
                    sellers.add(new Seller(userInfo[0], userInfo[1], userInfo[2], userInfo[4], userMessages,
                            blockedUsers));
                else if (userInfo[3].equals("Customer") && !blockedList.contains(currentUser))
                    customers.add(new Customer(userInfo[0], userInfo[1], userInfo[2], userMessages, blockedUsers));
                else if (userInfo[3].equals("Seller") && blockedList.contains(currentUser))
                    blockedSellers.add(new Seller(userInfo[0], userInfo[1], userInfo[2], userInfo[4], userMessages,
                            blockedUsers));
                else if (userInfo[3].equals("Customer") && blockedList.contains(currentUser))
                    blockedCustomers.add(new Customer(userInfo[0], userInfo[1], userInfo[2], userMessages,
                            blockedUsers));

                if (userInfo[3].equalsIgnoreCase(keyUser)) {
                    if (messageList.contains(currentUser) && !blockedList.contains(currentUser)) {
                        readCoversation(keyUser, userInfo[0]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
        Imports the existing conversation with the other user and stores it as a hashmap for printing later/saving
        and modifying
     */
    public void readCoversation(String keyUser, String otherUser) {
        ArrayList<String> conversation = new ArrayList<String>();
        String conversationFile = "";
        if (keyUser.equalsIgnoreCase("Seller"))
            conversationFile = currentUser + "&" + otherUser + ".txt";
        else
            conversationFile = otherUser + "&" + currentUser + ".txt";
        File f = new File(conversationFile);
        try {
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            while (true) {
                String line = bfr.readLine();
                if (line == null)
                    break;
                conversation.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        messages.put(otherUser, conversation);
    }
}