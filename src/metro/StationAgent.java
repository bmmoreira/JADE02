package metro;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Hashtable;

public class StationAgent extends Agent {

    private Station base;
    // Put agent initializations here
    protected void setup() {

        System.out.println("Hello, I'm an Station agent!");
        System.out.println("My local name is " + getAID().getLocalName());
        System.out.println("My GUID is " + getAID().getName());
        System.out.println("My addresses are " + String.join(",", getAID().getAddressesArray()));

        base = new Station("Base");
        base.setPlataformOneStatus(2);
        base.setPlataformTwoStatus(2);

        // Register station service in the yellow pages
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Station-service");
        sd.setName("Plataform-Status");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if(msg != null){
                    System.out.println(msg.getSender() +" : aa" + msg.getContent());
                } else {
                    block();
                }
            }
        });
    }

    /**
     * Inner class PlataformStatus.
     * This is the behaviour used by Train agents to serve incoming requests
     * for gatrhering information status of current plataform availability.
     */
    private class PlataformStatus extends CyclicBehaviour {
        public void action() {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null) {
                // CFP Message received. Process it
                String title = msg.getContent();
                ACLMessage reply = msg.createReply();

                Integer status = (Integer) base.getPlataformOneStatus();
                if (status != null) {
                    // The requested book is available for sale. Reply with the price
                    reply.setPerformative(ACLMessage.PROPOSE);
                    reply.setContent(String.valueOf(status.intValue()));
                } else {
                    // The requested book is NOT available for sale.
                    reply.setPerformative(ACLMessage.REFUSE);
                    reply.setContent("not-available");
                }
                myAgent.send(reply);
            } else {
                block();
            }
        }
    }  // End of inner class OfferRequestsServer
}
