package metro.behaviors.central;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import metro.extras.Ansi;

public class InformAgent extends OneShotBehaviour {

    private String action;
    public InformAgent(String ac){
        this.action = ac;
        this.className = getClass().getName();
        this.agentType = "Central Control Agent ";
    }
    private String className;
    private String agentType;
    private String  myName;

    public InformAgent() {
        this.className = getClass().getName();
        this.agentType = "Central Control Agent ";
    }

    @Override
    public void action() {
        this.myName= myAgent.getAID().getName();

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.addReceiver(new AID("t1@metro-system"));
        msg.setContent(action);
        msg.setConversationId("Track-Move");
        //msg.setReplyWith("cfp"+System.currentTimeMillis()); // Valor unico.

        printLogHead(": Sending message -  Performative "+msg.getPerformative() + "to : t1@metro-system");
        // envia mensagem
        this.myAgent.send(msg);

    }

    private void printLog(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.BLUE).format(agentType +
                myName) + text);
    }
    private void printLogHead(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.BLUE).format(agentType +
                myName) + text + " " + new Ansi(Ansi.BACKGROUND_BLUE, Ansi.BLACK).format("Class: " + className));
    }
}
