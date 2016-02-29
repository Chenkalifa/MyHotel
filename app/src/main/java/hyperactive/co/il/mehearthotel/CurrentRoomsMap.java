package hyperactive.co.il.mehearthotel;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Tal on 31/01/2016.
 */
public class CurrentRoomsMap extends HashMap<Integer, RoomData> {
//    static final int MAX=10;
    final static public CurrentRoomsMap initializeRooms(){
        CurrentRoomsMap roomsMap =new CurrentRoomsMap();
        //can  also be initialize by query from DB
        roomsMap.put(0, new RoomData(300, "premium_suite"));
        roomsMap.put(1, new RoomData(300, "premium_suite"));
        roomsMap.put(2, new RoomData(275, "supreme_suite"));
        roomsMap.put(3, new RoomData(275, "supreme_suite"));
        roomsMap.put(4, new RoomData(240, "junior_suite"));
        roomsMap.put(5, new RoomData(240, "junior_suite"));
        roomsMap.put(6, new RoomData(310, "classic_suite"));
        roomsMap.put(7, new RoomData(325, "dreams_luxury_suite"));
        roomsMap.put(8, new RoomData(350, "penthouse_luxury_suite"));
        roomsMap.put(9, new RoomData(420, "deco_superior_apartment"));
        return roomsMap;
    }
//    public RoomData put(Integer key, RoomData value) {
//            if (size() >= MAX && !containsKey(key)) {
//                return null;
//            } else {
//                put(key, value);
//                return value;
//            }
//    }

    public List<Integer> updateAvilableRoomsList(List<Integer> occupiedRoomsList) {
        List<Integer> avilableRoomList=new ArrayList<Integer>(keySet());
        keySet().removeAll(occupiedRoomsList);
        Log.i("myApp", "in updateAvilableRoomsList="+toString());
        avilableRoomList.removeAll(occupiedRoomsList);
        return avilableRoomList;
    }

    public List<Integer> getRoomsList(){
        return new ArrayList<Integer>(keySet());
    }

    public List<Integer> getRoomsNumbersByType(String type){
        Set<Integer> keySet = keySet();
        List<Integer> roomsNumbers=new ArrayList<Integer>();
        for(Integer i:keySet){
            if((get(i).getType().equals(type)))
                roomsNumbers.add(i);
        }
        Log.i("myApp", "in getRoomsNumbersByType="+roomsNumbers.toString());
        return roomsNumbers;
    }

    public int getRoomAmountByType(String type){
        int amount=0;
        Set<Integer> keys = keySet();
        for(Integer i:keys){
            if(get(i).getType().equals(type)){
                amount++;
                Log.i("myApp", "in getRoomAmountByType, amount="+amount);
            }

        }
        return amount;
    }

    public int getRoomAmountByRoomNumber(int roomNumber){
        if(containsKey(roomNumber)){
            String type=get(roomNumber).getType();
            return getRoomAmountByType(type);
        }
        return -1;
    }

    public String getRoomType(int roomNumber){
        if(containsKey(roomNumber))
            return get(roomNumber).getType();
        return null;
    }

    public boolean hasType(String type){
        Set<Integer> keys = keySet();
        for(Integer i:keys){
            if(get(i).getType().equals(type))
                return true;
        }
        return false;
    }

    static public CurrentRoomsMap getCurrentRoomsMap(List<Integer> occupiedRoomsList){
        CurrentRoomsMap currentRoomsMap = CurrentRoomsMap.initializeRooms();
        for(int i=0; i<occupiedRoomsList.size();i++){
            if(currentRoomsMap.containsKey(occupiedRoomsList.get(i)))
                currentRoomsMap.remove(occupiedRoomsList.get(i));
        }
        return currentRoomsMap;
    }
}