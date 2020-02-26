package edu.nju.mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class FileUploadController {

    @Value("${picture.product.address}")
    private String productPictureAddress;

    @Value("${picture.info.address}")
    private String productInfoPictureAddress;

    @RequestMapping("/upload-product-info")
    public Map<String, String> upload(@RequestParam("upload_file")MultipartFile file) {
        String path = null; // 文件保存路径
        try {
            path = ResourceUtils.getURL("classpath:").getPath() + "tempPicture/info/";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(path);
        System.out.println("!!!!!!!!!!!!!!!!!!!!");
        /**
         * 可能会出现重复文件，所以我们要对文件进行一个重命名的操作
         * 截取文件的原始名称，然后将扩展名和文件名分开，使用：时间戳-文件名.扩展名的格式保存
         */
        // 获取文件名称
        String fileName = file.getOriginalFilename();
        // 获取扩展名
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".") + 1);
        // 获取文件名
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        // 生成最终保存的文件名,格式为: 时间戳-文件名.扩展名
        String id = String.valueOf(new Date().getTime());
        String saveFileName = id + "-" + name + "." + fileExtensionName;
        /**
         * 上传操作：可能upload目录不存在，所以先判断一下如果不存在，那么新建这个目录
         */
        File fileDir = new File(path);
        if (!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        /**
         * 上传
         */
        File targetFile = new File(path, saveFileName);
        try {
            file.transferTo(targetFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 返回值，这三个对象是ng-zorro那边需要的
         */
        Map<String, String> result = new HashMap<>();
        result.put("url", saveFileName);
        result.put("uid", id);
        result.put("name", fileName);
        result.put("path", path);
        System.out.println(fileName);
        return result;
    }
}