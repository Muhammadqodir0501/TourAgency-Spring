package org.example.touragency.controller;

import lombok.RequiredArgsConstructor;
import org.example.touragency.dto.request.TourAddDto;
import org.example.touragency.dto.request.TourDeleteDto;
import org.example.touragency.dto.response.TourResponseDto;
import org.example.touragency.dto.response.TourUpdateDto;
import org.example.touragency.dto.response.UserUpdateDto;
import org.example.touragency.model.enity.Tour;
import org.example.touragency.model.enity.User;
import org.example.touragency.service.abstractions.TourService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tour")
@RequiredArgsConstructor
public class TourController {

    final private TourService tourService;

    @PostMapping("/add")
    public ResponseEntity<?> addNewTour(@RequestBody TourAddDto tourAddDto) {
        try{
            tourService.addNewTour(tourAddDto);
            return ResponseEntity.ok("Tour has successfully been added");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteTour(@RequestBody TourDeleteDto tourDeleteDto) {
        try{
            tourService.deleteTour(tourDeleteDto);
            return ResponseEntity.ok("Tour has successfully been removed");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateTour(@RequestBody TourUpdateDto tourUpdateDto) {
        try{
            tourService.updateTour(tourUpdateDto);
            return ResponseEntity.ok("Tour has successfully been updated");
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<TourResponseDto>> getAllTours() {
        return new ResponseEntity<>(tourService.getAllTours(), HttpStatus.OK);
    }


}
