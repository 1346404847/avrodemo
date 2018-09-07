package com.alina.avro.controller;

import java.io.File;

import org.apache.avro.Protocol;
import org.apache.avro.Protocol.Message;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.generic.GenericResponder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class AvroHttpServer extends GenericResponder {

    private static Log log = LogFactory.getLog(AvroHttpServer.class);

    public AvroHttpServer(Protocol protocol) {
        super(protocol);
    }

    public Object respond(Message message, Object request) throws Exception {
        GenericRecord req = (GenericRecord) request;
        GenericRecord reMessage = null;
        if (message.getName().equals("sayHello")) {
            Object name = req.get("PlatId");
            //  do something...
            //取得返回值的类型
            reMessage = new GenericData.Record(super.getLocal().getType("nameMessage"));
            //直接构造回复
            reMessage.put("PlatId", "Hello, " + name.toString());
            log.info(reMessage);
        }
        return reMessage;
    }

    public static void main(String[] args) throws Exception {
        int port = 8089;
        try {
            Server server = new HttpServer(
                    new AvroHttpServer(Protocol.parse(
                            new File("/Users/alina/project/avro/src/main/resources/avro/mail.avsc"))),
                    port);
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
