package metro.behaviors.passenger;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class BoardTrain extends CyclicBehaviour {
    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null) {
            //TODO: talvez nem seja necessário este comportamento
        } else {
            block();
        }
    }
}
