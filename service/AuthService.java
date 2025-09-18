package service;

import model.Client;
import repository.ClientRepository;
import util.Validation;
import java.util.Optional;

public class AuthService {
    private final ClientRepository clientRepo;

    public AuthService(ClientRepository clientRepo) { this.clientRepo = clientRepo; }

    public Client register(String fullName, String email, String password, Client.Role role) {
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

    public Client login(String email, String password) {
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
