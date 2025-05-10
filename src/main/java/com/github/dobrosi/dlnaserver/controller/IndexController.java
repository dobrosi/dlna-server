package com.github.dobrosi.dlnaserver.controller;

import java.io.IOException;
import java.util.List;

import com.github.dobrosi.dlnaserver.FileService;
import com.github.dobrosi.dlnaserver.model.FileItem;
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
        model.addAttribute("parentPath", path);
        return "index";
    }

    @GetMapping("/vlc")
    public String vlc(Model model) {
        String vlc = "vlc_cmd.sh%20pl_play%26id%3D";
        model.addAttribute("files", List.of(
                new FileItem(FileItem.Type.FILE, "BE", null, "xset+dpms+force+on"),
                new FileItem(FileItem.Type.FILE, "RTL", null, vlc + 4),
                new FileItem(FileItem.Type.FILE, "GOLD", null, vlc + 5),
                new FileItem(FileItem.Type.FILE, "ATV", null, vlc + 6),
                new FileItem(FileItem.Type.FILE, "KI", null, "xset+dpms+force+off")
        ));
        return "vlc";
    }
}
