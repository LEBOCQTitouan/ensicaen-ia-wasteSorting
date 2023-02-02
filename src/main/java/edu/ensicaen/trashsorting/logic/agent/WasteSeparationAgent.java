package edu.ensicaen.trashsorting.logic.agent;

import edu.ensicaen.trashsorting.TrashSortingApplication;
import edu.ensicaen.trashsorting.logic.Coordinate;
import edu.ensicaen.trashsorting.logic.Environment;
import edu.ensicaen.trashsorting.logic.EnvironmentEntity;
import edu.ensicaen.trashsorting.logic.Waste;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * An agent that separates wastes.
 */
public class WasteSeparationAgent extends EnvironmentEntity {
    protected static final double INFLUENCE_DISTANCE_ = 3;
    protected static final double PROBABILITY_CHANGING_DIRECTION_ = 0.05;

    protected Waste waste_;
    protected double velocityX_;
    protected double velocityY_;
    protected boolean busy_ = false;

    private final String id;


    public WasteSeparationAgent(double posX, double posY) {
        id = UUID.randomUUID().toString();

        posX_ = posX;
        posY_ = posY;
        velocityX_ = Environment.getInstance().generator_.nextDouble() - 0.5;
        velocityY_ = Environment.getInstance().generator_.nextDouble() - 0.5;
        normalize();
    }

    public boolean isCarryingAWaste() {
        return waste_ != null;
    }

    public void updatePosition() {
        posX_ += velocityX_;
        posY_ += velocityY_;
    }

    private void chooseDirection() {
        // randomizing direction + avoiding out of bound
        Random random = new Random();
        if (random.nextDouble() % 1 <= PROBABILITY_CHANGING_DIRECTION_) {
            velocityX_ = Environment.getInstance().generator_.nextDouble() - 0.5;
            velocityY_ = Environment.getInstance().generator_.nextDouble() - 0.5;
        }
        // avoiding out of bound
        if (posY_ + velocityY_ <= 0 || posY_ + velocityY_ >= TrashSortingApplication.height) {
            velocityY_ = -velocityY_;
        }
        if (posX_ + velocityX_ <= 0 || posX_ + velocityX_ >= TrashSortingApplication.width) {
            velocityX_ = -velocityX_;
        }
    }

    public boolean canPickUpColor(ArrayList<Waste> wastes, Waste w) {
        // TODO cleaner code
        int count = 0;
        for (Waste waste : wastes) {
            if (waste.type_ == w.type_) {
                count++;
                if (count > 1)
                    return true;
            }
        }
        return count > 1;
    }

    public void makeActionNotBusy(ArrayList<Waste> wastes) {
        for (Waste w : wastes) {
            if (w.influenceArea() >= w.distanceTo(this) && canPickUpColor(wastes, w)) {
                busy_ = true;
                waste_ = Environment.getInstance().pickUp(w);
                return;
            }
        }
        return;
    }

    public void makeActionBusy(ArrayList<Waste> wastes) {
        for (Waste w : wastes) {
            if (w.influenceArea() >= w.distanceTo(this) && w.type_ == waste_.type_) {
                Environment.getInstance().dropOff(w);
                busy_ = false;
                waste_ = null;
                return;
            }
        }
        return;
    }

    public void makeAction(ArrayList<Waste> wastes) {
        if (!busy_) {
            makeActionNotBusy(wastes);
            return;
        }
        makeActionBusy(wastes);
        return;
    }

    public void updateDirectionAndDecide(ArrayList<Waste> wastes) {
        chooseDirection();
        makeAction(wastes);
        normalize();
        return;
    }

    protected void normalize() {
        double length;

        length = Math.sqrt(velocityX_ * velocityX_ + velocityY_ * velocityY_);
        velocityX_ /= length;
        velocityY_ /= length;
    }

    @Override
    public String toString() {
        return "WasteSeparationAgent{" +
                "id=" + id +
                ", waste_=" + waste_ +
                ", posX_=" + posX_ +
                ", posY_=" + posY_ +
                '}';
    }
}
