package metro.behaviors.station;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import metro.Station;
import metro.StationAgent;
import metro.Train;
import metro.extras.Ansi;

import java.io.IOException;
import java.io.Serializable;

public class OfferDocking extends CyclicBehaviour {

    private StationAgent ag;
    private String  myName;
    private String className;
    private String agentType;

    public OfferDocking(StationAgent ag) {

        this.ag = ag;
    }

    public void action() {

        this.myName= myAgent.getAID().getName();
        this.className = getClass().getName();
        this.agentType = "Train Agent ";

        // template para receber CFP proposta de TrainAgent - Troca de Mensagens Passo 2
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
        ACLMessage msg = this.myAgent.receive(mt);

        // so executa quando receber mensagem (not null)
        if (msg != null) {

            printLogHead(": Passo 2 - Message CFP received from " + msg.getSender().getName() + " ID: " + msg.getConversationId());

            ACLMessage reply = msg.createReply();

            ag.freePlataform = ag.station.getFreePlataform();

            if (ag.freePlataform != 0) {
                String trainID = msg.getContent();

                reply.setPerformative(ACLMessage.PROPOSE);

                try {
                    // envia objeto Station para o Train com detalhes da Station
                    reply.setContentObject(ag.station);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                reply.setContent(String.valueOf(ag.freePlataform));
                printLog(": Sending ACLMessage.Propose to dock plataform " + ag.freePlataform);
            } else {
                reply.setPerformative(ACLMessage.REFUSE);
                reply.setContent("not-available plataforms");
                printLog(": Sending ACLMessage.REFUSE, no available plataforms.");
            }
            // envie mensagem de resposta com status da plataforma
            this.myAgent.send(reply);

        } else {
            this.block();
        }

    }

    private void printLog(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format(agentType +
                myName) + text);
    }
    private void printLogHead(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format(agentType +
                myName) + text + " " + new Ansi(Ansi.BACKGROUND_GREEN, Ansi.BLACK).format("Class: " + className));
    }

}
