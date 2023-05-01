# CS-180-GroupProject
_____________________
For project 4 our group chose option 2. For this we used lists and hashmaps in Main to hold information during runtime, adding the
contents of these to txt files as a way to hold information after program has been run. Everyone committed changes to the project, but in the end we deleted all of them and upated main with the most recent code so it may not show everyone made changes, but everyone did help in making this project.

## Program Flow
_____________________

The basic flow of this program is as follows: <br />
1. User begins on a screen with a customer and seller button to log in to their customer or seller account.
2. User is then prompted to log in with a preexisting account or press the creating account button to make a new account of the type they have selected.
3. Program then presents user with the main screen with all of the program's functionality with two drop-downs for conversations:
   1. One drop-down for preexisting conversations that displays conversation in a section to the right.
   2. One drop-down for new sellers where user can choose to initiate a new conversation.
4. User has several options for messaging:
   1. User can send a new message.
   2. User can edit a previous message by entering it in a JOption popup.
   3. User can delete a previous message by entering it in a JOption popup.
   4. User can import and send a txt file as a message.
   5. User can export conversation as a csv file.
5. User also has access to statistics:
   1. If the user is a seller they can view statistics based on the messages they have received from customers.
   2. If user is a customer they can view statistics based on messages they have sent to stores and messages they have received.
6. User also has access to a refresh button in case another user has sent a message while they were online which updates their chat logs.
7. Finally, users can change their information:
   1. Customers can change their email by pressing the change email button.
   2. Sellers can also change email and can additionally add new stores to their store list.
8. After user is done using the ap they can press the logout button which saves their data and exits the application.
<br />

## Vocarium Submissions
_____________________
Project Report Submission: Will Greenwood <br />
Project Workspace Submission: Parth Thakre
Project Presentation Submission: Parth Thakre

## Class Overview
_____________________
### MessagingApp.java
Class utilized to initiate instances of MessagingServer threads as each Messaging app.java attempts a connection through netIO.
This guarantees that every time a user is attempting to connect there is a thread waiting to accept the connection and work with 
the shared txt files that are used as a shared data repository. This class also shares a file with MessagingServer.java (specifics in messaging server description).
<br />

Testing for this was fairly simple as it only contains a main method and was created later in development. Still we tested for concurrency by 
opening several terminal tabs and running many MessagingApp programs at the same time.

### MessagingServer.java
Class that extends Thread and is in the same file as  StartServer.java. This class handles all the data reading and writing to our shared txt documents
and additionally utilizes hash maps and array lists to keep the current user's data as they are utilizing the messaging app. This means that every messaging
server thread has its own set of data that is written to conversation files whenever an action is performed and writes user data to UserInfo.txt when user logs out.
<br />

Testing for this class was done as it was being developed for each feature and was later tested with test cases when  the project was reaching completion.
These test cases checked for proper functionality of advanced features like concurrency and netIO working properly.

### MessagingApp.java
Class contains all the GUI functionality and handles displaying information from server to the user. Most of the other classes are not connected directly,
instead, all data handling methods are called in server and this data is then sent to MessagingApp.java. Additionally, this class takes
inputs from user in order to change user information like e-mail and stores (for sellers).
<br />

Testing for this class was done as it was being developed on a case by case basis and was then done through the usage of test cases
that checked proper functionality of user input events and other advanced implementations.

### Customer.java
Class used to represent a customer account and includes their username, password, an array of previous messages, account email and blocked users. This class contains the updateInfo
method which is used by main to update user information after a new conversation or message has been created. 
<br />

Testing only done for updateInfo to make sure information is stored correctly.

### Seller.java
Class used similarly to Customer.java to represent a seller account. Seller accounts include their username, password, previous messages, stores, account email and blocked users. Also contains
updateInfo method used by main to update user information after a new conversation or message has been created.
<br />

Testing only done for update info to make sure information is stored correctly, slightly different implementation from customer account.
