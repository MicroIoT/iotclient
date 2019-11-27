package top.microiot.client;

import top.microiot.api.client.stomp.SetResponseSubscriber;
import top.microiot.domain.Device;

public class MySet extends SetResponseSubscriber {

	@Override
	public void onSetResult(Device device, String attribute, Object value) {
		System.out.println(device.getString() + " set attribute[" + attribute + "]");
		if(attribute.equals("locked")) {
			boolean locked = (Boolean)value;
			System.out.println("locked: " + locked);
		}
	}

	@Override
	public void onSetError(Device device, String attribute, Object value, String error) {
		System.out.println(device.getString() + " set attribute[" + attribute + "] error: " + error);
	}

}
