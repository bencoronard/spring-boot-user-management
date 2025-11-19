package dev.hireben.user_management.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.hireben.user_management.dto.UserDTO;
import dev.hireben.user_management.entity.User;
import dev.hireben.user_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public Slice<User> listUsers(Pageable pageable) {
    return userRepository.findAll(pageable);
  }

  @Override
  @Transactional(readOnly = true)
  public User retrieveUser(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
  }

  @Override
  @Transactional
  public Long createUser(UserDTO dto) {

    User user = User.builder()
        .name(dto.name())
        .username(dto.username())
        .email(dto.email())
        .phone(dto.phone())
        .website(dto.website())
        .build();

    return userRepository.save(user).getId();
  }

  @Override
  @Transactional
  public void updateUser(Long id, UserDTO dto) {
    userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

  }

  @Override
  @Transactional
  public void deleteUser(Long id) {
    userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

    userRepository.deleteById(id);
  }

}
