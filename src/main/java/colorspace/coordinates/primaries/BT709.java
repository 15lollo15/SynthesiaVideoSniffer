package colorspace.coordinates.primaries;

import colorspace.coordinates.ReferenceWhite;
import colorspace.coordinates.data_types.CIExyY;

public class BT709 {

    public static final CIExyY red = new CIExyY(0.64, 0.33, 0.2126);
    public static final CIExyY green = new CIExyY(0.30, 0.60, 0.7152);
    public static final CIExyY blue = new CIExyY(0.15, 0.06, 0.0722);
    public static final CIExyY white = ReferenceWhite.D65;

    private BT709() {
        // avoid instantiation
    }

}
