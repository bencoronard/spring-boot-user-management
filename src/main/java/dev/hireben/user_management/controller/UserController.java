package dev.hireben.user_management.controller;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import dev.hireben.user_management.dto.UserDTO;
import dev.hireben.user_management.entity.User;
import dev.hireben.user_management.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/users")
  ResponseEntity<Slice<User>> listAllUsers(
      @PageableDefault(page = 0, size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

    Slice<User> users = userService.listUsers(pageable);

    return ResponseEntity.ok(users);
  }

  @GetMapping("/users/{id}")
  ResponseEntity<User> retrieveUser(@PathVariable Long id) {

    User user = userService.retrieveUser(id);

    return ResponseEntity.ok(user);
  }

  @PostMapping("/users")
  ResponseEntity<Void> createNewUser(
      @Valid @RequestBody UserDTO body) {

    Long id = userService.createUser(body);

    URI location = URI.create(String.format("/users/%s", id));

    return ResponseEntity.created(location).build();
  }

  @PutMapping("/users/{id}")
  ResponseEntity<Void> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO body) {

    userService.updateUser(id, body);

    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/users/{id}")
  ResponseEntity<Void> deleteUser(@PathVariable Long id) {

    userService.deleteUser(id);

    return ResponseEntity.noContent().build();
  }

}
