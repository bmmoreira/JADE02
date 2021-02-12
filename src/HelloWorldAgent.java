import jade.core.Agent;
import jade.lang.acl.ACLMessage;
import jade.core.behaviours.CyclicBehaviour;

public class HelloWorldAgent extends Agent{

    protected void setup() {
        System.out.println("Hello world! I'm an agent!");
        System.out.println("My local name is " + getAID().getLocalName());
        System.out.println("My GUID is " + getAID().getName());
        System.out.println("My addresses are " + String.join(",", getAID().getAddressesArray()));

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msgRx = receive();
                if (msgRx != null) {
                    System.out.println(msgRx);
                    ACLMessage msgTx = msgRx.createReply();
                    msgTx.setContent("Hello!");
                    send(msgTx);
                } else {
                    block();
                }
            }
        });



    }


}
