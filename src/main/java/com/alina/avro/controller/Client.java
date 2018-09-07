package com.alina.avro.controller;

import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;

import java.io.File;
import java.net.URL;

public class Client {

    private Protocol protocol;

    private GenericRequestor requestor = null;

    public void setUp() throws Exception
    {
        protocol = Protocol.parse(new File("/Users/alina/project/avro/src/main/resources/avro/send.avsc"));

        ////这里如果要在两台机器上运行记得把localhost改成服务端的ip
        Transceiver transceiver = new HttpTransceiver(new URL("http://localhost:8081"));
        requestor = new GenericRequestor(protocol, transceiver);
    }


    public void sendMessage() throws Exception
    {

        GenericRecord requestData = new GenericData.Record(protocol.getType("requestMessage"));

        requestData.put("PlatId", "zhenqin");
        requestData.put("source", "zhenqin222");


        System.out.println(requestData);

        Object result = requestor.request("sendMessage",requestData);

        if (result instanceof  GenericData.Record) {
            GenericData.Record record = (GenericData.Record) result;
            System.out.println(record);
        }

        System.out.println(result);
    }

    public static void main(String[] args )  throws  Exception {
        Client client = new Client();
        client.setUp();
        client.sendMessage();
    }
}