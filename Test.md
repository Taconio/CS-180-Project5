# Project 5 Testing

## Test 1: Login when user exists

1. Launches Messaging application.
2. Select if user is a customer or seller.
3. Welcome Customer/Seller! Login or Create account
4. Correctly introduce Username and Password
5. Clicks login button and access the app.

**Expectation**: User logs correctly into the application

**TEST PASSED**

## Test 2: Login when user does not exist (new account)

1. Launches Messaging application.
2. Select if user is a customer or seller.
3. Welcome Customer/Seller! Login or Create account
4. Click create new account button
5. Fill out the information required -> username, password, email, and store name (only for seller).
6. Clicks create button and access the application.

**Expectation**: A new user is created and access the application

**TEST PASSED**

## Test 3: Send messages for new conversation

1. After login, select “seller/customer to message”
2. Conversation Loads in text box
3. Type message to send in message box
4. Click Send
5. Conversation is updated in txt file (customer1&seller1.txt) and in text box

**Expectation**: Message is saved correctly and displays in box and in txt file between the 2 users

**TEST PASSED**

## Test 4: Send messages when conversation exists

1. After login, select “Start new conversation with Seller/Customer”
2. Type message to send in message box
3. Click Send
4. Conversation is updated in txt file (customer1&seller1.txt) and in text box

**Expectation**: Message is saved correctly and displays in box and in txt file between the 2 users

**TEST PASSED**

## Test 5: Change user email

1. Login
2. Click Change Email Button
3. Enter new email and click Ok.

**Expectation**: The email is updated and saved in the UserInfo.txt file after logout

**TEST PASSED**

## Test 6: Export CSV File

1. Login
2. Select existing conversation with Customer/Seller
3. Click Export CSV file

**Expectation**: New .csv file is created or updated (after logout) with the contents of the preexisting conversations including participants, timestamp, sender, and contents.

**TEST PASSED**

## Test 7: Seller adds new store

1. Login
2. Click New Store button
3. Introduce name of new store and click ok

**Expectation**: New store is added to the User in UserInfo.txt

**TEST PASSED**

## Test 8: Import and send file

1. Login
2. Select customer/store/seller to message
3. Click Import and send button
4. Enter a valid file name
5. The text inside the file will be read and sent to the selected user

**Expectation**: The contents of the file introduced displays in the text box as a message and it is saved in the message .txt file between the two users.

**TEST PASSED**

## Test 9: Conversation statistics (Customer) – By messages received

1. Login
2. Select user to load conversation with
3. Click sort stats by messages received

**Expectation**: A statistics window will pop up, it will show in numerical order, the seller/s and their stores, containing also the number of messages received.

**TEST PASSED**
## Test 10: Conversation statistics (Customer) – By messages sent

1. Login
2. Select user to load conversation with
3. Click sort stats by messages sent

**Expectation**: A statistics window will pop up, it will show in numerical order, the seller/s and their stores, containing also the number of messages sent.

**TEST PASSED**

## Test 11: Conversation statistics (Seller)

1. Login
2. Select user to load conversation with
3. Click sort stats by messages received or sorted stats

**Expectation**: A statistics window will pop up, it will show in numerical order the Customer username, messages received, and most commonly used words.

**TEST PASSED**

## Test 12: Edit Message and refresh button

1. Login
2. Select user to load conversation with
3. Click edit
4. Introduce the message to be replaced
5. Type the new message
6. Click Ok
7. Press refresh chat button

**Expectation**: The message should be replaced and displayed in the text box after using the refresh button, also updated in the .txt conversation file between the two users.

**TEST PASSED**

## Test 13: Block user

1. Login
2. Load existing conversation with user
3. Click block user
4. Type username to block

**Expectation**: Now, the blocked user will not display as a possible user to message, and the conversation will disappear from the text box. Also, the blocked user cannot message the user who blocked him.

**TEST PASSED**

## Test 14: Unblock user

1. Login
2. Load existing conversation with user
3. Click block user
4. Type username to unblock
5. Logout

**Expectation**: Now, the blocked user will be unblocked and conversation features should display again after logout

**TEST PASSED**
