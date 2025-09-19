package model;

import java.util.UUID;

public class Hotel {
    private String hotelId;
    private String name;
    private String address;
    private int availableRooms;
    private double rating;

    public Hotel(String name, String address, int availableRooms, double rating) {
        if (availableRooms < 0) {
            throw new IllegalArgumentException("Le nombre de chambres doit être >= 0");
        }
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 5");
        }

        this.hotelId = UUID.randomUUID().toString();
        this.name = name;
        this.address = address;
        this.availableRooms = availableRooms;
        this.rating = rating;
    }

    // --- Getters & Setters ---
    public String getHotelId() {
        return hotelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom ne peut pas être vide.");
        }
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("L'adresse ne peut pas être vide.");
        }
        this.address = address;
    }

    public int getAvailableRooms() {
        return availableRooms;
    }

    public void setAvailableRooms(int availableRooms) {
        if (availableRooms < 0) {
            throw new IllegalArgumentException("Le nombre de chambres doit être >= 0");
        }
        this.availableRooms = availableRooms;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        if (rating < 0 || rating > 5) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 5");
        }
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id='" + hotelId + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", rooms=" + availableRooms +
                ", rating=" + rating +
                '}';
    }
}
