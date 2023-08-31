package vttp2023.batch3.csf.assessment.cnserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;
import vttp2023.batch3.csf.assessment.cnserver.services.NewsService;
import vttp2023.batch3.csf.assessment.cnserver.utilities.Utilities;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @PostMapping("/news")
    public ResponseEntity<String> createNews(
            @RequestPart String title,
            @RequestPart String description,
            @RequestPart(required = false) String tags,
            @RequestPart MultipartFile image
    ) throws IOException {
        News news = new News();
        news.setTitle(title);
        news.setDescription(description);
        if (tags != null) {
            news.setTags(List.of(tags.split(",")));
        }
        String result = newsService.postNews(news, image);
        if (result != null && !result.isEmpty()) {
            return ResponseEntity.ok(Utilities.returnNewsId(result).toString());
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TagCount>> getTags(@RequestParam(defaultValue = "5") Integer time) {
        return ResponseEntity.ok(newsService.getTags(time));
    }

    @GetMapping("/news")
    public ResponseEntity<List<News>> getTags(
            @RequestParam String tag,
            @RequestParam(defaultValue = "5") Integer time
    ) {
        return ResponseEntity.ok(newsService.getNewsByTag(tag, time));
    }
}
