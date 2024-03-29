package utils;

import java.util.Scanner;

public class IOHandler {
    Screen screen;
    Scanner scanner;
    public IOHandler() {
        this.screen = new Screen();
        this.scanner = new Scanner(System.in);
    }

    public String takeUsernameFromClient() {
        return "Type username: " + scanner.nextLine();
    }

    public String takePasswordFromClient() {
        return "Type password: " + scanner.nextLine();
    }

}
