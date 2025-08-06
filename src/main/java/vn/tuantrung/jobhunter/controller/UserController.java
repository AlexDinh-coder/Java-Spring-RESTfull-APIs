package vn.tuantrung.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import vn.tuantrung.jobhunter.domain.User;
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
    public ResponseEntity<User> createNewUser(@RequestBody User postUser) {
        String hashPassword = this.passwordEncoder.encode(postUser.getPassword());
        postUser.setPassword(hashPassword);
        User trungUser = this.userService.handleCreateUser(postUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(trungUser);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (id >= 1500) {
            throw new IdInvalidException("Id must be greater than 1501");
        }
        this.userService.handleDeleteUser(id);
        // return ResponseEntity.ok("trungUser");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("trungUser");
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") long id) {
        User fetchUser = this.userService.fetchUserById(id);
        // return ResponseEntity.ok(fetchUser);
        return ResponseEntity.status(HttpStatus.OK).body(fetchUser);
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification<User> specification,
            Pageable pageable)
    {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUsers(specification, pageable));
    }

    @PutMapping("/users")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User trungUser = this.userService.handleUpdateUser(user);
        return ResponseEntity.ok(trungUser);
    }

}
