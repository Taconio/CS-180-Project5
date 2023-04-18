import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

/**
 * Tests major implementations for Project 4
 *
 * @author Group
 * @date 04-08-2023
 */
public class Tester {
    Main main = new Main();
    Seller sell = new Seller("Salesman", "Test123", "oilTycoon@yahoo.com", "OilTycoon");
    Customer customer = new Customer("Investor", "$Bill100", "hereComesTheMoney@gmail.com");

    public static void main(String[] args) {
        int TestFailures = 0;
     Tester test = new Tester();

     if (test.updateUserTest()) {
         System.out.println("User info incorrect, user not updated accordingly!");
         TestFailures++;
     }
    }

    //Test if csv exporting is working correctly
    public boolean csvTest() {

        return false;
    }

    //Test for correct update of user in file
    public boolean updateUserTest() {
        sell.updateInfo();
        try {
            BufferedReader bfr = new BufferedReader(new FileReader("UserInfo.txt"));
            String line = bfr.readLine();;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Return
}
