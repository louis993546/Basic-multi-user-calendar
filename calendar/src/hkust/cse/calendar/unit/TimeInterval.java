package hkust.cse.calendar.unit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.BitSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;
//import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class TimeInterval {
	private SortedMap<LocalDate, BitSet> timeIntervalMap;

	public TimeInterval(SortedMap<LocalDate, BitSet> timeInterval) {
		this.timeIntervalMap = timeInterval;
	}
	
	public TimeInterval(TimeInterval copy){
		this();
		this.timeIntervalMap = new ConcurrentSkipListMap<LocalDate, BitSet>(copy.timeIntervalMap);
	}
	
	public TimeInterval()
	{
		this.timeIntervalMap = new ConcurrentSkipListMap<LocalDate, BitSet>();
	}

	public TimeInterval(TimeSpan timeSpan) 
	{
		if (timeSpan.StartTime().toLocalDateTime().toLocalDate().isEqual(timeSpan.EndTime().toLocalDateTime().toLocalDate())) 
		{

			LocalTime startTime = timeSpan.StartTime().toLocalDateTime().toLocalTime();
			LocalTime endTime = timeSpan.EndTime().toLocalDateTime().toLocalTime();

			BitSet tmpBitset = new BitSet(40);
			LocalTime tmpTime = LocalTime.of(8, 0, 0, 1);
			for (int i = 0; i < 40; i++) 
			{
				if (startTime.isBefore(tmpTime) && tmpTime.isBefore(endTime)) 
				{
					tmpBitset.set(i);
				}
				tmpTime = tmpTime.plusMinutes(15);
			}
			this.timeIntervalMap = new ConcurrentSkipListMap<LocalDate, BitSet>();
			this.timeIntervalMap.put(timeSpan.StartTime().toLocalDateTime().toLocalDate(), tmpBitset);// only one date!!

		}
		else
		{
			this.timeIntervalMap = new ConcurrentSkipListMap<LocalDate, BitSet>();
			{
				LocalTime startTime = timeSpan.StartTime().toLocalDateTime().toLocalTime();
				BitSet tmpBitset = new BitSet(40);
				LocalTime tmpTime = LocalTime.of(8, 0, 0, 1);
				for (int i = 0; i < 40; i++) {
					if (startTime.isBefore(tmpTime)) {
						// start <tmp< end, 8:00:1
						tmpBitset.set(i);
					}
					tmpTime = tmpTime.plusMinutes(15);

				}
				this.timeIntervalMap.put(timeSpan.StartTime().toLocalDateTime()
						.toLocalDate(), tmpBitset);// start date
				
			}
			{
				LocalTime endTime = timeSpan.StartTime().toLocalDateTime()
						.toLocalTime();
				
				BitSet tmpBitset = new BitSet(40);
				LocalTime tmpTime = LocalTime.of(8, 0, 0, 1);
				for (int i = 0; i < 40; i++) {
					if (tmpTime.isBefore(endTime)) {
						// start <tmp< end, 8:00:1
						tmpBitset.set(i);
					}
					tmpTime = tmpTime.plusMinutes(15);

				}
				this.timeIntervalMap.put(timeSpan.StartTime().toLocalDateTime()
						.toLocalDate(), tmpBitset);// end date

			}
			{
				BitSet tmpBitset = new BitSet(40);
				tmpBitset.flip(0,40);//[T,T,T...T,T,T]
				//timeSpan.StartTime().toLocalDateTime().toLocalDate()
				//.isEqual(timeSpan.EndTime().toLocalDateTime().toLocalDate())
				LocalDate startDate = timeSpan.StartTime().toLocalDateTime().toLocalDate();
				LocalDate endDate = timeSpan.EndTime().toLocalDateTime().toLocalDate();
				for (LocalDate tmpdate=startDate.plusDays(1);tmpdate.isBefore(endDate);tmpdate=tmpdate.plusDays(1)){
					this.timeIntervalMap.put(tmpdate, tmpBitset);
				}

			}
			

		}

	}

	public TimeInterval(Appt[] apptlist) {
		
		this();
		for (Appt appt : apptlist) {
			System.out.println("TimeInterval.TimeInterval()begin loop");
			TimeSpan timeSpan = appt.getAppointment().getTimeSpan();
			TimeInterval singleTimeInterval = new TimeInterval(timeSpan);
			this.unionwith(singleTimeInterval);
			System.out.println("TimeInterval.TimeInterval()end loop");
		}
	}
	
	public void unionwith(TimeInterval anotherTimeInterval) {
		// union ??
		// for(datetmp: anothermap.keyset.retainALL(map.keyset));
		// map.get(datetmp) or anothermap.get(datetmp)
		// for(datetobeadd: anothermap.keyset.removeALL(map.keyset));
		// {map.put(datetobeadd,anothermap.get(datetobeadd));
		System.out.println("union1: this"+this);
		System.out.println("union2: anotherTimeInterval"+anotherTimeInterval);
		Set<LocalDate> tmpKeySetx = anotherTimeInterval.timeIntervalMap.keySet();
		Set<LocalDate> tmpKeySet = new TreeSet<LocalDate>(tmpKeySetx);
		tmpKeySet.retainAll(this.timeIntervalMap.keySet());// find common date
		
		System.out.println("union2b: anotherTimeInterval"+anotherTimeInterval);
		for (LocalDate datetmp : tmpKeySet) {
			BitSet tmpBitSet = (BitSet) this.timeIntervalMap.get(datetmp).clone();
			tmpBitSet.or(anotherTimeInterval.timeIntervalMap.get(datetmp));
			this.timeIntervalMap.put(datetmp,tmpBitSet);
		}
		
		System.out.println("union result1: this"+this);
		
		Set<LocalDate> tmpKeySet2x = anotherTimeInterval.timeIntervalMap
				.keySet();
		Set<LocalDate> tmpKeySet2 = new TreeSet<LocalDate>(tmpKeySet2x);
		tmpKeySet2.removeAll(this.timeIntervalMap.keySet());// find unique/new date
														// in another
		for (LocalDate datetobeadd : tmpKeySet2) 
		{
			this.timeIntervalMap.put(datetobeadd, anotherTimeInterval.timeIntervalMap.get(datetobeadd));
		}
		
		System.out.println("union result2: this"+this);

	}
	
//	public TimeInterval setTimeInterval(Appt[] abu)
//	{
//		TimeInterval ti = new TimeInterval(abu[0].TimeSpan());
//		for (int i = 0; i<abu.length; i++)
//		{
//			ti.unionwith(new TimeInterval(abu[i].TimeSpan()));
//		}
//		return ti;
//	}
//	
//	public TimeInterval addTimeInterval(TimeInterval ti, Appt[]abu)
//	{
//		for (int i = 0; i<abu.length; i++)
//		{
//			System.out.println("a4: " + abu[i].TimeSpan());
//			ti.unionwith(new TimeInterval(abu[i].TimeSpan()));
//		}
//		System.out.println("a5: " + ti.toString());
//		return ti;
//	}
	
//
//	public TimeInterval(Appt[] apptByUser)
//	{
//		//TODO
//		TimeInterval tempTI = new TimeInterval();
//		for (Appt a:apptByUser)
//		{
//			TimeInterval tempTI2 = new TimeInterval(a.TimeSpan());
//			tempTI.unionwith(tempTI2);
//		}		
//	}
//	
//	public TimeInterval(TimeInterval ti, Appt[] apptByUser) {
//		//TODO
//		TimeInterval ti2 = new TimeInterval(apptByUser);
//		ti.unionwith(ti2);
//	}


	public void intersectWith(TimeInterval anotherTimeInterval) {
		System.out.println("this "+this);
		System.out.println("anotherTimeInterval "+anotherTimeInterval);
		Set<LocalDate> tmpKeySet0 = this.timeIntervalMap.keySet();
		Set<LocalDate> tmpKeySet = new TreeSet<LocalDate>(tmpKeySet0);
		
		tmpKeySet.removeAll(anotherTimeInterval.timeIntervalMap.keySet());// find
																			// unique/new
																			// date
																			// in
																			// another
		for (LocalDate datetoberemove : tmpKeySet) {
			this.timeIntervalMap.remove(datetoberemove);
		}
		
		Set<LocalDate> keySet1 = new TreeSet<LocalDate>(this.timeIntervalMap.keySet());
		for (LocalDate tmpdate : keySet1) {
			BitSet tmpBitSet = (BitSet) this.timeIntervalMap.get(tmpdate).clone();
			tmpBitSet.and(anotherTimeInterval.timeIntervalMap.get(tmpdate));
			if (tmpBitSet.cardinality() == 0) {// if num of true ==0, remove
				this.timeIntervalMap.remove(tmpdate);
			} else {
				this.timeIntervalMap.put(tmpdate, tmpBitSet);
			}

		}

	}

	public void subtract(TimeInterval anotherTimeInterval) {
		Set<LocalDate> tmpKeySet0 = anotherTimeInterval.timeIntervalMap.keySet();
		Set<LocalDate> tmpKeySet = new TreeSet<LocalDate>(tmpKeySet0);
		
		tmpKeySet.retainAll(this.timeIntervalMap.keySet());
		for (LocalDate tmpdate : tmpKeySet) {
			BitSet tmpBitSet = (BitSet) this.timeIntervalMap.get(tmpdate).clone();
			tmpBitSet.andNot(anotherTimeInterval.timeIntervalMap.get(tmpdate));
			if (tmpBitSet.cardinality() == 0) {
				this.timeIntervalMap.remove(tmpdate);
			} else {
				this.timeIntervalMap.put(tmpdate, tmpBitSet);
			}
		}
	}

	public boolean isEmpty() {
		return this.timeIntervalMap.isEmpty();
	}

	public void clearBeforeNow() {
		System.out.println("TimeInterval.clearBeforeNow()");
		System.out.println(this);
		TimeMachine tm = TimeMachine.getInstance();
		clearBeforeTime(tm.getTMTimestamp().toLocalDateTime());//
		
	}

	public void clearBeforeTime(LocalDateTime time) {

		System.out.println("TimeInterval.clearBeforeTime(){clearbeforedate(time);...");
		System.out.println(this);
		
		clearbeforedate(time);
		LocalDate tmpdate = time.toLocalDate();
		if (this.timeIntervalMap.containsKey(time.toLocalDate())) {
			BitSet tmpBitSet = (BitSet) this.timeIntervalMap.get(time.toLocalDate()).clone();

			LocalTime tmpTime = LocalTime.of(8, 0, 0, 1);
			for (int i = 0; i < 40; i++) {
				if (tmpTime.isBefore(time.toLocalTime())) {
					// start <tmp< end, 8:00:1
					tmpBitSet.clear(i);
				}

				tmpTime = tmpTime.plusMinutes(15);

			}
			if (tmpBitSet.cardinality() == 0) {
				this.timeIntervalMap.remove(tmpdate);
			} else {
				this.timeIntervalMap.put(tmpdate, tmpBitSet);
			}

		}

	}

	public void clearbeforedate(LocalDateTime time) {
		System.out.println("TimeInterval.clearbeforedate()");
		System.out.println(this);
		int count=1;
		Set<LocalDate> keySet1 = new TreeSet<LocalDate>(this.timeIntervalMap.keySet());
		for (LocalDate tmpdate : keySet1) {
			System.out.println("loop count="+count);
			System.out.println(this);
			if (tmpdate.isBefore(time.toLocalDate())) {
				this.timeIntervalMap.remove(tmpdate);
			}
		}
	}

	@Override
	public String toString() {
		String result = "TimeInterval [timeIntervalMap=" + this.timeIntervalMap
				+ "]\n";
		Set<LocalDate> keySet1 = new TreeSet<LocalDate>(this.timeIntervalMap.keySet());
		for (LocalDate tmpdate : keySet1) {
			result += ("\t" + tmpdate + ": ");
			for (int i = 0; i < 40; i++) {
				if (this.timeIntervalMap.get(tmpdate).get(i) == true) {
					result += "O";
				} else {
					result += "X";
				}
			}
			result += "\n";

		}

		return result;
	}

	public boolean isExistCommonElement(TimeInterval t2) {
		// nessecery?
		System.out.println("TimeInterval.isExistCommonElement()");
		System.out.println("this "+this);
		TimeInterval timeInterval1 = new TimeInterval(this);
		System.out.println("this2 "+this);
		TimeInterval timeInterval2 = new TimeInterval(t2);
		timeInterval1.intersectWith(timeInterval2);
		
		
		System.out.println("timeInterval1"+timeInterval1);
		System.out.println("this3 "+this);
		return !(timeInterval1.isEmpty());
	}
				
	// intersect
	// for(datetoberemove: map.keyset.removeALL(anothermap.keyset));
	// {map.remove(datetoberemove);

	// for(tmpdate :map.keyset)
	// {map.get(tmpdate) and anothermap.get(tmpdate)

	// assert len()==40

	// union ??
	// for(datetmp: anothermap.keyset.retainALL(map.keyset));
	// map.get(datetmp) or anothermap.get(datetmp)
	// for(datetobeadd: anothermap.keyset.removeALL(map.keyset));
	// {map.put(datetobeadd,anothermap.get(datetobeadd));

	// minus
	// for(tmpdate :anothermap.keyset.retainALL(map.keyset))
	// {map.get(tmpdate) and not( anothermap.get(tmpdate) );
	// if thebitset.cardinality==0, then map.remove(tmpdate)}

	// isShared element
	// return intersect.keyset.len()>0

	// construct by timespan??

	// minus past

}
