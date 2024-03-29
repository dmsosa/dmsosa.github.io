package com.duvi.blogservice.repository;

import com.duvi.blogservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);
    public Optional<User>  findByEmail(String email);
    public boolean existsByUsername(String username);
    public boolean existsByEmail(String email);
    public boolean existsById(Long id);


}
