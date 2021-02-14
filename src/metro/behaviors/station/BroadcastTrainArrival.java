package metro.behaviors.station;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

// Comportamento para anunciar a chegada de comboio
public class BroadcastTrainArrival extends OneShotBehaviour {

    public void action() {
        System.out.println("Comboio chegou à estação, a informar passageiros!");
        for (int i = 0;  i < 250;  i++) {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.setContent("Chegou o comboio");
            msg.addReceiver(new AID("passageiro_" + i, AID.ISLOCALNAME));
            myAgent.send(msg);
        }
    }

}
