package metro;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;
import metro.behaviors.passenger.BoardTrain;
import metro.behaviors.passenger.WaitForTrain;

public class PassengerAgent extends Agent {
    protected void setup() {

        System.out.println("Nome Passageiro: " + getAID().getLocalName());
        System.out.println("GUID Passageiro: " + getAID().getName());
        System.out.println("Endereço(s) Passageiro: " + String.join(",", getAID().getAddressesArray()));

        // Iniciar comportamentos do Passageiro
        SequentialBehaviour seq = new SequentialBehaviour();
        seq.addSubBehaviour(new WaitForTrain());
        seq.addSubBehaviour(new BoardTrain());
        addBehaviour(seq);

    }
}
