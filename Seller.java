import java.util.*;
import java.io.*;

/**
 * Seller Class for our CS group project
 *
 * @author Parth Thakre, Anthony Rodriguez, Will Greenwood, Marcelo Moreno, Ji Bing Ni
 * @version 04-10-2023
 */

public class Seller {
    private String username;
    private String password;
    private String email;
    private String storeName;
    private String[] messagedCustomers;
    private String[] blockedCustomers;

     /*
        For new accounts
     */

    public Seller(String username, String password, String email, String storeName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.storeName = storeName;
        messagedCustomers = null;
        blockedCustomers = null;
    }

    /*
        For prexisting accounts
     */

    public Seller(String username, String password, String email, String storeName, String[] messagedCustomers,
                  String[] blockedCustomers) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.messagedCustomers = messagedCustomers;
        this.storeName = storeName;
        this.blockedCustomers = blockedCustomers;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String[] getMessagedCustomers() {
        return messagedCustomers;
    }

    public void setMessagedCustomers(String[] messagedCustomers) {
        this.messagedCustomers = messagedCustomers;
    }

    public String[] getBlockedCustomers() {
        return blockedCustomers;
    }

    public void setBlockedCustomers(String[] blockedCustomers) {
        this.blockedCustomers = blockedCustomers;
    }

    /*
            Reads all the information from update info, adds users name to other users conversation lists
         */
    public void updateInfo() {
        File f = new File("UserInfo.txt");
        ArrayList<String> newLines = new ArrayList<String>();
        try {
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            while (true) {
                String line = bfr.readLine(); // reads first line to check if the person is customer or seller
                if (line == null)
                    break;
                String messageList = bfr.readLine();
                String blockedList = bfr.readLine();
                String[] userInfo = line.split(",");


                if (messagedCustomers != null && userInfo[3].equals("Customer") && !messageList.contains(username)) {
                    for (int i = 0; i < messagedCustomers.length; i++) {
                        if (messagedCustomers[i].equals(userInfo[0])) {
                            if (messageList.equals("null"))
                                messageList = username;
                            else
                                messageList = messageList + "," + username;
                        }
                    }
                }
                if (blockedCustomers != null && userInfo[3].equals("Customer") && !blockedList.contains(username)) {
                    for (int i = 0; i < blockedCustomers.length; i++) {
                        if (blockedCustomers[i].equals(userInfo[0])) {
                            if (blockedList.equals("null"))
                                blockedList = username;
                            else
                                blockedList = blockedList + "," + username;
                        }
                    }
                }
                String[] blockedUsers = blockedList.split(",");
                if (blockedCustomers == null)
                    blockedList = "null";
                else if (blockedUsers.length > blockedCustomers.length) {
                    blockedList = "";
                    if (blockedCustomers.length == 0)
                        blockedList = "null";
                    else {
                        for (int i = 0; i < blockedCustomers.length; i++) {
                            if (i + 1 == blockedCustomers.length)
                                blockedList = blockedCustomers[i];
                            else blockedList += blockedCustomers[i] + ",";
                        }
                    }
                }
                if (!line.substring(0, line.indexOf(",")).equals(username)) {
                    newLines.add(line);
                    newLines.add(messageList);
                    newLines.add(blockedList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        String userInfo = username + "," + password + "," + email + "," + "Seller" + "," + storeName;
        String messages = "";
        String blocked = "";
        if (messagedCustomers == null)
            messages = "null";
        else {
            for (int i = 0; i < messagedCustomers.length - 1; i++) {
                messages = messages + messagedCustomers[i] + ",";
            }
            messages += messagedCustomers[messagedCustomers.length - 1];
        }
        if (blockedCustomers == null || blockedCustomers.length == 0)
            blocked = "null";
        else {
            for (int i = 0; i < blockedCustomers.length - 1; i++) {
                blocked = blocked + blockedCustomers[i] + ",";
            }
            blocked += blockedCustomers[blockedCustomers.length - 1];
        }
        try {
            FileOutputStream fos = new FileOutputStream(f);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < newLines.size(); i++)
                pw.println(newLines.get(i));
            pw.println(userInfo);
            pw.println(messages);
            pw.println(blocked);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
