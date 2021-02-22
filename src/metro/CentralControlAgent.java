package metro;


import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import metro.gui.TASS;
import metro.extras.Ansi;

/*
    Ideia deste agente seria estar conectado a uma sala de controle central.
    Determinar paragens totais, partidas, estacoes ativas etc..

 */

public class CentralControlAgent extends Agent{

    private TASS myGui;
    private AgentController t1 = null;

    protected void setup() {
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.BLUE).format("Central Agent: ") +
                "Central Control Agent " + this.getAID().getName() + " is ready.");
        this.myGui = new TASS("Central Train Station Control System",this);
        this.myGui.showGui();
        // Cria estacao padrao
        this.createAgent("st1", "metro.StationAgent", new String[]{"1"});



        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();

                if(msg != null){
                    if(msg.getContent().equals("showTrain")){
                        System.out.println(msg.getSender() +" : content: " + msg.getContent());
                        myGui.paintrain();
                    }
                } else {
                    block();
                }
            }
        });
    }
    public void createAgent(String name,String className,Object[]args){

        try {
            AgentContainer container = this.getContainerController();
            this.t1 = container.createNewAgent(name, className, args);
            this.t1.start();
            System.out.println(new Ansi(Ansi.ITALIC, Ansi.BLUE).format("Central Agent: ") +
                    "Initializing Agent: "+ name + " " + className);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
