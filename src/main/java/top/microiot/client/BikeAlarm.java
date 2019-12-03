package top.microiot.client;

import java.util.Date;

import org.springframework.stereotype.Component;

import top.microiot.api.client.stomp.AlarmSubscriber;
import top.microiot.domain.ManagedObject;

@Component
public class BikeAlarm extends AlarmSubscriber {

	@Override
	public void init() {
		addType("StateChangedAlarm", StateChangedAlarm.class);
	}

	@Override
	public void onAlarm(ManagedObject notifyObject, String alarmType, Object alarmInfo, Date reportTime, Date receiveTime) {
		if (alarmType.equals("StateChangedAlarm")) {
			StateChangedAlarm info = (StateChangedAlarm) alarmInfo;

			System.out.println("StateChangedAlarm:  locked: " + info.isLocked() + " from: " + notifyObject.getString());
		} else
			System.out.println(alarmType + " device: " + notifyObject.getString());
	}

}
