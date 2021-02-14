package metro;

import jade.core.*;
import jade.wrapper.AgentController;
import jade.wrapper.PlatformController;

public class PassengerLauncherAgent extends Agent {

    public static int NUM_PASSAGEIROS = 500;

    protected void setup() {

        // obter controlador para criar agentes
        PlatformController container = getContainerController();
        // Criar N passageiros
        try {
            for (int i = 0;  i < NUM_PASSAGEIROS;  i++) {
                String localName = "passageiro_" + i;
                AgentController passageiro = container.createNewAgent(localName, "metro.PassengerAgent", null);
                passageiro.start();
            }
        }
        catch (Exception e) {
            System.err.println("Exceção ao adicionar passageiro: " + e );
            e.printStackTrace();
        }
    }
}
