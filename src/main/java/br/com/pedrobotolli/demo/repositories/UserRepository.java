package br.com.pedrobotolli.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pedrobotolli.demo.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {

}
