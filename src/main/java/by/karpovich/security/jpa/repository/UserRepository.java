package by.karpovich.security.jpa.repository;

import by.karpovich.security.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

//   User findByLogin(String login);

   User findByUsername(String name);

   Optional<User> findByEmail(String email);

}
