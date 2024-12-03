package com.hotel.user.model.service.impl;

import com.hotel.user.model.dto.reponse.UserResponse;
import com.hotel.user.model.dto.request.UserRequest;
import com.hotel.user.model.entity.Role;
import com.hotel.user.model.entity.User;
import com.hotel.user.model.exception.UserAlreadyExistsException;
import com.hotel.user.model.repository.RoleRepository;
import com.hotel.user.model.repository.UserRepository;
import com.hotel.user.model.security.jwt.JwtProvider;
import com.hotel.user.model.security.user_principle.HotelUserDetails;
import com.hotel.user.model.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtProvider jwtProvider;



    @Override
    public User registerUser(User user) throws UserAlreadyExistsException {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_ADMIN").get();
        roles.add(userRole);

        User newUser = new User() ;
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(user.getPassword());
        newUser.setRoles(roles);
        newUser.setPhoneNumber(user.getPhoneNumber());

        return userRepository.save(newUser);
    }

    @Override
    public UserResponse login(UserRequest userRequest) throws Exception {
        try {

            Authentication authentication ;
            authentication = authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(userRequest.getEmail(),userRequest.getPassword()));
            HotelUserDetails userPrincipal = (HotelUserDetails) authentication.getPrincipal();
            return UserResponse.builder()
                    .token(jwtProvider.generateToken(userPrincipal))
                    .id(userPrincipal.getId())
                    .email(userPrincipal.getEmail())
                    .name(userPrincipal.getName())
                    .roles(userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                    .build();

        } catch (BadCredentialsException e) {
            // Handle incorrect email or password scenario
            throw new Exception("Email hoặc password không chính xác . Vui lòng thử lại .");
        } catch (DisabledException e) {
            // Handle account disabled scenario
            throw new Exception("Tài khoản của bạn đã bị khóa .");
        }catch (AuthenticationException authenticationException){
            System.err.println(authenticationException);
            throw new Exception("Xác thực không thành công. Vui lòng kiểm tra thông tin đăng nhập của bạn.");

        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if (theUser != null){
            userRepository.deleteByEmail(email);
        }

    }

    @Override
    public User getUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return user;

    }
    public  String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername(); // Email hoặc username của người dùng
        }
        throw new IllegalStateException("No logged-in user found");
    }
}