package org.example.touragency.repository;

import org.example.touragency.model.enity.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class Database {
    public final Map<UUID, Tour> tours = new ConcurrentHashMap<>();
    public final Map<UUID, User> users = new ConcurrentHashMap<>();
    //public final Map<UUID, Booking> bookings = new ConcurrentHashMap<>();
    public final Map<UUID,FavouriteTour> favorites = new ConcurrentHashMap<>();
    public final Map<UUID, Rating> ratings = new ConcurrentHashMap<>();
    public final Map<UUID, RatingCounter> ratingCounters = new ConcurrentHashMap<>();
}
