package com.secure.notes.service;
import com.secure.notes.dtos.UserDTO;
import com.secure.notes.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void updateUserRole(Long userId, String roleName);

    List<User> getAllUsers();

    UserDTO getUserById(Long id);

	User findByUsername(String username);

	void updateCredentialsExpiryStatus(Long userId, boolean expire);

	void updateAccountEnabledStatus(Long userId, boolean enabled);

	void updateAccountExpiryStatus(Long userId, boolean expire);

	void updateAccountLockStatus(Long userId, boolean lock);

	void updatePassword(Long userId, String password);

	void forgotPassword(String email);

	void resetPassword(String token, String newPassword);

	Optional<User> findByEmail(String email);

	void registerUser(User newUser);
}
