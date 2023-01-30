package edu.ensicaen.trashsorting.logic;

/**
 * An entity within an environment (i.e. an agent or a waste)
 */
public class EnvironmentEntity {
    public double posX_;
    public double posY_;

    /**
     * Creates a default entity at the origin.
     */
    public EnvironmentEntity() {}

    /**
     * Creates a new entity at a given position.
     * @param x The abscisse of the position.
     * @param y The ordinate of the position.
     */
    public EnvironmentEntity(double x, double y) {
        posX_ = x;
        posY_ = y;
    }

    /**
     * Calculates the distance between the current entity and the given one.
     * @param e The given entity from which we calculate the distance.
     * @return The distance between the current entity and the given one
     */
    public double distanceTo(EnvironmentEntity e) {
        return Math.sqrt((e.posX_ - posX_) * (e.posX_ - posX_) + (e.posY_ - posY_) * (e.posY_ - posY_));
    }

    /**
     * Calculates the squared distance between the current entity and the given
     * one.
     * @param e The given entity from which we calculate the squared distance.
     * @return The squared distance between the current entity and the given one
     */
    public double squaredDistanceTo(EnvironmentEntity e) {
        return (e.posX_ - posX_) * (e.posX_ - posX_) + (e.posY_ - posY_) * (e.posY_ - posY_);
    }
}