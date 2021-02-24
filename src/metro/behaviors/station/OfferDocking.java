package metro.behaviors.station;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import metro.StationAgent;
import metro.extras.Ansi;

public class OfferDocking extends CyclicBehaviour {

    private StationAgent ag;

    public OfferDocking(StationAgent ag) {

        this.ag = ag;
    }

    public void action() {
        // template para receber CFP proposta de TrainAgent - Troca de Mensagens Passo 2
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = this.myAgent.receive(mt);

        // so executa quando receber mensagem (not null)
        if (msg != null) {

            System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent: ") +
                    "Message received: " + msg.getContent());

            ACLMessage reply = msg.createReply();

            ag.freePlataform = ag.station.getFreePlataform();

            if (ag.freePlataform != 0) {
                String trainID = msg.getContent();
                System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent: ") +
                        "Offering docking to train: " + trainID + " on plataform " + ag.freePlataform);
                reply.setPerformative(ACLMessage.PROPOSE);
                // envie mensagem de resposta com status da plataforma
                reply.setContent(String.valueOf(ag.freePlataform));
            } else {
                reply.setPerformative(ACLMessage.REFUSE);
                System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent: ") +
                        "Sending ACLMessage.REFUSE, no available plataforms.");
                reply.setContent("not-available plataforms");
            }
            this.myAgent.send(reply);
            System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent: ") +
                    "enviando mensagem ACLMessage.Propose " + ag.freePlataform);
        } else {
            this.block();
        }

    }
}
