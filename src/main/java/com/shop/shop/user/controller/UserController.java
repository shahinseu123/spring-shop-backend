package com.shop.shop.user.controller;

import com.shop.shop.user.dto.role.RoleCreateDto;
import com.shop.shop.user.dto.role.RoleGetDto;
import com.shop.shop.user.dto.user.PasswordChangeDto;
import com.shop.shop.user.dto.user.UserCreateDto;
import com.shop.shop.user.dto.user.UserGetDto;
import com.shop.shop.user.dto.user.UserUpdateDto;
import com.shop.shop.user.entity.User;
import com.shop.shop.user.service.RoleService;
import com.shop.shop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/v2")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("/me")
    public ResponseEntity<UserGetDto> getLoggedInUser(){
        User currentUser = userService.getLoggedInUser();
        return new ResponseEntity<>(convertToUserDto(currentUser), HttpStatus.OK);
    }

//    @PutMapping("/me/change-password")
//    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeDto passwordChangeDto){
//        userService.changePassword(passwordChangeDto);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

//   @GetMapping("/users/{id}")
//    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long id){
//        UserResponseDto userResponseDto = userService.getUserResponseDtoById(id);
//        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
//    }

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestBody UserCreateDto dto) {
        userService.create(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/roles")
    public ResponseEntity<Void> createRole(@RequestBody RoleCreateDto dto) {
        userService.createRole(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
//    @DeleteMapping("/users/{id}")
//    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
//        userService.delete(id);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
    @PutMapping("/users/{id}")
    public ResponseEntity<Void> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto dto) {
        userService.update(id, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @GetMapping("/roles")
    public ResponseEntity<List<RoleGetDto>> getRoles(){
        return new ResponseEntity<>(roleService.findAll(), HttpStatus.OK);
    }

//    @GetMapping("/users")
//    public ResponseEntity<Page<UserProjection>> getUsers(@RequestParam("userType") String userType,
//                                                         @RequestParam("searchParamKey") Optional<String> searchParamKey,
//                                                         @RequestParam("searchParamValue") Optional<String> searchParamValue,
//                                                         Pageable pageable) {
//        Page<UserProjection> userProjections = userService.getPaginatedUsers(userType, searchParamKey.orElse(null), searchParamValue.orElse(null), pageable);
//        return new ResponseEntity<>(userProjections, HttpStatus.OK);
//    }

    public UserGetDto convertToUserDto(User currentUser) {
        RoleGetDto role = new RoleGetDto(currentUser.getRole().getId(), currentUser.getRole().getName());
        return new UserGetDto(currentUser.getId(), role, currentUser.getName(), currentUser.getEmail());
    }
}
