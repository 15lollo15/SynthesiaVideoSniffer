package colorspace.coordinates.data_types;

public class CIEXYZ implements ChromaticityCoord {

    public final double X;
    public final double Y;
    public final double Z;

    public CIEXYZ(double X, double Y, double Z) {
        this.X = X;
        this.Y = ChromaticityCoord.valueInRange(Y, 0.0, 1.0);
        this.Z = Z;
    }

    @Override
    public CIEXYZ toCIEXYZ() {
        return this;
    }

    @Override
    public CIExyY toCIExyY() {
        return new CIExyY(
                X / (X + Y + Z),
                Y / (X + Y + Z),
                Y
        );
    }

    @Override
    public CIELab toCIELab(double Xw, double Yw, double Zw) {
        double xw = X / Xw;
        double yw = Y / Yw;
        double zw = Z / Zw;

        double fx, fy, fz;

        if (xw > CIELab.e) {
            fx = Math.pow(xw, 1.0 / 3.0);
        } else {
            fx = (CIELab.k * xw + 16) / 116;
        }

        if (yw > CIELab.e) {
            fy = Math.pow(xw, 1.0 / 3.0);
        } else {
            fy = (CIELab.k * xw + 16) / 116;
        }

        if (zw > CIELab.e) {
            fz = Math.pow(xw, 1.0 / 3.0);
        } else {
            fz = (CIELab.k * xw + 16) / 116;
        }

        double L = 116 * fy - 16;
        double a = 500 * (fx - fy);
        double b = 200 * (fy - fz);

        return new CIELab(L, a, b);
    }

}
