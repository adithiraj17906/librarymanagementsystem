package com.library.service.impl;

import com.library.dto.UserDTO;
import com.library.entity.User;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.NotificationRepository;
import com.library.repository.UserRepository;
import com.library.repository.WaitingListEntryRepository;
import com.library.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BorrowRecordRepository borrowRecordRepository;
    private final NotificationRepository notificationRepository;
    private final WaitingListEntryRepository waitingListEntryRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BorrowRecordRepository borrowRecordRepository,
                           NotificationRepository notificationRepository,
                           WaitingListEntryRepository waitingListEntryRepository) {
        this.userRepository = userRepository;
        this.borrowRecordRepository = borrowRecordRepository;
        this.notificationRepository = notificationRepository;
        this.waitingListEntryRepository = waitingListEntryRepository;
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        if (userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email " + userDTO.getEmail() + " already exists");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
        User user = userDTO.toEntity();
        User savedUser = userRepository.save(user);
        return UserDTO.fromEntity(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));
        return UserDTO.fromEntity(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

        userRepository.findByEmail(userDTO.getEmail()).ifPresent(existingUser -> {
            if (!existingUser.getId().equals(id)) {
                throw new IllegalArgumentException("Email " + userDTO.getEmail() + " is already in use by another user");
            }
        });

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone());
        // Update password only if provided
        if (userDTO.getPassword() != null && !userDTO.getPassword().isBlank()) {
            user.setPassword(userDTO.getPassword());
        }

        User updatedUser = userRepository.save(user);
        return UserDTO.fromEntity(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id " + id));

        // Block deletion if user has currently borrowed (unreturned) books
        List<com.library.entity.BorrowRecord> activeBorrows =
                borrowRecordRepository.findByUserIdAndStatus(id, com.library.entity.BorrowStatus.BORROWED);
        if (!activeBorrows.isEmpty()) {
            throw new IllegalArgumentException(
                "Cannot delete user '" + user.getName() + "' — they have " +
                activeBorrows.size() + " book(s) currently borrowed. " +
                "Please return all books first.");
        }

        // Cascade-delete all related records in the correct FK order
        notificationRepository.deleteByUserId(id);
        waitingListEntryRepository.deleteByUserId(id);
        borrowRecordRepository.deleteByUserId(id);   // history records (already returned)
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return UserDTO.fromEntity(user);
    }
}
