package com.example.demo.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentHashMap;

public class receivepacket extends Thread{
    DatagramSocket server;
    ConcurrentHashMap<String,byte[]> getbytes;

    public receivepacket(DatagramSocket server, ConcurrentHashMap<String, byte[]> getbytes) {
        this.server = server;
        this.getbytes = getbytes;
    }

    @Override
    public void run() {
        while (true)
        {
            byte[] get = new byte[1024];
            DatagramPacket packet = new DatagramPacket(get, get.length);
            try {
                server.receive(packet);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            byte[] result = new byte[packet.getLength()];
            System.arraycopy(packet.getData(),0,result,0,result.length);
            getbytes.put(packet.getAddress()+"//"+packet.getPort()+"//",result);
        }
    }
}
