package com.hotel.user.model.repository;

import com.hotel.user.model.entity.Image_room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRoomRepository extends JpaRepository<Image_room, Long> {

}
