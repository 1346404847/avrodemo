package com.alina.avro;


import java.io.File;
import java.net.URL;

import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpTransceiver;
import org.apache.avro.ipc.Transceiver;
import org.apache.avro.ipc.generic.GenericRequestor;
import org.junit.Before;
import org.junit.Test;

public class TestClient {

    private Protocol protocol;

    private GenericRequestor requestor = null;

    @Before
    public void setUp() throws Exception {
        protocol = Protocol.parse(new File("/Users/alina/project/avro/src/main/resources/avro/mail.avsc"));
        Transceiver t = new HttpTransceiver(new URL("http://localhost:9092"));  //这里如果要在两台机器上运行记得把localhost改成服务端的ip
        requestor = new GenericRequestor(protocol, t);
    }

    @Test
    public void testSendMessage() throws Exception {
        GenericRecord requestData = new GenericData.Record(protocol.getType("nameMessage"));
        // initiate the request data
        requestData.put("PlatId","zhenqin");

        System.out.println(requestData);
        Object result = requestor.request("sayHello", requestData);
        if (result instanceof GenericData.Record) {
            GenericData.Record record = (GenericData.Record) result;
            System.out.println(record.get("PlatId"));
        }
        System.out.println(result);
    }

}