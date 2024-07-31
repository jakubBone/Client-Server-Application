# Client-Server Application 

Welcome to the Client-Server Application with Java. This project demonstrates a simple client-server architecture in Java, 
showcasing communication between a client and a server through socket connections. The project is under continuous development, 
with plans to add more features and improvements in future updates.


## Project Structure
The project is divided into several key components:

Client: handles user interactions and communicates with the server.

Server: Manages client connections and processes requests.

Database: Interfaces with the database for storing and retrieving data.

Mail: Manages email-related functionalities.

Request: Defines request types and factories.

Response: Handles responses to client requests.

Shared: Contains shared utilities and components.

User: Manages user credentials and roles.


## Project Overview
The application allows the client to send various requests to the server, which processes them and responds accordingly. 
Here are the main features:

Authentication: The client can register, log in, and log out. The server handles authentication and authorization.

Mailbox Operations: The client can send messages, read from the mailbox, and perform other related operations.

Admin Functions: The server includes admin capabilities for updating user data, managing user roles, and other administrative tasks.

Database Operations: The application uses a database to manage user data and mailbox information, leveraging JOOQ for efficient database interaction.

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
for user management.


## Additional Information
SQLite: Database used for storing users and emails data.

Gson: Library used for JSON parsing.

Log4j2: Logging framework for tracking application behavior.

JUnit & Mockito: Libraries used for unit testing and mocking dependencies.

JOOQ: Used for typesafe SQL query construction and execution.

BCrypt: Used for securely hashing user passwords.