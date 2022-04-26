package com.blindstick.utils;

import com.huaweicloud.sdk.core.auth.BasicCredentials;
import com.huaweicloud.sdk.core.auth.ICredential;
import com.huaweicloud.sdk.core.exception.ConnectionException;
import com.huaweicloud.sdk.core.exception.RequestTimeoutException;
import com.huaweicloud.sdk.core.exception.ServiceResponseException;
import com.huaweicloud.sdk.image.v2.ImageClient;
import com.huaweicloud.sdk.image.v2.model.ImageTaggingReq;
import com.huaweicloud.sdk.image.v2.model.RunImageTaggingRequest;
import com.huaweicloud.sdk.image.v2.model.RunImageTaggingResponse;
import com.huaweicloud.sdk.image.v2.region.ImageRegion;

public class ImageRecognition {

    public static void main(String[] args) {
        String ak = "PMAKSMTFCD4RJBO4474K";
        String sk = "kRxQ1O4d3DUceayxG8CzRkzKs12r2YbD6En0rv2O";
        ICredential auth = new BasicCredentials()
                .withAk(ak)
                .withSk(sk);
        ImageClient client = ImageClient.newBuilder()
                .withCredential(auth)
                .withRegion(ImageRegion.valueOf("cn-north-4"))
                .build();
        RunImageTaggingRequest request = new RunImageTaggingRequest();
        ImageTaggingReq body = new ImageTaggingReq();
        body.withLimit(50);
        body.withThreshold(95f);
        body.withLanguage("zh");
        String path="https://ai-traffic-demo.obs.cn-south-1.myhuaweicloud.com/bind/test.jpg";
        body.withUrl(path);
        request.withBody(body);
        try {
            RunImageTaggingResponse response = client.runImageTagging(request);
            System.out.println(response.toString());
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
    }
}
