package utils;

import mail.MailBox;
import mail.MailService;
import user.User;
import user.UserManager;

import java.util.Scanner;

public class Screen {

    public static String printLoginMenu(User user) {
        String menu = "+---------------------------------------------+\n" +
                "|              Welcome in MailBox!            |\n" +
                "|                                             |\n" +
                "| Select:                                     |\n" +
                "|                                             |\n" +
                "| Register                                 |\n" +
                "| Login                                    |\n";
        if (user.isUserLoggedIn()) {
            menu += "| Logout                                   |\n";
        }
        menu += "| Help                                     |\n" +
                "| Exit                                     |\n" +
                "+---------------------------------------------+";
        return menu;
    }

    public static String printMailBoxMenu() {
        return  "+---------------------------------------------+\n" +
                "|              You are logged in              |\n" +
                "|                                             |\n" +
                "| Select:                                     |\n" +
                "|                                             |\n" +
                "| 1. Write mail                               |\n" +
                "| 2. Read mails                               |\n" +
                "| 3. Sent mails                               |\n" +
                "| 4. Logout                                  |\n" +
                "+---------------------------------------------+";
    }

    public void handleScreen(User user, UserManager userManager, MailBox mailBox, MailService mailService) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println(printLoginMenu(user));
            System.out.print("Type choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "Register":
                    // registerUser();
                    break;
                case "Login":
                    //loginUser();
                    break;
                case "Logout":
                    //logoutUser();
                    break;
                case "4":
                    //showHelp();
                    break;
                case "exit":
                    System.out.println("Good bye!");
                    break;
                default:
                    System.out.println("Incorrect input, try arain");
            }
        }
    }
}
