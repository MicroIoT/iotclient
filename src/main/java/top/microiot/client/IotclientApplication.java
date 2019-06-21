package top.microiot.client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.ParameterizedTypeReference;

import top.microiot.api.client.WebsocketClientSession;
import top.microiot.domain.attribute.Location;
import top.microiot.exception.ValueException;

@SpringBootApplication
public class IotclientApplication implements CommandLineRunner{
	@Autowired
	private WebsocketClientSession wsession;
	@Autowired
	private BikeAlarm bikeAlarm;
	@Autowired
	private MyGet myGet;
	@Autowired
	private MySet mySet;
	@Autowired
	private MyAction myAction;
	private boolean sync = true;
	
	public static void main(String[] args) {
		SpringApplication.run(IotclientApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String bikeId = "5cbc3b032e454236b0ad64ea";

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
					if(sync) {
						Location location = wsession.get(bikeId, "location", Location.class);
						System.out.println("location: [longitude: " + location.getLongitude() + " latitude: " + location.getLatitude() + "]");
					}
					else {
						wsession.getAsync(bikeId, "location", Location.class, myGet);
					}
				}
				else if(line.equals("get locked")) {
					if(sync) {
						boolean locked = wsession.get(bikeId, "locked", Boolean.class);
						System.out.println("locked: " + locked);
					}
					else {
						wsession.getAsync(bikeId, "locked", Boolean.class, myGet);
					}
				}
				else if(line.equals("set locked")) {
					if(sync) {
						boolean locked = true;
						wsession.set(bikeId, "locked", locked);
					}
					else {
						wsession.setAsync(bikeId, "locked", true, mySet);
					}
				}
				else if(line.equals("set unlocked")) {
					if(sync) {
						boolean unlocked = false;
						wsession.set(bikeId, "locked", unlocked);
					}
					else {
						wsession.setAsync(bikeId, "locked", false, mySet);
					}
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
					
					if(sync) {
						List<Record> records = wsession.action(bikeId, "getHistory", filter, new ParameterizedTypeReference<List<Record>>() {});
						for(Record record : records) {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							System.out.println("record: " + record.getSessionid() + " from: " + sdf.format(record.getStartTime()) + " to: " + sdf.format(record.getEndTime()));
						}
					}
					else {
						wsession.actionAsync(bikeId, "getHistory", filter, new ParameterizedTypeReference<List<Record>>() {}, myAction);
					}
				}
				else if(line.startsWith("sync")) {
					this.sync = true;
					System.out.println("当前调用方式：同步调用");
				}
				else if(line.startsWith("async")) {
					this.sync = false;
					System.out.println("当前调用方式：异步调用");
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
		System.out.println("设置调用方式：sync; async");
		System.out.println("退出：exit");
		if(sync)
			System.out.println("当前调用方式：同步调用");
		else
			System.out.println("当前调用方式：异步调用");
	}

}
