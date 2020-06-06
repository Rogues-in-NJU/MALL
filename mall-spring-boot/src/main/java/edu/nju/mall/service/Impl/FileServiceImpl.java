package edu.nju.mall.service.Impl;

import edu.nju.mall.service.FileService;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {

    @Override
    public Map<String, String> saveImage(MultipartFile file) {
        String path =  "/home/ubuntu/images/"; // 文件保存路径
//        path = request.getServletContext().getRealPath("templates/images/");
//        try {
//            path = ResourceUtils.getURL("classpath:").getPath() + "static/";
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        System.out.println(path);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!");
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
        String saveFileName = id + "-" + "product" + "." + fileExtensionName;
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
        result.put("uid", id);
        result.put("name", fileName);
        result.put("url", '/' + saveFileName);
//        result.put("path", path);
//        System.out.println(fileName);
        return result;
    }

    @Override
    public Map<String, String> saveInfoImage(MultipartFile file) {
        String path =  "/home/ubuntu/images/"; // 文件保存路径
//        try {
//            path = ResourceUtils.getURL("classpath:").getPath() + "static/";
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        System.out.println(path);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!");
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
        String saveFileName = id + "-" + "productInfo" + "." + fileExtensionName;
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
        result.put("uid", id);
        result.put("name", fileName);
        result.put("url", '/' + saveFileName);
//        result.put("path", path);
//        System.out.println(fileName);
        return result;
    }

    @Override
    public Map<String, String> saveCoverImage(MultipartFile file) {
        String path =  "/home/ubuntu/images/"; // 文件保存路径
//        try {
//            path = ResourceUtils.getURL("classpath:").getPath() + "static/";
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        System.out.println(path);
//        System.out.println("!!!!!!!!!!!!!!!!!!!!");
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
        String saveFileName = id + "-" + "cover" + "." + fileExtensionName;
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
        result.put("uid", id);
        result.put("name", fileName);
        result.put("url", '/' + saveFileName);
//        result.put("path", path);
//        System.out.println(fileName);
        return result;
    }

    @Override
    public void deleteImage(String address) {

    }

    @Override
    public void deleteInfoImage(String address) {

    }
}
