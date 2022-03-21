package com.example.demo.controllers;


import com.example.demo.services.LargeFileHandlerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class LargeFileHandlerController {

    private final LargeFileHandlerService _largeFileHandlerService;

    public LargeFileHandlerController(LargeFileHandlerService largeFileHandlerService) {
        this._largeFileHandlerService = largeFileHandlerService;
    }

    @GetMapping("/large-file")
    public String GetLargeFile(@RequestParam(value = "id", defaultValue = "0") String id) throws IOException {
        return _largeFileHandlerService.GetLargeFile();
    }
}
