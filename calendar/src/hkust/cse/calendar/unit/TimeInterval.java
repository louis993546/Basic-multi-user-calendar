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

	public TimeInterval(TimeSpan timeSpan) {
		LocalTime startTime = timeSpan.StartTime().toLocalDateTime()
				.toLocalTime();
		LocalTime endTime = timeSpan.StartTime().toLocalDateTime()
				.toLocalTime();

		BitSet tmpBitset = new BitSet(40);
		LocalTime tmpTime = LocalTime.of(8, 0, 0, 1);
		for (int i = 0; i < 40; i++) {
			if (startTime.isBefore(tmpTime) && tmpTime.isBefore(endTime)) {
				// start <tmp< end, 8:00:1
				tmpBitset.set(i);
			}
			tmpTime = tmpTime.plusMinutes(15);

		}
		this.timeIntervalMap = new TreeMap<LocalDate, BitSet>();
		timeIntervalMap.put(timeSpan.StartTime().toLocalDateTime()
				.toLocalDate(), tmpBitset);

	}

	public void intersectWith(TimeInterval anotherTimeInterval) {
		Set<LocalDate> tmpKeySet = timeIntervalMap.keySet();
		tmpKeySet.removeAll(anotherTimeInterval.timeIntervalMap.keySet());
		for (LocalDate datetoberemove : tmpKeySet) {
			timeIntervalMap.remove(datetoberemove);
		}

		for (LocalDate tmpdate : timeIntervalMap.keySet()) {
			BitSet tmpBitSet = timeIntervalMap.get(tmpdate);
			tmpBitSet.and(anotherTimeInterval.timeIntervalMap.get(tmpdate));
			if (tmpBitSet.cardinality() == 0) {
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
		return "TimeInterval [timeIntervalMap=" + timeIntervalMap + "]";
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