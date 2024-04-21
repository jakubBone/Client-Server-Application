package utils;

public class Screen {

    /*
     * The Screen class is used to display various menu screens to the console
     * It provides static methods for printing different menu options
     */
    public static void printLoginMenu() {
        System.out.println("+---------------------------------------------+\n" +
                "|              Welcome in MailBox!            |\n" +
                "|                                             |\n" +
                "| Select:                                     |\n" +
                "|                                             |\n" +
                "| Register                                    |\n" +
                "| Login                                       |\n" +
                "| Help                                        |\n" +
                "| Exit                                        |\n" +
                "+---------------------------------------------+");
        System.out.print("Type: ");
    }

    public static void printMailBoxMenu() {
        System.out.println("+---------------------------------------------+\n" +
                "|              You are logged in              |\n" +
                "|                                             |\n" +
                "| Select:                                     |\n" +
                "|                                             |\n" +
                "| Write mail                                  |\n" +
                "| Mailbox                                     |\n" +
                "| Update                                      |\n" +
                "| Logout                                      |\n" +
                "+---------------------------------------------+");
        System.out.print("Type: ");
    }
}
