package com.nick.api.controller;

import com.alibaba.fastjson2.JSONObject;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.nick.common.utils.http.HttpUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class PictureLocation {
    public static void main(String[] args) throws Exception {

        File file = new File("C:\\Users\\PGS\\Downloads\\5.jpg");
        readImageInfo(file);
    }

    /**
     * 提取照片里面的信息
     *
     * @param file 照片文件
     * @throws ImageProcessingException
     * @throws Exception
     */
    private static void readImageInfo(File file) throws ImageProcessingException, Exception {
        Metadata metadata = ImageMetadataReader.readMetadata(file);

        System.out.println("---打印全部详情---");
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                System.out.format("[%s] - %s = %s\n",
                        directory.getName(), tag.getTagName(), tag.getDescription());
            }
            if (directory.hasErrors()) {
                for (String error : directory.getErrors()) {
                    System.err.format("ERROR: %s", error);
                }
            }
        }

        System.out.println("--打印常用信息---");

        Double lat = null;
        Double lng = null;
        for (Directory directory : metadata.getDirectories()) {
            for (Tag tag : directory.getTags()) {
                String tagName = tag.getTagName();  //标签名
                String desc = tag.getDescription(); //标签信息
                if (tagName.equals("Image Height")) {
                    System.err.println("图片高度: " + desc);
                } else if (tagName.equals("Image Width")) {
                    System.err.println("图片宽度: " + desc);
                } else if (tagName.equals("Date/Time Original")) {
                    System.err.println("拍摄时间: " + desc);
                } else if (tagName.equals("GPS Latitude")) {
                    System.err.println("纬度 : " + desc);
                    System.err.println("纬度(度分秒格式) : " + pointToLatlong(desc));
                    lat = latLng2Decimal(desc);
                } else if (tagName.equals("GPS Longitude")) {
                    System.err.println("经度: " + desc);
                    System.err.println("经度(度分秒格式): " + pointToLatlong(desc));
                    lng = latLng2Decimal(desc);
                } else {
                    lng = 118.767413;
                    lat = 32.041544;
                }
            }
        }
        System.err.println("--经纬度转地址--");
        //经纬度转地主使用百度api
        convertGpsToLoaction(lat, lng);


    }

    /**
     * 经纬度格式  转换为  度分秒格式 ,如果需要的话可以调用该方法进行转换
     *
     * @param point 坐标点
     * @return
     */
    public static String pointToLatlong(String point) {
        Double du = Double.parseDouble(point.substring(0, point.indexOf("°")).trim());
        Double fen = Double.parseDouble(point.substring(point.indexOf("°") + 1, point.indexOf("'")).trim());
        Double miao = Double.parseDouble(point.substring(point.indexOf("'") + 1, point.indexOf("\"")).trim());
        Double duStr = du + fen / 60 + miao / 60 / 60;
        return duStr.toString();
    }

    /***
     * 经纬度坐标格式转换（* °转十进制格式）
     * @param gps
     */
    public static double latLng2Decimal(String gps) {
        String a = gps.split("°")[0].replace(" ", "");
        String b = gps.split("°")[1].split("'")[0].replace(" ", "");
        String c = gps.split("°")[1].split("'")[1].replace(" ", "").replace("\"", "");
        double gps_dou = Double.parseDouble(a) + Double.parseDouble(b) / 60 + Double.parseDouble(c) / 60 / 60;
        return gps_dou;
    }

    /**
     * api_key：注册的百度api的key
     * coords：经纬度坐标
     * http://api.map.baidu.com/reverse_geocoding/v3/?ak="+api_key+"&output=json&coordtype=wgs84ll&location="+coords
     * <p>
     * 经纬度转地址信息
     *
     * @param gps_latitude  维度
     * @param gps_longitude 精度
     */
    private static void convertGpsToLoaction(double gps_latitude, double gps_longitude) throws IOException {
        String apiKey = "MTdrAebsGT07f9oUQL1cXwTOCFmWyttg";
        String url = "http://api.map.baidu.com/reverse_geocoding/v3/?ak=" + apiKey + "&output=json&coordtype=wgs84ll&location=" + (gps_latitude + "," + gps_longitude);
        //使用 java的HttpURLConnection方法
        String res = HttpUtils.sendGet(url);
        JSONObject object = JSONObject.parseObject(res);
        if (object.containsKey("result")) {
            JSONObject result = object.getJSONObject("result");
            if (result.containsKey("formatted_address")) {
                String address = result.getString("formatted_address");
                System.err.println("拍摄地点：" + address);
            }
        }
//        使用springboot的RestTemplate方法
//        System.err.println("【url】" + url);
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> res = restTemplate.getForEntity(url, String.class);
//        // 获取响应的状态码
//        int statusCode = res.getStatusCodeValue();
//        // 获取响应体
//        String responseBody = res.getBody();
//        ObjectMapper objectMapper = new ObjectMapper();
//        Map<String, Object> map = objectMapper.readValue(responseBody, Map.class);
//        Map<String, Object> info = (Map<String, Object>) map.get("result");
//        String address = (String) info.get("formatted_address");
//        System.err.println("地址是" + address);
    }
}
