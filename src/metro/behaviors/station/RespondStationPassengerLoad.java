package metro.behaviors.station;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import metro.StationAgent;
import metro.extras.Ansi;

public class RespondStationPassengerLoad extends CyclicBehaviour {

    private StationAgent ag;

    public RespondStationPassengerLoad(StationAgent ag) {

        this.ag = ag;
    }

    public void action() {
        // template para receber CFP proposta de TrainAgent - Troca de Mensagens Passo 2
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage msg = this.myAgent.receive(mt);

        // so executa quando receber mensagem (not null)
        if (msg != null && msg.getConversationId().equals("Request-Passenger-Load")) {

            System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent "+this.myAgent.getAID().getName()) +
                    ": Message received from " + msg.getSender().getName() + " " + msg.getConversationId());

            ACLMessage reply = msg.createReply();

            ag.setNumberOfPassengers(ag.station.getNumberOfPassengers());


            if (ag.getNumberOfPassengers() != 0) {

                System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent "+this.myAgent.getAID().getName()) +
                        " we have "+ ag.getNumberOfPassengers() + " waitting to board the train " +": Passo X - Offering docking to train." );
                reply.setPerformative(ACLMessage.INFORM);
                // envie mensagem de resposta com número de passageiros na estação
                reply.setContent(String.valueOf(ag.getNumberOfPassengers()));
            } else {
                //reply.setPerformative(ACLMessage.REFUSE);
                System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent: ") +
                        ": Sending ACLMessage.REFUSE, no data on passenger load.");
                reply.setContent("not-available passenger-load");
            }
            this.myAgent.send(reply);
        } else {
            this.block();
        }

    }
}