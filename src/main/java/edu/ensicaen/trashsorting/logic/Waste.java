package edu.ensicaen.trashsorting.logic;

import java.util.UUID;

public class Waste extends EnvironmentEntity {

    protected final static double PICKUP_DECAY_RATE_ = 0.6;
    /** The type of the waste */
    public final int type_;
    /** The size of the waste */
    protected int size_ = 1;

    private String id;

    public Waste(double _posX, double _posY, int _type, int size_) {
        id = UUID.randomUUID().toString();

        type_ = _type;
        posX_ = _posX;
        posY_ = _posY;
        this.size_ = size_;
    }

    public Waste(double _posX, double _posY, int _type) {
        this(_posX, _posY, _type, 1);
    }

    public Waste(Waste waste) {
        posX_ = waste.posX_;
        posY_ = waste.posY_;
        type_ = waste.type_;
    }

    public int influenceArea() {
        return 10 + 10 * (size_ - 1);
    }

    public void increaseSize() {
        size_++;
    }

    public void decreaseSize() {
        size_--;
    }

    public double probabilityToTake() {
        return Math.pow(PICKUP_DECAY_RATE_, size_ - 1);
    }

    public int getType() {
        return type_;
    }
    public int getSize() {
        return size_;
    }

    @Override
    public String toString() {
        return "Waste{" +
                "id=" + id +
                ", type_=" + type_ +
                ", size_=" + size_ +
                ", posX_=" + posX_ +
                ", posY_=" + posY_ +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Waste)) {
            return false;
        }
        Waste other = (Waste) obj;
        return other.id != null && other.id.equals(this.id);
    }
}
