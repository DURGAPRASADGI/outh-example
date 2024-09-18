package com.secure.notes.service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.secure.notes.dtos.UserDTO;
import com.secure.notes.model.AppRole;
import com.secure.notes.model.PasswordResetToken;
import com.secure.notes.model.Role;
import com.secure.notes.model.User;
import com.secure.notes.repository.PasswordResetTokenRepository;
import com.secure.notes.repository.RoleRepository;
import com.secure.notes.repository.UserRepository;
import com.secure.notes.utils.EmailService;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    UserRepository userRepository;
    
    @Autowired
    EmailService emailService;
    
    @Autowired
    PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Value("${frontend.url}")
    String frontendUri;

    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        AppRole appRole = AppRole.valueOf(roleName);
        Role role = roleRepository.findByRoleName(appRole)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRole(role);
        userRepository.save(user);
    }


    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Override
    public UserDTO getUserById(Long id) {
//        return userRepository.findById(id).orElseThrow();
        User user = userRepository.findById(id).orElseThrow();
        return convertToDto(user);
    }

    private UserDTO convertToDto(User user) {
        return new UserDTO(
                user.getUserId(),
                user.getUserName(),
                user.getEmail(),
                user.isAccountNonLocked(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isEnabled(),
                user.getCredentialsExpiryDate(),
                user.getAccountExpiryDate(),
                user.getTwoFactorSecret(),
                user.isTwoFactorEnabled(),
                user.getSignUpMethod(),
                user.getRole(),
                user.getCreatedDate(),
                user.getUpdatedDate()
        );
    }
    
    
    @Override
    public User findByUsername(String username) {
        User user = userRepository.findByUserName(username);
        if(user==null) {
        	throw new RuntimeException("User not found with username: " + username);
        }else {
        	return user;
        }
    }

    @Override
    public void updatePassword(Long userId, String password) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update password");
        }
    }


    @Override
    public void updateAccountLockStatus(Long userId, boolean lock) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setAccountNonLocked(!lock);
        userRepository.save(user);
    }

    @Override
    public void updateAccountExpiryStatus(Long userId, boolean expire) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setAccountNonExpired(!expire);
        userRepository.save(user);
    }

    @Override
    public void updateAccountEnabledStatus(Long userId, boolean enabled) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setEnabled(enabled);
        userRepository.save(user);
    }

    @Override
    public void updateCredentialsExpiryStatus(Long userId, boolean expire) {
        User user = userRepository.findById(userId).orElseThrow(()
                -> new RuntimeException("User not found"));
        user.setCredentialsNonExpired(!expire);
        userRepository.save(user);
    }


	@Override
	public void forgotPassword(String email) {
		// TODO Auto-generated method stub
		
		Optional<User> optional=userRepository.findByEmail(email);
		if(optional.isEmpty()) {
			throw new RuntimeException("user not found email");
		}
		
		String token=UUID.randomUUID().toString();
		Instant expiration=Instant.now().plus(24,ChronoUnit.HOURS);
		PasswordResetToken passwordResetToken=new PasswordResetToken(token, expiration, optional.get());
		passwordResetTokenRepository.save(passwordResetToken);
		String resetUri=frontendUri+"/resest-password?token="+token;
		emailService.sendPasswordThroughMail(optional.get().getEmail(), resetUri);
		
	}


	@Override
	public void resetPassword(String token, String newPassword) {
		// TODO Auto-generated method stub
		PasswordResetToken passwordResetToken=passwordResetTokenRepository.findByToken(token)
				.orElseThrow(()-> new RuntimeException("invalid token"));
		if(passwordResetToken.isUsed()) {
			throw new RuntimeException("this token has already used");
			
		}
		if(passwordResetToken.getExpiryDate().isBefore(Instant.now())) {
			throw new RuntimeException("token has expired");
			
		}
		passwordResetToken.setUsed(true);
		User user=passwordResetToken.getUser();
		user.setPassword(passwordEncoder.encode(newPassword));
		userRepository.save(user);
		passwordResetTokenRepository.save(passwordResetToken);
		
		
	}


	@Override
	public Optional<User> findByEmail(String email) {
		// TODO Auto-generated method stub
		return Optional.empty();
	}


	@Override
	public void registerUser(User newUser) {
		// TODO Auto-generated method stub
		
	}

}
