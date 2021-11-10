package colorspace.coordinates.data_types;

public interface ChromaticityCoord {

    CIELab toCIELab(double Xw, double Yw, double Zw);
    CIExyY toCIExyY();
    CIEXYZ toCIEXYZ();

    static double valueInRange(double value, double min, double max) {
        if (value < min) {
            System.out.println("value outside range: " + value);
            return min;
        }
        if (value > max) {
            System.out.println("value outside range: " + value);
            return max;
        }
        return value;
    }

}
