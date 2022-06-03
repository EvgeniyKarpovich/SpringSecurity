package by.karpovich.security.jpa.repository;

import by.karpovich.security.jpa.model.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

}
