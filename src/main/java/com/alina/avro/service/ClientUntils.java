package com.alina.avro.service;

import com.alina.avro.utils.Client;

import net.sf.json.JSONObject;
import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ClientUntils {
    private Protocol protocol;

    private String host = null;

    private int port = 0;

    public ClientUntils(Protocol protocol, String host, int port)
    {

        this.protocol = protocol;
        this.host = host;
        this.port = port;

    }


    public Object sendMessages() throws  Exception
    {
        //定义请求体
        String requestMessage = "requestMessage";

        //请求的接口
        String requestApi = "vegaUpdateUserReward";

        //请求地址
        Transceiver transceiver = new HttpTransceiver(new URL("http://" + host + ":" + port));

        GenericRequestor requestor = new GenericRequestor(protocol,transceiver);


        //获取发送的请求体
//        GenericRecord requestData = new GenericData.Record(protocol.getType("requestSendMail"));
        GenericRecord requestData = new GenericData.Record(protocol.getType(requestMessage));
        System.out.println("+"+ requestData);

        //初始化请求数据需要请求的接口VEGA_ADD_BLACK_CHAT_REQ
        GenericRecord  request = new GenericData.Record(protocol.getMessages().get(requestApi).getRequest());
        requestData.put("PlatId", "");
        requestData.put("source", "www");
        request.put(requestMessage,requestData);

        //往哪个接口发送数据
        Object result = requestor.request(requestApi, request);
        if (result instanceof GenericData.Record) {
            GenericData.Record record = (GenericData.Record) result;
            System.out.println(record);
        }

        System.out.println("返回数据"+result);
        return result;
    }


    /**
     * 启动测试
     * @return
     */

    public static void main(String[] args) throws Exception {


        new ClientUntils(Util.getProtocol(),"127.0.0.1",8081).sendMessages();
    }
}