package edu.ensicaen.trashsorting.logic.agent;

import edu.ensicaen.trashsorting.TrashSortingApplication;
import edu.ensicaen.trashsorting.logic.Waste;

import java.util.HashMap;

public class DepositPoint {
    private static final HashMap<Integer, DepositPoint> depositPoints = new HashMap<>();
    public static final int x = TrashSortingApplication.width / 2;
    public static final int y = TrashSortingApplication.height / 2;
    private Waste origin;

    private DepositPoint(Waste origin) {
        this.origin = origin;
    }

    public static DepositPoint getDepositPoint(int type) {
        if (!depositPoints.containsKey(type)) {
            return null;
        }
        return depositPoints.get(type);
    }

    public static void createDepositPoint(Waste origin) {
        if (depositPoints.containsKey(origin.getType())) {
            return;
        }
        depositPoints.put(origin.getType(), new DepositPoint(origin));
    }

    public Waste getOrigin() {
        return origin;
    }

    public static boolean isDepositPoint(Waste waste) {
        DepositPoint depositPoint = depositPoints.get(waste.getType());
        if (depositPoint == null) {
            return false;
        }
        Waste origin = depositPoints.get(waste.getType()).origin;
        return origin.equals(waste);
    }
}
