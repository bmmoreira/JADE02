package metro.behaviors.train;


import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
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

    private int step, currentStation;
    private MessageTemplate mt; // template para receber respostas(replies)

    public RequestDock(TrainAgent agent) {
        this.ag = agent;
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
                cfp.addReceiver(ag.stationAgents[ag.currentStation]);
                cfp.setContent(String.valueOf(ag.getAID().getName()));
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
                        dockingplataform = Integer.parseInt(reply.getContent());
                        gateAgent = reply.getSender();
                        String sName = reply.getSender().getName();
                        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent " +
                                this.myAgent.getAID().getName()) +  ": Passo 3 - Agent Station " + sName +
                                " informa plataforma livre " + dockingplataform);
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
                docking.setContent(String.valueOf(ag.currentStation));
                docking.setConversationId("Docking-operation");
                docking.setReplyWith("docking" + System.currentTimeMillis());
                this.myAgent.send(docking);
                this.mt = MessageTemplate.and(MessageTemplate.MatchConversationId("Docking-operation"), MessageTemplate.MatchInReplyTo(docking.getReplyWith()));
                this.step = 3;
                break;
            case  3:
                // Passo 6 - receber confimacao de docking de volta
                reply = this.myAgent.receive(this.mt);
                if (reply != null) {
                    if (reply.getPerformative() == ACLMessage.INFORM) {
                        // docking successfully
                        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+
                                this.myAgent.getAID().getName()) + ": Passo 6 - Successfully docked with agent " +
                                reply.getSender().getName());
                        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+
                                this.myAgent.getAID().getName()) + ": Opening train doors at " +
                                reply.getSender().getName() + " plataform " + dockingplataform);
                        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+
                                this.myAgent.getAID().getName()) + ": Ready to passengers board at " +
                                new java.util.Date(System.currentTimeMillis()));
                        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+
                                this.myAgent.getAID().getName()) + ": " + ag.train.getTrainDefaultDockTime()+
                        " minutes to close train doors");
                    } else {
                        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+
                                this.myAgent.getAID().getName()) + "Attempt failed");
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
            System.out.println("Attempt failed at Station: " + ag.currentStation );
        }

        return this.step == 2 && this.gateAgent == null || this.step == 4;
    }

}
