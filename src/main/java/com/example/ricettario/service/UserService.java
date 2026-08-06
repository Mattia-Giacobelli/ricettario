package com.example.ricettario.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ricettario.entities.User;
import com.example.ricettario.repositories.IUserRepository;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final IUserRepository userRepository;

    public UserService(IUserRepository userRepository) {

        this.userRepository = userRepository;

    }

    public Page<User> findAll(Pageable pageable) {

        return userRepository.findAll(pageable);

    }

    public User findById(Integer id) {

        return userRepository.findById(id).orElseThrow();

    }

    public User findByUsername(String username) {

        return userRepository.findByUsername(username).orElseThrow();

    }

    @Transactional
    public User create(User user) {

        return userRepository.save(user);

    }

    @Transactional
    public User update(User user) {

        return userRepository.save(user);

    }

    @Transactional
    public void delete(int id) {

        userRepository.deleteById(id);

    }

    // Utility

    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

}
