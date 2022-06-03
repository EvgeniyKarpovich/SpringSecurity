package by.karpovich.security.api.controller;

import by.karpovich.security.jpa.model.ResponseMessage;
import by.karpovich.security.jpa.model.ResponseUserPhoto;
import by.karpovich.security.jpa.model.UserPhoto;
import by.karpovich.security.security.UserPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/usersPhoto")
@CrossOrigin("http://localhost:8080")
public class UserPhotoController {

    @Autowired
    private UserPhotoService userPhotoService;
    @PostMapping("/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            userPhotoService.store(file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
        } catch (Exception e) {
            message = "Could not upload the file: " + file.getOriginalFilename() + "!";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }
    @GetMapping("/files")
    public ResponseEntity<List<ResponseUserPhoto>> getListFiles() {
        List<ResponseUserPhoto> files = userPhotoService.getAllFiles().map(dbFile -> {
            String fileDownloadUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/files/")
                    .path(String.valueOf(dbFile.getId()))
                    .toUriString();
            return new ResponseUserPhoto(
                    dbFile.getName(),
                    fileDownloadUri,
                    dbFile.getType(),
                    dbFile.getData().length);
        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(files);
    }
    @GetMapping("/files/{id}")
    public ResponseEntity<byte[]> getFile(@PathVariable Long id) {
        UserPhoto fileDB = userPhotoService.getFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
                .body(fileDB.getData());
    }
}
