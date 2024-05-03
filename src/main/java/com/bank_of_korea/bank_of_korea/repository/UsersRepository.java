package com.bank_of_korea.bank_of_korea.repository;


import com.bank_of_korea.bank_of_korea.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Integer> {

    boolean existsByEmail(String email);

    Users findByEmail(String email);
}
