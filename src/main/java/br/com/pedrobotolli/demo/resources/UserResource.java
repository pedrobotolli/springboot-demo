package br.com.pedrobotolli.demo.resources;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
    @Autowired
    private UserRepository userRepository;

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable UUID id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Optional<User> optionalRetrievedUser = userRepository.findById(user.getId());
        if(optionalRetrievedUser.isPresent()){
            User retrievedUser = optionalRetrievedUser.get();
            retrievedUser.setName(user.getName());
            retrievedUser.setPassword(user.getPassword());
            User updatedUser = userRepository.save(retrievedUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable UUID id) {
        Optional<User> retrievedUser = userRepository.findById(id);
        if(retrievedUser.isPresent()) {
            userRepository.deleteById(id);
            return new ResponseEntity<>(retrievedUser.get(), HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();       
    }
    
}
