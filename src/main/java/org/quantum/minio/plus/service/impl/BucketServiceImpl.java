package org.quantum.minio.plus.service.impl;

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
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.time.Instant;
import java.util.*;

/**
 * 桶服务实现
 * @author ike
 * @date 2021 年 03 月 29 日 17:31
 */
@Service
public class BucketServiceImpl implements BucketService {
    private S3Client s3Client;

    @Autowired
    public void setMinioClient(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public List<BucketDTO> getList() {
        List<BucketDTO> dtos = new ArrayList<>();

        ListBucketsResponse response = s3Client.listBuckets();
        List<Bucket> buckets = response.buckets();
        buckets.forEach(bucket -> {
            BucketDTO dto = new BucketDTO();
            dto.setName(bucket.name());
            dto.setCreationDate(bucket.creationDate());
            dtos.add(dto);
        });
        return dtos;
    }

    @Override
    public void create(BucketDTO dto) {
        CreateBucketRequest request = CreateBucketRequest.builder()
                .bucket(dto.getName())
                .build();
        s3Client.createBucket(request);
    }

    @Override
    public void remove(String name) {
        DeleteBucketRequest request = DeleteBucketRequest.builder()
                .bucket(name)
                .build();
        s3Client.deleteBucket(request);
    }

    @Override
    public List<BucketLifecycleRuleDTO> getLifecycleRuleList(BucketLifecycleConfigurationQuery lifecycleConfigurationQuery) {
        List<BucketLifecycleRuleDTO> dtos = new ArrayList<>();

        try {
            GetBucketLifecycleConfigurationResponse response = s3Client.getBucketLifecycleConfiguration(GetBucketLifecycleConfigurationRequest.builder()
                    .bucket(lifecycleConfigurationQuery.getBucketName())
                    .build());

            List<LifecycleRule> rules = response.rules();
            rules.forEach(lifecycleRule -> {
                BucketLifecycleRuleDTO lifecycleRuleDto = new BucketLifecycleRuleDTO();
                lifecycleRuleDto.setId(lifecycleRule.id());
                lifecycleRuleDto.setDate(lifecycleRule.expiration().date());
                lifecycleRuleDto.setDays(lifecycleRule.expiration().days());
                lifecycleRuleDto.setStatus(lifecycleRule.status().toString());

                if(Objects.nonNull(lifecycleRule.filter().and())){
                    lifecycleRuleDto.setPrefix(lifecycleRule.filter().and().prefix());
                    List<Tag> tags = lifecycleRule.filter().and().tags();
                    if(Objects.nonNull(tags) && tags.size() > 0){
                        Map<String, String> tagMap = new HashMap<>(16);
                        tags.forEach(tag -> {
                            tagMap.put(tag.key(), tag.value());
                        });
                        lifecycleRuleDto.setTags(tagMap);
                    }
                }

                dtos.add(lifecycleRuleDto);
            });
        } catch (S3Exception e) {
            return dtos;
        }
        return dtos;
    }

    @Override
    public void createLifecycleRule(BucketLifecycleRuleDTO lifecycleRuleDto) {
        try{
            GetBucketLifecycleConfigurationResponse response = s3Client.getBucketLifecycleConfiguration(GetBucketLifecycleConfigurationRequest.builder()
                    .bucket(lifecycleRuleDto.getBucketName())
                    .build());
            if(StringUtils.isEmpty(lifecycleRuleDto.getPrefix()) && response.rules().size() > 0) {
                throw new BizException(BizExceptionState.PARAMETER_ERROR);
            }
        }catch (S3Exception e){

        }catch (BizException e){
            e.printStackTrace();
        }

        String id = UUID.randomUUID().toString();
        ExpirationStatus status = this.toStatus(lifecycleRuleDto.getStatus());
        LifecycleRuleFilter filter = this.toLifecycleRuleFilter(lifecycleRuleDto.getPrefix(), lifecycleRuleDto.getTags());
        LifecycleExpiration expiration = this.toLifecycleExpiration(lifecycleRuleDto.getCleanStrategy(), lifecycleRuleDto.getDate(), lifecycleRuleDto.getDays());

        ArrayList<LifecycleRule> ruleList = new ArrayList<>();
        ruleList.add(LifecycleRule.builder()
                .id(id)
                .filter(filter)
                .expiration(expiration)
                .status(status)
                .build());

        BucketLifecycleConfiguration lifecycleConfiguration = BucketLifecycleConfiguration.builder()
                .rules(ruleList)
                .build();

        PutBucketLifecycleConfigurationRequest request = PutBucketLifecycleConfigurationRequest.builder()
                .bucket(lifecycleRuleDto.getBucketName())
                .lifecycleConfiguration(lifecycleConfiguration)
                .build();

        s3Client.putBucketLifecycleConfiguration(request);

    }

    @Override
    public void deleteLifecycleRule(String bucketName, String id) {
        GetBucketLifecycleConfigurationResponse getResponse = s3Client.getBucketLifecycleConfiguration(GetBucketLifecycleConfigurationRequest.builder()
                .bucket(bucketName)
                .build());

        List<LifecycleRule> rules = new LinkedList<>(getResponse.rules());

        // 如果只有一个，直接删除生命周期
        if(rules.size() == 1) {
            s3Client.deleteBucketLifecycle(DeleteBucketLifecycleRequest.builder()
                    .bucket(bucketName)
                    .build());
            return;
        }
        // 根据标识删除
        rules.removeIf(lifecycleRule -> lifecycleRule.id().equals(id));

        // 重设
        s3Client.putBucketLifecycleConfiguration(PutBucketLifecycleConfigurationRequest.builder()
                .bucket(bucketName)
                .lifecycleConfiguration(BucketLifecycleConfiguration.builder()
                        .rules(rules)
                        .build())
                .build());
    }

    public ExpirationStatus toStatus(String statusString){
        ExpirationStatus status;
        if(ExpirationStatus.ENABLED.toString().equals(statusString)) {
            status = ExpirationStatus.ENABLED;
        } else {
            status = ExpirationStatus.DISABLED;
        }
        return status;
    }

    public LifecycleRuleFilter toLifecycleRuleFilter(String prefix, Map<String, String> tags){
        // 设置规则过滤
        LifecycleRuleFilter.Builder filterBuilder = LifecycleRuleFilter.builder();

        LifecycleRuleAndOperator.Builder andOperatorBuilder = LifecycleRuleAndOperator.builder();
        andOperatorBuilder.prefix(prefix);
        if(Objects.nonNull(tags) && tags.size() > 0) {
            Tag[] tagArray = tags.entrySet().stream().map(d -> Tag.builder().key(d.getKey()).value(d.getValue()).build()).toArray(Tag[]::new);
            andOperatorBuilder.tags(tagArray);
        }
        filterBuilder.and(andOperatorBuilder.build());
        return filterBuilder.build();
    }

    public LifecycleExpiration toLifecycleExpiration(String cleanStrategy, Instant date, Integer days){
        // 设置过期时间
        LifecycleExpiration.Builder expirationBuilder = LifecycleExpiration.builder();
        if(CleanStrategy.EXPIRATION_DATE.toString().equals(cleanStrategy)){
            expirationBuilder.date(date);
        }else if(CleanStrategy.EXPIRATION_DAYS.toString().equals(cleanStrategy)){
            expirationBuilder.days(days);
        }else{
            throw new BizException(BizExceptionState.PARAMETER_ERROR);
        }
        return expirationBuilder.build();
    }
}
