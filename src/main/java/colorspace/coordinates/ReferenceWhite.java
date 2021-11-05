package colorspace.coordinates;

import colorspace.coordinates.data_types.CIExyY;

public class ReferenceWhite {

    public static final CIExyY D65 = new CIExyY(0.3127, 0.3290, 1);

    private ReferenceWhite() {
        // avoid instantiation
    }

}
