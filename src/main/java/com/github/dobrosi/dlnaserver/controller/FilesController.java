package com.github.dobrosi.dlnaserver.controller;

import java.io.IOException;
import java.util.List;

import com.github.dobrosi.dlnaserver.FileService;
import com.github.dobrosi.dlnaserver.model.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
