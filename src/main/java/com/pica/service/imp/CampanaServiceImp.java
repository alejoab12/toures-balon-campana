package com.pica.service.imp;

import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import com.pica.service.CampanaService;
import io.minio.messages.Item;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

@Service
public class CampanaServiceImp implements CampanaService {
    @Autowired
    private MinioService minioService;

    @Override
    public void addAttachement(MultipartFile file) {
        Path path = FileSystems.getDefault().getPath(file.getOriginalFilename());
        try {
            minioService.upload(path, file.getInputStream(), file.getContentType());
        } catch (MinioException e) {
            throw new IllegalStateException("The file cannot be upload on the internal storage. Please retry later", e);
        } catch (IOException e) {
            throw new IllegalStateException("The file cannot be read", e);
        }
    }

    @Override
    public List<Item> testMinio() throws MinioException {
        return minioService.list();
    }

    @Override
    public void getObject(String object, HttpServletResponse response) throws MinioException, IOException {
        Path path = FileSystems.getDefault().getPath(object);
        InputStream inputStream = minioService.get(path);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);

        response.addHeader("Content-disposition", "attachment;filename=" + object);
        response.setContentType(URLConnection.guessContentTypeFromName(object));

        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }
}
