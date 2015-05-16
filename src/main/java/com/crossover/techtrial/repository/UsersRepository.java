package com.crossover.techtrial.repository;

import com.crossover.techtrial.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User data access object.
 */
public interface UsersRepository extends JpaRepository<User, Long>
{
    User findByUsernameAndPassword(String username, String password);
}
