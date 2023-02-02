package com.example.demo;

import com.example.demo.server.UDPServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
public class UDPserverApplication {
	public static ConcurrentHashMap<String,String> userlist= new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String,Long> usermap = new ConcurrentHashMap<>();

	public static ConcurrentHashMap<String,byte[]> getbytes= new ConcurrentHashMap<>();
	public static final String RSAprivatKEY = "这是是RSA的私钥";


	public static void main(String[] args) {
		SpringApplication.run(UDPserverApplication.class, args);
		Thread udpserver = new UDPServer(usermap, userlist,getbytes);
		udpserver.start();
	}

}
