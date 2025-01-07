# ✉️ Client-Server Mailing System

[![Watch the video](src/main/resources/images/logo.png)](https://www.youtube.com/watch?v=dmwIVkuxnnA&t=253s)

Welcome to the Client-Server Mailing System! This project demonstrates a simple client-server architecture
that imitates an email system, showcasing communication between a client and a server using socket connections. 
It offers robust features such as user management, mailbox operations, and message handling.


## 🎯 Features
The project is divided into several key components:

- **User Management**: Registration, login, password changes, user remove

- **Mailbox Operations**: Send, read, delete emails, and manage mailbox capacity

- **Roles and Permissions**: Admin and User roles with different access rights

- **Client-Server Communication**: Real-time interaction via sockets

- **Database Integration**: User and email data persisted in an SQLite database


## 🚀 Technologies Used

**Java 17**: Core programming language for client-server logic

**SQLite**: Database for persisting user and email data

**jOOQ**: Library for database interactions

**Log4j2**: Logging system for debugging and monitoring

**JUnit**: Unit testing 

**BCrypt**: Secure password hashing


## 📂 Project Structure

```
src
├── client                # Client-side logic
├── server                # Server-side logic
├── database              # Database access and operations
├── mail                  # Email handling logic
├── request               # Request creation and handling
├── response              # Response creation and processing
├── shared                # Common utilities and components
└── user                  # User management and authentication
``` 

## 🚀 Getting Started

Follow these steps to set up and run the project:

### Ensure you have the following tools installed:
- **Java Development Kit (JDK)** 17 or higher
- **Gradle** for dependency management
- **SQLite** database library

### Setup Instructions

1. **Clone the Repository**  
   Download the project files to your local machine:
   ```bash
   git clone https://github.com/your-username/Client-Server.git
   cd Client-Server

2. **Configure the Database**  
   - Ensure the database directory exists: src/main/resources/db
   - SQLite database will automatically be initialized during the first run
   
3. **Build the Project**   
   Use Gradle to build the project:
   ```bash
   ./gradlew build

4. **Run the Server** to handle client requests:   
   Start the server to manage plane communications:
   ```bash
   java -cp build/classes/java/main server.Server

5. **Run the Client** to connect to the server:
   Simulate planes connecting to the server:
   ```bash
   java -cp build/classes/java/main/client.Client
 
## ✨ Key Functionalities

### Client
- Displays menu based on login status and role
- Allows operations like login, registration, mailbox handling, and user management

### Server
- Processes client requests using a factory-based architecture
- Manages user authentication, email transactions, and administrative operations

### Database
- Handles user credentials and emails
- Enforces mailbox size limits for efficient management

## 📧 Contact

If you have any questions, feedback, or suggestions, feel free to reach out to me:

- **Email**: [jakub.bone1990@gmail.com](mailto:jakub.bone1990@gmail,com)
- **Blog**: [javamPokaze.pl](https://javampokaze.pl)  
- **LinkedIn**: [Jakub Bone](https://www.linkedin.com/in/jakub-bone)  

Let's connect and discuss this project further! 🚀
