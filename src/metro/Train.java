package metro;

import jade.core.AID;

public class Train {

    private AID trainID;
    private boolean trainDocked; // if its on a plataform is docked
    private int trainCapacity;
    private int trainPassengerLoad; // actual passenger load
    private int trainTrackSector; // which sector currently is the Train in the railtrack
    private int trainDefaultDockTime = 5;

    public Train(AID trainAID){
        this.trainID = trainAID;

    }

    public int getTrainDefaultDockTime() {
        return trainDefaultDockTime;
    }

    public void setTrainDefaultDockTime(int trainDefaultDockTime) {
        this.trainDefaultDockTime = trainDefaultDockTime;
    }



    public AID getTrainID() {
        return trainID;
    }

    public void setTrainID(AID trainID) {
        this.trainID = trainID;
    }

    public int getTrainCapacity() {
        return trainCapacity;
    }

    public void setTrainCapacity(int trainCapacity) {
        this.trainCapacity = trainCapacity;
    }

    public int getTrainPassengerLoad() {
        return trainPassengerLoad;
    }

    public void setTrainPassengerLoad(int trainPassengerLoad) {
        this.trainPassengerLoad = trainPassengerLoad;
    }

}
