package ui;

import user.credential.User;

import java.util.List;

public class Screen {

    public static void printMainScreen() {
        System.out.println("+---------------------------------------------+\n" +
                "|              WELCOME IN MAILBOX!               |\n" +
                "|                                                |\n" +
                "| Select:                                        |\n" +
                "|                                                |\n" +
                "| 1. Register                                    |\n" +
                "| 2. Login                                       |\n" +
                "| 3. Uptime                                      |\n" +
                "| 4. Info                                        |\n" +
                "| 5. Help                                        |\n" +
                "| 6. Exit                                        |\n" +
                "+---------------------------------------------+");
        System.out.print("Select an option: ");
    }

    public static void printUserScreen() {
        System.out.println("+---------------------------------------------+\n" +
                "|                     USER                      |\n" +
                "|                                               |\n" +
                "| Select:                                       |\n" +
                "|                                               |\n" +
                "|1. Write mail                                  |\n" +
                "|2. Mailbox                                     |\n" +
                "|3. Logout                                      |\n" +
                "+---------------------------------------------+");
        System.out.print("Select an option: ");
    }

    public static void printAdminScreen() {
        System.out.println("+---------------------------------------------+\n" +
                "|                     ADMIN                     |\n" +
                "|                                               |\n" +
                "| Select:                                       |\n" +
                "|                                               |\n" +
                "|1. New e-mail                                  |\n" +
                "|2. Inbox                                       |\n" +
                "|3. Sent                                        |\n" +
                "|4. Delete                                      |\n" +
                "|5. Edit profile                                |\n" +
                "|6. Logout                                      |\n" +
                "+---------------------------------------------+");
        System.out.print("Select an option: ");
    }

    public static void printEditScreen() {
        System.out.println("+---------------------------------------------+\n" +
                "|                     ADMIN                     |\n" +
                "|                                               |\n" +
                "| Select:                                       |\n" +
                "|                                               |\n" +
                "|1. Change password                             |\n" +
                "|2. Change role                                 |\n" +
                "|3. Remove user                                 |\n" +
                "|4. Switch user                                 |\n" +
                "|5. Return                                      |\n" +
                "+---------------------------------------------+");
        System.out.print("Select an option: ");
    }

    public static void printUsers(List<User> users) {
        System.out.println("+---------------------------------------------+\n" +
                "|                     ADMIN                     |\n" +
                "|                                               |\n" +
                "| Select:                                       |\n" +
                "|                                               |\n" +
                "|1. Change password                             |\n" +
                "|2. Change role                                 |\n" +
                "|3. Remove user                                 |\n" +
                "|4. Switch user                                 |\n" +
                "|5. Return                                      |\n" +
                "+---------------------------------------------+");
        System.out.print("Select an option: ");
    }
}