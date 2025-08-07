package vn.tuantrung.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.tuantrung.jobhunter.domain.User;
import vn.tuantrung.jobhunter.domain.dto.ResCreateUserDTO;
import vn.tuantrung.jobhunter.domain.dto.ResUpdateUserDTO;
import vn.tuantrung.jobhunter.domain.dto.ResUserDTO;
import vn.tuantrung.jobhunter.domain.dto.ResultPaginationDTO;
import vn.tuantrung.jobhunter.service.UserService;
import vn.tuantrung.jobhunter.util.annotation.ApiMessage;
import vn.tuantrung.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // @GetMapping("/users/create")
    @PostMapping("/users")
    @ApiMessage("Create a user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postUser) throws IdInvalidException {
        boolean isEmailExist = this.userService.isEmailExist(postUser.getEmail());
        if (isEmailExist) {
            throw new IdInvalidException("Email " + postUser.getEmail() + "is existed, please use correct email");
        }

        String hashPassword = this.passwordEncoder.encode(postUser.getPassword());
        postUser.setPassword(hashPassword);
        User trungUser = this.userService.handleCreateUser(postUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.covertResCreateUserDTO(trungUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new IdInvalidException("User with id = " + id + " is not existed");

        }
        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws IdInvalidException {
        User fetchUser = this.userService.fetchUserById(id);
        if (fetchUser == null) {
            throw new IdInvalidException("User with id = " + id + " is not existed");
        }
        // return ResponseEntity.ok(fetchUser);
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.convertToResUserDTO(fetchUser));
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification<User> specification,
            Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUsers(specification, pageable));
    }

    @PutMapping("/users")
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@RequestBody User user) throws IdInvalidException {
        User trungUser = this.userService.handleUpdateUser(user);
        if (trungUser == null) {
            throw new IdInvalidException("User with id = " + user.getId() + " is not existed");
        }
        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(trungUser));
    }

}
