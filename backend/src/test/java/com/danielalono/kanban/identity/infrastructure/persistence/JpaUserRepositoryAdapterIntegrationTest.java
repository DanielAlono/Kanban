package com.danielalono.kanban.identity.infrastructure.persistence;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import com.danielalono.kanban.identity.domain.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.danielalono.kanban.identity.domain.User;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaUserRepositoryAdapter.class)
@Testcontainers
class JpaUserRepositoryAdapterIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldSaveAndFindUserByEmail() {
        User user = new User(
                "Daniel",
                "daniel@example.com",
                "hashed-password");

        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("daniel@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Daniel");
        assertThat(foundUser.get().getEmail()).isEqualTo("daniel@example.com");
        assertThat(foundUser.get().isEnabled()).isTrue();
        assertThat(foundUser.get().isEmailVerified()).isFalse();
        assertThat(foundUser.get().getCreatedAt()).isNotNull();
        assertThat(foundUser.get().getUpdatedAt()).isNotNull();
    }
}