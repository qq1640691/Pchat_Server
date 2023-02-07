package com.example.demo.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.example.demo.UDPserverApplication.RSAprivatKEY;
import static com.example.demo.server.UDPServer.errr;
import static com.example.demo.server.UDPServer.linkuser;

public class newuser//这个查找函数可以改进,先这样写,可以用
{
	ConcurrentHashMap<String,String> userlist;
	ConcurrentHashMap<String,Long> usermap;
	DatagramSocket server;
	DatagramPacket packet;
	ConcurrentHashMap<String, byte[]> getbytes;


    public newuser(ConcurrentHashMap<String, String> userlist, ConcurrentHashMap<String, Long> usermap, DatagramSocket server, DatagramPacket packet, ConcurrentHashMap<String, byte[]> getbytes) {
        this.userlist = userlist;
        this.usermap = usermap;
        this.server = server;
        this.packet = packet;
        this.getbytes = getbytes;
    }


	public synchronized void poll() {
		ScheduledExecutorService Service = Executors.newScheduledThreadPool(1);
			Service.scheduleWithFixedDelay(()->{
			if (getbytes.size() > 0) {
				Set<String> Keyset = getbytes.keySet();
				for (String ipinf : Keyset) {
					byte[] inf = getbytes.get(ipinf);
					byte[] trueinf;
					String userid;
					String useredkey;
					try {
						trueinf = RSACoder.decryptByPrivateKey(inf, RSAprivatKEY);
						userid = new String(Objects.requireNonNull(trueinf), 16, trueinf.length - 16);//消息转String字符串,这个String是用户id
						useredkey = new String(Objects.requireNonNull(trueinf), 0, 16);
					} catch (Exception e) {
						getbytes.remove(ipinf);
						break;
					}
					/**
					 * 如果不存在ip地址和id都一样的用户,就新增用户,
					 */
					Set<String> temp = userlist.keySet();
					int flag= 0;
					for(String str:temp)
					{
						if(str.contains("//"+userid+"#"))
						{
							flag=1;
							break;
						}
					}
					/**
					 * flag==0说明是新用户,存在key说明是老用户
					 */
					if(flag==0||usermap.containsKey(ipinf + userid + "#"))
					{
						userlist.put(ipinf + userid + "#", useredkey);
						usermap.put(ipinf + userid + "#", System.currentTimeMillis());
						try {
							linkuser(userlist, server, ipinf + userid + "#", useredkey);
						} catch (IOException e) {
							throw new RuntimeException(e);
						}
						getbytes.remove(ipinf);
					}
					else {
						errr(server,ipinf,useredkey);
						getbytes.remove(ipinf);
					}
				}
			}
			},0,100, TimeUnit.MILLISECONDS);
	}
}