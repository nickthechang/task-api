package com.nick.taskapp.repository;

import com.nick.taskapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
/*
 * Repository for performing database operations on User entities.
 *
 * Provides standard CRUD operations and a custom username lookup
 * used during registration and login.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /*
     * Finds a user by username.
     *
     * Returns Optional because the username may not exist.
     */
    Optional<User> findByUsername(String username);
}

/*
UserRepository is the database access layer for User entities.
It lets the auth service:
save new users
find users by username
use built-in CRUD methods from JpaRepository

1. Repository pattern
Keeps user database access separate from auth business logic.
2. Spring Data JPA
Extends:
JpaRepository<User, Long>
which gives built-in methods like:
save(user)
findById(id)
findAll()
delete(user)
3. Derived query method
Optional<User> findByUsername(String username);
Spring reads this method name and generates the query automatically.
It roughly means:
SELECT * FROM users WHERE username = ?;
4. Optional
The return type is Optional<User> because the user may or may not exist.
That forces the service to handle the “not found” case.
*/