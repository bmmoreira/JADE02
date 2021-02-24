package metro.behaviors.train;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;

public class TrainCapacity extends CyclicBehaviour {

    long delay;

    public TrainCapacity(Agent a, long delay){
        super(a);
        this.delay = delay;
    }

    public void action(){

    }




}
