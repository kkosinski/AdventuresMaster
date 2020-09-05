package com.wintermute.adventuresmaster.services.light;

import android.graphics.Color;

/**
 * Helper class to translate color to hue bridge standards.
 *
 * @author wintermute
 */
public class ColorHelper
{
    /**
     * @param colorTarget color value to translate to XY coordinates
     * @return XY Color coordinates which is hue standard.
     */
    public static double[] extractHueColorCoordinates(int colorTarget)
    {
        Color target = Color.valueOf(colorTarget);
        float[] colors = {target.red(), target.green(), target.blue()};

        for (int i = 0; i < colors.length; i++)
        {
            colors[i] = (colors[i] / 255) > 0.04045 ? (float) Math.pow(((colors[i] / 255) + 0.055) / (1.0 + 0.055), 2.4)
                                                    : (float) (colors[i] / 255 / 12.92);
        }

        double xRaw = (float) (colors[0] * 0.649926 + colors[1] * 0.103455 + colors[2] * 0.197109);
        double yRaw = (float) (colors[0] * 0.234327 + colors[1] * 0.743075 + colors[2] * 0.022598);
        double zRaw = (float) (colors[0] * 0.0000000 + colors[1] * 0.053077 + colors[2] * 1.035763);

        return new double[] {(xRaw / (xRaw + yRaw + zRaw)), (yRaw / (xRaw + yRaw + zRaw))};
    }
}
