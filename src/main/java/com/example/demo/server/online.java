package com.example.demo.server;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class online extends Thread
{
	ConcurrentHashMap<String,String> userlist;
	ConcurrentHashMap<String,Long> usermap;

	public online(ConcurrentHashMap<String, String> userlist, ConcurrentHashMap<String, Long> usermap) {
		this.userlist = userlist;
		this.usermap = usermap;
	}

	@Override
	public synchronized void run() {
		while(true)
		{
			Set<String> keyset = userlist.keySet();
			for(String user:keyset)
			{
					if(System.currentTimeMillis()-usermap.get(user)>120000)
					{
						usermap.remove(user);
						userlist.remove(user);
					}
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}
}