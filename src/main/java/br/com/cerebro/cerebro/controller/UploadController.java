package br.com.cerebro.cerebro.controller;

import br.com.cerebro.cerebro.service.UploadService;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadController {
    
    private final UploadService uploadService;
    
    public UploadController(UploadService uploadService){
        this.uploadService = uploadService;
    }
    
    @PostMapping("/upload")
    public List<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws Exception{
       return uploadService.upload(file);
    }
    
     @PostMapping("/upload1")
    public void upload1(@RequestParam("file") MultipartFile file) throws Exception{
        uploadService.upload1(file);
       //return uploadService.upload1(file);
    }
}
