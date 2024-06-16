# Client-Server Application 

Welcome to the Client-Server Application with Java. This project demonstrates a simple client-server architecture in Java, 
showcasing communication between a client and a server through socket connections. The project is under continuous development, 
with plans to add more features and improvements in future updates.

## Project Structure
The project is divided into several key components:

Client: The client-side application that establishes a connection with the server, sends requests, and receives responses.
Server: The server-side application that handles incoming client connections, processes requests, and sends back responses.
Utils: Various utility classes for handling tasks like JSON conversion, user interaction, and screen displaying.
User Management: Contains classes and functionality related to users, including user data, admin handling, and user management.
Mail System: Contains the mail-related features, with classes that manage mail details and individual mailboxes.

## Project Overview
The application allows the client to send various requests to the server, which processes them and responds accordingly. 
Here are the main features:

Authentication: The client can register, log in, and log out. The server handles authentication and authorization.
Mailbox Operations: The client can send messages, read from the mailbox, and perform other related operations.
Admin Functions: The server includes admin capabilities for updating user data, managing user roles, and other administrative tasks.
Server Information: The server can provide information such as uptime, version, and available commands.

## How to Run

To run the client-server application, ensure you have the Java Development Kit (JDK) installed on your system. 
Follow these steps:

### Clone this repository to your computer:
<https://github.com/jakubBone/Client-Server>

### Navigate to the project directory:
cd Client-Server

### Compile and run the server:
javac Server.java
java Server

### Compile and run the client:
javac Client.java
java Client

## Requirements
To compile and run the application, you'll need Java Development Kit (JDK) installed on your system

## Future Developments
This project is a work in progress, with ongoing updates focused on enhancing functionality. 
Future updates will aim to improve the security, and add more advanced features such as
enhanced error handling and logging, more robust mailbox operations, additional admin functionalities 
for user management and change the data storage approach to use a database system. 

## Additional Information
Logging: The application uses the Log4j2 library for logging.
JSON Handling: The Gson library is used for JSON conversion of data between the client and server.
Unit Testing: The project includes JUnit tests for key components to ensure code quality and functionality.
