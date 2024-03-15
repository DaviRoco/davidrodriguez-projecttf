package com.davidrodriguez.projecttf.repository;

import com.davidrodriguez.projecttf.dto.UserDto;
import com.davidrodriguez.projecttf.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserByEmail(String email);
}
