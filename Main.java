import java.util.*;
import java.io.*;
import java.time.*;
import java.time.format.*;
import java.util.Collections;

/**
 * Runs our project for CS180 Spring 2023
 *
 * @author Parth Thakre, Anthony Rodriguez, Will Greenwood, Marcelo Moreno, Ji Bing Ni
 * @version 04-10-2023
 */
public class Main {
    private String currentUser;
    private Customer customer;
    private Seller seller;
    private HashMap<String, ArrayList<String>> messages;
    private ArrayList<String> oppositeUsers;
    private ArrayList<Customer> customers;
    private ArrayList<Seller> sellers;
    private ArrayList<Seller> blockedSellers;
    private ArrayList<Customer> blockedCustomers;


    /*
        Intializes fields for later use
     */
    public Main() {
        currentUser = "";
        messages = new HashMap<String, ArrayList<String>>();
        oppositeUsers = new ArrayList<String>();
        customers = new ArrayList<Customer>();
        sellers = new ArrayList<Seller>();
        blockedSellers = new ArrayList<Seller>();
        blockedCustomers = new ArrayList<Customer>();
    }

    /*
        Runs the program outside of the main method for ease of access
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Main main = new Main();
        main.executeProgram(scanner);
    }

    /*
        Executes the program and handles based on what type of user is using the program
     */
    public void executeProgram(Scanner scanner) {
        while (true) {
            System.out.println("Welcome to the messaging System");
            int breakOut;
            do {
                System.out.println("Would you like to use this system? \n1. Yes\n2. No");
                breakOut = scanner.nextInt();
                scanner.nextLine();
            } while (breakOut != 2 && breakOut != 1);
            if (breakOut == 2)
                break;
            else {
                int userChoice;
                do {
                    System.out.println("Are you a customer or seller? \n1. Customer\n2. Seller");
                    userChoice = scanner.nextInt();
                    scanner.nextLine();
                } while (userChoice != 2 && userChoice != 1);
                if (userChoice == 1)
                    handleCustomer(scanner);
                else
                    handleSeller(scanner);
                if (currentUser == null) {
                    System.out.println("Error! Run the program again to try again!");
                    break;
                }
            }
        }
    }

    /*
        Handles the customer side of the program by calling login checks/makes new accounts, handles messaging features
        as well.
     */
    public void handleCustomer(Scanner scanner) {
        int newAcc;
        boolean login;
        String sendMessageTo;
        messages.clear();
        do {
            System.out.println("Do you already have an account? \n1. Yes\n2. No");
            newAcc = scanner.nextInt();
            scanner.nextLine();
        } while (newAcc != 2 && newAcc != 1);
        if (newAcc == 2) {
            makeNewCustomer(scanner);
            if (currentUser == null)
                return;
        }
        if (newAcc == 1) {
            login = loginCustomer(scanner);
            if (login == false) {
                System.out.println("Wrong login!");
                currentUser = null;
                return;
            }
        }

        int userChoice = 1;
        while (userChoice == 1) {
            ArrayList<Seller> unmessagedSellers = new ArrayList<Seller>();
            do {
                System.out.println("Please enter your choice of action:");
                System.out.println("1. Message sellers");
                System.out.println("2. Block customer/Unblock customer");
                System.out.println("3. Log out ");
                userChoice = scanner.nextInt();
                scanner.nextLine();
            } while (userChoice != 3 && userChoice != 2 && userChoice != 1);
            //Pulls up existing seller messages
            if (userChoice == 1) { // if user picks to message, it will go through checks to decide how messaging is set
                if (messages.size() != 0) { //checks if the account has prexisting messages
                    System.out.println("Conversations with existing Sellers");
                    Set<String> keys = messages.keySet();
                    Iterator<String> i = keys.iterator();
                    ArrayList<String> toSort1 = new ArrayList<>();
                    ArrayList<String> sortedList = new ArrayList<>();
                    while (i.hasNext()) {
                        String name = i.next();
                        int count = 0;
                        int countSelfMessages = 0;
                        String filename = currentUser + "&" + name + ".txt";

                        String mostCommonWord = "";
                        int maxCount = 0;
                        FileInputStream fileInputStream = null;
                        ArrayList<String> commonWords = new ArrayList<>();
                        try {
                            //Gets statistics for number of messages the customer sent and most frequent word in the conversation
                            fileInputStream = new FileInputStream(filename);
                            BufferedReader bfr1 = new BufferedReader(new InputStreamReader(fileInputStream));
                            bfr1.readLine();
                            String line = bfr1.readLine();

                            while (line != null) {
                                String senderName = line.substring(line.indexOf(",") + 1, line.indexOf(": "));
                                String contents = line.substring(line.indexOf(": ") + 2);
                                String words[] = contents.toLowerCase().split("\\s+");

                                if (senderName.equals(name)) {
                                    count++;
                                } else {
                                    countSelfMessages++;
                                }
                                line = bfr1.readLine();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        String existingInfo = (count + "$" + countSelfMessages +
                                "%Seller: " + name + " - Store: " + findSellerStore(name) + " - Stats: Seller sent "
                                + count + " messages total, You sent " + countSelfMessages + " mesages total");
                        toSort1.add(existingInfo);
                        sortedList.add(existingInfo);
                    }

                    for (int ind = 0; ind < toSort1.size(); ind++) {
                        System.out.println(toSort1.get(ind).substring(toSort1.get(ind).indexOf("%") + 1));
                    }
                    //Brings user to statistics dashboard
                    //They can sort by messages sent, messages received, or continue
                    String cont = null;
                    do {
                        System.out.println("Choose next action:");
                        System.out.println("1. Sort by messages received");
                        System.out.println("2. Sort by messages sent");
                        System.out.println("3. Continue to message sellers");
                        cont = scanner.nextLine();
                        if (cont.equals("1")) {
                            Collections.sort(toSort1, new Comparator<String>() {
                                public int compare(String str1, String str2) {
                                    int num1 = Integer.parseInt(str1.split("\\$")[0]);
                                    int num2 = Integer.parseInt(str2.split("\\$")[0]);
                                    return num1 - num2;
                                }
                            });
                            for (int p = 0; p < toSort1.size(); p++) {
                                System.out.println(toSort1.get(p).substring(toSort1.get(p).indexOf("%") + 1));
                            }
                        } else if (cont.equals("2")) {
                            Collections.sort(sortedList, new Comparator<String>() {
                                @Override
                                public int compare(String s1, String s2) {
                                    int num1 = Integer.parseInt(s1.split("\\$")[1].split("%")[0]);
                                    int num2 = Integer.parseInt(s2.split("\\$")[1].split("%")[0]);
                                    return Integer.compare(num1, num2);
                                }
                            });
                            for (int x = 0; x < sortedList.size(); x++) {
                                System.out.println(sortedList.get(x).substring(sortedList.get(x).indexOf("%") + 1));
                            }
                        } else {

                        }
                    } while (!cont.equals("3"));

                    //Ask if they want to message prexisting people or add new person to message.

                    do {
                        System.out.println("Type name of Seller to open chat.");
                        System.out.println("Or start new conversation Type \"new\" ");
                        sendMessageTo = scanner.nextLine();
                    } while (messages.get(sendMessageTo) == null && !sendMessageTo.equalsIgnoreCase("new"));

                    // Send message to new person despite having pre existing messages
                    if (sendMessageTo.equalsIgnoreCase("new")) {
                        ArrayList<String> sellerNames = new ArrayList<String>();
                        for (int x = 0; x < sellers.size(); x++) {
                            if (messages.get(sellers.get(x).getUsername()) == null) {
                                unmessagedSellers.add(sellers.get(x));
                                sellerNames.add(sellers.get(x).getUsername());
                            }
                        }
                        if (unmessagedSellers.size() == 0) {
                            System.out.println("Error: No new sellers to be messaged! Try again later!");
                            customer.updateInfo();
                            currentUser = null;
                            return;
                        }
                        for (int x = 0; x < unmessagedSellers.size(); x++)
                            System.out.println("Seller: " + unmessagedSellers.get(x).getUsername() + " Store: "
                                    + unmessagedSellers.get(x).getStoreName());
                        do {
                            System.out.println("Type name of Seller to open chat.");
                            sendMessageTo = scanner.nextLine();
                        } while (sellerNames.indexOf(sendMessageTo) < 0);
                        System.out.println("Enter the message you would like to send.");
                        String send = scanner.nextLine();
                        System.out.println("Sent!");
                        DateTimeFormatter globalFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
                        ZonedDateTime currentISTime = ZonedDateTime.now();
                        ZonedDateTime currentETime =
                                currentISTime.withZoneSameInstant(ZoneId.of("America/New_York"));
                        String timeStamp = globalFormat.format(currentETime);
                        String line = timeStamp + "," + currentUser + ": " + send;
                        ArrayList<String> output = new ArrayList<String>();
                        output.add(line);
                        unmessagedSellers.remove(sendMessageTo);
                        messages.put(sendMessageTo, output);
                        writeConversation("Seller", sendMessageTo);

                    } else { // this is the option that allows them to send messeges in preexisting conversations
                        ArrayList<String> conversation = messages.get(sendMessageTo);
                        for (int x = 0; x < conversation.size(); x++)
                            System.out.println(conversation.get(x));
                        int sendMessage;
                        do {
                            System.out.println("What would you like to do? \n1. Send message" +
                                    "\n2. Edit message\n3. Delete message" +
                                    "\n4. Export conversation to CSV\n5. Import file\n6. Exit");
                            sendMessage = scanner.nextInt();
                            scanner.nextLine();
                        } while (sendMessage != 1 && sendMessage != 2 && sendMessage != 3 && sendMessage != 4 &&
                                sendMessage != 5 && sendMessage != 6);
                        if (sendMessage == 1) {
                            System.out.println("Enter the message you would like to send.");
                            String send = scanner.nextLine();
                            DateTimeFormatter globalFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
                            ZonedDateTime currentISTime = ZonedDateTime.now();
                            ZonedDateTime currentETime =
                                    currentISTime.withZoneSameInstant(ZoneId.of("America/New_York"));
                            String timeStamp = globalFormat.format(currentETime);
                            String line = timeStamp + "," + currentUser + ": " + send;
                            messages.get(sendMessageTo).add(line); // adds the new line into the value of the hashmap
                            System.out.println("Sent!");
                            writeConversation("Seller", sendMessageTo);
                        } else if (sendMessage == 2) {
                            String lineToChange;
                            int lineIndex = 0;
                            boolean lineFound = false;
                            do {
                                System.out.println("Type out which message you would like to edit:");
                                lineToChange = scanner.nextLine();
                                for (int x = 0; x < messages.get(sendMessageTo).size(); x++) {
                                    if (messages.get(sendMessageTo).get(x).contains(lineToChange)) {
                                        lineFound = true;
                                        lineIndex = x;
                                    }
                                }
                                if (!lineFound)
                                    System.out.println("Error! Type the correct line that you would like to edit!");
                            } while (!lineFound);
                            System.out.println("Type out what you would like the message to be edited to:");
                            String desiredLine = scanner.nextLine();
                            messages.get(sendMessageTo).set(lineIndex,
                                    messages.get(sendMessageTo).get(lineIndex).replace(lineToChange, desiredLine));
                            writeConversation("Seller", sendMessageTo);
                            System.out.println("Message has been edited!");
                        } else if (sendMessage == 3) {
                            String lineToChange;
                            boolean lineFound = false;
                            do {
                                System.out.println("Type out which message you would like to delete:");
                                lineToChange = scanner.nextLine();
                                for (int x = 0; x < messages.get(sendMessageTo).size(); x++) {
                                    if (messages.get(sendMessageTo).get(x).contains(lineToChange)) {
                                        lineFound = true;
                                        messages.get(sendMessageTo).remove(x);
                                    }
                                }
                                if (!lineFound)
                                    System.out.println("Error! Type the correct line that you would like to delete!");
                            } while (!lineFound);
                            writeConversation("Seller", sendMessageTo);
                            System.out.println("Message has been deleted!");
                        } else if (sendMessage == 4) {
                            //get filename of conversation and run export to CVS, should run for all cases
                            String conversationFile = conversation.get(0).substring(conversation.get(0).indexOf(":") +
                                    2);
                            csvExport(conversationFile);
                        } else if (sendMessage == 5) {
                            System.out.println("File import!");
                            try {
                                boolean existing = false;
                                StringBuilder messageContent = new StringBuilder();
                                String messageLine;
                                do {
                                    System.out.println("Please enter a file to import: ");
                                    String fileName = scanner.nextLine();
                                    if (fileName.equals("")) {
                                        System.out.println("Enter a valid file!");
                                    } else
                                        try {
                                            File f = new File(fileName);
                                            if (f.exists()) {
                                                existing = true;
                                                BufferedReader bfr2 =
                                                        new BufferedReader(new FileReader(fileName));

                                                while ((messageLine = bfr2.readLine()) != null) {
                                                    messageContent.append(messageLine + " ");
                                                }
                                                bfr2.close();
                                                System.out.println("File content sent successfully!");

                                            } else {
                                                System.out.println("This file does not exist! " +
                                                        "Please enter a valid file name:");
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            return;
                                        }

                                } while (!existing);


                                File conversationFile = new File(currentUser + "&" + sendMessageTo + ".txt");
                                if (!conversationFile.exists()) {
                                    PrintWriter pw1 = new PrintWriter(new FileOutputStream(conversationFile,
                                            true));
                                    DateTimeFormatter globalFormat =
                                            DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
                                    ZonedDateTime currentISTime = ZonedDateTime.now();
                                    ZonedDateTime currentETime =
                                            currentISTime.withZoneSameInstant(ZoneId.of("America/New_York"));
                                    String timeStamp = globalFormat.format(currentETime);

                                    pw1.println(timeStamp + "," + currentUser + ": " + messageContent);
                                    pw1.close();
                                } else {
                                    PrintWriter pw1 = new PrintWriter(new FileOutputStream(conversationFile,
                                            true));
                                    DateTimeFormatter globalFormat =
                                            DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
                                    ZonedDateTime currentISTime = ZonedDateTime.now();
                                    ZonedDateTime currentETime =
                                            currentISTime.withZoneSameInstant(ZoneId.of("America/New_York"));
                                    String timeStamp = globalFormat.format(currentETime);

                                    pw1.println(timeStamp + "," + currentUser + ": " + messageContent);
                                    pw1.close();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {    //this is for  entirely new accounts to send messages.
                    ArrayList<String> sellerNames = new ArrayList<String>();
                    if (sellers.size() == 0) {
                        System.out.println("Error: No new sellers to be messaged! Try again later!");
                        customer.updateInfo();
                        currentUser = null;
                        return;
                    }
                    for (int x = 0; x < sellers.size(); x++) {
                        System.out.println(sellers.get(x).getUsername() + ", Store: "
                                + sellers.get(x).getStoreName());
                        sellerNames.add(sellers.get(x).getUsername());
                    }
                    do {
                        System.out.println("Type name of Seller to open a new chat.");
                        sendMessageTo = scanner.nextLine();
                    } while (sellerNames.indexOf(sendMessageTo) < 0);
                    System.out.println("Enter the message you would like to send.");
                    String send = scanner.nextLine();
                    System.out.println("Sent!");
                    DateTimeFormatter globalFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
                    ZonedDateTime currentISTime = ZonedDateTime.now();
                    ZonedDateTime currentETime =
                            currentISTime.withZoneSameInstant(ZoneId.of("America/New_York"));
                    String timeStamp = globalFormat.format(currentETime);
                    String line = timeStamp + "," + currentUser + ": " + send;
                    ArrayList<String> output = new ArrayList<String>();
                    output.add(line);
                    unmessagedSellers.remove(sendMessageTo);
                    messages.put(sendMessageTo, output); // saves the conversation with assigned user
                    writeConversation("Seller", sendMessageTo); // writes the conversation into the file
                }

            } else if (userChoice == 2) {
                if (sellers.size() == 0 && blockedSellers.size() == 0) {
                    System.out.println("Error! No sellers can be blocked or unblocked! Try again later! ");
                    currentUser = null;
                    return;
                }
                System.out.println("Unblocked Sellers:");
                for (int i = 0; i < sellers.size(); i++)
                    System.out.println("Customer: " + sellers.get(i).getUsername());
                System.out.println("Blocked Sellers: ");
                for (int i = 0; i < blockedSellers.size(); i++)
                    System.out.println("Customer: " + blockedSellers.get(i).getUsername());
                System.out.println("Enter name of seller to block or unblock.");
                String toBlock = scanner.nextLine();
                boolean doneBlocking = false;
                for (int i = 0; i < sellers.size(); i++) {
                    if (sellers.get(i).getUsername().equals(toBlock)) {
                        messages.remove(toBlock);
                        blockedSellers.add(sellers.get(i));
                        sellers.remove(i);
                        doneBlocking = true;
                    }
                }
                if (doneBlocking)
                    System.out.println("Successfully Blocked!");
                else {
                    for (int i = 0; i < blockedSellers.size(); i++) {
                        if (blockedSellers.get(i).getUsername().equals(toBlock)) {
                            sellers.add(blockedSellers.get(i));
                            blockedSellers.remove(i);
                            doneBlocking = true;
                        }
                    }
                    if (doneBlocking)
                        System.out.println("Successfully Unblocked! Rerun the program to load messages with newly" +
                                " unblocked user");
                    else {
                        System.out.println("Error! Couldn't block the listed user");
                        currentUser = null;
                        return;
                    }
                }
                if (messages.size() > 0) {
                    Set<String> keys = messages.keySet();
                    Iterator<String> i = keys.iterator();
                    String[] messagesWith = new String[messages.size()];
                    for (int x = 0; x < messages.size(); x++)
                        messagesWith[x] = i.next();
                    seller.setMessagedCustomers(messagesWith);
                }
                String[] blockedUsers = new String[blockedSellers.size()];
                for (int i = 0; i < blockedSellers.size(); i++) {
                    blockedUsers[i] = blockedSellers.get(i).getUsername();
                }
                customer.setBlockedSellers(blockedUsers);
                customer.updateInfo();

            } else { // saves the new people talked to into the updateinfo.txt file.
                if (messages.size() > 0) {
                    Set<String> keys = messages.keySet();
                    Iterator<String> i = keys.iterator();
                    String[] messagesWith = new String[messages.size()];
                    for (int x = 0; x < messages.size(); x++)
                        messagesWith[x] = i.next();
                    customer.setMessagedSellers(messagesWith);
                }
                customer.updateInfo();
            }
        }
    }

    /*
        To be implemented but it handles the seller side of things with similar implementation as the customer method.
     */
    public void handleSeller(Scanner scanner) {
        int newAcc;
        boolean login;
        String sendMessageTo;
        messages.clear();
        do {
            System.out.println("Do you already have an account? \n1. Yes\n2. No");
            newAcc = scanner.nextInt();
            scanner.nextLine();
        } while (newAcc != 2 && newAcc != 1);
        if (newAcc == 2) {
            makeNewSeller(scanner);
            if (currentUser == null)
                return;
        }
        if (newAcc == 1) {
            login = loginSeller(scanner);
            if (login == false) {
                System.out.println("Wrong login!");
                currentUser = null;
                return;
            }
        }
        int userChoice = 1;
        while (userChoice == 1) {
            ArrayList<Customer> unmessagedCustomers = new ArrayList<Customer>();
            do {
                System.out.println("Please enter your choice of action:");
                System.out.println("1. Message customers");
                System.out.println("2. Block customer/Unblock customer");
                System.out.println("3. Log out");
                userChoice = scanner.nextInt();
                scanner.nextLine();
            } while (userChoice != 3 && userChoice != 2 && userChoice != 1);
            //Pulls up existing seller messages
            if (userChoice == 1) { // if user picks to message, it will go through checks to decide how messaging is set
                if (messages.size() != 0) { //checks if the account has prexisting messages
                    System.out.println("Conversations with existing customers");
                    Set<String> keys = messages.keySet();
                    Iterator<String> i = keys.iterator();
                    ArrayList<String> toSort = new ArrayList<>();
                    while (i.hasNext()) {
                        String name = i.next();
                        int count = 0;
                        String filename = name + "&" + currentUser + ".txt";

                        String mostCommonWord = "";
                        int maxCount = 0;
                        FileInputStream fileInputStream = null;
                        ArrayList<String> commonWords = new ArrayList<>();
                        try {
                            //Gets statistics for number of messages the customer sent and most frequent word in the
                            // conversation
                            fileInputStream = new FileInputStream(filename);
                            BufferedReader bfr = new BufferedReader(new InputStreamReader(fileInputStream));
                            bfr.readLine();
                            String line = bfr.readLine();
                            while (line != null) {
                                String senderName = line.substring(line.indexOf(",") + 1, line.indexOf(": "));
                                String contents = line.substring(line.indexOf(": ") + 2);
                                String words[] = contents.toLowerCase().split("\\s+");
                                for (String word : words) {
                                    int freqCount = freqWordCount(word, filename);
                                    if (freqCount > maxCount) {
                                        commonWords.clear();
                                        commonWords.add(word);
                                        maxCount = freqCount;
                                    } else if (freqCount == maxCount && !commonWords.contains(word)) {
                                        commonWords.add(word);
                                    }
                                }
                                if (senderName.equals(name)) {
                                    count++;
                                }
                                line = bfr.readLine();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        String com = "";
                        for (int k = 0; k < commonWords.size(); k++) {
                            com += commonWords.get(k);
                            if (k < commonWords.size() - 1) {
                                com += ", ";
                            }
                        }

                        String existingInfo = (count + "$Customer: " +
                                name + " - Stats: Customer sent " + count
                                + " messages total, Most frequent word/words in conversation: " + com);
                        toSort.add(existingInfo);
                    }
                    ArrayList<String> sortedList = new ArrayList<>();
                    for (int ind = 0; ind < toSort.size(); ind++) {
                        System.out.println(toSort.get(ind).substring(toSort.get(ind).indexOf("$") + 1));
                    }
                    //Asks user if they would like to sort the messages by number of messages received

                    System.out.println("Would you like to sort existing customer messages by number of messages " +
                            "received?");
                    System.out.println("1. Yes");
                    System.out.println("2. No");
                    String cont = scanner.nextLine();
                    if (cont.equals("1")) {
                        Collections.sort(toSort, new Comparator<String>() {
                            public int compare(String str1, String str2) {
                                int num1 = Integer.parseInt(str1.split("\\$")[0]);
                                int num2 = Integer.parseInt(str2.split("\\$")[0]);
                                return num1 - num2;
                            }
                        });
                        for (int p = 0; p < toSort.size(); p++) {
                            System.out.println(toSort.get(p).substring(toSort.get(p).indexOf("$") + 1));
                        }
                    }

                    //Ask if they want to message prexisting people or add new person to message.

                    do {
                        System.out.println("Type name of Customer to open chat.");
                        System.out.println("Or start new conversation Type \"new\" ");
                        sendMessageTo = scanner.nextLine();
                    } while (messages.get(sendMessageTo) == null && !sendMessageTo.equalsIgnoreCase("new"));

                    // Send message to new person despite having pre existing messages
                    if (sendMessageTo.equalsIgnoreCase("new")) {
                        ArrayList<String> customerNames = new ArrayList<String>();
                        for (int x = 0; x < customers.size(); x++) {
                            if (messages.get(customers.get(x).getUsername()) == null) {
                                unmessagedCustomers.add(customers.get(x));
                                customerNames.add(customers.get(x).getUsername());
                            }
                        }
                        if (unmessagedCustomers.size() == 0) {
                            System.out.println("Error: No new customers to be messaged! Try again later!");
                            seller.updateInfo();
                            currentUser = null;
                            return;
                        }
                        for (int x = 0; x < unmessagedCustomers.size(); x++)
                            System.out.println("Customer: " + unmessagedCustomers.get(x).getUsername());
                        do {
                            System.out.println("Type name of Customer to open chat.");
                            sendMessageTo = scanner.nextLine();
                        } while (customerNames.indexOf(sendMessageTo) < 0);
                        System.out.println("Enter the message you would like to send.");
                        String send = scanner.nextLine();
                        System.out.println("Sent!");
                        DateTimeFormatter globalFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
                        ZonedDateTime currentISTime = ZonedDateTime.now();
                        ZonedDateTime currentETime =
                                currentISTime.withZoneSameInstant(ZoneId.of("America/New_York"));
                        String timeStamp = globalFormat.format(currentETime);
                        String line = timeStamp + "," + currentUser + ": " + send;
                        ArrayList<String> output = new ArrayList<String>();
                        output.add(line);
                        unmessagedCustomers.remove(sendMessageTo);
                        messages.put(sendMessageTo, output);
                        writeConversation("Customer", sendMessageTo);

                    } else { // this is the option that allows them to send messeges in preexisting conversations
                        ArrayList<String> conversation = messages.get(sendMessageTo);
                        for (int x = 0; x < conversation.size(); x++)
                            System.out.println(conversation.get(x));
                        int sendMessage;
                        do {
                            System.out.println("What would you like to do? \n1. Send message" +
                                    "\n2. Edit message\n3. Delete message\n" +
                                    "4. Export conversation to CSV\n5. File import" +
                                    "\n6. Exit");
                            sendMessage = scanner.nextInt();
                            scanner.nextLine();
                        } while (sendMessage != 1 && sendMessage != 2 && sendMessage != 3 && sendMessage != 4 &&
                                sendMessage != 5 && sendMessage != 6);
                        if (sendMessage == 1) {
                            System.out.println("Enter the message you would like to send.");
                            String send = scanner.nextLine();
                            DateTimeFormatter globalFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
                            ZonedDateTime currentISTime = ZonedDateTime.now();
                            ZonedDateTime currentETime =
                                    currentISTime.withZoneSameInstant(ZoneId.of("America/New_York"));
                            String timeStamp = globalFormat.format(currentETime);
                            String line = timeStamp + "," + currentUser + ": " + send;
                            messages.get(sendMessageTo).add(line); // adds the new line into the value of the hashmap
                            System.out.println("Sent!");
                            writeConversation("Customer", sendMessageTo);
                        } else if (sendMessage == 2) {
                            String lineToChange;
                            int lineIndex = 0;
                            boolean lineFound = false;
                            do {
                                System.out.println("Type out which message you would like to edit:");
                                lineToChange = scanner.nextLine();
                                for (int x = 0; x < messages.get(sendMessageTo).size(); x++) {
                                    if (messages.get(sendMessageTo).get(x).contains(lineToChange)) {
                                        lineFound = true;
                                        lineIndex = x;
                                    }
                                }
                                if (!lineFound)
                                    System.out.println("Error! Type the correct line that you would like to edit!");
                            } while (!lineFound);
                            System.out.println("Type out what you would like the message to be edited to:");
                            String desiredLine = scanner.nextLine();
                            messages.get(sendMessageTo).set(lineIndex,
                                    messages.get(sendMessageTo).get(lineIndex).replace(lineToChange, desiredLine));
                            writeConversation("Customer", sendMessageTo);
                            System.out.println("Message has been edited!");
                        } else if (sendMessage == 3) {
                            String lineToChange;
                            boolean lineFound = false;
                            do {
                                System.out.println("Type out which message you would like to delete:");
                                lineToChange = scanner.nextLine();
                                for (int x = 0; x < messages.get(sendMessageTo).size(); x++) {
                                    if (messages.get(sendMessageTo).get(x).contains(lineToChange)) {
                                        lineFound = true;
                                        messages.get(sendMessageTo).remove(x);
                                    }
                                }
                                if (!lineFound)
                                    System.out.println("Error! Type the correct line that you would like to delete!");
                            } while (!lineFound);
                            writeConversation("Customer", sendMessageTo);
                            System.out.println("Message has been deleted!");
                        } else if (sendMessage == 4) {
                            //get filename of conversation and run export to CVS, should run for all cases
                            String conversationFile = conversation.get(0).substring(conversation.get(0).indexOf(":") +
                                    2);
                            csvExport(conversationFile);
                        } else if (sendMessage == 5) {
                            //This section will prompt the user for a file name, read the file and send it as a message
                            //to the desired user.
                            System.out.println("File import!");
                            try {
                                boolean existing = false;
                                StringBuilder messageContent = new StringBuilder();
                                String messageLine;
                                do {
                                    System.out.println("Please enter a file to import: ");
                                    String fileName = scanner.nextLine();
                                    if (fileName.equals("")) {
                                        System.out.println("Enter a valid file!");
                                    } else
                                        try {
                                            File f = new File(fileName);
                                            if (f.exists()) {
                                                existing = true;
                                                BufferedReader bfr2 =
                                                        new BufferedReader(new FileReader(fileName));

                                                while ((messageLine = bfr2.readLine()) != null) {
                                                    messageContent.append(messageLine + " ");
                                                }
                                                bfr2.close();
                                                System.out.println("File content sent successfully!");

                                            } else {
                                                System.out.println("This file does not exist! " +
                                                        "Please enter a valid file name:");
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            return;
                                        }

                                } while (!existing);


                                File conversationFile = new File(sendMessageTo + "&" + currentUser + ".txt");
                                if (!conversationFile.exists()) {
                                    PrintWriter pw1 = new PrintWriter(new FileOutputStream(conversationFile,
                                            true));
                                    DateTimeFormatter globalFormat =
                                            DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
                                    ZonedDateTime currentISTime = ZonedDateTime.now();
                                    ZonedDateTime currentETime =
                                            currentISTime.withZoneSameInstant(ZoneId.of("America/New_York"));
                                    String timeStamp = globalFormat.format(currentETime);

                                    pw1.println(timeStamp + "," + currentUser + ": " + messageContent);
                                    pw1.close();
                                } else {
                                    PrintWriter pw1 = new PrintWriter(new FileOutputStream(conversationFile,
                                            true));
                                    DateTimeFormatter globalFormat =
                                            DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
                                    ZonedDateTime currentISTime = ZonedDateTime.now();
                                    ZonedDateTime currentETime =
                                            currentISTime.withZoneSameInstant(ZoneId.of("America/New_York"));
                                    String timeStamp = globalFormat.format(currentETime);

                                    pw1.println(timeStamp + "," + currentUser + ": " + messageContent);
                                    pw1.close();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {    //this is for  entirely new accounts to send messages.
                    ArrayList<String> customerNames = new ArrayList<String>();
                    if (customers.size() == 0) {
                        System.out.println("Error: No new customers to be messaged! Try again later!");
                        seller.updateInfo();
                        currentUser = null;
                        return;
                    }
                    for (int x = 0; x < customers.size(); x++) {
                        System.out.println("Customer: " + customers.get(x).getUsername());
                        customerNames.add(customers.get(x).getUsername());
                    }
                    do {
                        System.out.println("Type name of Customer to open a new chat.");
                        sendMessageTo = scanner.nextLine();
                    } while (customerNames.indexOf(sendMessageTo) < 0);
                    System.out.println("Enter the message you would like to send.");
                    String send = scanner.nextLine();
                    System.out.println("Sent!");
                    DateTimeFormatter globalFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mma z");
                    ZonedDateTime currentISTime = ZonedDateTime.now();
                    ZonedDateTime currentETime =
                            currentISTime.withZoneSameInstant(ZoneId.of("America/New_York"));
                    String timeStamp = globalFormat.format(currentETime);
                    String line = timeStamp + "," + currentUser + ": " + send;
                    ArrayList<String> output = new ArrayList<String>();
                    output.add(line);
                    unmessagedCustomers.remove(sendMessageTo);
                    messages.put(sendMessageTo, output); // saves the conversation with assigned user
                    writeConversation("Customer", sendMessageTo); // writes the conversation into the file
                }
            } else if (userChoice == 2) {
                if (customers.size() == 0 && blockedCustomers.size() == 0) {
                    System.out.println("Error! No customers can be blocked or unblocked! Try again later! ");
                    currentUser = null;
                    return;
                }
                System.out.println("Unblocked Customers:");
                for (int i = 0; i < customers.size(); i++)
                    System.out.println("Customer: " + customers.get(i).getUsername());
                System.out.println("Blocked Customers: ");
                for (int i = 0; i < blockedCustomers.size(); i++)
                    System.out.println("Customer: " + blockedCustomers.get(i).getUsername());
                System.out.println("Enter name of customer to block or unblock.");
                String toBlock = scanner.nextLine();
                boolean doneBlocking = false;
                for (int i = 0; i < customers.size(); i++) {
                    if (customers.get(i).getUsername().equals(toBlock)) {
                        messages.remove(toBlock);
                        blockedCustomers.add(customers.get(i));
                        customers.remove(i);
                        doneBlocking = true;
                    }
                }
                if (doneBlocking)
                    System.out.println("Successfully Blocked!");
                else {
                    for (int i = 0; i < blockedCustomers.size(); i++) {
                        if (blockedCustomers.get(i).getUsername().equals(toBlock)) {
                            customers.add(blockedCustomers.get(i));
                            blockedCustomers.remove(i);
                            doneBlocking = true;
                        }
                    }
                    if (doneBlocking)
                        System.out.println("Successfully Unblocked! Rerun the program to load messages with newly" +
                                " unblocked user");
                    else {
                        System.out.println("Error! Couldn't block the listed user");
                        currentUser = null;
                        return;
                    }
                }
                if (messages.size() > 0) {
                    Set<String> keys = messages.keySet();
                    Iterator<String> i = keys.iterator();
                    String[] messagesWith = new String[messages.size()];
                    for (int x = 0; x < messages.size(); x++)
                        messagesWith[x] = i.next();
                    seller.setMessagedCustomers(messagesWith);
                }
                String[] blockedUsers = new String[blockedCustomers.size()];
                for (int i = 0; i < blockedCustomers.size(); i++) {
                    blockedUsers[i] = blockedCustomers.get(i).getUsername();
                }
                seller.setBlockedCustomers(blockedUsers);
                seller.updateInfo();

            } else { // saves the new people talked to into the updateinfo.txt file.
                if (messages.size() > 0) {
                    Set<String> keys = messages.keySet();
                    Iterator<String> i = keys.iterator();
                    String[] messagesWith = new String[messages.size()];
                    for (int x = 0; x < messages.size(); x++)
                        messagesWith[x] = i.next();
                    seller.setMessagedCustomers(messagesWith);
                }
                seller.updateInfo();
            }
        }
    }

    /*
        Makes new Customer
     */
    /*
        Makes new Customer
     */
    public void makeNewCustomer(Scanner scanner) {
        System.out.println("Please enter a username: ");
        currentUser = scanner.nextLine();
        System.out.println("Please enter a password: ");
        String password = scanner.nextLine();
        System.out.println("Please enter an email: ");
        String email = scanner.nextLine();

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
                System.out.println("Username taken!");
                currentUser = null;
                return;
            }
        }
        customer = new Customer(currentUser, password, email);
    }

    /*
        Makes new seller
     */
    public void makeNewSeller(Scanner scanner) {
        System.out.println("Please enter a username: ");
        currentUser = scanner.nextLine();
        System.out.println("Please enter a password: ");
        String password = scanner.nextLine();
        System.out.println("Please enter a mail: ");
        String email = scanner.nextLine();
        System.out.println("Please enter a store name: ");
        String storeName = scanner.nextLine();
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
                System.out.println("Username taken!");
                currentUser = null;
                return;
            }
        }
        seller = new Seller(currentUser, password, email, storeName);
    }

    /*
        Checks login for new customer
     */
    public boolean loginCustomer(Scanner scanner) {
        boolean loggedIn = false;
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
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

    public boolean loginSeller(Scanner scanner) {
        boolean loggedIn = false;
        System.out.println("Please enter your username: ");
        String username = scanner.nextLine();
        System.out.println("Please enter your password: ");
        String password = scanner.nextLine();
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
        Finds the sellers store based on the sellers name
     */
    public String findSellerStore(String name) {
        for (int i = 0; i < sellers.size(); i++)
            if (sellers.get(i).getUsername().equals(name))
                return sellers.get(i).getStoreName();
        return "";
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

    /*
        Creates new file for new conversation.
     */
    public void writeConversation(String keyUser, String otherUser) {
        String fileName;
        if (keyUser.equals("Seller"))
            fileName = currentUser + "&" + otherUser + ".txt";
        else
            fileName = otherUser + "&" + currentUser + ".txt";
        File f = new File(fileName);
        try {
            FileOutputStream fos = new FileOutputStream(f);
            PrintWriter pw = new PrintWriter(fos);
            if (!messages.get(otherUser).get(0).contains("&Seller"))
                pw.println("Participants (Customer&Seller) : " + fileName);
            for (int i = 0; i < messages.get(otherUser).size(); i++)
                pw.println(messages.get(otherUser).get(i));
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // export to CVS method, should work for both user and seller
    public void csvExport(String conversationTextFile) {
        try {
            String participants = conversationTextFile.substring(0, conversationTextFile.length() - 4);


            BufferedReader bfr = new BufferedReader(new FileReader(conversationTextFile));
            File filecsv = new File(participants + ".csv");
            BufferedWriter bfw = new BufferedWriter(new FileWriter(filecsv));

            //Reading twice here because we have to skip first line of the conversation
            String line = bfr.readLine();
            line = bfr.readLine();
            bfw.write("Customer&Seller,Timestamp,Sender,Contents\n");
            while (line != null) {
                //Constants used to build CSV line, array used specifically to get timestamp
                String[] lineArr = line.split(" ");
                String content = line.substring(line.indexOf(":", 20) + 2);
                String sender = lineArr[3].substring(lineArr[3].indexOf(",") + 1, lineArr[3].indexOf(":"));

                //Final string that is going to be written to CSV file
                String temp = String.format("%s,%s,%s,\"%s\"\n", participants, lineArr[2], sender, content);
                bfw.write(temp);

                line = bfr.readLine();
            }

            //If everything worked succesfully close reader and writer objects and print to terminal.
            bfw.close();
            bfr.close();
            System.out.println("Conversation succesfully exported!");

        } catch (IOException e) {
            System.out.println("Error reading file!");
            e.printStackTrace();
        }
    }

    // Counts frequency of words in a file *used for stats
    public int freqWordCount(String word, String fileName) throws Exception {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] words = line.toLowerCase().split("\\s+");
                for (String w : words) {
                    if (w.equals(word)) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
}
