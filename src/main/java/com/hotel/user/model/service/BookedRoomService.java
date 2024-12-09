package com.hotel.user.model.service;

import com.hotel.user.model.dto.reponse.BookedRoomDTO;
import com.hotel.user.model.dto.reponse.TotalAmountDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookedRoomService {
    Page<BookedRoomDTO> getBookedRoomsForAdmin(String adminEmail, Pageable pageable);

    Page<BookedRoomDTO> getAllBookedRoom(String adminEmail, Pageable pageable);

    TotalAmountDTO caculateTotalAmount(String adminEmail);
}
