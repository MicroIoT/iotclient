package top.microiot.client;

import java.util.Date;

import org.springframework.stereotype.Component;

import top.microiot.api.client.stomp.AlarmSubscriber;
import top.microiot.domain.Device;

@Component
public class BikeAlarm extends AlarmSubscriber {

	@Override
	public void init() {
		addType("StateChangedAlarm", StateChangedAlarm.class);
	}

	@Override
	public void onAlarm(Device device, String alarmType, Object alarmInfo, Date reportTime, Date receiveTime) {
		if (alarmType.equals("StateChangedAlarm")) {
			StateChangedAlarm info = (StateChangedAlarm) alarmInfo;

			System.out
					.println("StateChangedAlarm: sessionid is " + info.getSessionid() + " locked: " + info.isLocked());
		} else
			System.out.println(alarmType + " device: " + device.getString());
	}

}
