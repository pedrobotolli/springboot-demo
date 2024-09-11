package br.com.pedrobotolli.demo.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import org.junit.jupiter.api.Test;
import java.util.UUID;

public class UserTest {
    
    @Test
    void testEqualsSameObject() {
        UUID id = UUID.randomUUID();
        User user = new User("Pedro", "pedro", "senha");
        user.setId(id);
        assertEquals(user, user, "A User object should be equal to itself");
    }

    @Test
    void testEqualsDifferentObjectsWithSameValues() {
        UUID id = UUID.randomUUID();
        User user1 = new User("Pedro", "pedro", "senha");
        user1.setId(id);
        User user2 = new User("Pedro", "pedro", "senha");
        user2.setId(id);
        assertEquals(user1, user2, "Users with the same values and id should be equal");
    }

    @Test
    void testEqualsDifferentObjectsWithDifferentValues() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        User user1 = new User("Pedro", "pedro", "senha");
        user1.setId(id1);
        User user2 = new User("João", "joao", "senhajoao");
        user2.setId(id2);
        assertNotEquals(user1, user2, "Users with different values should not be equal");
    }

    @Test
    void testEqualsNull() {
        User user = new User("Pedro", "pedro", "senha");
        user.setId(UUID.randomUUID());
        assertNotEquals(user, null, "User should not be equal to null");
    }

    @Test
    void testHashCodeConsistency() {
        UUID id = UUID.randomUUID();
        User user = new User("Pedro", "pedro", "senha");
        user.setId(id);
        int hashCode1 = user.hashCode();
        int hashCode2 = user.hashCode();
        assertEquals(hashCode1, hashCode2, "Hash code should be consistent");
    }

    @Test
    void testHashCodeEqualsConsistency() {
        UUID id = UUID.randomUUID();
        User user1 = new User("Pedro", "pedro", "senha");
        user1.setId(id);
        User user2 = new User("Pedro", "pedro", "senha");
        user2.setId(id);
        assertEquals(user1.hashCode(), user2.hashCode(), "Equal objects should have the same hash code");
    }
}
