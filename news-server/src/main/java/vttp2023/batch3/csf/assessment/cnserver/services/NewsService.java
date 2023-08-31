package vttp2023.batch3.csf.assessment.cnserver.services;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.stereotype.Service;

import org.springframework.web.multipart.MultipartFile;
import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;
import vttp2023.batch3.csf.assessment.cnserver.repositories.ImageRepository;
import vttp2023.batch3.csf.assessment.cnserver.repositories.NewsRepository;

@Service
@RequiredArgsConstructor
public class NewsService {
	private final ImageRepository imageRepository;
	private final NewsRepository newsRepository;

	public String postNews(News news, MultipartFile picture) throws IOException {
		String imageName = UUID.randomUUID().toString();
		imageRepository.putObject(picture, imageName);
		news.setImage(imageName);
		Document doc = newsRepository.createNews(news);
		return doc.getObjectId("_id").toString();
	}

	public List<TagCount> getTags(int timeInMinutes) {
		return newsRepository.getTags(timeInMinutes);
	}

	public List<News> getNewsByTag(String tag, int timeInMinutes) {
		return newsRepository.getNewsByTag(tag, timeInMinutes);
	}
	
}
