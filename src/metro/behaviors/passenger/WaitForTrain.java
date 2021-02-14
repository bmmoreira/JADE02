package metro.behaviors.passenger;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class WaitForTrain extends CyclicBehaviour {

    public void action() {
        ACLMessage msg = myAgent.receive();
        if(msg != null) {
            if (msg.getContent().contentEquals("Chegou o comboio")) {
                System.out.println(this.myAgent.getLocalName() + ": foi informado que o comboio chegou.");
            }
        } else {
            block();
        }
    }
}