package org.quantum.minio.plus.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.services.s3.transfer.model.UploadResult;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import io.minio.messages.ListMultipartUploadsResult;
import org.quantum.minio.plus.dto.ComposeUploadPartDTO;
import org.quantum.minio.plus.dto.MultipartUploadDTO;
import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.dto.UploadPartDTO;
import org.quantum.minio.plus.dto.query.ObjectQuery;
import org.quantum.minio.plus.dto.query.PartQuery;
import org.quantum.minio.plus.service.ObjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 18:09
 */
@Service
public class ObjectServiceImpl implements ObjectService {

    private MinioClient minioClient;

    private AmazonS3 amazonS3;

    @Autowired
    public void setMinioClient(MinioClient minioClient, AmazonS3 amazonS3) {
        this.minioClient = minioClient;
        this.amazonS3 = amazonS3;
    }

    @Override
    public List<ObjectDTO> getList(ObjectQuery query) {
        List<ObjectDTO> dtos = new ArrayList<>();

        ListObjectsArgs.Builder argsBuilder = ListObjectsArgs.builder();
        argsBuilder.bucket(query.getBucketName());
        List<String> prefixs = query.getPrefixs();
        if(Objects.nonNull(prefixs) && prefixs.size() > 0){
            argsBuilder.prefix(prefixs.stream().collect(Collectors.joining()));
        }

        Iterable<Result<Item>> results = minioClient.listObjects(argsBuilder.build());
        results.forEach(result -> {
            ObjectDTO dto = new ObjectDTO();
            try {
                Item item = result.get();
                dto.setEtag(item.etag());
                dto.setObjectName(item.objectName());
                dto.setSize(item.size());
                dto.setUserMetaData(item.userMetadata());

                if(!item.isDir()){
                    dto.setLastModified(item.lastModified());
                }

                dto.setStorageClass(item.storageClass());
                dto.setDir(item.isDir());
            } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
                // 有问题
                return;
            }
            dtos.add(dto);
        });
        dtos.sort((s1, s2) -> {
            if(s1.getDir() ^ s2.getDir()){
                return s1.getDir()? -1 : 1;
            }else{
                return 0;
            }
        });
        return dtos;
    }

    @Override
    public MultipartUploadDTO initiateMultipartUpload(MultipartUploadDTO inputDto) {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(inputDto.getBucketName(), inputDto.getKey());
        InitiateMultipartUploadResult result = amazonS3.initiateMultipartUpload(request);

        MultipartUploadDTO dto = new MultipartUploadDTO();
        dto.setUploadId(result.getUploadId());
        dto.setKey(result.getKey());
        return dto;
    }

    @Override
    public List<MultipartUploadDTO> getMultipartUploadList(String bucketName) {
        List<MultipartUploadDTO> dtos = new ArrayList<>();

        ListMultipartUploadsRequest request = new ListMultipartUploadsRequest(bucketName);
        MultipartUploadListing listing = amazonS3.listMultipartUploads(request);
        List<MultipartUpload> multipartUploads = listing.getMultipartUploads();
        multipartUploads.forEach(multipartUpload -> {
            MultipartUploadDTO dto = new MultipartUploadDTO();
            dto.setKey(multipartUpload.getKey());
            dto.setUploadId(multipartUpload.getUploadId());
            dtos.add(dto);
        });
        return dtos;
    }

    @Override
    public List<UploadPartDTO> getUploadPartList(PartQuery partQuery) {
        List<UploadPartDTO> dtos = new ArrayList<>();

        ListPartsRequest request = new ListPartsRequest(partQuery.getBucketName(), partQuery.getKey(), partQuery.getUploadId());
        PartListing uploadResult = amazonS3.listParts(request);
        List<PartSummary> partSummaries = uploadResult.getParts();
        partSummaries.forEach(partSummary -> {
            UploadPartDTO dto = new UploadPartDTO();
            dto.setETag(partSummary.getETag());
            dto.setSize(partSummary.getSize());
            dto.setPartNumber(partSummary.getPartNumber());
            dtos.add(dto);
        });
        return dtos;
    }

    @Override
    public String composeUploadPart(ComposeUploadPartDTO dto) {
        List<PartETag> partETags = new ArrayList<>();
        dto.getParts().forEach(uploadPartDto -> {
            PartETag partETag = new PartETag(uploadPartDto.getPartNumber(), uploadPartDto.getETag().substring(1, uploadPartDto.getETag().length() - 1));
            partETags.add(partETag);
        });

        CompleteMultipartUploadRequest request = new CompleteMultipartUploadRequest(dto.getBucketName(), dto.getKey(), dto.getUploadId(), partETags);
        amazonS3.completeMultipartUpload(request);
        return this.getPresignedUrl(dto.getBucketName(), dto.getKey(), Method.GET.toString());
    }

    @Override
    public String getPresignedUrl(String bucketName, String objectName, String method) {
        String url = "";
        try {
            url = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs
                            .builder()
                            .method(Method.valueOf(method))
                            .expiry(7, TimeUnit.DAYS)
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return url;
    }

    @Override
    public Map<String, String> getPresignedFormData(String bucketName, String objectName) {
        PostPolicy policy = new PostPolicy(bucketName, ZonedDateTime.now().plusDays(7));
        policy.addEqualsCondition("key", objectName);

        Map<String, String> formData = null;
        try {
            formData = minioClient.getPresignedPostFormData(policy);
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return formData;
    }

    @Override
    public void create(ObjectDTO dto) {
        try {
            String objectName = objectNameHandler(dto);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(dto.getBucketName())
                    .object(objectName)
                    .stream(new ByteArrayInputStream(new byte[] {}), 0, -1)
                    .build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String compose(String bucketName, String objectName, List<String> parts) {
        List<ComposeSource> sourceObjectList = new ArrayList<>();
        parts.forEach(part -> {
            sourceObjectList.add(ComposeSource.builder().bucket(bucketName).object(part).build());
        });
        try {
            minioClient.composeObject(ComposeObjectArgs.builder().bucket(bucketName).object(objectName).sources(sourceObjectList).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return String.format("%s/%s/%s");
    }

    @Override
    public void delete(String bucketName, String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs
                            .builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build()
            );
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String objectNameHandler(ObjectDTO dto) {
        String targetName;
        List<String> prefixs = dto.getPrefixs();
        if(Objects.nonNull(prefixs) && prefixs.size() > 0){
            targetName = prefixs.stream().collect(Collectors.joining()) + dto.getObjectName();
        } else {
            targetName = dto.getObjectName();
        }
        return targetName;
    }
}
