package classifier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Data for the test has been taken from the examples/ folder in the Gnip-Trend-Detection repo:
 * https://github.com/jeffakolb/Gnip-Trend-Detection/tree/92d71c3460db1482dc5bb0e640cea2d4d725e5ec/example
 */
@RunWith(JUnit4.class)
public class WeightedDataTemplatesTest implements Serializable {
    private static final String CONFIG_FILE = "/config.properties";
    private static final String SERIES_LENGTH = "seriesLength";
    private static final String REFERENCE_LENGTH = "referenceLength";
    private static final String LAMBDA = "lambda";
    private static final String BASELINE_OFFSET = "baselineOffset";
    private static final String N_SMOOTH = "nSmooth";
    private static final String ALPHA = "alpha";
    private static final double EPSILON = 0.02;
    private static final double[][] INPUT =
        new double[][] {
                {1827, 1801, 1757, 1744, 1905, 1675, 1304, 1281, 1337, 1213, 1332, 1601, 1586, 2146, 2552, 2747,
                2969, 3022, 2998, 3161, 2949, 2751, 2665, 2362, 2093, 1987, 2067, 2016, 1891, 1564, 1140, 1430, 1339,
                1217, 1498, 1548, 1690, 2070, 2398, 2590, 2634, 2668, 2672, 2692, 2750, 2534, 2439, 2221, 2066, 2190,
                1979, 1733, 1677, 1460, 1378, 1412, 1136, 1260, 1186, 1281, 1508, 1780, 2169, 2480, 2694, 2681, 2834,
                2817, 2655, 2493, 2150, 1966, 1913, 1847, 1879, 1838, 1669, 1411, 1153, 1317, 1289, 1324, 1128, 1325,
                1658, 1893, 2173, 2331, 2339, 2480, 2640, 2245, 2380, 2229, 2050, 1796, 1698, 1513, 1688, 1697, 1376,
                1250, 1033, 1101, 960, 1145, 1121, 1096, 1242, 1370, 1611, 1792, 1832, 1923, 1916, 2062, 2060, 2222,
                2158, 2007, 1881, 2036, 2215, 1824, 1596, 1611, 1192, 1174, 1151, 1270, 1310, 1340, 1813, 2010, 2175,
                2378, 2557, 2784, 2780, 2963, 2896, 3517, 2820, 2345, 2270, 2467, 2452, 2457, 2068, 1919, 1766, 1989,
                1908, 2095, 2115, 2247, 3072, 3731, 3994, 4262, 4623, 5063, 5775, 6118, 6244, 6283, 5366, 5466, 5521,
                8710, 79414, 65765, 21666, 13232, 12010, 11361, 10604, 9635, 9758, 10978, 12721, 14384, 14811, 15003,
                14795, 16058, 18830, 16626, 15553, 14308, 13438, 10643, 9513, 9244, 10347, 10007, 8376, 6973, 6287,
                5550, 5257, 5084, 4813, 5058, 5909, 6533, 7348, 7384, 7564, 7698, 7978, 8091, 8202, 7586, 7351, 6223,
                5782, 6055, 5884, 5901, 5452, 4360, 3755, 3607, 3112, 3076, 3005, 3333, 3772, 4544, 5090, 5654, 5938,
                6047, 6363, 6307, 6346, 6154, 5200, 4873, 4689, 5359, 4944, 4830, 4379, 3677, 3168, 3068, 3550, 3015,
                2930, 3008, 3718, 4011, 4316, 4627, 4911, 5015, 5334, 5391, 4832, 5569, 4842, 4481},

                {76, 91, 69, 76, 39, 36, 59, 58, 58, 66, 70, 34, 74, 180, 93, 47, 37, 26, 23, 67, 62, 270, 460,
                160, 91, 130, 110, 110, 310, 220, 330, 170, 240, 160, 90, 250, 120, 190, 170, 140, 160, 110, 97,
                180, 120, 110, 240, 180, 110, 140, 83, 49, 75, 66, 150, 170, 200, 160, 160, 200, 77, 190, 130, 210,
                170, 190, 190, 200, 160, 210, 240, 170, 82, 140, 89, 57, 77, 96, 130, 180, 130, 95, 80, 170, 84, 80,
                55, 36, 28, 40, 71, 67, 90, 170, 140, 86, 94, 110, 93, 87, 32, 27, 91, 120, 74, 170, 170, 91, 71,
                56, 120, 79, 68, 100, 110, 180, 150, 180, 280, 180, 200, 150, 120, 85, 77, 95, 190, 190, 240, 200,
                130, 110, 110, 120, 100, 66, 53, 41, 63, 120, 140, 140, 160, 73, 99, 170, 63, 60, 89, 120, 320, 180,
                140, 200, 160, 110, 170, 150, 100, 58, 44, 48, 84, 140, 100, 140, 77, 53, 110, 100, 120, 75, 62, 65,
                260, 160, 62, 83, 120, 61, 110, 150, 64, 67, 57, 66, 120, 96, 110, 180, 120, 94, 100, 79, 75, 43,
                65, 75, 110, 150, 180, 120, 140, 94, 110, 97, 86, 58, 52, 43, 230, 320, 99, 130, 470, 190, 140, 170,
                75, 56, 51, 51, 89, 110, 84, 82, 100, 85, 140, 130, 97, 14, 28, 35, 54, 130, 150, 110, 120, 45, 240,
                65, 75, 32, 16, 25, 32, 52, 80, 48, 45, 55, 160, 120, 41, 37, 16, 11, 36, 33, 64, 39, 44, 52, 280,
                84, 24, 13, 17, 130, 110, 140, 150, 120, 140, 120, 240, 120, 57, 48, 78, 57, 87, 100, 130, 93, 110,
                49, 260, 290, 190, 110, 65, 120, 160, 180, 140, 180, 140, 130, 170, 93, 57, 59, 55, 38, 72, 120, 77,
                76, 76, 67, 86, 92, 95, 140, 290, 400, 330, 370, 480, 1200, 1400, 510, 290, 750, 480, 440, 420, 340,
                440, 880, 420, 290, 190, 150, 820, 220, 160, 130, 160, 510, 320, 430, 160, 190, 90, 86, 1000, 310,
                190, 210, 88, 450, 260, 230, 130, 340, 180, 180, 1100, 430, 160, 340, 280, 350, 570, 390, 48}};

    private static final double[][] EXPECTED =
            new double[][] {
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.55, 0.54, 0.53, 0.53, 0.53, 0.53,
                0.53, 0.55, 0.57, 0.59, 0.61, 0.62, 0.64, 0.66, 0.68, 0.71, 0.73, 0.74, 0.75, 0.77, 0.82, 0.96,
                1.1, 1.1, 1.1, 1.2, 1.2, 1.2, 1.2, 1.2, 1.2, 1.3, 1.3, 1.4, 1.4, 1.4, 1.5, 1.6, 1.6, 1.7, 1.7,
                1.8, 1.8, 1.8, 1.8, 1.8, 1.9, 1.8, 1.8, 1.7, 1.7, 1.6, 1.5, 1.5, 1.4, 1.4, 1.3, 1.3, 1.3, 1.3,
                1.3, 1.1, 1, 1, 1, 1, 1, 0.99, 0.98, 0.97, 0.96, 0.93, 0.89, 0.85, 0.81, 0.77, 0.74, 0.7, 0.67,
                0.64, 0.62, 0.61, 0.61, 0.6, 0.6, 0.6, 0.59, 0.59, 0.59, 0.58, 0.57, 0.56, 0.55, 0.54, 0.53,
                0.52, 0.51, 0.5, 0.49, 0.48, 0.47, 0.46, 0.45, 0.44, 0.44, 0.43, 0.43, 0.43, 0.43, 0.43, 0.42,
                0.42, 0.42, 0.42, 0.42},

                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0.46, 0.51, 0.52, 0.52, 0.53, 0.53, 0.51,
                0.51, 0.51, 0.5, 0.48, 0.46, 0.45, 0.45, 0.46, 0.46, 0.46, 0.46, 0.45, 0.44, 0.44, 0.44, 0.44, 0.44,
                0.44, 0.47, 0.48, 0.47, 0.45, 0.46, 0.45, 0.45, 0.46, 0.46, 0.45, 0.44, 0.43, 0.44, 0.44, 0.45,
                0.47, 0.47, 0.46, 0.45, 0.45, 0.44, 0.43, 0.43, 0.43, 0.44, 0.46, 0.48, 0.48, 0.48, 0.47, 0.47,
                0.46, 0.46, 0.45, 0.45, 0.44, 0.47, 0.52, 0.51, 0.5, 0.55, 0.56, 0.55, 0.55, 0.53, 0.5, 0.49, 0.47,
                0.47, 0.47, 0.47, 0.47, 0.47, 0.47, 0.48, 0.5, 0.49, 0.48, 0.46, 0.46, 0.45, 0.46, 0.48, 0.48, 0.48,
                0.47, 0.49, 0.48, 0.47, 0.45, 0.44, 0.44, 0.43, 0.43, 0.42, 0.42, 0.41, 0.41, 0.43, 0.44, 0.43,
                0.42, 0.41, 0.4, 0.4, 0.39, 0.39, 0.38, 0.38, 0.38, 0.41, 0.41, 0.4, 0.39, 0.38, 0.39, 0.4, 0.42,
                0.43, 0.44, 0.44, 0.44, 0.47, 0.47, 0.45, 0.43, 0.42, 0.41, 0.41, 0.42, 0.43, 0.43, 0.44, 0.43,
                0.46, 0.5, 0.52, 0.51, 0.48, 0.47, 0.49, 0.51, 0.51, 0.52, 0.52, 0.52, 0.53, 0.52, 0.5, 0.49, 0.47,
                0.46, 0.46, 0.47, 0.47, 0.47, 0.46, 0.45, 0.46, 0.46, 0.47, 0.48, 0.52, 0.58, 0.61, 0.64, 0.68,
                0.76, 0.82, 0.82, 0.81, 0.86, 0.88, 0.89, 0.9, 0.89, 0.91, 0.98, 0.98, 0.97, 0.93, 0.88, 0.96, 0.94,
                0.9, 0.86, 0.83, 0.9, 0.93, 0.98, 0.94, 0.92, 0.86, 0.81, 0.91, 0.94, 0.93, 0.92, 0.86, 0.91, 0.92,
                0.93, 0.88, 0.89, 0.86, 0.82, 0.91, 0.89, 0.79, 0.78, 0.8, 0.8, 0.84, 0.86, 0.78}};

    @Test
    public void classify() throws IOException, ClassNotFoundException {
        InputStream input = getClass().getResourceAsStream(CONFIG_FILE);
        assertNotNull(input);

        Properties properties = new Properties();
        properties.load(input);
        input.close();

        for (int i = 1; i < INPUT.length; i++) {
            WeightedDataTemplates template =
                    new WeightedDataTemplates(
                            parseInt(properties.getProperty(SERIES_LENGTH)),
                            parseInt(properties.getProperty(REFERENCE_LENGTH)),
                            parseDouble(properties.getProperty(LAMBDA)),
                            new ReferenceTrends(
                                    parseInt(properties.getProperty(REFERENCE_LENGTH)),
                                    parseInt(properties.getProperty(BASELINE_OFFSET)),
                                    parseInt(properties.getProperty(N_SMOOTH)),
                                    parseDouble(properties.getProperty(ALPHA))));

            List<Double> results = new ArrayList<>();
            for (double d : INPUT[i]) {
                template.update(d);
                results.add(template.getResult());
            }

            assertEquals(results.size(), EXPECTED[i].length);

            for (int j = 0; j < results.size(); j++) {
                assertEquals(EXPECTED[i][j], results.get(j), EPSILON);
            }
        }
    }
}
