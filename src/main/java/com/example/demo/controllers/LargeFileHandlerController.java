package com.example.demo.controllers;


import com.example.demo.services.LargeFileHandlerService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class LargeFileHandlerController {

    private final LargeFileHandlerService _largeFileHandlerService;

    public LargeFileHandlerController(LargeFileHandlerService largeFileHandlerService) {
        this._largeFileHandlerService = largeFileHandlerService;
    }

    @GetMapping("/large-file")
    public String GetFile(@RequestParam(value = "filename", defaultValue = "test.txt") String filename) throws IOException {
//        return _largeFileHandlerService.GetFile("");
        return _largeFileHandlerService.AddFile("");
    }

    @PostMapping("/large-file")
    public String AddFile(@RequestParam(value = "filename", defaultValue = "test.txt") String id) throws IOException {
        return _largeFileHandlerService.AddFile("");
    }

    @DeleteMapping ("/large-file")
    public String DeleteFile(@RequestParam(value = "id", defaultValue = "0") String id) throws IOException {
        return _largeFileHandlerService.DeleteFile("");
    }
}
