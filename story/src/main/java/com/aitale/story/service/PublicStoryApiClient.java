package com.aitale.story.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.aitale.story.domain.dto.PublicStoryItemDTO;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PublicStoryApiClient {

    private final RestTemplate restTemplate;

    @Value("${public-data.story.url:https://www.culture.go.kr/openapi/rest/publicstory/list}")
    private String publicStoryApiUrl;

    @Value("${public-data.story.service-key:c59a0de8-2688-47b3-a19d-0f41f7f45db9}")
    private String serviceKey;

    public List<PublicStoryItemDTO> fetchStories(String keyword, Integer pageNo, Integer numOfRows) {
        String url = UriComponentsBuilder.fromUriString(publicStoryApiUrl)
                .queryParam("serviceKey", serviceKey)
                .queryParam("keyword", keyword)
                .queryParam("pageNo", pageNo)
                .queryParam("numOfRows", numOfRows)
                .build(true)
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        return parseXml(response.getBody());
    }

    private List<PublicStoryItemDTO> parseXml(String xml) {
        List<PublicStoryItemDTO> result = new ArrayList<>();

        if (!StringUtils.hasText(xml)) {
            return result;
        }

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            NodeList items = document.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                org.w3c.dom.Node itemNode = items.item(i);
                NodeList children = itemNode.getChildNodes();

                String apiIdText = "";
                String title = "";
                String author = "";
                String content = "";
                String sourceUrl = "";
                String originThumbUrl = "";

                for (int j = 0; j < children.getLength(); j++) {
                    org.w3c.dom.Node child = children.item(j);
                    if (child.getNodeName().equals("api_id")) {
                        apiIdText = child.getTextContent();
                    } else if (child.getNodeName().equals("title")) {
                        title = child.getTextContent();
                    } else if (child.getNodeName().equals("author")) {
                        author = child.getTextContent();
                    } else if (child.getNodeName().equals("content")) {
                        content = child.getTextContent();
                    } else if (child.getNodeName().equals("source_url")) {
                        sourceUrl = child.getTextContent();
                    } else if (child.getNodeName().equals("origin_thumb_url")) {
                        originThumbUrl = child.getTextContent();
                    }
                }

                Long apiId = null;
                try {
                    apiId = StringUtils.hasText(apiIdText) ? Long.parseLong(apiIdText.trim()) : null;
                } catch (NumberFormatException ignore) {
                    apiId = null;
                }

                result.add(PublicStoryItemDTO.builder()
                        .apiId(apiId)
                        .title(title)
                        .author(author)
                        .content(content)
                        .sourceUrl(sourceUrl)
                        .originThumbUrl(originThumbUrl)
                        .build());
            }
        } catch (Exception e) {
            throw new RuntimeException("공공데이터 API XML 파싱 실패", e);
        }

        return result;
    }
}
