package com.nick.api.service;

import com.alibaba.fastjson2.JSON;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * minio 操作类
 */
@Service
@Slf4j
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    @Value("${minio.endpoint}")
    private String endpoint;

    public List<Object> list() throws Exception {
        //获取bucket列表
        Iterable<Result<Item>> myObjects = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucket).build());
        Iterator<Result<Item>> iterator = myObjects.iterator();
        List<Object> items = new ArrayList<>();
        String format = "{'fileName':'%s','filesize':'%s'}";
        while (iterator.hasNext()) {
            Item item = iterator.next().get();
            items.add(JSON.parse(String.format(format, item.objectName(), formatFileSize(item.size()))));
        }
        return items;
    }

    private static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " GB";
        }
        return fileSizeString;
    }

    /**
     * 本地文件上传
     *
     * @param localPath  本地路径
     * @param remotePath 远程路径
     * @return 可访问地址
     */
    public String upload(String localPath, String remotePath) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        UploadObjectArgs uploadObjectArgs = UploadObjectArgs.builder()
                .bucket(bucket)
                .object(localPath)
                .filename(remotePath)
                .build();
        minioClient.uploadObject(uploadObjectArgs);
        return "/" + bucket + "/" + remotePath;
    }

    /**
     * 用流上传
     *
     * @param file       文件
     * @param remotePath 远程路径
     * @return 可访问地址
     */
    public String upload(MultipartFile file, String remotePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        file.getSize可以获取文件大小
//        文件大小设置为 -1 表示未知，分片大小设置为 10485760 字节（即 10 MB）。
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucket)
                .object(remotePath)
                .stream(file.getInputStream(), -1, 10485760)
                .contentType(file.getContentType())
                .build();
        minioClient.putObject(args);
        return "/" + bucket + "/" + remotePath;
    }

    /**
     * 删除文件
     *
     * @param filename 文件名
     * @return
     */
    public void delete(String filename) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        StatObjectArgs statObjectArgs = StatObjectArgs.builder().object(filename).bucket(bucket).build();
        boolean isObjectExists = minioClient.statObject(statObjectArgs) != null;
        if (isObjectExists) {
            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .build();
            minioClient.removeObject(args);
        }
    }

    /**
     * 获取流
     *
     * @param remotePath 远程路径
     */
    public InputStream getInputStream(String remotePath) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectArgs args = GetObjectArgs.builder()
                .bucket(bucket)
                .object(remotePath)
                .build();
        return minioClient.getObject(args);
    }
}