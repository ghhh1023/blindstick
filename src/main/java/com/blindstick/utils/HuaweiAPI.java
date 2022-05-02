package com.blindstick.utils;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.image.v2.ImageClient;
import com.huaweicloud.sdk.image.v2.model.ImageTaggingItemBody;
import com.huaweicloud.sdk.image.v2.model.ImageTaggingReq;
import com.huaweicloud.sdk.image.v2.model.RunImageTaggingRequest;
import com.huaweicloud.sdk.image.v2.model.RunImageTaggingResponse;
import com.huaweicloud.sdk.image.v2.region.ImageRegion;
import com.obs.services.ObsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 许金涛
 * 与华为云验证相关的东西,得到华为API图像识别的相关类
 */
@Component
public class HuaweiAPI {
    @Value("${Huawei.ImageApi.ak}")
    private String ak;
    @Value("${Huawei.ImageApi.sk}")
    private String sk;
    @Value("${Huawei.Obs}")
    private String obs;
    @Value("${Huawei.EndPoint}")
    private String endPoint;
    @Value("${Huawei.BucketName}")
    private String BucketName;
    @Value("${Huawei.PrePath}")
    private String prePath;

    public String getImageTag(String path){
        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);
        ImageClient client = ImageClient.newBuilder()
                .withCredential(auth)
                .withRegion(ImageRegion.valueOf("cn-north-4"))
                .build();
        String NewPath=obs+path;
        RunImageTaggingRequest request = new RunImageTaggingRequest();
        ImageTaggingReq body = new ImageTaggingReq();
        body.withLimit(50);
        body.withThreshold(95f);
        body.withLanguage("zh");
        body.withUrl(NewPath);
        request.withBody(body);
        try {
            RunImageTaggingResponse response = client.runImageTagging(request);
            List<String> tags = new ArrayList<>();
            List<ImageTaggingItemBody> res = response.getResult().getTags();
            for (int i=0; i<res.size(); i++){
                tags.add(res.get(i).getTag());
            }
            return tags.toString();
        } catch (ConnectionException e) {
            e.printStackTrace();
        } catch (RequestTimeoutException e) {
            e.printStackTrace();
        } catch (ServiceResponseException e) {
            e.printStackTrace();
            System.out.println(e.getHttpStatusCode());
            System.out.println(e.getErrorCode());
            System.out.println(e.getErrorMsg());
        }
        return "API-Error";
    }


    public Boolean uploadImage(String localPath, String obsPath)  {
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        try {
            // 使用访问OBS上传文件, localPath为待上传的本地文件路径，需要指定到具体的文件名
            obsClient.putObject(BucketName, obsPath, new File(localPath));
            // 关闭obsClient
            obsClient.close();
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


}
