# ✉️ Client-Server Application

[![Watch the video](src/main/resources/image/logo.png)](https://www.youtube.com/watch?v=dmwIVkuxnnA&t=253s)

Welcome to the Client-Server App! This project demonstrates a simple client-server architecture
that imitates an email system, showcasing communication between a client and a server using sockets. 
It offers robust features such as user management, mailbox operations, and message handling.


## 🎯 Features
The project is divided into several key components:

- **User Management**: Registration, login, password changes, user remove

- **Mailbox Operations**: Send, read, delete emails, and manage mailbox capacity

- **Roles and Permissions**: Admin and User roles with different access rights

- **Client-Server Communication**: Real-time interaction via sockets

- **Database Integration**: User and email data persisted in an PosgtreSQL database


## 🚀 Technologies and Libraries Used

**Java 17**: Core programming language for client-server logic

**PostgreSQL**: Database for persisting user and email data

**JOOQ**: Library for database interactions

**Docker**: Containerization

**Gradle**: Building and dependency management

**Log4j2**: Logging system for debugging and monitoring

**JUnit**: Unit testing 

**BCrypt**: Secure password hashing


## 📂 Project Structure

```
.
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com/jakub/bone/command           # Client commands and server handlers                     
│   │   │   ├── com/jakub/bone/controller        # Client app launcher & controller
│   │   │   ├── com/jakub/bone/data              # DataSource & database initialization
│   │   │   ├── com/jakub/bone/domain            # Domain models (User, Mail, Admin)
│   │   │   ├── com/jakub/bone/network           # Client/server connection management
│   │   │   ├── com/jakub/bone/repository        # Database repositories (User, Mail)
│   │   │   ├── com/jakub/bone/server            # Server launcher & request processor
│   │   │   ├── com/jakub/bone/session           # Session management
│   │   │   ├── com/jakub/bone/ui                # Console-based user interface
│   │   │   └── com/jakub/bone/utils             # Utility classes (ConfigLoader, JsonConverter, etc.)
│   │   └── resources                        # Resources 
│   └── test                                 # Unit & integration tests
├── Dockerfile                               # Builds the JAR into a container image
├── docker-compose.yml                       # Container orchestration
├── build.gradle                             # Build configuration
└── ...                                    
``` 

## 🚀 Getting Started

Follow these steps to set up and run the project:

### Ensure you have the following tools installed:
- **Java Development Kit (JDK)** 17 or higher
- **Gradle 8.5** for dependency management
- **PostgreSQL** database 
- **Docker and Docker Compose** for containerization

### Setup Instructions

1. **Clone the Repository**  
   Download the project files to your local machine:
   ```bash
   git clone https://github.com/your-username/Client-Server.git
   cd Client-Server

2. **Configure the Database**  
   Set up a PostgreSQL database:
   - Create new data base 
   - Update gradle JOOQ configuration with your database credentials 
   - Update `config.properties` file with your database credentials
   
3. **Build the Project**   
   Use Gradle to build the project and the shadow JAR. The JAR will be located under build/libs/ServerLauncher.jar:
   ```bash
   ./gradlew build
   ./gradlew shadowJar

### Containerized Deployment

The application is fully containerized using Docker. 
A sample docker-compose.yml is provided to orchestrate both the server app and the PostgreSQL database

1. **Configure the Environment**   
    Edit the config.properties if needed to update database credentials
	
2. **Docker Desktop**     
	Ensure that Docker Desktop is running before building and running the containers.

3. **Run Docker Compose**   
   From the project root, run:
   ```bash
   docker-compose up --build
   ```
   This command will:
   - Build nad run the PostgreSQL container (using the postgres:15 image) with pre-configured credentials
   - Build and run the server container (start to listen on port 5000)
   
4. **Expose the Application**  
   The application will be accessible at `http://localhost:5000`
  

5. **Run the Client**
   In a separate terminal window, start the client application:
   ```bash
   java -cp build/classes/java/main com.jakub.bone.controller.ClientApp
 
## ✨ Key Functionalities

### Client-Side
- **User Interface:**  
  - Dynamic menus that change based on login status and role (User or Admin)
  - Options for registration, login, email operations, and administrative tasks
  
- **Command Handling:**  
  - Commands (e.g., LOGIN, REGISTER, READ, DELETE) 
  
- **Input & Output:**  
  - Interactive console screens guide users through email operations and system commands

### Server-Side
- **Request Processing:**  
  - A modular architecture that uses command handlers to process client requests
  - Centralized `RequestProcessor` and `CommandHandlerFactory`
  
- **Business Logic:**  
  - Authentication and email operations handled by dedicated services
  - Administrative commands allow for user management and system diagnostics (e.g., server uptime and info)

### Database & Persistence
- **PostgreSQL with jOOQ:**  
  - User credentials and email data are stored persistently
  - Automatic table creation and initialization streamline the setup process

### Communication & Serialization
- **Real-Time Messaging:**  
  - Socket connections enable seamless client-server communication
  - JSON-based message serialization and deserialization

## 📧 Contact

If you have any questions, feedback, or suggestions, feel free to reach out to me:

- **Email**: [jakub.bone1990@gmail.com](mailto:jakub.bone1990@gmail,com)
- **Blog**: [javamPokaze.pl](https://javampokaze.pl)  
- **LinkedIn**: [Jakub Bone](https://www.linkedin.com/in/jakub-bone)  

Let's connect and discuss this project further! 🚀
