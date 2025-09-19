// package auth;  // 👉 mets le bon package si besoin

import model.Client;
import model.Hotel;
import repository.InMemoryClientRepository;
import repository.InMemoryHotelRepository;
import service.AuthService;
import service.HotelService;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Dépendances
        InMemoryClientRepository clientRepo = new InMemoryClientRepository();
        InMemoryHotelRepository hotelRepo = new InMemoryHotelRepository();

        AuthService authService = new AuthService(clientRepo);
        HotelService hotelService = new HotelService(hotelRepo);

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
                System.out.println("3) Gestion des hôtels");
                System.out.println("4) Logout");
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
                            hotelMenu(scanner, hotelService);
                            break;
                        case "4":
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

    // === Sous-menu gestion des hôtels ===
    private static void hotelMenu(Scanner scanner, HotelService hotelService) {
        while (true) {
            System.out.println("\n=== Gestion des Hôtels ===");
            System.out.println("1) Ajouter un hôtel");
            System.out.println("2) Lister les hôtels");
            System.out.println("3) Mettre à jour un hôtel");
            System.out.println("4) Supprimer un hôtel");
            System.out.println("5) Retour");
            System.out.print("Choix: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    System.out.print("Nom: ");
                    String name = scanner.nextLine();
                    System.out.print("Adresse: ");
                    String address = scanner.nextLine();
                    System.out.print("Nombre de chambres: ");
                    int rooms = Integer.parseInt(scanner.nextLine());
                    System.out.print("Note (0-5): ");
                    double rating = Double.parseDouble(scanner.nextLine());

                    hotelService.createHotel(name, address, rooms, rating);
                    System.out.println("Hôtel ajouté ✅");
                    break;

                case "2":
                    System.out.println("\n--- Liste des hôtels ---");
                    hotelService.listAllHotels().forEach(h ->
                        System.out.println(h.getHotelId() + " | " + h.getName() + " | " +
                                           h.getAddress() + " | Chambres: " + h.getAvailableRooms() +
                                           " | Note: " + h.getRating()));
                    break;

                case "3":
                    System.out.print("ID de l'hôtel à mettre à jour: ");
                    String updateId = scanner.nextLine();
                    System.out.print("Nouveau nom: ");
                    String newName = scanner.nextLine();
                    System.out.print("Nouvelle adresse: ");
                    String newAddress = scanner.nextLine();
                    System.out.print("Nouveau nombre de chambres: ");
                    int newRooms = Integer.parseInt(scanner.nextLine());

                    boolean updated = hotelService.updateHotel(updateId, newName, newAddress, newRooms);
                    if (updated) {
                        System.out.println("Hôtel mis à jour ✅");
                    } else {
                        System.out.println("Hôtel introuvable ❌");
                    }
                    break;

                case "4":
                    System.out.print("ID de l'hôtel à supprimer: ");
                    String deleteId = scanner.nextLine();
                    boolean deleted = hotelService.deleteHotel(deleteId);
                    if (deleted) {
                        System.out.println("Hôtel supprimé ✅");
                    } else {
                        System.out.println("Impossible de supprimer (inexistant ou réservé) ❌");
                    }
                    break;

                case "5":
                    return; // retour au menu utilisateur

                default:
                    System.out.println("Choix invalide.");
            }
        }
    }
}
