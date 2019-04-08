package com.leaniot.client;

import java.util.Date;

import org.springframework.stereotype.Component;

import com.leaniot.api.client.stomp.AlarmSubscriber;
import com.leaniot.domain.Device;

@Component
public class BikeAlarm extends AlarmSubscriber {

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
