package com.alina.avro.service;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.apache.avro.Protocol;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.ipc.HttpServer;
import org.apache.avro.ipc.generic.GenericResponder;

import java.util.Iterator;


public class ServiceUntils extends GenericResponder {

    private Protocol protocol = null;

    private int port;

   public ServiceUntils(Protocol protocol,int port)
   {
       super(protocol);
       this.protocol = protocol;
       this.port = port;

   }
   //返回数据
   public Object respond(Protocol.Message message , Object request)  throws Exception {
       GenericRecord req = (GenericRecord) request;

       //获取请求的数据，做处理
       GenericRecord msg = (GenericRecord)req.get("requestSendMail");
       System.out.print("接收到数据："+msg);
       //打印接收到的数据

       System.out.println("打印接收到的数据"+req.toString());

       //转成字符串
       String s = req.toString();

       //转成json对象
       JSONObject jsonObject1=JSONObject.fromObject(s);

       Iterator<String> iterator1 =jsonObject1.keys();
       String nameFun = "";
       //遍历json对象
       while(iterator1.hasNext()){
            nameFun = iterator1.next();
           System.out.println(nameFun);
       }


       //获取接收

       Object reMessage = null;
       switch (nameFun) {
           case "requestSendMail":
               reMessage = this.responseSendMail();
               break;
           case "requestMessage":
               reMessage = this.responseMessage();
               break;
       }
       return reMessage;
   }






   //发送道具接口
   public Object responseSendMail()
   {
       GenericRecord reMessage =  new GenericData.Record(protocol.getType("responseSendMail"));

       System.out.println("没有返回值前："+ reMessage);
       reMessage.put("PlatId", "香梨");
       reMessage.put("source", "我是发送邮件11111w");
       return reMessage;
   }

    //修改文件信息
    public Object responseMessage()
    {
        GenericRecord reMessage =  new GenericData.Record(protocol.getType("responseMessage"));

        System.out.println("没有返回值前："+ reMessage);
        reMessage.put("PlatId", "香梨");
        reMessage.put("source", "我是发道具");
        return reMessage;

    }


    /**
     * 启动服务
     */
    public void run()
    {
        try{
            HttpServer serverStart = new HttpServer(this,port);

            serverStart.start();
            serverStart.join();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 运行服务监听端口
     * @param args
     */
   public static void main(String[] args)
   {
       new ServiceUntils(Util.getProtocol(),8081).run();
   }

}