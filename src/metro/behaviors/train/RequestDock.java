package metro.behaviors.train;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import metro.Station;
import metro.StationAgent;
import metro.Train;
import metro.TrainAgent;
import metro.extras.Ansi;

/**
 *  Train dock Operations...
 *
 */
public class RequestDock extends Behaviour {

    private AID gateAgent;
    private TrainAgent ag;
    private int dockingplataform;
    private int repliesCnt;
    private int step, currentStation;
    private String  myName;
    private String className;
    private String agentType;

    private MessageTemplate mt; // template para receber respostas(replies)

    public RequestDock(TrainAgent agent) {
        this.ag = agent;
        this.currentStation = 0;
        this.step = 0;
        this.repliesCnt = 0;
    }

    // neste metodo incluimos codigo referente ao comportamento a ser executado pelo agente
    public void action(){
        this.myName= myAgent.getAID().getName();
        this.className = getClass().getName();
        this.agentType = "Train Agent ";

        ACLMessage reply;

        switch (step){
            case 0:
                // envia mensagem Station Agent pedindo plataformas livres
                // Troca de Mensagens TrainAgent-StationAgent - Passo 1
                ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
                // configurar como recebedor das mensagens a proxima estacao
               // for(int i = 0; i < ag.stationAgents.length; ++i) {
               //     cfp.addReceiver(ag.stationAgents[i]);
                //}
                cfp.addReceiver(ag.stationAgents[ag.currentStation]);

                cfp.setContent(String.valueOf(ag.getAID().getName()));
                cfp.setConversationId("Docking-operation");
                cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Valor unico.
                // envia mensagem
                printLogHead( ": Passo 1 - Requesting dock operation at station " + ag.currentStation + " - ACLMessage.CFP - ");
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
                        dockingplataform = Integer.parseInt(reply.getContent());
                        gateAgent = reply.getSender();
                        String sName = reply.getSender().getName();

                        printLogHead( ": Passo 3 - Agent Station " + sName + " informa plataforma livre " + dockingplataform);

                    }

                    //++this.repliesCnt;
                    //if (this.repliesCnt >= ag.stationAgents.length) {
                        this.step = 2;
                    //}
                } else {
                    this.block();
                }
                break;
            case 2:
                // envia mensagem de aceite de volta para estacao
                // Passo 4
                ACLMessage docking = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                docking.addReceiver(gateAgent);
                docking.setContent(String.valueOf(ag.currentStation));
                docking.setConversationId("Docking-operation");
                docking.setReplyWith("docking" + System.currentTimeMillis());

                printLog( ": Passo 4 - Enviando ACCEPT_PROPOSAL para " + gateAgent.getName());

                this.myAgent.send(docking);
                this.mt = MessageTemplate.and(MessageTemplate.MatchConversationId("Docking-operation"), MessageTemplate.MatchInReplyTo(docking.getReplyWith()));
                this.step = 3;
                break;
            case  3:
                // Passo 6 - receber confimacao de docking de volta
                reply = this.myAgent.receive(this.mt);
                if (reply != null) {
                    if (reply.getPerformative() == ACLMessage.INFORM) {
                        String senderName = reply.getSender().getName();
                        String plataformLoad = reply.getContent();

                        printLogHead(": Passo 6 - Successfully docked with agent " + senderName);
                        printLog(": Successfully docked with agent " + senderName + " Opening train doors on plataform " + dockingplataform);
                        printLog(": Plataform on " + senderName + " with load number of " + plataformLoad + " passengers");
                        printLog(": Ready to board passengers at " + new java.util.Date(System.currentTimeMillis()));
                        printLog(": " + ag.train.getTrainDefaultDockTime() + " minutes to close train doors");


                        // informa ao Central Control Agent
                        ag.addBehaviour(new metro.behaviors.train.InformCentralAgent(senderName));
                    } else {
                        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format(agentType +
                                myName) + "Attempt failed");
                        this.done();
                    }

                    this.step = 4;

                    ag.currentStation++;
                } else {
                    this.block();
                }
        }


    }


    // este metodo indica se o comportamento foi finalizado ou nao
    public boolean done(){
        // caseo este metodo retorne TRUE o comportamento sera finalizado
        if (this.step == 2 && gateAgent == null) {
            System.out.println("Attempt failed at Station: " + ag.currentStation );
        }

        return this.step == 2 && this.gateAgent == null || this.step == 4;
    }

    private void printLog(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format(agentType +
                myName) + text);
    }
    private void printLogHead(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format(agentType +
                myName) + text + " " + new Ansi(Ansi.BACKGROUND_YELLOW, Ansi.BLACK).format("Class: " + className));
    }
}
