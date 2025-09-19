package repository;

import model.Hotel;
import java.util.*;

public class InMemoryHotelRepository implements HotelRepository {

    private final Map<String, Hotel> storage = new HashMap<>();

    @Override
    public void save(Hotel hotel) {
        storage.put(hotel.getHotelId(), hotel);
    }

    @Override
    public Optional<Hotel> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Hotel> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(String id) {
        storage.remove(id);
    }
}
