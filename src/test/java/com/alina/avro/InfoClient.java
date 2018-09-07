package com.alina.avro;



import java.io.File;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.alina.avro.utils.HttpGetRequest;
import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;
import org.junit.Before;
import org.junit.Test;


import javax.annotation.Resource;


public class InfoClient {

    private Protocol protocol;

    private GenericRequestor requestor = null;
    @Resource
    private HttpGetRequest httpGetRequest;

    @Before
    public void setUp() throws Exception {

        //String avro = httpGetRequest.doGet("http://localhost:8080/vega-info");
//        System.out.println("66666"+httpGetRequest.doGet("http://localhost:8080/avro-info"));
        protocol = Protocol.parse(new File("/Users/alina/project/avro/src/main/resources/avro/info.avsc"));
        //protocol = Protocol.parse(avro);
        Transceiver t = new HttpTransceiver(new URL("http://localhost:9095"));  //这里如果要在两台机器上运行记得把localhost改成服务端的ip
        requestor = new GenericRequestor(protocol, t);
    }

    @Test
    public void testSendMessage() throws Exception {
        GenericRecord requestData = new GenericData.Record(protocol.getType("requestMessage"));
        // initiate the request data
        requestData.put("PlatId", "dev");

        //多接口
        GenericRecord requestData1 = new GenericData.Record(protocol.getType("requestUpdateUserInfo"));
        requestData1.put("PlatId","我是修改玩家信息");


        //初始化请求
        GenericRecord  request = new GenericData.Record(protocol.getMessages().get("vegaSendMail").getRequest());

        //密钥加密规则，md5(游戏简称+时间错+私钥)
        System.out.println(System.currentTimeMillis()/1000);
        request.put("requestMessage",requestData);
        request.put("secret",md5("vega"+System.currentTimeMillis()+md5("123456")));

        //发送修改玩家信息接口

        GenericRecord  request1 = new GenericData.Record(protocol.getMessages().get("vegaUpdateUserInfo").getRequest());

        request1.put("requestUpdateUserInfo",requestData1);
        request1.put("secret",md5("vega"+System.currentTimeMillis()+md5("123456")));






//        Object result = requestor.request("vegaSendMail", request);
        Object result = requestor.request("vegaUpdateUserInfo", request1);
//        if (result instanceof GenericData.Record) {
//            GenericData.Record record = (GenericData.Record) result;
//
//        }
        System.out.println(result);
    }

    //写一个md5加密的方法
    public static String md5(String plainText) {
        //定义一个字节数组
        byte[] secretBytes = null;
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            //对字符串进行加密
            md.update(plainText.getBytes());
            //获得加密后的数据
            secretBytes = md.digest();
        } catch (NoSuchAlgorithmException e) {

            throw new RuntimeException("没有md5这个算法！");
        }
        //将加密后的数据转换为16进制数字
        String md5code = new BigInteger(1, secretBytes).toString(16);

        // 16进制数字 // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++)
        {
            md5code = "0" + md5code;
        }
        return md5code;
    }




}