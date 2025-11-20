package ui;

import model.LoginRequest;
import model.RegisterRequest;
import serverFacade.ServerFacade;

import java.util.Scanner;

public class PreLoginUI {

    ServerFacade serverFacade;

    Scanner scanner;

    public PreLoginUI(ServerFacade serverFacade){
        this.serverFacade = serverFacade;
        scanner = new Scanner(System.in);
    }

    public void run(){
        boolean running = true;

        while (running){
            System.out.println("Prelogin\n Enter a command please:\n");
            String command = scanner.nextLine();

            switch (command){
                case "help":
                    displayHelpMenu();
                    break;
                case "register":
                    register();
                    break;
                case "login":
                    login();
                    break;
                case "quit":
                    running = false;
                    break;
            }

        }

    }

    private void displayHelpMenu(){
        System.out.println("Available Commands:\n" +
                "help - Display this help menu\n" +
                "register - Register an account\n" +
                "login - Login user\n" +
                "quit - Exit");
    }

    private void register(){
        System.out.println("Create Username:");
        String username = scanner.nextLine();
        System.out.println("Create Password:");
        String password = scanner.nextLine();
        System.out.println("Enter email:");
        String email = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty()){
            System.out.println("Invalid credentials");
            return;
        }

        serverFacade.register(new RegisterRequest(username, password, email));

    }

    private void login(){
        System.out.println("Enter username:");
        String username = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();

        if (username.isEmpty() || password.isEmpty()){
            System.out.println("Invalid username or password");
            return;
        }

        serverFacade.login(new LoginRequest(username, password));

    }

}
