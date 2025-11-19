package dev.hireben.user_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.hireben.user_management.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
