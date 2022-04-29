package team.jit.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import team.jit.service.CsvService;


@AllArgsConstructor
@RestController
@RequestMapping("/files")
public class FilesController {

    private final CsvService csvService;

    @GetMapping(value = "/csv")
    public ResponseEntity<byte[]> downloadCsv() {
        byte[] content = csvService.exportFile();
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, "text/csv");
        return new ResponseEntity<>(content, responseHeaders, HttpStatus.OK);
    }

    @PostMapping(value = "/csv")
    public void uploadCsv(@RequestBody MultipartFile file) {
        csvService.importFile(file);
    }
}
