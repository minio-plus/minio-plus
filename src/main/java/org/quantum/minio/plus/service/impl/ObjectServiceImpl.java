package org.quantum.minio.plus.service.impl;

import io.minio.Result;
import io.minio.errors.*;
import io.minio.messages.Item;
import org.quantum.minio.plus.dto.ObjectDTO;
import org.quantum.minio.plus.repository.MinioRepository;
import org.quantum.minio.plus.service.ObjectServiceI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ike
 * @date 2021 年 03 月 29 日 18:09
 */
@Service
public class ObjectServiceImpl implements ObjectServiceI {

    @Autowired
    private MinioRepository minioRepository;

    @Override
    public List<ObjectDTO> getList() {
        List<ObjectDTO> dtos = new ArrayList<>();
        Iterable<Result<Item>> results = minioRepository.getObjects();
        results.forEach(result -> {
            ObjectDTO dto = new ObjectDTO();
            try {
                Item item = result.get();
                dto.setEtag(item.etag());
                dto.setName(item.objectName());
                dto.setSize(item.size());
                dto.setLastUpdateTime(item.lastModified());
            } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {

            }
            dtos.add(dto);
        });
        return dtos;
    }
}
