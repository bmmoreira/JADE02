package metro.behaviors.train;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

public class TrainCapacity extends Behaviour {

    long delay;

    public TrainCapacity(Agent a, long delay){
        super(a);
        this.delay = delay;
    }

    public void action(){

    }

    public boolean done(){
        return false;
    }

    public int onEnd(){
        return 0;
    }


}
