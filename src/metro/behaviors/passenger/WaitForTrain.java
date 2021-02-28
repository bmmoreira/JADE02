package metro.behaviors.passenger;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import metro.Passenger;

public class WaitForTrain extends CyclicBehaviour {

    Passenger passenger;

    public WaitForTrain(Passenger p) {
        this.passenger = p;
    }

    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null) {
            if (msg.getContent().contentEquals("EMBARCAR")) {
                passenger.setCanBoardTrain(true);
                //System.out.println(this.myAgent.getLocalName() + ": foi informado que o comboio chegou.");
            }
        } else {
            block();
        }
    }
}