package edu.ensicaen.trashsorting.logic.agent;

import edu.ensicaen.trashsorting.TrashSortingApplication;
import edu.ensicaen.trashsorting.logic.Coordinate;
import edu.ensicaen.trashsorting.logic.Environment;
import edu.ensicaen.trashsorting.logic.EnvironmentEntity;
import edu.ensicaen.trashsorting.logic.Waste;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * An agent that separates wastes.
 */
public class WasteSeparationAgent extends EnvironmentEntity {
    protected Waste waste_;
    protected double velocityX_;
    protected double velocityY_;
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

    private void moveTowards(double x, double y) {
        velocityX_ = x - posX_;
        velocityY_ = y - posY_;
        normalize();
    }

    private Waste findClosestWaste(ArrayList<Waste> wastes) {
        Waste closestWaste = null;
        double minDistance = Double.MAX_VALUE;
        for (Waste w : wastes) {
            if (!DepositPoint.isDepositPoint(w) && distanceTo(w) < minDistance) {
                minDistance = distanceTo(w);
                closestWaste = w;
            }
        }
        return closestWaste;
    }

    public void makeActionNotBusy(ArrayList<Waste> wastes) {
        Waste closestWaste = findClosestWaste(wastes);
        if (closestWaste == null) {
            moveTowards(TrashSortingApplication.width/2,TrashSortingApplication.height/2);
            return;
        }

        if (closestWaste.distanceTo(this) <= closestWaste.influenceArea()) {
            waste_ = Environment.getInstance().pickUp(closestWaste);
            makeActionBusy(wastes);
            return;
        }

        moveTowards(closestWaste.posX_, closestWaste.posY_);
    }

    private void noDepositPointStrategy(ArrayList<Waste> wastes) {
        if (
                posX_ > DepositPoint.x - 1
                && posX_ < DepositPoint.x + 1
                && posY_ > DepositPoint.y - 1
                && posY_ < DepositPoint.y + 1
        ) {
            // Approximating the position for deposit point creation
            posX_ = DepositPoint.x;
            posY_ = DepositPoint.y;
            waste_.posX_ = DepositPoint.x;
            waste_.posY_ = DepositPoint.y;

            DepositPoint.createDepositPoint(waste_);
            Environment.getInstance().dropOff(waste_);
            waste_ = null;
            return;
        }
        moveTowards(DepositPoint.x, DepositPoint.y);
    }

    private void depositPointStrategy(DepositPoint depositPoint) {
        if (depositPoint.getOrigin().distanceTo(this) <= depositPoint.getOrigin().influenceArea()) {
            depositPoint.getOrigin().increaseSize();
            waste_ = null;
        }
        moveTowards(DepositPoint.x, DepositPoint.y);
    }

    public void makeActionBusy(ArrayList<Waste> wastes) {
        DepositPoint depositPoint = DepositPoint.getDepositPoint(waste_.getType());
        if (depositPoint == null) {
            noDepositPointStrategy(wastes);
            return;
        }
        depositPointStrategy(depositPoint);
    }

    public void makeAction(ArrayList<Waste> wastes) {
        if (isCarryingAWaste()) {
            makeActionBusy(wastes);
            return;
        }
        makeActionNotBusy(wastes);
    }

    public void updateDirectionAndDecide(ArrayList<Waste> wastes) {
        makeAction(wastes);
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
