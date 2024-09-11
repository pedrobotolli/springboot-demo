package br.com.pedrobotolli.demo.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.pedrobotolli.demo.entities.User;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Cacheable(value="userEntityCache", key="#id")
    Optional<User> findById(UUID id);

    @Cacheable(value="allUserEntitiesCache")
    List<User> findAll();

    @Caching(evict = {
        @CacheEvict(value="allUserEntitiesCache", allEntries=true)
    })
    <S extends User> S save(S user);

    @Caching(evict = {
        @CacheEvict(value="allUserEntitiesCache", allEntries=true),
        @CacheEvict(value = "userEntityCache", key = "#id")
    })
    void deleteById(UUID id);

}
