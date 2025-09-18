import java.util.*;
import java.util.regex.Pattern;

public class AuthModule {

    static class Client {
        enum Role { USER, ADMIN }

        private UUID id;
        private String fullName;
        private String email;
        private String password;
        private Role role;

        public Client(String fullName, String email, String password, Role role) {
            this.id = UUID.randomUUID();
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.role = role;
        }

        public UUID getId() { return id; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }

        @Override
        public String toString() {
            return String.format("%s (%s) - %s", fullName, id.toString(), email);
        }
    }

    interface ClientRepository {
        void save(Client client);
        Optional<Client> findByEmail(String email);
        Optional<Client> findById(UUID id);
        void update(Client client);
    }

    static class InMemoryClientRepository implements ClientRepository {
        private final Map<UUID, Client> store = new HashMap<>();

        @Override
        public void save(Client client) { store.put(client.getId(), client); }

        @Override
        public Optional<Client> findByEmail(String email) {
            return store.values().stream()
                    .filter(c -> c.getEmail().equalsIgnoreCase(email))
                    .findFirst();
        }

        @Override
        public Optional<Client> findById(UUID id) { return Optional.ofNullable(store.get(id)); }

        @Override
        public void update(Client client) { store.put(client.getId(), client); }
    }

    static class AuthService {
        private final ClientRepository clientRepo;

        public AuthService(ClientRepository clientRepo) { this.clientRepo = clientRepo; }

        public Client register(String fullName, String email, String password, Client.Role role) throws IllegalArgumentException {
            if (fullName == null || fullName.trim().isEmpty())
                throw new IllegalArgumentException("Le nom est requis.");
            if (!Validation.isValidEmail(email))
                throw new IllegalArgumentException("Email invalide.");
            if (password == null || password.length() < 6)
                throw new IllegalArgumentException("Mot de passe ≥ 6 caractères requis.");
            if (clientRepo.findByEmail(email).isPresent())
                throw new IllegalArgumentException("Email déjà utilisé.");

            Client c = new Client(fullName.trim(), email.trim().toLowerCase(), password, role);
            clientRepo.save(c);
            return c;
        }

        public Client login(String email, String password) throws IllegalArgumentException {
            Optional<Client> maybe = clientRepo.findByEmail(email);
            if (maybe.isEmpty()) throw new IllegalArgumentException("Email inconnu.");
            Client c = maybe.get();
            if (!c.getPassword().equals(password))
                throw new IllegalArgumentException("Mot de passe incorrect.");
            return c;
        }

        public void updateEmail(Client client, String newEmail) {
            if (!Validation.isValidEmail(newEmail))
                throw new IllegalArgumentException("Email invalide.");
            Optional<Client> other = clientRepo.findByEmail(newEmail);
            if (other.isPresent() && !other.get().getId().equals(client.getId()))
                throw new IllegalArgumentException("Email déjà utilisé.");
            client.setEmail(newEmail.toLowerCase());
            clientRepo.update(client);
        }

        public void changePassword(Client client, String newPassword) {
            if (newPassword == null || newPassword.length() < 6)
                throw new IllegalArgumentException("Mot de passe ≥ 6 caractères requis.");
            client.setPassword(newPassword);
            clientRepo.update(client);
        }
    }

    static class Validation {
        private static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        public static boolean isValidEmail(String email) {
            return email != null && EMAIL.matcher(email).matches();
        }
    }

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
                            // here
                            Client c = authService.register(name, email, pwd, Client.Role.USER);
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
