package metro;

import jade.core.AID;

import java.util.ArrayList;

public class RailTrack {

    private boolean initTrack;
    private ArrayList<String> stationTrackList;

    public ArrayList<String> getStationTrackList() {
        return stationTrackList;
    }

    public void setStationTrackList(ArrayList<String> stationTrackList) {
        this.stationTrackList = stationTrackList;
    }

    public RailTrack(){
        stationTrackList = new ArrayList<>();
    }

    public boolean isInitTrack() {
        return initTrack;
    }

    public void setInitTrack(boolean initTrack) {
        this.initTrack = initTrack;
    }

    public void addStation(String stationName){
        stationTrackList.add(stationName);
    }

}
