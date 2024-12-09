package com.hotel.user.model.service;

import com.hotel.user.model.dto.reponse.RoomDTO;
import com.hotel.user.model.dto.reponse.RoomResponse;
import com.hotel.user.model.dto.request.RoomRequest;
import com.hotel.user.model.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {
    RoomResponse createRoom(Long hotelId, RoomRequest roomRequest);
    RoomResponse getRoom(Long id);
    Page<RoomDTO> getRoomsByHotelIdAndKeyword(List<Long> hotelIds, String keyword, Pageable pageable);
    RoomResponse updateRoom(Long roomId, RoomRequest roomRequest);
    Page<Room> getRoomsByHotelIds(List<Long> hotelIds, Pageable pageable);
    List<RoomResponse> getRoomsByHotelId(Long hotelId);
}
