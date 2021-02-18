package metro.gui;

import metro.CentralControlAgent;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TASS extends JFrame implements ActionListener {
    private JPanel mainPanel = new JPanel();
    private ImagePanel imgPanel = new ImagePanel();
    private CentralControlAgent myAgent;
    private JButton startBtn;
    private JButton stopBtn;



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
        stopBtn = new JButton("Stop");
        startBtn.setPreferredSize(new Dimension(340, 30));
        stopBtn.setPreferredSize(new Dimension(340, 30));
        startBtn.addActionListener(this);
        stopBtn.addActionListener(this);
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(imgPanel, BorderLayout.PAGE_START);
        mainPanel.add(startBtn, BorderLayout.LINE_START);
        mainPanel.add(stopBtn, BorderLayout.LINE_END);


    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JButton src = (JButton) actionEvent.getSource();
        if(src.getActionCommand().equals("Start")){
            System.out.println("Start pressed");
            imgPanel.ellipse.setFrame(315, 225, 50, 50);
            imgPanel.repaint();
        } else if(src.getActionCommand().equals("Stop")){
            System.out.println("Stop pressed");
        }
    }

    class ImagePanel extends JPanel {

        final private int teste =1;

        Image img;
        final public Ellipse2D ellipse;

        final public Ellipse2D getEllipse(){
            return ellipse;
        }

        public ImagePanel() {
            setLayout(new GridBagLayout());
            ellipse = new Ellipse2D.Float(45, 225, 50, 50);
            //add(new JButton("Test Button"));

            //img = new ImageIcon("images/tass2.png");

            try {
                img = ImageIO.read(new File("images/tass2.png")) ;
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
            //g.setColor(Color.RED);
            //g.fillOval(45, 225, 50, 50);
            doDrawing(g);

        }

        private void doDrawing(Graphics g){
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.GREEN);
            g2d.fill(ellipse);
            g2d.dispose();
        }


    }
}
