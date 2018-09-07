package com.alina.avro.service;

import org.apache.avro.Protocol;


import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Util {

    public static Protocol getProtocol()
    {
        Protocol protocol = null;

        try{
            URL url = Util.class.getClassLoader().getResource("avro/vega.avsc");
            protocol = Protocol.parse(new File(url.getPath()));
        }catch(IOException e){

            e.printStackTrace();
        }

        return protocol;
    }
}