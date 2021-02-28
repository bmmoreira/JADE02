package metro.behaviors.train;

import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import metro.TrainAgent;
import metro.extras.Ansi;

public class TrackMove extends CyclicBehaviour {

    private String  myName;
    private String className;
    private String agentType;

    public TrackMove(){

        this.className = getClass().getName();
        this.agentType = "Train Agent ";
    }
    @Override
    public void action() {
        this.myName= myAgent.getAID().getName();
        MessageTemplate mt = MessageTemplate.MatchConversationId("Track-Move");
        ACLMessage msg = this.myAgent.receive(mt);

        if(msg != null){

            msg.getPerformative();
            printLogHead(": Received message -  Performative ACL " +msg.getPerformative() + " from " + msg.getSender().getName());
            this.myAgent.addBehaviour(new metro.behaviors.train.RequestDock((TrainAgent) this.myAgent));

        } else {
            block();
        }

    }

    private void printLog(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format(agentType +
                myName) + text);
    }
    private void printLogHead(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format(agentType +
                myName) + text + " " + new Ansi(Ansi.BACKGROUND_YELLOW, Ansi.BLACK).format("Class: " + className));
    }
}
