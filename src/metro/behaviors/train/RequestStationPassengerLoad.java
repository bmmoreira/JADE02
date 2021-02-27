package metro.behaviors.train;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import metro.extras.Ansi;

public class RequestStationPassengerLoad extends OneShotBehaviour {

    private String action;
    private AID stationAgent;
    public RequestStationPassengerLoad(AID stationAgent){
        this.stationAgent = stationAgent
        ;
    }
     /// not being used
    @Override
    public void action() {
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+
                "RequestStationPassengerLoad"));
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(this.stationAgent);
        msg.setConversationId("Request-Passenger-Load");
        //msg.setReplyWith("cfp"+System.currentTimeMillis()); // Valor unico.
        // envia mensagem
        this.myAgent.send(msg);


    }


}