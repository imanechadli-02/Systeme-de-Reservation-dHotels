// package auth;

import model.Client;
import repository.InMemoryClientRepository;
import service.AuthService;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        InMemoryClientRepository clientRepo = new InMemoryClientRepository();
        AuthService authService = new AuthService(clientRepo);

        Client current = null;

        outer: while (true) {
            if (current == null) {
                System.out.println("\n=== Bienvenue ===");
                System.out.println("1) S'inscrire");
                System.out.println("2) Se connecter");
                System.out.println("3) Quitter");
                System.out.print("Choix: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        try {
                            System.out.print("Nom complet: ");
                            String name = scanner.nextLine();
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Mot de passe (≥6): ");
                            String pwd = scanner.nextLine();
                            authService.register(name, email, pwd, Client.Role.USER);
                            System.out.println("Inscription réussie. Connectez-vous maintenant !");
                        } catch (Exception e) {
                            System.out.println("Erreur: " + e.getMessage());
                        }
                        break;
                    case "2":
                        try {
                            System.out.print("Email: ");
                            String email = scanner.nextLine();
                            System.out.print("Mot de passe: ");
                            String pwd = scanner.nextLine();
                            current = authService.login(email, pwd);
                            System.out.println("Connecté en tant que " + current.getFullName());
                        } catch (Exception e) {
                            System.out.println("Erreur: " + e.getMessage());
                        }
                        break;
                    case "3":
                        System.out.println("Au revoir 👋");
                        break outer;
                    default:
                        System.out.println("Choix invalide.");
                }
            } else {
                System.out.println("\n=== Menu Utilisateur (" + current.getFullName() + ") ===");
                System.out.println("1) Changer email");
                System.out.println("2) Changer mot de passe");
                System.out.println("3) Logout");
                System.out.print("Choix: ");
                String choice = scanner.nextLine().trim();

                try {
                    switch (choice) {
                        case "1":
                            System.out.print("Nouvel email: ");
                            String newEmail = scanner.nextLine();
                            authService.updateEmail(current, newEmail);
                            System.out.println("Email mis à jour ✅");
                            break;
                        case "2":
                            System.out.print("Nouveau mot de passe (≥6): ");
                            String newPwd = scanner.nextLine();
                            authService.changePassword(current, newPwd);
                            System.out.println("Mot de passe mis à jour ✅");
                            break;
                        case "3":
                            current = null;
                            System.out.println("Déconnecté.");
                            break;
                        default:
                            System.out.println("Choix invalide.");
                    }
                } catch (Exception e) {
                    System.out.println("Erreur: " + e.getMessage());
                }
            }
        }

        scanner.close();
    }
}
