package metro.behaviors.station;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

// Comportamento para anunciar a chegada de comboio
public class BroadcastTrainArrival extends OneShotBehaviour {

    public void action() {

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("passageiro");
        template.addServices(sd);
        try {
            DFAgentDescription[] result = DFService.search(myAgent, template);
            //TODO: aqui poderá limitar-se o número de passageiros a informar para o embarque
            for (DFAgentDescription dfAgentDescription : result) {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.setContent("EMBARCAR");
                msg.addReceiver(new AID(dfAgentDescription.getName().getLocalName(), AID.ISLOCALNAME));
                myAgent.send(msg);
            }
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

    }

}
