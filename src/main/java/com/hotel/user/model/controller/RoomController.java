package com.hotel.user.model.controller;


import com.hotel.user.model.dto.reponse.RoomResponse;
import com.hotel.user.model.dto.request.RoomRequest;
import com.hotel.user.model.exception.RoomNotFoundException;
import com.hotel.user.model.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping("/create/{hotelId}")
    public ResponseEntity<?> createRoom(@PathVariable Long hotelId, @RequestBody @Valid RoomRequest roomRequest, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors().stream().map(ObjectError:: getDefaultMessage ).collect(Collectors.joining("\n")));
        }
        return new ResponseEntity<>(roomService.createRoom(hotelId, roomRequest), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getRoom(@PathVariable Long id) {
        try {
            RoomResponse roomResponse = roomService.getRoom(id);
            return new ResponseEntity<>(roomResponse, HttpStatus.OK);
        } catch (RoomNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRoom(@PathVariable Long id, @RequestBody @Valid RoomRequest roomRequest) throws Exception {
        RoomResponse roomResponse = roomService.updateRoom(id, roomRequest);
        return new ResponseEntity<>(roomResponse, HttpStatus.OK);

    }

//    @PostMapping("/by-hotels")
//    public ResponseEntity<Page<Room>> getRoomsByHotelIds(@RequestBody RoomPaging command) {
//
//        Pageable pageable = PageRequest.of(command.getPage(), command.getSize(), Sort.by(Sort.Order.asc("number")));  // Sắp xếp theo trường 'number'
//        // Gọi service để lấy các phòng theo hotelIds và phân trang
//        Page<Room> rooms = roomService.getRoomsByHotelIds(command.getHotelIds(), pageable);
//
//        // Trả về kết quả
//        return ResponseEntity.ok(rooms);
//    }

}
