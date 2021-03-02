package metro.behaviors.station;


import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import metro.StationAgent;
import metro.extras.Ansi;

public class RespondStationPassengerLoad extends CyclicBehaviour {

    private StationAgent ag;
    private String className;
    private String agentType;
    private String myName;
    private AID[] passengersAgents;

    public RespondStationPassengerLoad(StationAgent ag) {
        this.ag = ag;
        this.agentType = "Station Agent ";
        this.className = getClass().getName();
    }

    public void action() {
        this.myName= myAgent.getAID().getName();
        // template para receber CFP proposta de TrainAgent - Troca de Mensagens Passo 2
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage msg = this.myAgent.receive(mt);

        // so executa quando receber mensagem (not null)
        if (msg != null && msg.getConversationId().equals("Request-Passenger-Load")) {


            printLogHead(": Message received from " + msg.getSender().getName() + " " + msg.getConversationId());

            ACLMessage reply = msg.createReply();

            ag.setNumberOfPassengers(ag.station.getNumberOfPassengers());

            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription sd = new ServiceDescription();
            sd.setType("Board-train");
            template.addServices(sd);
            int passengerLoad = 0;

            try {
                DFAgentDescription[] result = DFService.search(this.myAgent, template);
                passengerLoad = result.length;
                printLog(": Searching in Directory Facilitator Service for Passengers ");
                printLog(": Found " + passengerLoad + " passengers waiting to board the train");

                passengersAgents = new AID[result.length];

                for(int i = 0; i < result.length; ++i) {
                    passengersAgents[i] = result[i].getName();
                }

            } catch (FIPAException var5) {
                var5.printStackTrace();
            }
            ag.setNumberOfPassengers(passengerLoad);

            if (ag.getNumberOfPassengers() != 0) {

                reply.setPerformative(ACLMessage.INFORM);
                // envie mensagem de resposta com número de passageiros na estação
                reply.setContent(String.valueOf(ag.getNumberOfPassengers()));

                // Envia mensagens a todos os passageiros
                ACLMessage informPassenger = new ACLMessage(ACLMessage.INFORM);
                for(int i = 0; i < passengersAgents.length; ++i) {
                    informPassenger.addReceiver(passengersAgents[i]);
                }
                informPassenger.setContent("Board available");
                informPassenger.setConversationId("Board-now");
                // envia mensagem
                this.myAgent.send(informPassenger);

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

    private void printLog(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format(agentType +
                myName) + text);
    }
    private void printLogHead(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format(agentType +
                myName) + text + " " + new Ansi(Ansi.BACKGROUND_GREEN, Ansi.BLACK).format("Class: " + className));
    }
}