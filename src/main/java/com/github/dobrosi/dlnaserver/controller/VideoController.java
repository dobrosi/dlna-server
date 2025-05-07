package com.github.dobrosi.dlnaserver.controller;

import com.github.dobrosi.dlnaserver.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
public class VideoController {
    private final FileService fileService;

    @Autowired
    public VideoController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/video")
    public String video(@RequestParam String path, Model model) {
        model.addAttribute("url", "/play?path=" + path);
        return "video";
    }

    @GetMapping("/play")
    public ResponseEntity<StreamingResponseBody> play(
        @RequestParam String path,
        @RequestHeader(value = "Range", required = false) String rangeHeader) {
        return fileService.prepareDownloadContent(path, rangeHeader, true);
    }
}
