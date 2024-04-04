package com.nick.api.controller;

import com.nick.api.service.MinioService;
import com.nick.common.core.domain.AjaxResult;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("/api")
public class MinioController {

    @Autowired
    private MinioService minioService;


    @GetMapping("/getBucketList")
    public AjaxResult list() throws Exception {
        //获取bucket列表
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("data", minioService.list());
        return ajaxResult;
    }


    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam(name = "file", required = false) MultipartFile file) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String originalFilename = file.getOriginalFilename();
        String suffix = null;
        if (originalFilename != null) {
            suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        //使用当前时间戳和文件类型作为文件名
        String fileName = System.currentTimeMillis() + suffix;
        String url = minioService.upload(file, fileName);
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("url", url);
        return ajaxResult;
    }

    @PostMapping("/remove")

    public AjaxResult remove(String filename) {
        try {
            minioService.delete(filename);
        } catch (Exception e) {
            log.error(e.getMessage());
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }
}