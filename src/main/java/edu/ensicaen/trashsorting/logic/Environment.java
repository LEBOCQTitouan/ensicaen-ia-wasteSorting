package edu.ensicaen.trashsorting.logic;

import edu.ensicaen.trashsorting.logic.agent.WasteSeparationAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Observable;
import java.util.Random;

/**
 * An environment that contains wastes and agents.
 */
public class Environment extends Observable {
    private static Environment instance_;
    public static final int nbTypesOfWaste = 4;
    public Random generator_;
    protected double width_;
    protected double height_;
    protected int iterations_ = 0;

    public ArrayList<Waste> wastes_;
    public ArrayList<WasteSeparationAgent> agents_;

    private Environment() {
        wastes_ = new ArrayList();
        agents_ = new ArrayList();
        generator_ = new Random();
    }

    public static Environment getInstance() {
        if (instance_ == null) {
            instance_ = new Environment();
        }
        return instance_;
    }

    public void initialize(int nbWastes, int nbAgents, double width, double height, int nbTypesOfWaste) {
        width_ = width;
        height_ = height;
        wastes_.clear();
        Random generator = new Random();
        for (int i = 0; i < nbWastes; i++) {
            Waste waste = new Waste(
                    generator_.nextDouble() * width_,
                    generator_.nextDouble() * height_,
                    generator_.nextInt(nbTypesOfWaste),
                    generator_.nextInt(3) + 1
            );
            wastes_.add(waste);
        }
        agents_.clear();
        for (int i = 0; i < nbAgents; i++) {
            WasteSeparationAgent agent = new WasteSeparationAgent(generator_.nextDouble() * width_, generator_.nextDouble() * height_);
            agents_.add(agent);
        }
    }

    /**
     * Drops off a waste on the environment.
     *
     * @param waste The waste to drop off.
     */
    public void dropOff(Waste waste) {
        if (wastes_.contains(waste)) {
            waste.increaseSize();
            return;
        }
        wastes_.add(waste);
    }

    /**
     * Picks up from the environment a waste if its size is equals to 1 or a
     * part of it otherwise.
     *
     * @param waste The waste to pick up.
     * @return The waste that has been picked up.
     */
    public Waste pickUp(Waste waste) {
        if (waste.size_ == 1) {
            wastes_.remove(waste);
            return waste;
        } else {
            waste.decreaseSize();
            return new Waste(waste);
        }
    }

    public void update() {
        for (WasteSeparationAgent agent : agents_) {
            agent.updateDirectionAndDecide(wastes_);
            agent.updatePosition();
        }

        iterations_++;
        if (iterations_ % 500 == 0) {
            Collections.reverse(wastes_);
        }

        setChanged();
        notifyObservers();
    }
}
