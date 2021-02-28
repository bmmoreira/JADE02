package metro.gui;

import jade.core.*;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import metro.CentralControlAgent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class TASS extends JFrame implements ActionListener {
    private JPanel mainPanel = new JPanel();
    private ImagePanel imgPanel = new ImagePanel();
    private CentralControlAgent myAgent;
    private JButton startBtn;
    private JButton stopBtn;
    private String trainID;
    private JLabel imagelabel;
    private int trackState;



    public TASS(String title, CentralControlAgent a){
        super(title);
        this.createUIComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.myAgent = a;

    }

    public void showGui() {
        //this.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int)screenSize.getWidth() / 2;
        int centerY = (int)screenSize.getHeight() / 2;
        this.setLocation(centerX - this.getWidth() / 2, centerY - this.getHeight() / 2);
        this.setSize(685, 500);
        super.setVisible(true);
    }



    private void createUIComponents() {

        startBtn = new JButton("Start");
        stopBtn = new JButton("Next");
        startBtn.setPreferredSize(new Dimension(340, 30));
        stopBtn.setPreferredSize(new Dimension(340, 30));
        startBtn.addActionListener(this);
        stopBtn.addActionListener(this);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(imgPanel, BorderLayout.PAGE_START);
        mainPanel.add(startBtn, BorderLayout.LINE_START);
        mainPanel.add(stopBtn, BorderLayout.LINE_END);

        //ImageIcon image = new ImageIcon("images/train2.png");
        //imagelabel = new JLabel(image);
        //imagelabel.setBounds(125, 290, 75, 75);
        //imagelabel.repaint(25, 290, 75, 75);
        //imgPanel.add(imagelabel);




    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JButton src = (JButton) actionEvent.getSource();
        if(src.getActionCommand().equals("Start")){
            if(myAgent.track.isInitTrack()){
                System.out.println("Track Simulation already started");
            } else {
                System.out.println("Start Button - Starting Simulation");
                //imgPanel.ellipse.setFrame(315, 192, 50, 50);
                imgPanel.repaint();
                // inicia comportamento
                myAgent.switchInform = true;
                //myAgent.createAgent("t1", "metro.TrainAgent", new String[]{"3"});
                // inicia Track Status
                myAgent.track.setInitTrack(true);
            }


        } else if(src.getActionCommand().equals("Next")){
            if(myAgent.track.isInitTrack()) {
                System.out.println("Next pressed");
                imgPanel.trainMove = true;
                imgPanel.repaint();
            } else {
                System.out.println("Track has not been started");
            }
        }
    }

    public void paintrain(String trainID){
        this.trainID = trainID;
        imgPanel.trainVisible = true;
        imgPanel.repaint();
    }



    class ImagePanel extends JPanel {

        final private int teste =1;
        public boolean trainVisible = false;
        public  boolean trainMove = false;
        BufferedImage img;
        BufferedImage trainIcon;
        final public Ellipse2D ellipse;
        final public Ellipse2D ellipse2;

        final public Ellipse2D getEllipse(){
            return ellipse;
        }

        public ImagePanel() {
            setLayout(new GridBagLayout());
            ellipse = new Ellipse2D.Float(86, 195, 50, 50);
            ellipse2 = new Ellipse2D.Float(102, 347, 20, 20);

            try {
                img = ImageIO.read(new File("images/tass7.png")) ;
                trainIcon = ImageIO.read(new File("images/train2.png")) ;
            }
            catch (IOException ex) {
                System.err.println("Caught IOException: " + ex.getMessage());
            }

        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(685, 412);
        }
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(685, 412);
        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawImage(img, 0, 0, 685, 412, this);

            if(trainVisible){
                doDrawing(g);
            }


        }



        public void doDrawing(Graphics g){

            Graphics2D g2d = (Graphics2D) g.create();
            Ellipse2D plataformElipse = null;
            Ellipse2D stationElipse = null;
            g.setFont(new Font(
                    "SansSerif",
                    Font.BOLD,
                    12));
            switch(TASS.this.trackState){
                case 0:
                    g.drawImage(trainIcon, 25, 290, 75, 75, this);
                    g.drawString(TASS.this.trainID,25,390);
                    plataformElipse = new Ellipse2D.Float(102, 347, 20, 20);
                    stationElipse = new Ellipse2D.Float(86, 195, 50, 50);
                    TASS.this.trackState++;
                break;
                case 1:
                    g.drawImage(trainIcon, 245, 290, 75, 75, this);
                    g.drawString(TASS.this.trainID,225,390);
                    plataformElipse = new Ellipse2D.Float(331, 347, 20, 20);
                    stationElipse = new Ellipse2D.Float(315, 192, 50, 50);
                    TASS.this.trackState++;
                break;
                case 2:
                    g.drawImage(trainIcon, 475, 290, 75, 75, this);
                    g.drawString(TASS.this.trainID,455,390);
                    plataformElipse = new Ellipse2D.Float(553, 347, 20, 20);
                    stationElipse = new Ellipse2D.Float(538, 192, 50, 50);
            }
            g2d.setColor(Color.GREEN);
            g2d.fill(plataformElipse);
            g2d.fill(stationElipse);
            g.dispose();
            g2d.dispose();

        }


    }
}
