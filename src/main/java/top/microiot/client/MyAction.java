package top.microiot.client;

import java.util.List;

import org.springframework.stereotype.Component;

import top.microiot.api.client.stomp.ActionResponseSubscriber;
import top.microiot.domain.Device;

@Component
public class MyAction extends ActionResponseSubscriber {

	@SuppressWarnings("deprecation")
	@Override
	public void onActionResult(Device device, String action, Object request, Object response) {
		System.out.println(device.getString() + " async action[" + action + "]");
		if(action.equals("getHistory")) {
			@SuppressWarnings("unchecked")
			List<Record> records = (List<Record>)response;
			for(Record record : records) {
				System.out.println("record: " + record.getSessionid() + " from: " + record.getStartTime().toLocaleString() + " to: " + record.getEndTime().toLocaleString());
			}
		}
		
	}

	@Override
	public void onActionError(Device device, String action, Object request, String error) {
		System.out.println(device.getString() + " async action[" + action + "] error: " + error);
	}
}
