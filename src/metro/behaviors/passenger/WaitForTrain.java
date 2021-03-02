package metro.behaviors.passenger;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import metro.Passenger;
import metro.extras.Ansi;

public class WaitForTrain extends CyclicBehaviour {
    private String  myName;
    private String className;
    private String agentType;
    Passenger passenger;

    public WaitForTrain(Passenger p) {
        this.passenger = p;
    }

    public void action() {

        this.myName= myAgent.getAID().getName();
        this.className = getClass().getName();
        this.agentType = "Passenger Agent ";
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = this.myAgent.receive(mt);

        if(msg != null) {
            String aName = msg.getSender().getName();
            String id = msg.getConversationId();
            if (id.equals("Board-now")) {
                passenger.setCanBoardTrain(true);
                printLogHead(" "+ this.myAgent.getLocalName() + ": foi informado que o comboio chegou.");
            }
        } else {
            block();
        }
    }

    private void printLog(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.RED).format(agentType +
                myName) + text);
    }
    private void printLogHead(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.RED).format(agentType +
                myName) + text + " " + new Ansi(Ansi.BACKGROUND_RED, Ansi.BLACK).format("Class: " + className));
    }
}