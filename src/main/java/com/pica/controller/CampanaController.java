package com.pica.controller;

import com.jlefebure.spring.boot.minio.MinioException;
import com.pica.service.CampanaService;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/campana")
@CrossOrigin(value = "*", methods = {RequestMethod.POST, RequestMethod.POST})
public class CampanaController {

    @Autowired
    private CampanaService campanaService;

    @GetMapping
    public List<Item> testMinio() throws MinioException {
        return campanaService.testMinio();

    }

    @GetMapping("/{object}")
    public void getObject(@PathVariable("object") String object, HttpServletResponse response) throws MinioException, IOException {
        campanaService.getObject(object, response);
    }


    @PostMapping
    public void addAttachement(@RequestParam("file") MultipartFile file) {
        campanaService.addAttachement(file);
    }
}
