package com.alina.avro.utils;


import org.apache.avro.Protocol;
import org.apache.avro.Protocol.Message;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpServer;
import org.apache.avro.ipc.generic.GenericResponder;

import java.util.*;

public class Server extends GenericResponder {

    private Protocol protocol = null;

    private int port;

    /**
     * 构造函数
     * @param protocol
     * @param port
     */
    public Server(Protocol protocol,int port)
    {
        super(protocol);
        this.protocol = protocol;
        this.port = port;
    }

    /**
     * 返回数据
     * @param message
     * @param request
     * @return
     * @throws Exception
     */
    public Object respond(Message message ,Object request) throws Exception
    {
        GenericRecord req = (GenericRecord) request;

        GenericRecord reMessage = null;
        GenericRecord bodyMessage = null;
        GenericRecord messageArray = null;

        if (message.getName().equals("sendMessage")) {


            //获取请求的数据，做处理
            GenericRecord msg = (GenericRecord)req.get("messageRequest");

            System.out.print("接收到数据：");
            //打印请求的数据
            System.out.println(msg);
            //取得返回值的类型,需要返回的类型
            reMessage =  new GenericData.Record(protocol.getType("messageResponse"));
            bodyMessage =  new GenericData.Record(protocol.getType("bodyMessage"));





            List<Object> list=new ArrayList<Object>();

            for (int i=0;i<10;i++) {
                messageArray =  new GenericData.Record(protocol.getType("messageArray"));

                messageArray.put("name", "香梨"+i);
                messageArray.put("type", 36+i);
                list.add(messageArray);

            }


            bodyMessage.put("name", "香梨");
            bodyMessage.put("type", 36);
            bodyMessage.put("price", 5.6);
            bodyMessage.put("valid", true);
            bodyMessage.put("content", "价钱便宜");
            bodyMessage.put("prop", list);



            //填充返回数据
            reMessage.put("result", 1);
            reMessage.put("body", bodyMessage);

            System.out.println(reMessage);


        }
        //返回数据
        return reMessage;
    }

    /**
     * 启动服务
     */
    public void run()
    {

//        try{
//            HttpServer server = new HttpServer(this,port);
//
//            server.start();
//            server.join();
//        }catch (Exception e) {
//
//            e.printStackTrace();
//        }
    }


    public static void main(String[] args)
    {
        new Server(Utils.getProtocol(),9090).run();

    }
}