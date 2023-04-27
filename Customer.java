import java.util.*;
import java.io.*;

/**
 * Customer Class for our CS group project.
 *
 * @author Parth Thakre, Anthony Rodriguez, Will Greenwood, Marcelo Moreno, Ji Bing Ni
 * @version 04-10-2023
 */

public class Customer implements Serializable {
    private String username;
    private String password;
    private String email;
    private String[] messagedSellers;
    private String[] blockedSellers;

    /*
        For new accounts
     */
    public Customer(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        messagedSellers = null;
        blockedSellers = null;
    }

    /*
        For prexisting accounts
     */
    public Customer(String username, String password, String email, String[] messagedSellers, String[] blockedSellers) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.messagedSellers = messagedSellers;
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

    public String[] getMessagedSellers() {
        return messagedSellers;
    }

    public void setMessagedSellers(String[] messagedSellers) {
        this.messagedSellers = messagedSellers;
    }

    public String[] getBlockedSellers() {
        return blockedSellers;
    }

    public void setBlockedSellers(String[] blockedSellers) {
        this.blockedSellers = blockedSellers;
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

                if (messagedSellers != null && userInfo[3].equals("Seller") && !messageList.contains(username)) {
                    for (int i = 0; i < messagedSellers.length; i++) {
                        if (messagedSellers[i].equals(userInfo[0])) {
                            if (messageList.equals("null"))
                                messageList = username;
                            else
                                messageList = messageList + "," + username;
                        }
                    }
                }
                if (blockedSellers != null && userInfo[3].equals("Seller") && !blockedList.contains(username)) {
                    for (int i = 0; i < blockedSellers.length; i++) {
                        if (blockedSellers[i].equals(userInfo[0])) {
                            if (blockedList.equals("null"))
                                blockedList = username;
                            else
                                blockedList = blockedList + "," + username;
                        }
                    }
                }
                String[] blockedUsers = blockedList.split(",");
                if (blockedSellers == null)
                    blockedList = "null";
                else if (blockedUsers.length > blockedSellers.length) {
                    blockedList = "";
                    if (blockedSellers.length == 0)
                        blockedList = "null";
                    else {
                        for (int i = 0; i < blockedSellers.length; i++) {
                            if (i + 1 == blockedSellers.length)
                                blockedList = blockedSellers[i];
                            else blockedList += blockedSellers[i] + ",";
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

        String userInfo = username + "," + password + "," + email + "," + "Customer";
        String messages = "";
        String blocked = "";
        if (messagedSellers == null)
            messages = "null";
        else {
            for (int i = 0; i < messagedSellers.length - 1; i++) {
                messages = messages + messagedSellers[i] + ",";
            }
            messages += messagedSellers[messagedSellers.length - 1];
        }
        if (blockedSellers == null || blockedSellers.length == 0)
            blocked = "null";
        else {
            for (int i = 0; i < blockedSellers.length - 1; i++) {
                blocked = blocked + blockedSellers[i] + ",";
            }
            blocked += blockedSellers[blockedSellers.length - 1];
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
