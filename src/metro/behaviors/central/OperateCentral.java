package metro.behaviors.central;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
import metro.CentralControlAgent;
import metro.extras.Ansi;

public class OperateCentral extends TickerBehaviour {

    CentralControlAgent ag;
    private String className;
    private String agentType;
    private String  myName;

    public OperateCentral(CentralControlAgent a, long period) {
        super(a, period);
        ag = a;
        this.className = getClass().getName();
        this.agentType = "Central Control Agent ";
    }

    @Override
    protected void onTick() {

        this.myName= myAgent.getAID().getName();

        printLogHead(": Operational...");

        if(ag.switchInform){
            System.out.println("Testando...switch");
            this.myAgent.addBehaviour(new metro.behaviors.central.InformAgent());
            ag.switchInform = false;
        }

    }
    private void printLog(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.BLUE).format(agentType +
                myName) + text);
    }
    private void printLogHead(String text){
        System.out.println(new Ansi(Ansi.ITALIC, Ansi.BLUE).format(agentType +
                myName) + text + " " + new Ansi(Ansi.BACKGROUND_BLUE, Ansi.BLACK).format("Class: " + className));
    }

}
