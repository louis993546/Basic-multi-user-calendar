package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.gui.AcceptOrNotDialog;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.MessageBody;
import hkust.cse.calendar.unit.TimeMachine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class MessageStorage implements java.io.Serializable {
	static SortedMap<Integer, MessageBody> deleteUser = loadMap("deleteUser.map");
	// deleteUser.put(-1, new
	// MessageBody(-1,-1,-1,MessageBody.UserResponse.NotYet,LocalDateTime.now());
	static SortedMap<Integer, MessageBody> deleteLocation = loadMap("deleteLocation.map");

	private MessageStorage() {

	}

	public static SortedMap<Integer, MessageBody> getDeleteUser() {
		return deleteUser;
	}

	public static SortedMap<Integer, MessageBody> getDeleteLocation() {
		// TODO Auto-generated method stub
		return deleteLocation;
	}

	public static void save(String filename, String filename2) {
		try {
			// Serialize data object to a file
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(filename));
			out.writeObject(deleteUser);
			out.flush();
			out.close();

			out = new ObjectOutputStream(new FileOutputStream(filename2));
			out.writeObject(deleteLocation);
			out.flush();
			out.close();
			System.out.println("");
		} catch (IOException e) {
			System.err.println("io error...");
		}
		//
		// // Serialize data object to a byte array
		// ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
		// out = new ObjectOutputStream(bos) ;
		// out.writeObject(object);
		// out.close();
		//
		// // Get the bytes of the serialized object
		// byte[] buf = bos.toByteArray();
		// } catch (IOException e) {
		// }
	}

	public static SortedMap<Integer, MessageBody> loadMap(String filename){
		SortedMap<Integer, MessageBody> map=new ConcurrentSkipListMap<Integer, MessageBody>();
		try{
			ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(filename)));
			try{
				map = (SortedMap<Integer, MessageBody>) ois.readObject();
				
			}catch(ClassNotFoundException e){
				System.out.println("MessageStorage.load()");
				e.printStackTrace();
			}
		}catch(FileNotFoundException e){
	        File file = new File(filename);
	        boolean fileCreated = false;
	        try {
	            fileCreated = file.createNewFile();
	        } catch (IOException ioe) {
	            System.out.println("Error while creating empty file: " + ioe);
	        }
	        
	        map= new ConcurrentSkipListMap<Integer, MessageBody>();
		}catch(IOException e){// why??
			System.out.println("MessageStorage.load(): io exception");
			PrintWriter writer = null;
			try {
				writer = new PrintWriter(filename);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				
			}
			writer.print("");
			writer.close();
			
		}
		return new ConcurrentSkipListMap<Integer, MessageBody>(map);
	}


	// public static void popupMsgAndSave(int msgid, String userOrLocation) {
	// // if(userOrLocation.equals("user")){
	// // //MessageBody tmpMB = deleteUser.get(msgid);
	// // //System.out.println("user with id " + tmpMB.getUserToBeDeletedID()
	// // //+ "will be deleted. Do you accept?");
	// // AcceptOrNotDialog tmpDialog = new AcceptOrNotDialog(msgid,"user");
	// //
	// // // if yes, if no
	// // } else if (userOrLocation.equals("location")){
	// // AcceptOrNotDialog tmpDialog = new AcceptOrNotDialog(msgid,"location");
	// // }
	//
	// }

	public static SortedMap<Integer, LocalDateTime> getCreatorToLastRelatedEventMap(
			int IDofUserOrLocToBeDeleted, String userOrLoc) {
		// locked the user
		System.out.println("MessageStorage.getCreatorToLastRelatedEventMap()");
		SortedMap<Integer, LocalDateTime> tmpmap = new ConcurrentSkipListMap<Integer, LocalDateTime>();

		// appt[] apptlist=getApptListInvolveUserInFuture(uid);
		ApptDB adb = new ApptDB();
		Appt[] apptlist = null;
		if (userOrLoc.equals("user")) {
			apptlist = adb.getFutureApptWithUser(IDofUserOrLocToBeDeleted);
			// getApptListInvolveUserInFuture(IDofUserToBeDeleted);
		} else if (userOrLoc.equals("location")) {
			apptlist = adb.getApptByLocationName(LocationDB.getInstance()
					.getLocationName(IDofUserOrLocToBeDeleted));
		} else {
			System.err
					.println("MessageStorage.getCreatorToLastRelatedEventMap()");
		}
		System.out.println(Arrays.deepToString(apptlist));
		// this may work

		Map<Integer, List<LocalDateTime>> CreatorToEndTimesMap = new HashMap<Integer, List<LocalDateTime>>();
		for (Appt tmpappt : apptlist) {

			LocalDateTime endTime = tmpappt.getAppointment().getTimeSpan()
					.EndTime().toLocalDateTime();
			LocalDateTime currTime = TimeMachine.getInstance().getTMTimestamp()
					.toLocalDateTime();

			if (currTime.isBefore(endTime)) {// check again to ensure only
												// future events count

				int tmpUserID = tmpappt.getAppointment().getCreaterUID();

				List<LocalDateTime> tmplist;
				if (CreatorToEndTimesMap.containsKey(tmpUserID)) {
					tmplist = CreatorToEndTimesMap.get(tmpUserID);

				} else {
					tmplist = new ArrayList<LocalDateTime>();
				}

				tmplist.add(endTime);

				CreatorToEndTimesMap.put(tmpUserID, tmplist);
			}
		}
		// now CreatorToEndTimesMap is group by creator
		// try get max endtime for diff creator

		SortedMap<Integer, LocalDateTime> CreatorToMaxEndTimeMap = new ConcurrentSkipListMap<Integer, LocalDateTime>();
		for (int i : CreatorToEndTimesMap.keySet()) {
			List<LocalDateTime> listOfDateTime = CreatorToEndTimesMap.get(i);
			LocalDateTime tmpmax = Collections.max(listOfDateTime,
					new Comparator<LocalDateTime>() {
						public int compare(LocalDateTime o1, LocalDateTime o2) {
							return o1.compareTo(o2);
						}
					});// really need to give comparator??
			CreatorToMaxEndTimeMap.put(i, tmpmax);

		}
		// now we got max time
		System.out.println("MessageStorage.getCreatorToLastRelatedEventMap()");
		return CreatorToMaxEndTimeMap;
	}

	// find id (user, location)
	public static boolean isExistID(int id, String userOrLoc) {
		if (userOrLoc.equals("user")) {
			for (MessageBody tmpmb : deleteUser.values()) {
				if (tmpmb.getUserToBeDeletedID() == id) {
					return true;
				}
			}
		} else if (userOrLoc.equals("location")) {

		}
		return false;
	}

	public static void deleteAllMsgWithUserOrLocationToBeDeleted(
			int userOrLocToBeDeletedID, String userOrLoc) {
		if (userOrLoc.equals("user")) {
			for (int tmpkey : deleteUser.keySet()) {
				MessageBody tmpMessageBody = deleteUser.get(tmpkey);//
				if (tmpMessageBody.getUserToBeDeletedID() == userOrLocToBeDeletedID) {
					deleteUser.remove(tmpkey);
				}
			}

		} else if (userOrLoc.equals("location")) {
			for (int tmpkey : deleteLocation.keySet()) {
				MessageBody tmpMessageBody = deleteLocation.get(tmpkey);//
				if (tmpMessageBody.getLocationToBeDeletedID() == userOrLocToBeDeletedID) {
					deleteLocation.remove(tmpkey);
				}
			}
		} else {
			System.out
					.println("This part should never run. "
							+ "MessageStorage.deleteAllMsgWithUserOrLocationToBeDeleted()");
		}

	}

}
