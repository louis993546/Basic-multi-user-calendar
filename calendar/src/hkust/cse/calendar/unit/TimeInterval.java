package hkust.cse.calendar.unit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.BitSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class TimeInterval {
	private SortedMap<LocalDate, BitSet> timeIntervalMap;

	public TimeInterval(SortedMap<LocalDate, BitSet> timeInterval) {
		this.timeIntervalMap = timeInterval;
	}
	
	public TimeInterval()
	{
		timeIntervalMap = new TreeMap<LocalDate, BitSet>();
	}

	public TimeInterval(TimeSpan timeSpan) 
	{
		if (timeSpan.StartTime().toLocalDateTime().toLocalDate().isEqual(timeSpan.EndTime().toLocalDateTime().toLocalDate())) 
		{
			System.out.println("afadslfasdfasdf");
			LocalTime startTime = timeSpan.StartTime().toLocalDateTime().toLocalTime();
			LocalTime endTime = timeSpan.EndTime().toLocalDateTime().toLocalTime();

			BitSet tmpBitset = new BitSet(40);
			LocalTime tmpTime = LocalTime.of(8, 0, 0, 1);
			for (int i = 0; i < 40; i++) 
			{
				System.out.println("afadslfasdfasdf2");
				System.out.println("tmpTime: " + tmpTime);
				System.out.println("startTime: " + startTime);
				System.out.println("endTime: " + endTime);
				if (startTime.isBefore(tmpTime) && tmpTime.isBefore(endTime)) 
				{
					System.out.println("afadslfasdfasdf3");
					// start <tmp< end, 8:00:1
					tmpBitset.set(i);
				}
				tmpTime = tmpTime.plusMinutes(15);
			}
			this.timeIntervalMap = new TreeMap<LocalDate, BitSet>();
			timeIntervalMap.put(timeSpan.StartTime().toLocalDateTime().toLocalDate(), tmpBitset);// only one date!!

		}
		else
		{
			this.timeIntervalMap = new TreeMap<LocalDate, BitSet>();
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
				timeIntervalMap.put(timeSpan.StartTime().toLocalDateTime()
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
				timeIntervalMap.put(timeSpan.StartTime().toLocalDateTime()
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
					timeIntervalMap.put(tmpdate, tmpBitset);
				}

			}
			

		}

	}

	public void unionwith(TimeInterval anotherTimeInterval) {
		// union ??
		// for(datetmp: anothermap.keyset.retainALL(map.keyset));
		// map.get(datetmp) or anothermap.get(datetmp)
		// for(datetobeadd: anothermap.keyset.removeALL(map.keyset));
		// {map.put(datetobeadd,anothermap.get(datetobeadd));
		Set<LocalDate> tmpKeySet = anotherTimeInterval.timeIntervalMap.keySet();
		tmpKeySet.retainAll(timeIntervalMap.keySet());// find common date
		for (LocalDate datetmp : tmpKeySet) {
			BitSet tmpBitSet = timeIntervalMap.get(datetmp);
			tmpBitSet.or(anotherTimeInterval.timeIntervalMap.get(datetmp));
			timeIntervalMap.put(datetmp,tmpBitSet);
		}

		Set<LocalDate> tmpKeySet2 = anotherTimeInterval.timeIntervalMap
				.keySet();
		tmpKeySet2.removeAll(timeIntervalMap.keySet());// find unique/new date
														// in another
		for (LocalDate datetobeadd : tmpKeySet2) {
			timeIntervalMap.put(datetobeadd,
					anotherTimeInterval.timeIntervalMap.get(datetobeadd));
		}
		;

	}
	
	public TimeInterval setTimeInterval(Appt[] abu)
	{
		System.out.println(abu[0].TimeSpan().toString());
		TimeInterval ti = new TimeInterval(abu[0].TimeSpan());
		System.out.println("a1: " + ti.toString());
		for (int i = 0; i<abu.length; i++)
		{
			System.out.println("a2: " + abu[i].TimeSpan());
			ti.unionwith(new TimeInterval(abu[i].TimeSpan()));
		}
		System.out.println("a3: " + ti.toString());
		return ti;
	}
	
	public TimeInterval addTimeInterval(TimeInterval ti, Appt[]abu)
	{
		for (int i = 0; i<abu.length; i++)
		{
			System.out.println("a4: " + abu[i].TimeSpan());
			ti.unionwith(new TimeInterval(abu[i].TimeSpan()));
		}
		System.out.println("a5: " + ti.toString());
		return ti;
	}
	
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
		Set<LocalDate> tmpKeySet = timeIntervalMap.keySet();
		tmpKeySet.removeAll(anotherTimeInterval.timeIntervalMap.keySet());// find
																			// unique/new
																			// date
																			// in
																			// another
		for (LocalDate datetoberemove : tmpKeySet) {
			timeIntervalMap.remove(datetoberemove);
		}

		for (LocalDate tmpdate : timeIntervalMap.keySet()) {
			BitSet tmpBitSet = timeIntervalMap.get(tmpdate);
			tmpBitSet.and(anotherTimeInterval.timeIntervalMap.get(tmpdate));
			if (tmpBitSet.cardinality() == 0) {// if num of true ==0, remove
				timeIntervalMap.remove(tmpdate);
			} else {
				timeIntervalMap.put(tmpdate, tmpBitSet);
			}

		}

	}

	public void subtract(TimeInterval anotherTimeInterval) {
		Set<LocalDate> tmpKeySet = anotherTimeInterval.timeIntervalMap.keySet();
		tmpKeySet.retainAll(timeIntervalMap.keySet());
		for (LocalDate tmpdate : tmpKeySet) {
			BitSet tmpBitSet = timeIntervalMap.get(tmpdate);
			tmpBitSet.andNot(anotherTimeInterval.timeIntervalMap.get(tmpdate));
			if (tmpBitSet.cardinality() == 0) {
				timeIntervalMap.remove(tmpdate);
			} else {
				timeIntervalMap.put(tmpdate, tmpBitSet);
			}
		}
	}

	public boolean isEmpty() {
		return this.timeIntervalMap.isEmpty();
	}

	public void clearBeforeNow() {
		TimeMachine tm = TimeMachine.getInstance();
		clearBeforeTime(tm.getTMTimestamp().toLocalDateTime());//
	}

	public void clearBeforeTime(LocalDateTime time) {

		clearbeforedate(time);
		LocalDate tmpdate = time.toLocalDate();
		if (timeIntervalMap.containsKey(time.toLocalDate())) {
			BitSet tmpBitSet = timeIntervalMap.get(time.toLocalDate());

			LocalTime tmpTime = LocalTime.of(8, 0, 0, 1);
			for (int i = 0; i < 40; i++) {
				if (tmpTime.isBefore(time.toLocalTime())) {
					// start <tmp< end, 8:00:1
					tmpBitSet.clear(i);
				}

				tmpTime = tmpTime.plusMinutes(15);

			}
			if (tmpBitSet.cardinality() == 0) {
				timeIntervalMap.remove(tmpdate);
			} else {
				timeIntervalMap.put(tmpdate, tmpBitSet);
			}

		}

	}

	public void clearbeforedate(LocalDateTime time) {
		for (LocalDate tmpdate : timeIntervalMap.keySet()) {
			if (tmpdate.isBefore(time.toLocalDate())) {
				timeIntervalMap.remove(tmpdate);
			}
		}
	}

	@Override
	public String toString() {
		String result = "TimeInterval [timeIntervalMap=" + timeIntervalMap
				+ "]\n";
		for (LocalDate tmpdate : timeIntervalMap.keySet()) {
			result += ("\t" + tmpdate + ": ");
			for (int i = 0; i < 40; i++) {
				if (timeIntervalMap.get(tmpdate).get(i) == true) {
					result += "O";
				} else {
					result += "X";
				}
			}
			result += "\n";

		}

		return result;
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
