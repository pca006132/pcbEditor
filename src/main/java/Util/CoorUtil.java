package Util;

/**
 * Created by pca006132 on 2016/4/28.
 */

public class CoorUtil {
    public static byte directionToCBDamage(Direction dir) {
        byte damage = (byte)0;
        switch (dir) {
            case positiveX:
                damage = 5;
                break;
            case negativeX:
                damage = 4;
                break;
            case positiveZ:
                damage = 3;
                break;
            case negativeZ:
                damage = 2;
                break;
            case positiveY:
                damage = 1;
                break;
            case negativeY:
                damage = 0;
                break;
        }
        return damage;
    }
    public static Direction CBDamageToDirection(int damage) {
        Direction dir = Direction.positiveX;
        switch (damage) {
            case 5:
                dir = Direction.positiveX;
                break;
            case 4:
                dir = Direction.negativeX;
                break;
            case 3:
                dir = Direction.positiveZ;
                break;
            case 2:
                dir = Direction.negativeZ;
                break;
            case 1:
                dir = Direction.positiveY;
                break;
            case 0:
                dir = Direction.negativeY;
                break;
        }
        return dir;
    }
    public static int[] move(int[] coor, Direction dir) {
        int[] result = coor;
        switch (dir) {
            case positiveX:
                result[0]++;
                break;
            case negativeX:
                result[0]--;
                break;
            case positiveZ:
                result[2]++;
                break;
            case negativeZ:
                result[2]--;
                break;
            case positiveY:
                result[1]++;
                break;
            case negativeY:
                result[1]--;
                break;
        }
        return result;
    }
    public static Direction inverseDir(Direction dir) {
        Direction result = dir;
        switch (dir) {
            case positiveX:
                result = Direction.negativeX;
                break;
            case negativeX:
                result = Direction.positiveX;
                break;
            case positiveZ:
                result = Direction.negativeZ;
                break;
            case negativeZ:
                result = Direction.positiveZ;
                break;
            case positiveY:
                result = Direction.negativeY;
                break;
            case negativeY:
                result = Direction.positiveY;
                break;
        }
        return result;
    }
}
