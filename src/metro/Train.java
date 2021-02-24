package metro;

import jade.core.AID;

public class Train {


    private int trainCapacity;
    private AID trainID;

    // atual carregamento de passegeiros
    private int trainPassengerLoad;

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
