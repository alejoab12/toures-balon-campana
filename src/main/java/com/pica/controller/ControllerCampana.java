package com.pica.controller;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List; 
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import io.minio.messages.Item;



@RestController
@RequestMapping("/files")
public class ControllerCampana {
	
	@Autowired
    private MinioService minioService;
	
	@GetMapping
    public List<Item> testMinio() throws MinioException {		
        return minioService.list();
    }

	@GetMapping("/{object}")
    public void getObject(@PathVariable("object") String object, HttpServletResponse response) throws MinioException, IOException {
		Path path = FileSystems.getDefault().getPath(object);
        InputStream inputStream = minioService.get(path);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        
        response.addHeader("Content-disposition", "attachment;filename=" + object);
        response.setContentType(URLConnection.guessContentTypeFromName(object));
        
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }
	
	
	@PostMapping
    public void addAttachement(@RequestParam("file") MultipartFile file) {
    
		Path path = FileSystems.getDefault().getPath(file.getOriginalFilename());
        
        try {
            minioService.upload(path, file.getInputStream(), file.getContentType());
        } catch (MinioException e) {
            throw new IllegalStateException("The file cannot be upload on the internal storage. Please retry later", e);
        } catch (IOException e) {
            throw new IllegalStateException("The file cannot be read", e);
        }
    }
}
