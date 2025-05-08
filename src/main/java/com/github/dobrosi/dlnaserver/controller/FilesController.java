package com.github.dobrosi.dlnaserver.controller;

import java.io.IOException;
import java.util.List;

import com.github.dobrosi.dlnaserver.FileService;
import com.github.dobrosi.dlnaserver.model.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/files")
public class FilesController {
    private final FileService fileService;

    @Autowired
    public FilesController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping
    public List<FileItem> files(@RequestParam(required = false) String path) throws IOException {
        return fileService.list(path);
    }

    @PostMapping
    public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file,
                                 @RequestParam(value = "filename", required = false) String filename,
                                 @RequestParam(value = "path", required = false) String path,
                                 @RequestParam(value = "parentPath", required = false) String parentPath) {
        try {
            fileService.upload(
                    file.getInputStream(),
                    filename != null ? filename : file.getOriginalFilename(),
                    path,
                    parentPath);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
