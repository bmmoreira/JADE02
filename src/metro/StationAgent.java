package metro;

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

    private String agentName;
    private AID[] trainAgents;
    public Station station;
    public int freePlataform; // which plataform is free - 2 is full

    // Put agent initializations here
    protected void setup() {
        Object[] args;

        try {
            args = this.getArguments();
            this.agentName = getAID().getName();
            System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent "+ agentName)
                    + ": " + " is ready.");
            System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Station Agent "+ agentName) +
                    ": " + (String) args[0] + " initialized.");
            if (args != null && args.length > 0) {

                int sNumber = Integer.valueOf((String) args[0]);
                //cria uma station com duas plataformas para teste
                station = new Station(sNumber, 2);
                // para teste vamos colocar a plataforma 1 livre(0)
                station.setPlataformStatus(0, 1);
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            System.out.println("Error1: No Station number in arguments");
        } catch (Exception e) {
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

        this.addBehaviour(new metro.behaviors.station.OfferDocking(this));
        this.addBehaviour(new metro.behaviors.station.DockServer(this));

    }


}

