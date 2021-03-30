package org.quantum.minio.plus.service.impl;

import io.minio.messages.Bucket;
import io.minio.messages.Tags;
import org.quantum.minio.plus.dto.BucketDTO;
import org.quantum.minio.plus.repository.MinioRepository;
import org.quantum.minio.plus.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 17:31
 */
@Service
public class BucketServiceImpl implements BucketService {

    private MinioRepository minioRepository;

    @Autowired
    public BucketServiceImpl(MinioRepository minioRepository) {
        this.minioRepository = minioRepository;
    }

    @Override
    public List<BucketDTO> getList() {
        List<BucketDTO> dtos = new ArrayList<>();

        List<Bucket> buckets = minioRepository.getBuckets();
        buckets.forEach(bucket -> {
            BucketDTO dto = new BucketDTO();
            dto.setName(bucket.name());
            dto.setCreationDate(bucket.creationDate());

            Optional<Tags> tagsOptional = minioRepository.getBucketTags(bucket.name());
            tagsOptional.ifPresent(tags -> {
                dto.setTags(tags.get());
            });
            dtos.add(dto);
        });
        return dtos;
    }

    @Override
    public void create(BucketDTO dto) {
        minioRepository.createBucket(dto.getName());
    }

    @Override
    public void remove(String name) {
        minioRepository.deleteBucket(name);
    }
}
