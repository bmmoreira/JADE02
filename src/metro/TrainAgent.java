package metro;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

public class TrainAgent extends Agent {

    protected void setup() {

        System.out.println("Hello, I'm an Train agent!");
        System.out.println("My local name is " + getAID().getLocalName());
        System.out.println("My GUID is " + getAID().getName());
        System.out.println("My addresses are " + String.join(",", getAID().getAddressesArray()));

        ServiceDescription service = new ServiceDescription();
        service.setType("Station-service");
        plataformStatus(service,2);

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

    protected void plataformStatus(final ServiceDescription sd, final int plataformID){

        addBehaviour(new TickerBehaviour(this,10000) {
            @Override
            protected void onTick() {
                DFAgentDescription dfd = new DFAgentDescription();
                dfd.addServices(sd);
                try{
                    DFAgentDescription[] result = DFService.search(myAgent,dfd);
                    if(result.length != 0){
                        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                        msg.addReceiver(result[0].getName());
                        msg.setContent(String.valueOf(plataformID));
                        myAgent.send(msg);
                        System.out.println("PlataformStatus: " + result[0].getName() +" : " + msg.getContent());
                        stop();
                    }
                } catch (FIPAException e){
                    e.printStackTrace();
                }
            }
        });

    }

}
