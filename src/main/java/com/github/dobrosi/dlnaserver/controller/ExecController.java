package com.github.dobrosi.dlnaserver.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import jakarta.servlet.ServletResponse;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/exec")
public class ExecController {
    @GetMapping
    public void command(@RequestParam String command, ServletResponse response) throws InterruptedException, IOException {
        ProcessBuilder builder = new ProcessBuilder(command.split("\\s"));
        builder.redirectErrorStream(true);

        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                response.getWriter().println(line);
            }
        }

        int exitCode = process.waitFor();
        System.out.println("Kilépési kód: " + exitCode);
    }
}
