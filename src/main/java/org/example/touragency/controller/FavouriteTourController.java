package org.example.touragency.controller;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.FavouriteTourDto;
import org.example.touragency.model.enity.Tour;
import org.example.touragency.service.abstractions.FavouriteTourService;
import org.example.touragency.service.abstractions.TourService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fav")
@RequiredArgsConstructor
public class FavouriteTourController {

    private final FavouriteTourService favouriteTourService;

    @PostMapping("/add")
    public ResponseEntity<?> addFavTour(@RequestBody FavouriteTourDto favouriteTourDto) {
        try{
            favouriteTourService.addFavouriteTour(favouriteTourDto);
            return ResponseEntity.ok("Tour has been added to the Favourite list");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteFavouriteTour(@RequestBody FavouriteTourDto favouriteTourDto) {
        try{
            favouriteTourService.deleteFavouriteTour(favouriteTourDto);
            return ResponseEntity.ok("Tour has been deleted from the Favourite list");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Tour>> getUserFavouriteTour(@RequestParam UUID id){
       return new ResponseEntity<>(favouriteTourService.getUserFavouriteTours(id), HttpStatus.OK);
    }
}
