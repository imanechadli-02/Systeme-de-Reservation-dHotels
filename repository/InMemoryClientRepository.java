package repository;

import model.Client;
import java.util.*;

public class InMemoryClientRepository implements ClientRepository {
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
    public Optional<Client> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public void update(Client client) { store.put(client.getId(), client); }
}
