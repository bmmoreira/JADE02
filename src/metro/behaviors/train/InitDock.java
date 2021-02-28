package metro.behaviors.train;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import metro.TrainAgent;
import metro.extras.Ansi;

public class InitDock extends OneShotBehaviour {

    private TrainAgent ag;
    private String myName;
    //private int step = 0;

    public InitDock(TrainAgent agent) {
        this.ag = agent;

    }

    @Override
    public void action() {
        this.myName= myAgent.getAID().getName();
        String className = getClass().getName();

        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Docking-operation");
        template.addServices(sd);

        try {
            DFAgentDescription[] result = DFService.search(this.myAgent, template);
            System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+ myName) +
                    ": Searching in Directory Facilitator Service "+
                    new Ansi(Ansi.BACKGROUND_YELLOW, Ansi.BLACK).format("Train Agent "+ className));

            System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+ myName) +
                    ": Found the following station agents:");
            ag.stationAgents = new AID[result.length];
            int stationCount = 0;
            for(int i = 0; i < result.length; ++i) {
                ag.stationAgents[i] = result[i].getName();
                System.out.println(ag.stationAgents[i].getName());
                stationCount++;
            }
            System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+ myName) +
                    ": Number of enabled stations: " + stationCount);
        } catch (FIPAException var5) {
            var5.printStackTrace();
        }

        //for(int i = 0; i<ag.stationAgents.length;i++) {

        //}
        //this.myAgent.addBehaviour(new metro.behaviors.train.RequestDock((TrainAgent) this.myAgent));





    }



}
