package app.repositories;

import app.enums.PermissionLevel;
import app.models.User;
import jakarta.persistence.Entity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = { Repository.class, Entity.class }))
class UserJPARepositoryTest {

    @Autowired
    private EntityRepositoryJPA<User> userRepo;

    @BeforeAll
    static void setup() {
        Locale.setDefault(Locale.ENGLISH);
    }

    @BeforeEach
    void autowiredSuccessfully() {
        assertNotNull(userRepo);
    }

    @Test
    void testRepoCRUD() {
        // create
        final PermissionLevel USER_PERMISSIONLEVEL =  PermissionLevel.ADMIN;
        final String USER_NAME =  "Full Name";
        final String USER_EMAIL = "email@mail.com";
        final String USER_PASSWORD = "password";

        User user = new User(USER_PERMISSIONLEVEL, USER_NAME, USER_EMAIL, null, USER_PASSWORD, null);

        assertTrue(user.getId() <= 0);
        User savedUser = userRepo.save(user);
        assertTrue(savedUser.getId() > 0 );
        assertEquals(savedUser.getPermissionLevel(), USER_PERMISSIONLEVEL);
        assertEquals(savedUser.getName(), USER_NAME);
        assertEquals(savedUser.getEmail(), USER_EMAIL);


        // read
        assertEquals(userRepo.findAll(), List.of(savedUser));
        assertEquals(userRepo.findById(savedUser.getId()), savedUser);

        // update
        final PermissionLevel UPDATED_USER_PERMISSIONLEVEL = PermissionLevel.VIEWER;
        final String UPDATED_USER_NAME = "Updated Full Name";
        final String UPDATED_USER_EMAIL = "updatedemail@maill.com";

        savedUser.setPermissionLevel(UPDATED_USER_PERMISSIONLEVEL);
        savedUser.setName(UPDATED_USER_NAME);
        savedUser.setEmail(UPDATED_USER_EMAIL);
        User updatedUser = userRepo.save(savedUser);
        assertEquals(userRepo.findAll(), List.of(updatedUser));
        assertEquals(userRepo.findById(updatedUser.getId()), updatedUser);

        assertEquals(updatedUser.getPermissionLevel(), UPDATED_USER_PERMISSIONLEVEL);
        assertNotEquals(updatedUser.getPermissionLevel(), USER_PERMISSIONLEVEL);

        assertEquals(updatedUser.getName(), UPDATED_USER_NAME);
        assertNotEquals(updatedUser.getName(), USER_NAME);

        assertEquals(updatedUser.getEmail(), UPDATED_USER_EMAIL);
        assertNotEquals(updatedUser.getEmail(), USER_EMAIL);

        // delete
        assertFalse(userRepo.findAll().isEmpty());
        User deletedUser = userRepo.delete(updatedUser);
        assertNull(userRepo.findById(deletedUser.getId()));
        assertTrue(userRepo.findAll().isEmpty());
    }

}
