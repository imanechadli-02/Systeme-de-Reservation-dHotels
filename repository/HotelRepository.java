package repository;

import model.Hotel;
import java.util.List;
import java.util.Optional;

public interface HotelRepository {
    void save(Hotel hotel);
    Optional<Hotel> findById(String id);
    List<Hotel> findAll();
    void deleteById(String id);
}
