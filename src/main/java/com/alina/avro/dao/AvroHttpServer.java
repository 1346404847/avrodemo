package com.alina.avro.dao;


import com.alina.avro.utils.HttpGetRequest;
import net.sf.json.JSONObject;
import org.apache.avro.Protocol;
import org.apache.avro.Protocol.Message;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpServer;
import org.apache.avro.ipc.Server;
import org.apache.avro.ipc.generic.GenericResponder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

public class AvroHttpServer extends GenericResponder {

        private static Log log = LogFactory.getLog(AvroHttpServer.class);


        public AvroHttpServer(Protocol protocol) {
            super(protocol);
        }

        public Object respond(Message message, Object request) throws Exception {
            GenericRecord req = (GenericRecord) request;

            System.out.println("接收到的参数"+req);

            GenericRecord reMessage = null;
//            if (message.getName().equals("vegaSendMail")) {


                String s = req.toString();

                //转成json对象
                JSONObject jsonObject1=JSONObject.fromObject(s);

              System.out.println(jsonObject1.getString("secret"));


                //  do something...
                //取得返回值的类型
//                reMessage = new GenericData.Record(super.getLocal().getType("responseMessage"));
//                //直接构造回复
//                reMessage.put("PlatId", "Hello, " + "dev");
//                log.info(reMessage);

                reMessage = new GenericData.Record(super.getLocal().getType("responseUpdateUserInfo"));
                reMessage.put("Result",1);
                log.info(reMessage);

//            }
            return reMessage;
        }

        public static void main(String[] args) throws Exception {

//            String avroInfo =  HttpGetRequest.doGet("http://localhost:8080/vega-info");

//            System.out.println(avroInfo);
            int port = 9095;
            try {
                Server server = new HttpServer(
                        new AvroHttpServer(Protocol.parse(new File("/Users/alina/project/avro/src/main/resources/avro/info.avsc"))),
//                        new AvroHttpServer(Protocol.parse(avroInfo)),
                        port);
                server.start();
                server.join();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

