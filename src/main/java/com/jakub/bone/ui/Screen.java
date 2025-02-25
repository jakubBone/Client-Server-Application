package com.jakub.bone.ui;

public class Screen {

    public void printClientUI(boolean isLogged, boolean isAuthorized){
        if(!isLogged) {
            Screen.printMainScreen();
        } else {
            if(isAuthorized){
                Screen.printAdminScreen();
            } else{
                Screen.printUserScreen();
            }
        }
    }

    public static void printMainScreen() {
        System.out.println("+---------------------------------------------+\n" +
                "|              WELCOME IN MAILBOX!               |\n" +
                "|                                                |\n" +
                "| 1. Register                                    |\n" +
                "| 2. Login                                       |\n" +
                "| 3. Uptime                                      |\n" +
                "| 4. Info                                        |\n" +
                "| 5. Help                                        |\n" +
                "| 6. Exit                                        |\n" +
                "+---------------------------------------------+");
        System.out.print("Select: ");
    }

    public static void printUserScreen() {
        System.out.println("+---------------------------------------------+\n" +
                "|                     USER                      |\n" +
                "|                                               |\n" +
                "|1. New e-mail                                  |\n" +
                "|2. Read e-mails                                |\n" +
                "|3. Delete e-mails                              |\n" +
                "|4. Logout                                      |\n" +
                "+---------------------------------------------+");
        System.out.print("Select: ");
    }

    public static void printAdminScreen() {
        System.out.println("+---------------------------------------------+\n" +
                "|                     ADMIN                     |\n" +
                "|                                               |\n" +
                "|1. New e-mail                                  |\n" +
                "|2. Read e-mails                                |\n" +
                "|3. Delete e-mails                              |\n" +
                "|4. Edit users                                  |\n" +
                "|5. Logout                                      |\n" +
                "+---------------------------------------------+");
        System.out.print("Select: ");
    }


    public static void printEditScreen() {
        System.out.println("+---------------------------------------------+\n" +
                "|                     ADMIN                     |\n" +
                "|                                               |\n" +
                "|1. Change password                             |\n" +
                "|2. Assign role                                 |\n" +
                "|3. Remove user                                 |\n" +
                "|4. Switch user                                 |\n" +
                "+---------------------------------------------+");
        System.out.print("Select: ");
    }

    public static void printMailboxScreen() {
        System.out.println("+---------------------------------------------+\n" +
                "|                  MAILBOXES                    |\n" +
                "|                                               |\n" +
                "|1. Inbox                                       |\n" +
                "|2. Sent                                        |\n" +
                "+---------------------------------------------+");
        System.out.print("Select: ");
    }

    public static void printResponse(String response) {
        System.out.println("===============================================");
        System.out.println(response);
        System.out.println("===============================================\n");
    }
}