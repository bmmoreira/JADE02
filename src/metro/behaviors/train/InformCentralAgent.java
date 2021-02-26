package metro.behaviors.train;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class InformCentralAgent extends OneShotBehaviour {

    private String action;
    public InformCentralAgent(String ac){
        this.action = ac;
    }

    @Override
    public void action() {

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("ca@metro-system"));
        msg.setContent(action);
        msg.setConversationId("inform-Dock");
        //msg.setReplyWith("cfp"+System.currentTimeMillis()); // Valor unico.
        // envia mensagem
        this.myAgent.send(msg);

    }

}