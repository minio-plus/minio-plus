package org.quantum.minio.plus.service.impl;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.*;
import org.apache.commons.lang3.StringUtils;
import org.quantum.minio.plus.BizException;
import org.quantum.minio.plus.BizExceptionState;
import org.quantum.minio.plus.constant.CleanStrategy;
import org.quantum.minio.plus.dto.BucketDTO;
import org.quantum.minio.plus.dto.BucketLifecycleRuleDTO;
import org.quantum.minio.plus.dto.query.BucketLifecycleConfigurationQuery;
import org.quantum.minio.plus.service.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * 桶服务实现
 * @author ike
 * @date 2021 年 03 月 29 日 17:31
 */
@Service
public class BucketServiceImpl implements BucketService {

    private MinioClient minioClient;

    @Autowired
    public void setMinioClient(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public List<BucketDTO> getList() {
        List<BucketDTO> dtos = new ArrayList<>();

        try {
            List<Bucket> buckets = minioClient.listBuckets();
            buckets.forEach(bucket -> {
                BucketDTO dto = new BucketDTO();
                dto.setName(bucket.name());
                dto.setCreationDate(bucket.creationDate());
                dtos.add(dto);
            });
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return dtos;
    }

    @Override
    public void create(BucketDTO dto) {
        try {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(dto.getName()).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void remove(String name) {
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(name).build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<BucketLifecycleRuleDTO> getLifecycleRuleList(BucketLifecycleConfigurationQuery lifecycleConfigurationQuery) {
        List<BucketLifecycleRuleDTO> dtos = new ArrayList<>();
        try {
            LifecycleConfiguration lifecycleConfiguration = minioClient.getBucketLifecycle(GetBucketLifecycleArgs.builder()
                    .bucket(lifecycleConfigurationQuery.getBucketName())
                    .build());
            if(Objects.isNull(lifecycleConfiguration)){
                return dtos;
            }

            List<LifecycleRule> lifecycleRules = lifecycleConfiguration.rules();
            if(Objects.isNull(lifecycleRules)){
                return dtos;
            }

            lifecycleRules.forEach(lifecycleRule -> {
                BucketLifecycleRuleDTO lifecycleRuleDto = new BucketLifecycleRuleDTO();
                lifecycleRuleDto.setId(lifecycleRule.id());
                lifecycleRuleDto.setDate(lifecycleRule.expiration().date());
                lifecycleRuleDto.setDays(lifecycleRule.expiration().days());
                lifecycleRuleDto.setStatus(lifecycleRule.status().toString());

                if(Objects.nonNull(lifecycleRule.filter().andOperator())){
                    lifecycleRuleDto.setPrefix(lifecycleRule.filter().andOperator().prefix());
                    lifecycleRuleDto.setTags(lifecycleRule.filter().andOperator().tags());
                }

                dtos.add(lifecycleRuleDto);
            });
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return dtos;
    }

    @Override
    public void createLifecycleRule(BucketLifecycleRuleDTO lifecycleRuleDto) {
        String id = UUID.randomUUID().toString();

        Status status = Status.ENABLED;
        if(Status.DISABLED.toString().equals(lifecycleRuleDto.getStatus())){
            status = Status.DISABLED;
        }

        // 过期策略
        Expiration expiration;
        if(CleanStrategy.EXPIRATION_DATE.toString().equals(lifecycleRuleDto.getCleanStrategy())) {
            ZonedDateTime zonedDateTime = lifecycleRuleDto.getDate();
            // 日期过期，时间必须是格林威治时间的午夜
            expiration = new Expiration(zonedDateTime.withHour(0).withMinute(0).withSecond(0), null, null);
        } else if(CleanStrategy.EXPIRATION_DAYS.toString().equals(lifecycleRuleDto.getCleanStrategy())) {
            // 天数过期
            expiration = new Expiration((ZonedDateTime) null, lifecycleRuleDto.getDays(), null);
        } else {
            // 参数错误
            throw new BizException(BizExceptionState.PARAMETER_ERROR);
        }

        try {
            // 规则处理
            // 查询已存在的配置
            LifecycleConfiguration lifecycleConfiguration = minioClient.getBucketLifecycle(GetBucketLifecycleArgs.builder().bucket(lifecycleRuleDto.getBucketName()).build());

            List<LifecycleRule> rules = new LinkedList<>();
            if(Objects.nonNull(lifecycleConfiguration) && Objects.nonNull(lifecycleConfiguration.rules())){
                rules = new LinkedList(lifecycleConfiguration.rules());
            }
            // 如果前缀为空且规则大于0 或者如果 已存在一个规则并且前缀为空，直接放弃所有以免出错，前端应提示此敏感操作。
            if(StringUtils.isEmpty(lifecycleRuleDto.getPrefix()) && rules.size() > 0) {
                rules = new LinkedList<>();
            } else if(rules.size() == 1 && Objects.isNull(rules.get(0).filter().andOperator())) {
                rules = new LinkedList<>();
            } else if(rules.size() == 1 && StringUtils.isEmpty(rules.get(0).filter().andOperator().prefix())) {
                rules = new LinkedList<>();
            }

            // 规则过滤配置
            Map<String, String> tags = lifecycleRuleDto.getTags();
            AndOperator andOperator = new AndOperator(lifecycleRuleDto.getPrefix(), tags);
            RuleFilter ruleFilter = new RuleFilter(andOperator);

            rules.add(new LifecycleRule(
                            status,
                            null,
                            expiration,
                            ruleFilter,
                            id,
                            null,
                            null,
                            null
                    )
            );

            LifecycleConfiguration config = new LifecycleConfiguration(rules);

            minioClient.setBucketLifecycle(SetBucketLifecycleArgs.builder()
                    .bucket(lifecycleRuleDto.getBucketName())
                    .config(config)
                    .build()
            );
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteLifecycleRule(String bucketName, String id) {
        try {
            LifecycleConfiguration lifecycleConfiguration = minioClient.getBucketLifecycle(GetBucketLifecycleArgs.builder().bucket(bucketName).build());
            if(Objects.isNull(lifecycleConfiguration)){
                // 抛出异常，不存在的记录
                return;
            }

            // 如果只有一个，直接删除生命周期
            if(lifecycleConfiguration.rules().size() == 1) {
                minioClient.deleteBucketLifecycle(DeleteBucketLifecycleArgs.builder().bucket(bucketName).build());
                return;
            }

            List<LifecycleRule> rules = new LinkedList<>(lifecycleConfiguration.rules());
            // 根据标识删除
            rules.removeIf(lifecycleRule -> lifecycleRule.id().equals(id));

            // 重设
            LifecycleConfiguration config = new LifecycleConfiguration(rules);
            minioClient.setBucketLifecycle(SetBucketLifecycleArgs.builder()
                    .bucket(bucketName)
                    .config(config)
                    .build());
        } catch (MinioException | InvalidKeyException | IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
