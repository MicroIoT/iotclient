package top.microiot.client;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.microiot.domain.attribute.Location;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Record {
	private Location startLocation;
	private Date startTime;
	private Location endLocation;
	private Date endTime;
	
}
