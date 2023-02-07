package com.example.demo.server;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class UDPServer extends Thread{
	/**
	 * 问题是好像用户列表的更新不及时?,这样,直接把新用户加入之后直接调用dis就可以了
	 */
	ConcurrentHashMap<String,Long> usermap;
	ConcurrentHashMap<String,String> userlist;

	ConcurrentHashMap<String,byte[]> getbytes;

	public UDPServer(ConcurrentHashMap<String, Long> usermap, ConcurrentHashMap<String, String> userlist, ConcurrentHashMap<String, byte[]> getbytes) {
		this.usermap = usermap;
		this.userlist = userlist;
		this.getbytes = getbytes;
	}

	@Override
	public void run() {
		try {
			udpserve();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public void udpserve() throws IOException {
		InetAddress localIp=getLocalHostExactAddress();
		System.out.println(localIp);
		DatagramSocket server = new DatagramSocket(41000,localIp);
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		online online = new online(userlist,usermap);
		online.poll();
		newuser newuser = new newuser(userlist,usermap,server,packet,getbytes);
		newuser.poll();
		Thread receivepacket = new receivepacket(server, getbytes);
		receivepacket.start();
	}
	/**
	 * 我应该向用户发送什么呢?只要有一个用户上线,我就向所有的用户广播这里有一个新用户上线了吗?
	 */
	public static void linkuser(ConcurrentHashMap<String,String> userlist, DatagramSocket server,String ip,String key) throws IOException
	{
		CopyOnWriteArrayList<String> keylist = new CopyOnWriteArrayList<>(userlist.keySet());
		String[] inf = ip.split("//");
		InetAddress address = InetAddress.getByName(inf[0].replace("/",""));
		int port = Integer.parseInt(inf[1]);
		for(String user:keylist) {
			if (Objects.equals(userlist.get(user), key))
			{
				sendtouser(server,address,port,user,key);
			}
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static InetAddress getLocalHostExactAddress() {
    try {
        InetAddress candidateAddress = null;
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface iface = networkInterfaces.nextElement();
            // 该网卡接口下的ip会有多个，也需要一个个的遍历，找到自己所需要的
            for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                InetAddress inetAddr = inetAddrs.nextElement();
                // 排除loopback回环类型地址（不管是IPv4还是IPv6 只要是回环地址都会返回true）
                if (!inetAddr.isLoopbackAddress()) {
                    if (inetAddr.isSiteLocalAddress()) {
                        // 如果是site-local地址，就是它了 就是我们要找的
                        // ~~~~~~~~~~~~~绝大部分情况下都会在此处返回你的ip地址值~~~~~~~~~~~~~
                        return inetAddr;
                    }
                    // 若不是site-local地址 那就记录下该地址当作候选
                    if (candidateAddress == null) {
                        candidateAddress = inetAddr;
                    }

                }
            }
        }
        // 如果出去loopback回环地之外无其它地址了，那就回退到原始方案吧
        return candidateAddress == null ? InetAddress.getLocalHost() : candidateAddress;
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

	public static void sendtouser(DatagramSocket server, InetAddress address, int port, String send, String key) {
		byte[] sendbuf;
		try {
			sendbuf = AES.encrypt(("list" + send).getBytes(StandardCharsets.UTF_8),key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		DatagramPacket sendPacket = new DatagramPacket(Objects.requireNonNull(sendbuf),sendbuf.length,address, port);
		try {
			server.send(sendPacket);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void errr(DatagramSocket server, String ipinf, String key) {
		byte[] sendbuf;
		String[] inf = ipinf.split("//");
		InetAddress address;
		try {
			address = InetAddress.getByName(inf[0].replace("/",""));
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		int port = Integer.parseInt(inf[1]);
		try {
			sendbuf = AES.encrypt("errr" .getBytes(StandardCharsets.UTF_8),key);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		DatagramPacket sendPacket = new DatagramPacket(Objects.requireNonNull(sendbuf),sendbuf.length,address, port);
		try {
			server.send(sendPacket);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}




