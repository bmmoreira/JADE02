package metro;

import jade.core.AID;
import java.io.Serializable;

public class Passenger implements Serializable {

    // o passageiro pode embarcar?
    private boolean canBoardTrain = false;
    // ID do comboio em que embarcou
    private AID trainID = null;

    public void setTrainID(AID tID) {
        this.trainID = tID;
    }

    public AID getTrainID() {
        return this.trainID;
    }

    public boolean getCanBoardTrain() {
        return this.canBoardTrain;
    }
    public void setCanBoardTrain(boolean boarded) {
        canBoardTrain = boarded;
    }

}
