package shared;

public class Screen {

    public static void printLoginMenu() {
        System.out.println("+---------------------------------------------+\n" +
                "|              Welcome in MailBox!            |\n" +
                "|                                             |\n" +
                "| Select:                                     |\n" +
                "|                                             |\n" +
                "| Register                                    |\n" +
                "| Login                                       |\n" +
                "| Uptime                                      |\n" +
                "| Info                                        |\n" +
                "| Help                                        |\n" +
                "| Exit                                        |\n" +
                "+---------------------------------------------+");
        System.out.print("Select an option: ");
    }

    public static void printAdminMailBoxMenu() {
        System.out.println("+---------------------------------------------+\n" +
                "|              You are logged in              |\n" +
                "|                                             |\n" +
                "| Select:                                     |\n" +
                "|                                             |\n" +
                "| Write mail                                  |\n" +
                "| Mailbox                                     |\n" +
                "| Update                                      |\n" +
                "| Switch                                      |\n" +
                "| Logout                                      |\n" +
                "+---------------------------------------------+");
        System.out.print("Select an option: ");
    }

    public static void printUserMailBoxMenu() {
        System.out.println("+---------------------------------------------+\n" +
                "|              You are logged in              |\n" +
                "|                                             |\n" +
                "| Select:                                     |\n" +
                "|                                             |\n" +
                "| Write mail                                  |\n" +
                "| Mailbox                                     |\n" +
                "| Logout                                      |\n" +
                "+---------------------------------------------+");
        System.out.print("Select an option: ");
    }
}
