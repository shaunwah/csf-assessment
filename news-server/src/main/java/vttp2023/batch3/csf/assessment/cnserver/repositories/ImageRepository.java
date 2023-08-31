package vttp2023.batch3.csf.assessment.cnserver.repositories;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Repository
@RequiredArgsConstructor
public class ImageRepository {
    private final AmazonS3 s3;
    private final String BUCKET_NAME = "csf-assessment";
	
    public void putObject(MultipartFile file, String imageName) throws IOException {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        PutObjectRequest req = new PutObjectRequest(BUCKET_NAME, imageName, file.getInputStream(), objectMetadata);
        req.withCannedAcl(CannedAccessControlList.PublicRead);
        s3.putObject(req);
    }

}
