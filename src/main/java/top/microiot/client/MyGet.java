package top.microiot.client;

import org.springframework.stereotype.Component;

import top.microiot.api.client.stomp.GetResponseSubscriber;
import top.microiot.domain.Device;
import top.microiot.domain.attribute.Location;

@Component
public class MyGet extends GetResponseSubscriber {
	@Override
	public void onGetResult(Device device, String attribute, Object value) {
		System.out.println(device.getString() + " attribute[" + attribute + "]:");
		if(attribute.equals("location")) {
			Location location = (Location)value;
			System.out.println(location.getLongitude() + ":" + location.getLatitude());
		}
		else if(attribute.equals("locked")) {
			boolean locked = (boolean)value;
			System.out.println("locked: " + locked );
		}
		
	}
}
