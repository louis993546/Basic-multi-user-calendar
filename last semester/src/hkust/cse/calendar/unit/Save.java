package hkust.cse.calendar.unit;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name="list")
@XmlAccessorType(XmlAccessType.FIELD)
public class Save {

	private List<Userlist> list=new ArrayList<Userlist>();
	
	public List<Userlist> getApptlist(){
		return list;
	}
	public void setApptlist(List<Userlist> mlist){
		this.list=mlist;
	}

}
