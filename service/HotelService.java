package service;

import model.Hotel;
import repository.HotelRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HotelService {

    private final HotelRepository hotelRepo;

    public HotelService(HotelRepository hotelRepo) {
        this.hotelRepo = hotelRepo;
    }

    // --- Création d’hôtel ---
    public Hotel createHotel(String name, String address, int rooms, double rating) {
        Hotel hotel = new Hotel(name, address, rooms, rating);
        hotelRepo.save(hotel);
        return hotel;
    }

    // --- Liste de tous les hôtels ---
    public List<Hotel> listAllHotels() {
        return hotelRepo.findAll();
    }

    // --- Liste des hôtels disponibles ---
    public List<Hotel> listAvailableHotels() {
        return hotelRepo.findAll()
                .stream()
                .filter(h -> h.getAvailableRooms() > 0)
                .collect(Collectors.toList());
    }

    // --- Mise à jour d’un hôtel ---
    public boolean updateHotel(String id, String newName, String newAddress, int newRooms) {
        Optional<Hotel> optHotel = hotelRepo.findById(id);
        if (optHotel.isPresent()) {
            Hotel hotel = optHotel.get();
            hotel.setName(newName);
            hotel.setAddress(newAddress);
            hotel.setAvailableRooms(newRooms);
            hotelRepo.save(hotel); // ré-enregistrer
            return true;
        }
        return false;
    }

    // --- Suppression d’un hôtel ---
    public boolean deleteHotel(String id) {
        Optional<Hotel> optHotel = hotelRepo.findById(id);
        if (optHotel.isPresent()) {
            hotelRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
