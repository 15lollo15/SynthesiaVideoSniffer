package colorspace.coordinates.data_types;

public class CIExyY implements ChromaticityCoord {

    public final double x;
    public final double y;
    public final double Y;
    public final double z;

    public CIExyY(double x, double y, double Y) {
        this.x = x;
        this.y = y;
        this.Y = Y;
        this.z = 1 - x - y;
    }

    @Override
    public CIEXYZ toCIEXYZ() {
        return new CIEXYZ(
                (x * Y) / y,
                Y,
                (z * Y) / y
        );
    }

    @Override
    public CIExyY toCIExyY() {
        return this;
    }

    @Override
    public CIELab toCIELab(double Xw, double Yw, double Zw) {
        throw new UnsupportedOperationException("not yet implemented");
    }

}
