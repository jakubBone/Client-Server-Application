package utils;

public class Screen {

    public String printLoginMenu() {
        return  "+---------------------------------------------+\n" +
                "|              Welcome in MailBox!            |\n" +
                "|                                             |\n" +
                "| Select:                                     |\n" +
                "|                                             |\n" +
                "| Register                                    |\n" +
                "| Login                                       |\n" +
                "| Logout                                      |\n" +
                "| Help                                        |\n" +
                "| Exit                                        |\n" +
                "+---------------------------------------------+";
    }

    public String printMailBoxMenu() {
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
}
