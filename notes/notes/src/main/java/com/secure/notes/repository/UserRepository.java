package com.secure.notes.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.secure.notes.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUserName(String username);


	 Boolean existsByEmail(String email);
	    Boolean existsByUserName(String username);


		Optional<User> findByEmail(String email);
}
