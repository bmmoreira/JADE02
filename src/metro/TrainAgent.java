package metro;

import examples.bookTrading.BookBuyerAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import metro.behaviors.station.BroadcastTrainArrival;
import metro.extras.Ansi;

public class TrainAgent extends Agent {

    private int stations;
    private int currentStation = 0;
    private AID[] stationAgents;



    protected void setup() {

        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent: ") + this.getAID().getName() + " is ready.");
        Object[] args = this.getArguments();
        if (args != null && args.length > 0) {
            System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent: ") + "Enable Stations: "+ (String)args[0]);
            stations = Integer.valueOf((String)args[0]) ;
        }

        addBehaviour(TrainAgent.this.new informCentralAgent()) ;


        dockingOperation();

        /*
        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if(msg != null){
                    System.out.println(msg.getSender() +" : aa " + msg.getContent());
                } else {
                    block();
                }
            }
        });

        // TODO: melhorar isto. Aguardar que os agentes existam em vez de usar Thread.sleep
        // Deveria estar no "StationAgent"?
        try {
            Thread.sleep(5000);
            SequentialBehaviour seq = new SequentialBehaviour();
            seq.addSubBehaviour(new BroadcastTrainArrival());
            addBehaviour(seq);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

    }


    private void dockingOperation(){
        this.addBehaviour(new OneShotBehaviour(this) {
            public void action() {
                System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent: ") +
                        "Trying dock operation at station " + TrainAgent.this.currentStation);
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("Station-service");
                template.addServices(sd);

                try {
                    DFAgentDescription[] result = DFService.search(this.myAgent, template);
                    System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent: ") +
                            "Searching in Directory Facilitator Service");
                    System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent: ") +
                            "Found the following station agents:");
                    TrainAgent.this.stationAgents = new AID[result.length];

                    for(int i = 0; i < result.length; ++i) {
                        TrainAgent.this.stationAgents[i] = result[i].getName();
                        System.out.println(TrainAgent.this.stationAgents[i].getName());
                    }
                } catch (FIPAException var5) {
                    var5.printStackTrace();
                }

                this.myAgent.addBehaviour(TrainAgent.this.new RequestDock());
            }
        });
    }

    public class RequestDock extends Behaviour {

        private AID gateAgent;
        private int repliesCnt;

        private int step, currentStation;
        private MessageTemplate mt; // template para receber respostas(replies)

        private RequestDock() {
            this.currentStation = 0;
            this.step = 0;
        }

        // neste metodo incluimos codigo referente ao comportamento a ser executado pelo agente
        public void action(){

            ACLMessage reply;

            switch (step){
                case 0:
                    // envia mensagem Station Agent pedindo plataformas livres
                    // Troca de Mensagens TrainAgent-StationAgent - Passo 1
                    ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                    // configurar como recebedor das mensagens a proxima estacao
                    cfp.addReceiver(stationAgents[currentStation]);
                    cfp.setContent(String.valueOf(TrainAgent.this.getAID().getName()));
                    cfp.setConversationId("Docking-operation");
                    cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Valor unico.
                    // envia mensagem
                    this.myAgent.send(cfp);
                    // Preparar template para ofertas de agentes
                    this.mt = MessageTemplate.and(MessageTemplate.MatchConversationId("Docking-operation"), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                    this.step = 1;
                    break;
                case 1:
                    // Receber proposal/refute dos Station Agent da Estacao corrente
                    // Passo 3   (passo 2 foi no Station.java)
                    reply = this.myAgent.receive(this.mt);
                    if(reply != null){
                        // resposta recebida
                        if(reply.getPerformative() == ACLMessage.PROPOSE){
                            // this is an offer
                            int plataformNumber = Integer.parseInt(reply.getContent());
                            gateAgent = reply.getSender();
                            String sName = reply.getSender().getName();
                            System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent: ") +
                                    "Station: "+sName+" Informa plataforma Livre: :"+plataformNumber);
                        }
                        this.step = 2;
                    } else {
                        this.block();
                    }
                    break;
                case 2:
                    // envia mensagem de aceite de volta para estacao
                    // Passo 4
                    ACLMessage docking = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    docking.addReceiver(gateAgent);
                    docking.setContent(String.valueOf(TrainAgent.this.currentStation));
                    docking.setConversationId("Docking-operation");
                    docking.setReplyWith("docking" + System.currentTimeMillis());
                    this.myAgent.send(docking);
                    this.mt = MessageTemplate.and(MessageTemplate.MatchConversationId("Docking-operation"), MessageTemplate.MatchInReplyTo(docking.getReplyWith()));
                    this.step = 3;
                    break;
                case  3:
                    // receber confimacao de docking de volta
                    reply = this.myAgent.receive(this.mt);
                    if (reply != null) {
                        if (reply.getPerformative() == ACLMessage.INFORM) {
                            // docking successfully
                            System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent: ") +
                                    "Successfully docked with agent" + reply.getSender().getName());

                        } else {
                            System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent: ") +
                                    "Attempt failed");
                            this.done();
                        }

                        this.step = 4;
                    } else {
                        this.block();
                    }
            }


        }


        // este metodo indica se o comportamento foi finalizado ou nao
        public boolean done(){
            // caseo este metodo retorne TRUE o comportamento sera finalizado
            if (this.step == 2 && gateAgent == null) {
                System.out.println("Attempt failed at Station: " + TrainAgent.this.currentStation );
            }

            return this.step == 2 && this.gateAgent == null || this.step == 4;
        }

    }

    private class informCentralAgent extends OneShotBehaviour{

        @Override
        public void action() {
            ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
            msg.addReceiver(new AID("ca@metro-system"));
            msg.setContent("showTrain");
            msg.setConversationId("inform-Control");
            //msg.setReplyWith("cfp"+System.currentTimeMillis()); // Valor unico.
            // envia mensagem
            this.myAgent.send(msg);

        }


    }

}
