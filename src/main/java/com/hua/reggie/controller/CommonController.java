package com.hua.reggie.controller;
import com.hua.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @author : hua
 * @date : 2023/3/1 22:10
 */
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private  String basePath;

    @PostMapping("/upload")

    /* 在不使用框架之前，都是使用原生的HttpServletRequest来接收上传的数据，
      文件是以二进制流传递到后端的，然后需要我们自己转换为File类。
      使用了MultipartFile工具类之后，我们对文件上传的操作就简便许多了。
     */
    public R<String> upload(MultipartFile file){

        log.info(file.toString());//file临时文件，需要放到指定文件中

        String originFileName =  file.getOriginalFilename();//  public int lastIndexOf(String str): 返回指定子字符串在此字符串中最右边出现处的索引，如果此字符串中没有这样的字符，则返回 -1。
        String suffix = originFileName.substring(originFileName.lastIndexOf("."));//使用UUID重新生成文件名，防止文件名重复
        String fileName = UUID.randomUUID()+suffix;
        log.info(fileName);
        //dir 石否存在
        File dir = new File(basePath);
        if(!dir.exists()){
            dir.mkdirs();
        }

        try{
            file.transferTo(new File(basePath+fileName));//将临时文件转储到指定位置
        }catch (IOException e){
            e.printStackTrace();
        }
        return R.success(fileName);
    }
    @GetMapping("/download")
    public void download(@RequestParam String name, HttpServletResponse response) {
        try{
            FileInputStream fileInputStream = new FileInputStream(new File(basePath+name));
            log.info(basePath+name);
            ServletOutputStream fileOutputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len =0;
            byte [] bytes = new byte[1024];
            while((len =fileInputStream.read(bytes))!=-1){
                fileOutputStream.write(bytes,0,len);
                fileOutputStream.flush();
            }
            fileOutputStream.close();
            fileInputStream.close();

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
