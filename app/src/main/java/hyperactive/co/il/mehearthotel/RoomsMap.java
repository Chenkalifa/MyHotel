package hyperactive.co.il.mehearthotel;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * Created by Tal on 31/01/2016.
 */
final class RoomsMap extends HashMap<Integer, RoomData> {
    static final int MAX=10;
    private HashMap<Integer, RoomData> mMap;
    {
        mMap=new HashMap<Integer, RoomData>();
        //can  also be initialize by query from DB
        mMap.put(0, new RoomData(300, "premium_suite"));
        mMap.put(1, new RoomData(300, "premium_suite"));
        mMap.put(2, new RoomData(275, "supreme_suite"));
        mMap.put(3, new RoomData(275, "supreme_suite"));
        mMap.put(4, new RoomData(240, "junior_suite"));
        mMap.put(5, new RoomData(240, "junior_suite"));
        mMap.put(6, new RoomData(310, "classic_suite"));
        mMap.put(7, new RoomData(325, "dreams_luxury_suite"));
        mMap.put(8, new RoomData(350, "penthouse_luxury_suite"));
        mMap.put(9, new RoomData(420, "deco_superior_apartment"));
    }
//    public static void initializeRooms() {
//
//    }

    @Override
    public RoomData put(Integer key, RoomData value) {
        if (mMap.size() == MAX) {
            Log.e("myApp", "can't add or remove object from this map");
            return null;
        } else {
            mMap.put(key, value);
            return value;

        }
    }

    @Override
    public int size() {
        return mMap.size();
    }

    @Override
    public RoomData remove(Object key) {
        Log.e("myApp", "can't add or remove object from this map");
        return null;
    }

    public List<Integer> getAvilableRoomsList(List<Integer> occupiedRoomsList) {
        List<Integer> avilableRoomList=new ArrayList<Integer>(mMap.keySet());
        avilableRoomList.removeAll(occupiedRoomsList);
        return avilableRoomList;
    }

    public int getRoomAmountByType(String type) {
        int amount = 0;
        Set<Integer> keys = mMap.keySet();
        for (Integer i : keys) {
            if (mMap.get(i).getType().equals(type))
                amount++;
        }
        return amount;
    }

    public int getRoomAmountByRoomNumber(int roomNumber) {
        if (mMap.containsKey(roomNumber)) {
            String type = mMap.get(roomNumber).getType();
            return getRoomAmountByType(type);
        }
        return -1;
    }

    public String getRoomType(int roomNumber) {
        if (mMap.containsKey(roomNumber))
            return mMap.get(roomNumber).getType();
        return null;
    }

    public boolean hasType(String type) {
        Set<Integer> keys = mMap.keySet();
        for (Integer i : keys) {
            if (mMap.get(i).getType().equals(type))
                return true;
        }
        return false;
    }
}