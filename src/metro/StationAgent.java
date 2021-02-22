package metro;

import examples.bookTrading.BookSellerAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import metro.extras.Ansi;

import java.util.Hashtable;

public class StationAgent extends Agent {

    private int stationNumber;
    private AID[] trainAgents;
    private Station station;
    private int plataformStatus;

    // Put agent initializations here
    protected void setup() {
        Object[] args;
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent: ") +  " " +
                this.getAID().getName() + " is ready.");
        try {
            args = this.getArguments();
            System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent: ") +
                    "Station number: "+ (String)args[0] + " initialized.");
            if (args != null && args.length > 0) {

                this.stationNumber = Integer.valueOf((String) args[0]);
                //cria uma station com duas plataformas para teste
                station = new Station(stationNumber,2);
                // para teste vamos colocar a plataforma 1 livre(0)
                station.setPlataformStatus(0,1);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println("Error1: No Station number in arguments");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Error2: No Station number in arguments");
        }


        // Register station service in the yellow pages
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Station-service");
        sd.setName("Dock-Operation");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        this.addBehaviour(new StationAgent.OfferDocking());
        this.addBehaviour(new StationAgent.DockServer());

        /*
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if(msg != null){
                    System.out.println(msg.getSender() +" : aa5" + msg.getContent());
                } else {
                    block();
                }
            }
        });
        */

    }


    private class OfferDocking extends CyclicBehaviour {
        private OfferDocking() {
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

                plataformStatus = station.getFreePlataform();

                if (plataformStatus != 0) {
                    String trainID = msg.getContent();
                    System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent: ") +
                            "Offering docking to train: " + trainID + " on plataform " + plataformStatus);
                    reply.setPerformative(ACLMessage.PROPOSE);
                    // envie mensagem de resposta com status da plataforma
                    reply.setContent(String.valueOf(plataformStatus));
                } else {
                    reply.setPerformative(ACLMessage.REFUSE);
                    System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent: ") +
                            "Sending ACLMessage.REFUSE, no available plataforms.");
                    reply.setContent("not-available plataforms");
                }
                this.myAgent.send(reply);
                System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent: ") +
                        "enviando mensagem ACLMessage.Propose " + plataformStatus);
            } else {
                this.block();
            }



        }
    }

    private class DockServer extends CyclicBehaviour {
        private DockServer() {
        }

        public void action() {
            // se oferta de docking foi aceita - servir a plataforma para o comboio
            // Passo 5 (receber proposta de Aceite do TrainAgent
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = this.myAgent.receive(mt);
            if (msg != null) {
                String title = msg.getContent();

                System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent: ") +
                        "Passo 5 recebendo mensagem: "+title);
                ACLMessage reply = msg.createReply();


                // Alocar comboio para plataforma livre
                int plataformNumber = plataformStatus;
                //station.setTrainID(msg.getSender().getName(),plataformNumber);
            // Passo 6 informar alocacao de plataforma
                reply.setPerformative(ACLMessage.INFORM);
                System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent: ") +
                        "Informing to Agent " + msg.getSender().getName());
                this.myAgent.send(reply);
            } else {
                this.block();
            }

        }
    }

}
