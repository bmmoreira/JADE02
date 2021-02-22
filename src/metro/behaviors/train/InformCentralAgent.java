package metro.behaviors.train;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class InformCentralAgent extends OneShotBehaviour {



    @Override
    public void action() {
        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("ca@metro-system"));
        msg.setContent("showTrain");
        msg.setConversationId("inform-Control");
        //msg.setReplyWith("cfp"+System.currentTimeMillis()); // Valor unico.
        // envia mensagem
        this.myAgent.send(msg);

    }


}