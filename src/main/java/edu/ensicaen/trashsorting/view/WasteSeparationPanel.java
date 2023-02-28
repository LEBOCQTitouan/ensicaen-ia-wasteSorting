package edu.ensicaen.trashsorting.view;

import edu.ensicaen.trashsorting.TrashSortingApplication;
import edu.ensicaen.trashsorting.logic.Environment;
import edu.ensicaen.trashsorting.logic.Waste;
import edu.ensicaen.trashsorting.logic.agent.WasteSeparationAgent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.Timer;

/**
 * Panel to display the simulation of waste separation.
 */
public class WasteSeparationPanel extends JPanel implements Observer, MouseListener {
    public static int refreshRate = 1;
    public final static Color SADDLEBROWN = new Color(0.5450981f, 0.2705882f, 0.07450981f);
    public final static Color SANDYBROWN = new Color(0.9568627f, 0.643137276f,0.3764706f);
    public final static Color MEDIUMORCHID = new Color(0.7294118f, 0.33333334f, 0.827451f);

    public final static Color YELLOWGREEN = new Color(0.6039216f, 0.8039216f, 0.1960784f);
    public final static Color ORANGERED = new Color(1.0f, 0.27058825f, 0.0f);
    public final static Color OLIVEDRAB = new Color(0.41960785f, 0.5568628f, 0.1372549f);

    private Timer timer_;
    private Environment environment_;
    private boolean running_ = false;

    public WasteSeparationPanel() {
        setBackground(Color.WHITE);
        addMouseListener(this);
    }

    public void run() {
        environment_ = Environment.getInstance();
        environment_.initialize(100, 5, getWidth(), getHeight(), Environment.nbTypesOfWaste);
        environment_.addObserver(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        TimerTask task;
        if (running_) {
            // We stop the timer
            timer_.cancel();
            timer_ = null;
            running_ = false;

            return;
        }

        // We run the timer
        timer_ = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                environment_.update();
            }
        };
        timer_.scheduleAtFixedRate(task, 0, refreshRate);
        running_ = true;
        return;
    }
    @Override
    public void mousePressed(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent e) { }
    @Override
    public void mouseEntered(MouseEvent e) { }
    @Override
    public void mouseExited(MouseEvent e) { }

    @Override
    public void update(Observable o, Object arg) {
        int agents = 0;
        repaint();
    }

    public void drawAgent(WasteSeparationAgent agent, Graphics g) {
        if (agent.isCarryingAWaste()) {
            g.setColor(SADDLEBROWN);
        } else {
            g.setColor(SANDYBROWN);
        }
        g.fillRect((int) agent.posX_ - 1, (int) agent.posY_ - 1, 7, 7);
    }

    public void drawWaste(Waste waste, Graphics g) {
        // Choose a color according to the type of waste
        Color color;
        int area;

        switch(waste.type_) {
            case 1 :
                color = MEDIUMORCHID;
                break;
            case 2 :
                color = ORANGERED;
                break;
            case 3:
                color = YELLOWGREEN;
                break;
            default :
                color = OLIVEDRAB;
        }
        g.setColor(color);
        // A square to display the waste itself
        g.fillRect((int) waste.posX_ - 1, (int) waste.posY_ - 1, 3, 3);

        // and a circular transparent region for its influence area
        float[] rgb = { 1.0f, 1.0f, 1.0f };
        color.getRGBColorComponents(rgb);
        color = new Color(rgb[0], rgb[1], rgb[2], 0.6f);
        g.setColor(color);
        area = waste.influenceArea();
        g.fillOval((int) waste.posX_ - area, (int) waste.posY_ - area,
                area * 2,
                area * 2);
    }

    private int numberOfAgents() {
        return environment_.agents_.size();
    }

    private int numberOfAgentsCarryingAWaste() {
        int nb = 0;
        for (WasteSeparationAgent agent: environment_.agents_) {
            if (agent.isCarryingAWaste()) {
                nb++;
            }
        }
        return nb;
    }

    private int numberOfWastes() {
        return environment_.wastes_.size();
    }

    public void drawStatistics(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, 200, 22);
        g.drawString("Number of agents: " + numberOfAgents() + "(" + numberOfAgentsCarryingAWaste() + ")", 10, 11);
        g.drawString("Number of wastes: " + numberOfWastes(), 10, 21);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (WasteSeparationAgent agent: environment_.agents_) {
            drawAgent(agent, g);
        }
        for (Waste waste : environment_.wastes_) {
            drawWaste(waste, g);
        }
        drawStatistics(g);
    }
}
