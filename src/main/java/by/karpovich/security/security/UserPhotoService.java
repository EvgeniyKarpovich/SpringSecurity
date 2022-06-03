package by.karpovich.security.security;

import by.karpovich.security.jpa.model.UserPhoto;
import by.karpovich.security.jpa.repository.UserPhotoRepository;
import by.karpovich.security.jpa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.stream.Stream;

@Service
@Transactional
public class UserPhotoService {

    @Autowired
    private UserPhotoRepository userPhotoRepository;

    public UserPhoto store(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        UserPhoto userPhoto = new UserPhoto(fileName, file.getContentType(), file.getBytes());
        return userPhotoRepository.save(userPhoto);
    }
    public UserPhoto getFile(Long id) {
        return userPhotoRepository.findById(id).get();
    }

    public Stream<UserPhoto> getAllFiles() {
        return userPhotoRepository.findAll().stream();
    }
}
