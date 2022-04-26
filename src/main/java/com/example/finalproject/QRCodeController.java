package com.example.finalproject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QRCodeController {

    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";


    //@GetMapping(value = "/genrateAndDownloadQRCode/{codeText}/{width}/{height}")
    /*@GetMapping(value = "/users/{userId}/qr")
    public void download(
            @PathVariable("codeText") String codeText,
            @PathVariable("width") Integer width,
            @PathVariable("height") Integer height)
            throws Exception {
        QRCodeGenerator.generateQRCodeImage(codeText, width, height, QR_CODE_IMAGE_PATH);
    }*/

    @GetMapping(value = "/users/{userId}/qr")
    public byte[] generateQRCode(
            @PathVariable("userId") String userId)
            throws Exception {
        System.out.println(userId);
        //return ResponseEntity.status(HttpStatus.OK).body(QRCodeGenerator.getQRCodeImage(userId, 10, 10));
        return QRCodeGenerator.getQRCodeImage(userId, 25, 25);
    }
}
