package vttp2023.batch3.csf.assessment.cnserver.repositories;

import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;
import vttp2023.batch3.csf.assessment.cnserver.models.News;
import vttp2023.batch3.csf.assessment.cnserver.models.TagCount;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class NewsRepository {
    private final MongoTemplate mongoTemplate;

	// db.news.insertOne({
    //    postDate: 1,
    //    title: "title",
    //    description: "description",
    //    image: "imageUrl",
    //    tags: ["tag1", "tag2", "tag3"]
    // })
	public Document createNews(News news) {
        Document doc = new Document();
        doc.append("postDate", System.currentTimeMillis())
                .append("title", news.getTitle())
                .append("description", news.getDescription())
                .append("image", news.getImage())
                .append("tags", news.getTags());
        return mongoTemplate.insert(doc, "news");
    }

	// db.news.aggregate([
    // {
    //    $match: {
    //        tags: { $exists: true, $ne: null },
    //        postDate: { $gte: currentTime - timeInMinutes }
    //    }
    // },
    // {
    //    $unwind: "$tags"
    // },
    // {
    //    $group: {
    //        _id: "$tags",
    //        count: {
    //            $sum: 1
    //        }
    //    }
    // },
    // {
    //    $project: {
    //        _id: 0,
    //        tag: "$_id",
    //        count: 1
    //    }
    // },
    // {
    //    $sort: {
    //        count: -1
    //    }
    // },
    // {
    //    $limit: 10
    // }
    // ]);
    public List<TagCount> getTags(int timeInMinutes) {
        int time = timeInMinutes * 60000; // convert minutes to milliseconds
        MatchOperation matchTagsAndPostDate = Aggregation.match(
                Criteria.where("tags").exists(true).ne(null)
                        .and("postDate").gte(System.currentTimeMillis() - time)
        );
        UnwindOperation unwindTags = Aggregation.unwind("tags");
        GroupOperation groupByTags = Aggregation.group("tags")
                .count().as("count");
        ProjectionOperation projectTags = Aggregation.project("count").and("_id").as("tag").andExclude("_id");
        SortOperation sortByCount = Aggregation.sort(Sort.by(Sort.Direction.DESC, "count"));
        LimitOperation limitTo10 = Aggregation.limit(10);

        Aggregation pipeline = Aggregation.newAggregation(matchTagsAndPostDate, unwindTags, groupByTags, projectTags, sortByCount, limitTo10);

        return mongoTemplate.aggregate(pipeline, "news", TagCount.class).getMappedResults();
    }


	// db.news.aggregate([
    // {
    //    $match: {
    //        tags: "tag",
    //        postDate: { $gte: currentTime - timeInMinutes }
    //    }
    // },
    // {
    //    $sort: {
    //        postDate: -1
    //    }
    // },
    // {
    //    $limit: 10
    // }
    // ]);
    public List<News> getNewsByTag(String tag, int timeInMinutes) {
        int time = timeInMinutes * 60000; // convert minutes to milliseconds
        MatchOperation matchTagAndPostDate = Aggregation.match(
                Criteria.where("tags").is(tag)
                        .and("postDate").gte(System.currentTimeMillis() - time)
        );
        SortOperation sortByPostDate = Aggregation.sort(Sort.by(Sort.Direction.DESC, "postDate"));

        Aggregation pipeline = Aggregation.newAggregation(matchTagAndPostDate, sortByPostDate);

        return mongoTemplate.aggregate(pipeline, "news", News.class).getMappedResults();
    }


}
