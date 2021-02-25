package metro;


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
import metro.behaviors.train.RequestDock;
import metro.extras.Ansi;

public class TrainAgent extends Agent {

    private int stations;
    public int currentStation = 0;
    public AID[] stationAgents;
    public Train train;



    protected void setup() {

        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+this.getAID().getName()) +
                 " is ready.");
        Object[] args = this.getArguments();
        if (args != null && args.length > 0) {
            // set max numbers of station to course
            stations = Integer.valueOf(args[0].toString()) ;
        }

        this.train = new Train(this.getAID());
        /*
         *  Aporta na primeira estacao e faz troca complexa
         *  de mensagens entre Agente de Comboio e Agente de Estacao
         */
        addBehaviour(new metro.behaviors.train.InitDock(this));

        /*
         *   Envia mensagem para Controle Central informando train docked
         *  Controle Central mostra comboio na estacao na GUI
         */
        addBehaviour(new metro.behaviors.train.InformCentralAgent("trainDock"));



        // TODO: melhorar isto. Aguardar que os agentes existam em vez de usar Thread.sleep
        // Deveria estar no "StationAgent"?
        /*
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


}
