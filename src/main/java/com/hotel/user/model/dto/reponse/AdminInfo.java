package com.hotel.user.model.dto.reponse;

import com.hotel.user.model.entity.User;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AdminInfo {
    private String name ;
    private String email;
    private String phoneNumber;
    private String identification;

    @NonNull
    public static AdminInfo getAdminInfo(@NotNull User user) {
        return new AdminInfo(user.getName(), user.getEmail(), user.getPhoneNumber(), "");
    }
}
