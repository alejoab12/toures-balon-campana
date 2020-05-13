package com.pica.service;

import com.jlefebure.spring.boot.minio.MinioException;
import io.minio.messages.Item;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface CampanaService {
    void addAttachement(MultipartFile file);

    List<Item> testMinio() throws MinioException;

    public void getObject(String object, HttpServletResponse response) throws MinioException, IOException;
}
