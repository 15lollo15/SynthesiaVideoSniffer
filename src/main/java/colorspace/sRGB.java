package colorspace;

import colorspace.coordinates.data_types.CIELab;
import colorspace.coordinates.data_types.CIEXYZ;
import colorspace.coordinates.data_types.CIExyY;
import colorspace.coordinates.primaries.BT709;

import java.awt.*;

public class sRGB {

    private static final CIExyY R = BT709.red;
    private static final CIExyY G = BT709.green;
    private static final CIExyY B = BT709.blue;
    private static final CIExyY W = BT709.white;

    private sRGB() {
        // avoid instantiation
    }

    /**
     * Calculate eye perceptual difference between
     * two colors using Delta E 1976 standard.
     *
     * This is the less accurate Delta E of the four available
     * (Delta E 1976, 1994, 2000, CMC) but is also the faster
     */
    public static double deltaE76(CIELab lab1, CIELab lab2) {
        return Math.sqrt(square(lab1.L - lab2.L) +
                square(lab1.a - lab2.a) + square(lab1.b - lab2.b));
    }

    /**
     * Calculate eye perceptual difference between
     * two colors using Delta E 1994 standard.
     *
     * This more accurate than Delta E 1976.
     */
    public static double deltaE94(CIELab lab1, CIELab lab2) {
        double K1 = 0.045;
        double K2 = 0.015;

        double deltaA = lab1.a - lab2.a;
        double deltaB = lab1.b - lab2.b;

        double C1 = Math.sqrt(square(lab1.a) + square(lab1.b));
        double C2 = Math.sqrt(square(lab2.a) + square(lab2.b));

        double deltaL = lab1.L - lab2.L;
        double deltaC = C1 - C2;
        double deltaH_square = square(deltaA) + square(deltaB) - square(deltaC);

        double Kl = 1;
        double Kc = 1;
        double Kh = 1;

        double Sl = 1;
        double Sc = 1 + K1 * C1;
        double Sh = 1 + K2 * C1;

        return Math.sqrt(
                square(deltaL / (Kl * Sl)) + square(deltaC / (Kc * Sc)) + deltaH_square / square(Kh * Sh)
        );
    }

    public static double deltaE2000(CIELab lab1, CIELab lab2) {

        final double _L = (lab1.L + lab2.L) / 2.0;

        final double C1 = Math.sqrt(square(lab1.a) + square(lab1.b));
        final double C2 = Math.sqrt(square(lab2.a) + square(lab2.b));
        final double C = (C1 + C2) / 2.0;

        final double G = (1 - Math.sqrt(Math.pow(C, 7)/(Math.pow(C, 7) + Math.pow(25, 7)))) / 2.0;

        final double _a1 = lab1.a * (1 + G);
        final double _a2 = lab2.a * (1 + G);

        final double _C1 = Math.sqrt(square(_a1) + square(lab1.b));
        final double _C2 = Math.sqrt(square(_a2) + square(lab2.b));

        final double _C = (_C1 + _C2) / 2.0;

        double _h1 = Math.atan2(lab1.b, _a1);
        if (_h1 < 0)
            _h1 += Math.toRadians(360);

        double _h2 = Math.atan2(lab2.b, _a2);
        if (_h2 < 0)
            _h2 += Math.toRadians(360);

        double _H;
        if (Math.abs(_h1 - _h2) > Math.toRadians(180))
            _H = (_h1 + _h2 + Math.toRadians(360)) / 2.0;
        else
            _H = (_h1 + _h2) / 2.0;

        final double T = 1 - 0.17*Math.cos(_H - Math.toRadians(30)) + 0.24*Math.cos(2*_H)
                + 0.32*Math.cos(3*_H + Math.toRadians(6)) - 0.2*Math.cos(4*_H - Math.toRadians(63));

        double _deltah;
        if (Math.abs(_h2 - _h1) <= Math.toRadians(180)) {
            _deltah = _h2 - _h1;
        } else if (Math.abs(_h2 - _h1) > Math.toRadians(180) && _h2 <= _h1) {
            _deltah = _h2 - _h1 + Math.toRadians(360);
        } else {
            _deltah = _h2 - _h1 - Math.toRadians(360);
        }

        final double _deltaL = lab2.L - lab1.L;
        final double _deltaC = _C2 - _C1;
        final double _deltaH = 2 * Math.sqrt(_C1 * _C2) * Math.sin(_deltah / 2.0);

        final double Sl = 1 + (0.015 * square(_L - 50)) / (Math.sqrt(20 + square(_L - 50)));
        final double Sc = 1 + 0.045 * _C;
        final double Sh = 1 + 0.015 * _C * T;

        final double deltaTheta = 30 * Math.exp(-square((_H - Math.toRadians(-275)) / 25));

        final double Rc = 2 * Math.sqrt(Math.pow(_C, 7) / (Math.pow(_C, 7) + Math.pow(25, 7)));

        final double Rt = -Rc * Math.sin(2 * deltaTheta);

        final double Kl = 1.0;
        final double Kc = 1.0;
        final double Kh = 1.0;

        // compute the final result
        final double tmp1 = square(_deltaL / (Kl * Sl));
        final double tmp2 = square(_deltaC / (Kc * Sc));
        final double tmp3 = square(_deltaH / (Kh * Sh));
        final double tmp4 = Rt * (_deltaC / (Kc * Sc)) * (_deltaH / (Kh * Sh));

        return Math.sqrt(tmp1 + tmp2 + tmp3 + tmp4);
    }

    private static double square(double value) {
        return value * value;
    }

    public static CIELab toCIELab(Color color) {
        double red = linearize(normalize(color.getRed()));
        double green = linearize(normalize(color.getGreen()));
        double blue = linearize(normalize(color.getBlue()));

        CIEXYZ xyz = toCIEXYZ(red, green, blue);
        CIEXYZ white = W.toCIEXYZ();
        return xyz.toCIELab(white.X, white.Y, white.Z);
    }

    private static CIEXYZ toCIEXYZ(double r, double g, double b) {
        double X = 0.4124 * r + 0.3576 * g + 0.1805 * b;
        double Y = 0.2126 * r + 0.7152 * g + 0.0722 * b;
        double Z = 0.0193 * r + 0.1192 * g + 0.9505 * b;

        return new CIEXYZ(X, Y, Z);
    }

    public static double normalize(int value) {
        return value / 255.0;
    }

    public static double denormalize(double value) {
        return value * 255.0;
    }

    public static double linearize(double value) {
        return inverseCompanding(value);
    }

    public static double delinearize(double value) {
        return companding(value);
    }

    private static double inverseCompanding(double value) {
        if (value <= 0.04045) {
            return value / 12.92;
        } else {
            return Math.pow((value + 0.055) / 1.055, 2.4);
        }
    }

    private static double companding(double value) {
        if (value <= 0.0031308) {
            return 12.92 * value;
        } else {
            return 1.055 * Math.pow(value, 1.0 / 2.4) - 0.055;
        }
    }

}
