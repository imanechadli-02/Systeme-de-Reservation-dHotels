package repository;

import model.Client;
import java.util.*;

public interface ClientRepository {
    void save(Client client);
    Optional<Client> findByEmail(String email);
    Optional<Client> findById(UUID id);
    void update(Client client);
}
