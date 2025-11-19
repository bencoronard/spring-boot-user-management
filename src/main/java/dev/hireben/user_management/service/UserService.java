package dev.hireben.user_management.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import dev.hireben.user_management.dto.UserDTO;
import dev.hireben.user_management.entity.User;

public interface UserService {

  Slice<User> listUsers(Pageable pageable);

  User retrieveUser(Long id);

  Long createUser(UserDTO dto);

  void updateUser(Long id, UserDTO dto);

  void deleteUser(Long id);

}
