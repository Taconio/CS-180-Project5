# CS-180-GroupProject
_____________________
For project 4 our group chose option 2. For this we used lists and hashmaps in Main to hold information during runtime, adding the
contents of these to txt files as a way to hold information after program has been run. Everyone committed changes to the project, but in the end we deleted all of them and upated main with the most recent code so it may not show everyone made changes, but everyone did help in making this project.

## Program Flow
_____________________
Project 5 Testing:

Test 1: Login when user exist.

Step 1: Launches Messaging application.
Step 2: Select if user is a customer or seller.
Step 3: Welcome Customer/Seller! Login or Create account
Step 4: Correctly introduce Username and Password
Step 5: Clicks login button and access the app.

TEST PASSED

Test 2: Login when user does not exist (new account)

Step 1: Launches Messaging application.
Step 2: Select if user is a customer or seller.
Step 3: Welcome Customer/Seller! Login or Create account
Step 4: Click create new account button
Step 5: Fill out the information required -> username, password, email, and store name (only for seller).
Step 6: Clicks create button and access the application.

TEST PASSED

Test 3: Send messages for new conversation

Step 1: After login, select “seller/customer to message”
Step 2: Conversation Loads in text box
Step 3: Type message to send in message black box
Step 4: Click Send
Step 5: Conversation is updated in txt file (customer1&seller1.txt) and in text box

TEST PASSED


Test 4: Send messages when conversation exist

Step 1: After login, select “Start new conversation with Seller/Customer”
Step 3: Type message to send in message black box
Step 4: Click Send
Step 5: Conversation is updated in txt file (customer1&seller1.txt) and in text box

TEST PASSED

Test 5: Change user email

Step 1: Login
Step 2: Click Change Email Button
Step 3: Enter new email and click Ok.
Step 4: New email is changed after logout in the UserInfo.txt file

TEST PASSED

Test 6: Export CSV File
 
Step 1: Login
Step 2: Select conversation with Customer/Seller
Step 3: Click Export CSV file
Step 4: New csv file is created or updated (after logout) with the contents of the preexisting conversations including participants, timestamp, sender and contents.

TEST PASSED

Test 7: Seller adds new store 

Step 1: Login
Step 2: Click New Store button
Step 3: Introduce name of new store and click ok
Step 4: New store is added to the User in UserInfo.txt

TEST PASSED



The basic flow of this program is as follows: <br />
1. Program asks whether user wants to use program. (1 = Yes | 2 = No)
2. User is then prompted if they are a customer or a seller in order for program to know to search for a seller or a 
customer when login in / creating account. (1 = Customer | 2 = Seller)
3. Program asks the user whether they want to log in or create a new account:
   1. If no account is found for given username and password combination tells user no account was found and quits.
   2. If a match is found for given username, password and account type user is loged in and allowed to use message interface.
4. User can choose to message a seller or a customer (depending on their account type) or block a user or log out.
5. If user decides to message and have no previous conversations they can choose which account to message by typing their username. If they have preexisting conversations they can either:
   1. Message an existing seller by typing their username.
   2. Message a new seller by typing 'new.'
6. If the user decides to enter a preexisting conversation they can:
   1. Send another message.
   2. Edit a previous message.
   3. Delete a previous message.
   4. Export their conversation as a CSV file of the format "Customer&Seller,Timestamp,Sender,Contents"
   5. Import a txt file into the conversation to send as a message.
   6. Exit message screen.
7. After a user has completed their action program goes back to step 4 until they decide to log out.
8. Program goes back to first step: 
   1. If User chooses yes program starts again from step 2
   2. If user chooses no program ends


Warnings:
1. Do not enter string input when prompted for an integer, do not include any characters as well. 
2. When inputing the name of a user program will simply ask again if user was not found.
<br />

## Vocarium Submissions
_____________________
Project Report Submission: Will Greenwood <br />
Project Workspace Submission: Parth Thakre

## Class Overview
_____________________
### Main.java
Contains program main method that controls the displaying of menus and application control flow. Also contains several methods that work with the storing
of data in txt files through fileIO implementation. Program first gathers data from user input and then calls these methods in main to log them in or create a new account. Main also uses 
Customer and Seller objects to represent accounts and sets one of these as the "active" account using a boolean field. Customer accounts are only able to message Seller accounts and vice-versa. Depending on the type of account
different permissions and functionality are given to user like the creation of stores. Additionally, each user can have preexisting messages stored in .txt 
files, which will be updated in runtime and stored in a hashmap for later use. Finally, user can be edit, delete or export (to .csv) their messages for a preexisting conversation. 
<br />

Testing for this class was done through several test cases checking for proper functionality in edge cases such as a user already having conversation with 
all available sellers.

### Customer.java
Class used to represent a custumer account and includes their username, password, an array of previous messages and their account email. This class contains the updateInfo
method which is used by main to update user information after a new conversation or message has been created. 
<br />

Testing only done for updateInfo to make sure information is stored correctly.

### Seller.java
Class used similarly to Customer.java to represent a seller account. Seller accounts include their username,password,previous messages, store and account email. Also contains
updateInfo method used by main to update user information after a new conversation or message has been created.
<br />

Testing only done for update info to make sure information is stored correctly, slightly different implementation from customer account.
