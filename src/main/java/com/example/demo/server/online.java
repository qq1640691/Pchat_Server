package com.example.demo.server;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class online
{
	ConcurrentHashMap<String,String> userlist;
	ConcurrentHashMap<String,Long> usermap;

	public online(ConcurrentHashMap<String, String> userlist, ConcurrentHashMap<String, Long> usermap) {
		this.userlist = userlist;
		this.usermap = usermap;
	}

	public void poll()
	{
		ScheduledExecutorService Service = Executors.newScheduledThreadPool(10);
			Service.scheduleWithFixedDelay(()->{
			Set<String> keyset = userlist.keySet();
			for(String user:keyset)
			{
					if(System.currentTimeMillis()-usermap.get(user)>120000)
					{
						usermap.remove(user);
						userlist.remove(user);
					}
			}
		},0,20, TimeUnit.SECONDS);
	}
}