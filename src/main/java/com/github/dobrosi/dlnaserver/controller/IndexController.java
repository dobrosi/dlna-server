package com.github.dobrosi.dlnaserver.controller;

import java.io.IOException;

import com.github.dobrosi.dlnaserver.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
    private final FileService fileService;

    @Autowired
    public IndexController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/")
    public String showGrid(@RequestParam(required = false) String path, Model model) throws IOException {
        model.addAttribute("files", fileService.list(path));
        return "index";
    }
}
