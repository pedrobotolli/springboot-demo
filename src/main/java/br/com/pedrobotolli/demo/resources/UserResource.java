package br.com.pedrobotolli.demo.resources;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.pedrobotolli.demo.entities.User;
import br.com.pedrobotolli.demo.repositories.UserRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping(value = "/users")
public class UserResource {
    private final UserRepository userRepository;

    public UserResource(final UserRepository userRepository) {
        this.userRepository = Objects.requireNonNull(userRepository);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll()); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(findById(id));
    }

    @PutMapping("/{id}")
    @CacheEvict(value = "userEntityCache", key = "#id")
    public ResponseEntity<User> updateUser(@RequestBody User user, @PathVariable UUID id) {
        User retrievedUser = findById(id);
        retrievedUser.setName(user.getName());
        retrievedUser.setPassword(user.getPassword());
        User updatedUser = userRepository.save(retrievedUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable UUID id) {
        User retrievedUser = findById(id);
        userRepository.delete(retrievedUser);
        return ResponseEntity.ok(retrievedUser);
    }

    private User findById (UUID id){
        return userRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id %s not found".formatted(id)));
    }
}
