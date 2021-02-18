package metro;


import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import metro.gui.TASS;
import metro.gui.TASSGui;

/*
    Ideia deste agente seria estar conectado a uma sala de controle central.
    Determinar paragens totais, partidas, estacoes ativas etc..

 */

public class CentralControlAgent extends Agent{

    private TASS myGui;

    protected void setup() {

        System.out.println("Train Agent " + this.getAID().getName() + " is ready.");
        this.myGui = new TASS("Central Train Station Control System",this);
        this.myGui.showGui();


        addBehaviour(new CyclicBehaviour(this) {
            @Override
            public void action() {
                ACLMessage msg = receive();
                if(msg != null){
                    System.out.println(msg.getSender() +" : aa" + msg.getContent());
                } else {
                    block();
                }
            }
        });
    }

}