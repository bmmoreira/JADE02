package metro;

import jade.core.*;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;
import metro.extras.Ansi;

import java.util.Random;

public class PassengerLaunchAgent extends Agent {

    Random rand = new Random();
    //public int numPassageiros = rand.nextInt(100 - 20 + 1) + 20;
    public int numPassageiros = rand.nextInt(10 - 5 + 1) + 5;

    protected void setup() {

        // obter controlador para criar agentes
        PlatformController container = getContainerController();
        // Criar N passageiros
        try {
            for (int i = 0;  i < numPassageiros;  i++) {
                String localName = "passageiro_" + i;
                AgentController passageiro = container.createNewAgent(localName, "metro.PassengerAgent", null);
                passageiro.start();
            }
            System.out.println(new Ansi(Ansi.ITALIC, Ansi.GREEN).format("Foram iniciados "
                    + (numPassageiros) + " passageiros."));
        }
        catch (Exception e) {
            System.err.println("Exceção ao adicionar passageiros: " + e );
            e.printStackTrace();
        }
    }
}
