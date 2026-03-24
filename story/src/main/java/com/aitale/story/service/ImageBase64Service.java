package com.aitale.story.service;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import org.springframework.stereotype.Service;

@Service
public class ImageBase64Service {

    public String convertImageUrlToDataUri(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            return null;
        }

        try {
            URL url = new URL(imageUrl);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);

            String contentType = connection.getContentType();
            if (contentType == null || contentType.isBlank()) {
                contentType = "image/png";
            }

            try (InputStream inputStream = connection.getInputStream()) {
                byte[] bytes = inputStream.readAllBytes();
                String base64 = Base64.getEncoder().encodeToString(bytes);
                return "data:" + contentType + ";base64," + base64;
            }
        } catch (Exception e) {
            throw new IllegalStateException("이미지를 base64로 변환하지 못했습니다.", e);
        }
    }
}