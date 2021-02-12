//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package metro;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BookBuyerAgent extends Agent {
    private String targetBookTitle;
    private AID[] sellerAgents;

    public BookBuyerAgent() {
    }

    protected void setup() {
        System.out.println("Hallo! Buyer-agent " + this.getAID().getName() + " is ready.");
        Object[] args = this.getArguments();
        if (args != null && args.length > 0) {
            this.targetBookTitle = (String)args[0];
            System.out.println("Target book is " + this.targetBookTitle);
            this.addBehaviour(new TickerBehaviour(this, 60000L) {
                protected void onTick() {
                    System.out.println("Trying to buy " + BookBuyerAgent.this.targetBookTitle);
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("book-selling");
                    template.addServices(sd);

                    try {
                        DFAgentDescription[] result = DFService.search(this.myAgent, template);
                        System.out.println("Found the following seller agents:");
                        BookBuyerAgent.this.sellerAgents = new AID[result.length];

                        for(int i = 0; i < result.length; ++i) {
                            BookBuyerAgent.this.sellerAgents[i] = result[i].getName();
                            System.out.println(BookBuyerAgent.this.sellerAgents[i].getName());
                        }
                    } catch (FIPAException var5) {
                        var5.printStackTrace();
                    }

                    this.myAgent.addBehaviour(BookBuyerAgent.this.new RequestPerformer());
                }
            });
        } else {
            System.out.println("No target book title specified");
            this.doDelete();
        }

    }

    protected void takeDown() {
        System.out.println("Buyer-agent " + this.getAID().getName() + " terminating.");
    }

    private class RequestPerformer extends Behaviour {
        private AID bestSeller;
        private int bestPrice;
        private int repliesCnt;
        private MessageTemplate mt;
        private int step;

        private RequestPerformer() {
            this.repliesCnt = 0;
            this.step = 0;
        }

        public void action() {
            ACLMessage reply;
            switch(this.step) {
                case 0:
                    ACLMessage cfp = new ACLMessage(3);

                    for(int i = 0; i < BookBuyerAgent.this.sellerAgents.length; ++i) {
                        cfp.addReceiver(BookBuyerAgent.this.sellerAgents[i]);
                    }

                    cfp.setContent(BookBuyerAgent.this.targetBookTitle);
                    cfp.setConversationId("book-trade");
                    cfp.setReplyWith("cfp" + System.currentTimeMillis());
                    this.myAgent.send(cfp);
                    this.mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"), MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
                    this.step = 1;
                    break;
                case 1:
                    reply = this.myAgent.receive(this.mt);
                    if (reply != null) {
                        if (reply.getPerformative() == 11) {
                            int price = Integer.parseInt(reply.getContent());
                            if (this.bestSeller == null || price < this.bestPrice) {
                                this.bestPrice = price;
                                this.bestSeller = reply.getSender();
                            }
                        }

                        ++this.repliesCnt;
                        if (this.repliesCnt >= BookBuyerAgent.this.sellerAgents.length) {
                            this.step = 2;
                        }
                    } else {
                        this.block();
                    }
                    break;
                case 2:
                    ACLMessage order = new ACLMessage(0);
                    order.addReceiver(this.bestSeller);
                    order.setContent(BookBuyerAgent.this.targetBookTitle);
                    order.setConversationId("book-trade");
                    order.setReplyWith("order" + System.currentTimeMillis());
                    this.myAgent.send(order);
                    this.mt = MessageTemplate.and(MessageTemplate.MatchConversationId("book-trade"), MessageTemplate.MatchInReplyTo(order.getReplyWith()));
                    this.step = 3;
                    break;
                case 3:
                    reply = this.myAgent.receive(this.mt);
                    if (reply != null) {
                        if (reply.getPerformative() == 7) {
                            System.out.println(BookBuyerAgent.this.targetBookTitle + " successfully purchased from agent " + reply.getSender().getName());
                            System.out.println("Price = " + this.bestPrice);
                            this.myAgent.doDelete();
                        } else {
                            System.out.println("Attempt failed: requested book already sold.");
                        }

                        this.step = 4;
                    } else {
                        this.block();
                    }
            }

        }

        public boolean done() {
            if (this.step == 2 && this.bestSeller == null) {
                System.out.println("Attempt failed: " + BookBuyerAgent.this.targetBookTitle + " not available for sale");
            }

            return this.step == 2 && this.bestSeller == null || this.step == 4;
        }
    }
}
