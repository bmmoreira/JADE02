package metro;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import metro.behaviors.passenger.WaitForTrain;

public class PassengerAgent extends Agent {

    public Passenger passenger = new Passenger();

    protected void setup() {

        // Informar a presença do passageiro
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("passageiro");
        sd.setName("Agente do tipo passageiro");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Iniciar comportamentos do Passageiro
        SequentialBehaviour seq = new SequentialBehaviour();
        seq.addSubBehaviour(new WaitForTrain(passenger));
        addBehaviour(seq);

    }

    protected void takeDown() {
        // Retirar o passageiro
        try {
            DFService.deregister(this);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
    }
}
