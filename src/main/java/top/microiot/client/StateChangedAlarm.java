package top.microiot.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.microiot.domain.attribute.Location;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StateChangedAlarm {
	private Location location;
	private boolean locked;
}
