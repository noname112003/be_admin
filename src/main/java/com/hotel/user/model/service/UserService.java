package com.hotel.user.model.service;


import com.hotel.user.model.dto.reponse.UserResponse;
import com.hotel.user.model.dto.request.UserRequest;
import com.hotel.user.model.dto.request.command.UpdateAdminCommand;
import com.hotel.user.model.entity.User;
import com.hotel.user.model.exception.UserAlreadyExistsException;
import java.util.List;

public interface UserService {
    User registerUser(User user) throws UserAlreadyExistsException;
    UserResponse login (UserRequest userRequest) throws Exception;
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
    String getCurrentUserEmail();
    void updateUser(UpdateAdminCommand command);
}
