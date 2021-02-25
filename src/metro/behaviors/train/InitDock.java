package metro.behaviors.train;

import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import metro.TrainAgent;
import metro.extras.Ansi;

public class InitDock extends OneShotBehaviour {

    private TrainAgent ag;

    public InitDock(TrainAgent agent) {
        this.ag = agent;

    }

    @Override
    public void action() {
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+ this.myAgent.getAID().getName()) +
                ": trying dock operation at station " + ag.currentStation);
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Station-service");
        template.addServices(sd);

        try {
            DFAgentDescription[] result = DFService.search(this.myAgent, template);
            System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+ this.myAgent.getAID().getName()) +
                    ": searching in Directory Facilitator Service");
            System.out.println(new Ansi(Ansi.ITALIC, Ansi.YELLOW).format("Train Agent "+ this.myAgent.getAID().getName()) +
                    ": Found the following station agents:");
            ag.stationAgents = new AID[result.length];

            for(int i = 0; i < result.length; ++i) {
                ag.stationAgents[i] = result[i].getName();
                System.out.println(ag.stationAgents[i].getName());
            }
        } catch (FIPAException var5) {
            var5.printStackTrace();
        }

        this.myAgent.addBehaviour(new metro.behaviors.train.RequestDock((TrainAgent) this.myAgent));
    }
}
