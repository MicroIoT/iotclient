package com.leaniot.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.leaniot.api.client.WebsocketClientSession;
import com.leaniot.domain.attribute.Location;
import com.leaniot.exception.ValueException;

@SpringBootApplication
public class IotclientApplication implements CommandLineRunner{
	@Autowired
	private WebsocketClientSession wsession;
	@Autowired
	private BikeAlarm bikeAlarm;
	
	public static void main(String[] args) {
		SpringApplication.run(IotclientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String bikeId = "5caa9cf1ddd50a20164a4cda";

		Map<String, Class<?>> alarmType = new HashMap<String, Class<?>>();
		alarmType.put("StateChangedAlarm", StateChangedAlarm.class);
		bikeAlarm.setAlarmInfoType(alarmType);
		wsession.subscribe(bikeId, bikeAlarm);
		
		System.out.println("请输入命令：");
		command();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		while(true) {
			try {
				String line = scanner.nextLine();
				if(line.equals("exit")) {
					wsession.stop();
					System.exit(0);
				}
				else if(line.equals("get location")) {
					Location location = wsession.get(bikeId, "location", Location.class);
					System.out.println("location: [longitude: " + location.getLongitude() + " lantitude: " + location.getLantitude() + "]");
				}
				else if(line.equals("get locked")) {
					boolean locked = wsession.get(bikeId, "locked", Boolean.class);
					System.out.println("locked: " + locked);
				}
				else if(line.equals("set locked")) {
					boolean locked = true;
					wsession.set(bikeId, "locked", locked);
				}
				else if(line.equals("set unlocked")) {
					boolean unlocked = false;
					wsession.set(bikeId, "locked", unlocked);
				}
				else if(line.startsWith("getHistory")) {
					String[] p = line.split(" ");
					Filter filter = null;
					SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
					Date start = new Date();
					Date end = new Date();
					if(p.length == 2) {
						start = format.parse(p[1]);
						end = new Date();
					}
					else if(p.length == 3) {
						start = format.parse(p[1]);
						end = format.parse(p[2]);
					}
					else if(p.length > 3) {
						throw new ValueException("usage: getHistory startDate endDate");
					}
					if(start.after(end))
						throw new ValueException("start date must before end date");
					
					filter = new Filter(start, end);
					Records records = wsession.action(bikeId, "getHistory", filter, Records.class);
					for(Record record : records.getRecords()) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						System.out.println("record: " + record.getSessionid() + " from: " + sdf.format(record.getStartTime()) + " to: " + sdf.format(record.getEndTime()));
					}
				}
				else {
					command();
				}
			}
			catch(Throwable e) {
				e.printStackTrace(new java.io.PrintStream(System.out));
			}
		}
	}

	private void command() {
		System.out.println("获取location属性值：get location");
		System.out.println("获取locked属性值：get locked");
		System.out.println("设置locked属性值：set locked; set unlocked");
		System.out.println("获取骑行数据：getHistory");
		System.out.println("退出：exit");
	}

}
