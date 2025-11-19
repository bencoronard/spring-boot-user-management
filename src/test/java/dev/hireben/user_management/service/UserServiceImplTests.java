package dev.hireben.user_management.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import dev.hireben.user_management.entity.User;
import dev.hireben.user_management.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTests {

  @Mock
  private UserRepository repository;

  @InjectMocks
  private UserServiceImpl service;

  @Test
  void testDeleteUser_UserExists_Success() {

    Long userId = 1L;
    User mockUser = User.builder().id(userId).build();

    when(repository.findById(userId)).thenReturn(Optional.of(mockUser));

    service.deleteUser(userId);

    verify(repository, times(1)).findById(userId);
    verify(repository, times(1)).deleteById(userId);
  }

  @Test
  void testDeleteUser_UserNotFound_ThrowsException() {
    Long userId = 999L;

    when(repository.findById(userId)).thenReturn(Optional.empty());

    EntityNotFoundException exception = assertThrows(
        EntityNotFoundException.class,
        () -> service.deleteUser(userId));
    assertEquals("User not found: " + userId, exception.getMessage());
    verify(repository, times(1)).findById(userId);
    verify(repository, never()).deleteById(userId);
  }

  @Test
  void testDeleteUser_UserNotFound_DoesNotCallDelete() {

    Long userId = 100L;

    when(repository.findById(userId)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> service.deleteUser(userId));
    verify(repository, never()).deleteById(anyLong());
  }

}
