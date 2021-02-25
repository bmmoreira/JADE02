package metro.behaviors.central;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import metro.CentralControlAgent;
import metro.extras.Ansi;
import metro.gui.TASS;

public class ReceiveReport extends CyclicBehaviour {

    TASS myGui;

    public ReceiveReport(TASS gui){
        this.myGui = gui;
    }
    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
        ACLMessage msg = this.myAgent.receive(mt);

        //ACLMessage msg = receive(mt);

        if(msg != null){
            String aName = msg.getSender().getName();
            String id = msg.getConversationId();
            if(msg.getConversationId().equals("inform-Dock")){
                System.out.println(new Ansi(Ansi.ITALIC, Ansi.BLUE).format("Central Agent: ") +
                        "Message from " + aName +" Station: " + msg.getContent() +
                        " ID: "+ id );
                myGui.paintrain(aName);
            }
        } else {
            block();
        }
    }
}
