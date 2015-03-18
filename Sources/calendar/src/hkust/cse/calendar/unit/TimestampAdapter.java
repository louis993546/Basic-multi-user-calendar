package hkust.cse.calendar.unit;

import java.sql.Timestamp;

import javax.xml.bind.annotation.adapters.XmlAdapter;



public class TimestampAdapter extends XmlAdapter <String, Timestamp> {
	  
	/*class AdaptedTimeSpan {
		String StartTime;
		
		String EndTime;
	}*/
	
	public String marshal(Timestamp v) {
	      return v.toString();
	  }
	public Timestamp unmarshal(String v) throws Exception {
		
		
		return   Timestamp.valueOf(v);
		
		}
}