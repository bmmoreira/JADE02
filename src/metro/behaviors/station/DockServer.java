package metro.behaviors.station;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import metro.StationAgent;
import metro.Train;
import metro.extras.Ansi;

import java.io.IOException;

public class DockServer extends CyclicBehaviour {

    StationAgent ag;

    public DockServer(StationAgent ag) {
        this.ag = ag;
    }

    public void action() {
        String  myName= myAgent.getAID().getName();
        String className = getClass().getName();

        // se oferta de docking foi aceita - servir a plataforma para o comboio
        // Passo 5 (receber proposta de Aceite do TrainAgent
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
        ACLMessage msg = this.myAgent.receive(mt);
        if (msg != null) {
            String title = msg.getContent();

            System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent "+ myName) +
                    ": Passo 5 recebendo mensagem ACCEPT_PROPOSAL from " + msg.getSender().getName() + " " +
                    new Ansi(Ansi.BACKGROUND_GREEN, Ansi.BLACK).format("Class: " + className));


            ACLMessage reply = msg.createReply();

            // Alocar comboio para plataforma livre
            int plataformNumber = ag.freePlataform;


            ag.station.setTrainID(msg.getSender(),plataformNumber);
            // Passo 6 informar alocacao de plataforma
            reply.setPerformative(ACLMessage.INFORM);
            System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent " +
                    this.myAgent.getAID().getName()) + ": Informing to Agent " + msg.getSender().getName());



            // chama metodo para calcular numero de passageiros atual na plataforma
            //reply.setContent("155");
            // envia dados da Station
            try {
                reply.setContentObject(new Train());
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.myAgent.send(reply);
        } else {
            this.block();
        }

    }
}

