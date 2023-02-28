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

    public static HashMap<Integer, Waste> depositPoints;

    protected Waste waste_;
    protected double velocityX_;
    protected double velocityY_;

    private final String id;

    public WasteSeparationAgent(double posX, double posY) {
        id = UUID.randomUUID().toString();

        depositPoints = new HashMap<>();

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
            if (!depositPoints.containsValue(w) && distanceTo(w) < minDistance) {
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

    public void makeActionBusy(ArrayList<Waste> wastes) {
        Waste depositPoint = depositPoints.get(waste_.getType());
        if (depositPoint == null)
            return;
        if (depositPoint.distanceTo(this) <= depositPoint.influenceArea()) {
            depositPoint.increaseSize();
            waste_ = null;
            return;
        }
        moveTowards(depositPoint.posX_, depositPoint.posY_);
    }

    private void updateDepositPoints(ArrayList<Waste> wastes) {
        if (depositPoints.keySet().size() >= Environment.nbTypesOfWaste)
            return;

        for (Waste w : wastes) {
            if (depositPoints.keySet().size() >= Environment.nbTypesOfWaste)
                return;
            if (!depositPoints.containsKey(w.getType())) {
                depositPoints.put(w.getType(), w);
            }
        }
    }

    public void makeAction(ArrayList<Waste> wastes) {
        updateDepositPoints(wastes);
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
