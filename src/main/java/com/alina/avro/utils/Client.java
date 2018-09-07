package com.alina.avro.utils;

import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;

import java.net.URL;

public class Client {

    private Protocol protocol;
    private String host = null;
    private int port = 0;

    public Client(Protocol protocol, String host, int port) {
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }


    public Object sendMessage() throws Exception
    {
        GenericRecord requestData = new GenericData.Record(protocol.getType("messageRequest"));


        requestData.put("name", "香梨");
        requestData.put("type", 36);
        requestData.put("price", 5.6);
        requestData.put("valid", true);
        requestData.put("content", "价钱便宜");

        //初始化请求数据
        GenericRecord  request = new GenericData.Record(protocol.getMessages().get("sendMessage").getRequest());


        request.put("messageRequest",requestData);

        //请求地址
        Transceiver transceiver = new HttpTransceiver(new URL("http://" + host + ":" + port));

        GenericRequestor requestor = new GenericRequestor(protocol,transceiver);


        Object result = requestor.request("sendMessage", request);
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
        new Client(Utils.getProtocol(),"127.0.0.1",9090).sendMessage();
    }
}