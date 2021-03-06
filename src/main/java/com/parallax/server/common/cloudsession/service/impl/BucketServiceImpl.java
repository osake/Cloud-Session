/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.parallax.server.common.cloudsession.service.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.persist.Transactional;
import com.parallax.server.common.cloudsession.db.dao.BucketDao;
import com.parallax.server.common.cloudsession.db.dao.UserDao;
import com.parallax.server.common.cloudsession.db.generated.tables.records.BucketRecord;
import com.parallax.server.common.cloudsession.db.generated.tables.records.UserRecord;
import com.parallax.server.common.cloudsession.exceptions.EmailNotConfirmedException;
import com.parallax.server.common.cloudsession.exceptions.InsufficientBucketTokensException;
import com.parallax.server.common.cloudsession.exceptions.UnknownBucketTypeException;
import com.parallax.server.common.cloudsession.exceptions.UnknownUserIdException;
import com.parallax.server.common.cloudsession.exceptions.UserBlockedException;
import com.parallax.server.common.cloudsession.service.BucketService;
import java.sql.Timestamp;
import java.util.Date;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Michel
 */
@Singleton
@Transactional
public class BucketServiceImpl implements BucketService {

    private static final Logger LOG = LoggerFactory.getLogger(BucketServiceImpl.class);

    private BucketDao bucketDao;

    private UserDao userDao;

    private Configuration configuration;

    @Inject
    public void setBucketDao(BucketDao bucketDao) {
        this.bucketDao = bucketDao;
    }

    @Inject
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Inject
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void consumeTokens(Long idUser, String type, int tokenCount) throws UnknownBucketTypeException, UnknownUserIdException, InsufficientBucketTokensException, EmailNotConfirmedException, UserBlockedException {
        // check type is supported (throws exception)
        if (!(configuration.getList("bucket.types").contains(type))) {
            LOG.warn("Unknown bucket type: {}", type);
            throw new UnknownBucketTypeException(type);
        }
        // check user exists (throws exception)
        UserRecord user = userDao.getUser(idUser);
        if (user.getBlocked()) {
            throw new UserBlockedException();
        }
        if (!user.getConfirmed()) {
            throw new EmailNotConfirmedException();
        }
        consumeTokensInternal(idUser, type, tokenCount);
    }

    @Override
    public void consumeTokensInternal(Long idUser, String type, int tokenCount) throws InsufficientBucketTokensException {
        BucketRecord bucket = bucketDao.getBucket(idUser, type);

        int bucketSize = configuration.getInt("bucket." + type + ".size", 0);
        int inputStream = configuration.getInt("bucket." + type + ".input", 0);
        int inputFrequency = configuration.getInt("bucket." + type + ".freq", 1000);
        int oldBucketContent = bucket.getContent();

        if (bucket.getContent() == -1) {
            // New bucket (set to full)
            bucket.setContent(bucketSize);
            oldBucketContent = bucketSize;
        } else {
            // Add tokens depending on timestamp (not more then bucket size)
            long elapsedMilliseconds = new Date().getTime() - bucket.getTimestamp().getTime();
            long inputCount = elapsedMilliseconds / inputFrequency;
            long bucketContent = Math.min(bucketSize, (inputCount * inputStream) + bucket.getContent());
            bucket.setContent(new Long(bucketContent).intValue());
        }

        // check if there are enough tokens in the bucket
        LOG.info("Consume: Content: {}, using {}. has enough? {}", bucket.getContent(), tokenCount, (bucket.getContent() < tokenCount ? "no" : "yes"));
        if (bucket.getContent() < tokenCount) {
            long millisecondsTillEnough = (tokenCount - oldBucketContent) * inputFrequency;
            Date dateWhenEnough = new Date(bucket.getTimestamp().getTime() + millisecondsTillEnough);
            throw new InsufficientBucketTokensException(type, bucket.getContent(), tokenCount, dateWhenEnough);
        }

        // if enough tokens are available, remove tokens, update time and save
        bucket.setContent(bucket.getContent() - tokenCount);
        bucket.setTimestamp(new Timestamp(new Date().getTime()));
        bucket.update();
    }

    @Override
    public void hasSufficientTokensInternal(Long idUser, String type, int tokenCount) throws InsufficientBucketTokensException {
        BucketRecord bucket = bucketDao.getBucket(idUser, type);

        int bucketSize = configuration.getInt("bucket." + type + ".size", 0);
        int inputStream = configuration.getInt("bucket." + type + ".input", 0);
        int inputFrequency = configuration.getInt("bucket." + type + ".freq", 1000);
        int oldBucketContent = bucket.getContent();

        if (bucket.getContent() == -1) {
            // New bucket (set to full)
            bucket.setContent(bucketSize);
            oldBucketContent = bucketSize;
        } else {
            // Add tokens depending on timestamp (not more then bucket size)
            long elapsedMilliseconds = new Date().getTime() - bucket.getTimestamp().getTime();
            long inputCount = elapsedMilliseconds / inputFrequency;
            long bucketContent = Math.min(bucketSize, (inputCount * inputStream) + bucket.getContent());
            bucket.setContent(new Long(bucketContent).intValue());
        }

        // check if there are enough tokens in the bucket
        LOG.info("Check: Content: {}, using {}. has enough? {}", bucket.getContent(), tokenCount, (bucket.getContent() < tokenCount ? "no" : "yes"));
        if (bucket.getContent() < tokenCount) {
            long millisecondsTillEnough = (tokenCount - oldBucketContent) * inputFrequency;
            Date dateWhenEnough = new Date(bucket.getTimestamp().getTime() + millisecondsTillEnough);
            throw new InsufficientBucketTokensException(type, bucket.getContent(), tokenCount, dateWhenEnough);
        }
    }

}
