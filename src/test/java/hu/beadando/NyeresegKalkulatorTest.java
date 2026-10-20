package hu.beadando;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NyeresegKalkulatorTest {

    private static final double DELTA = 1e-6;   // double összehasonlítás tűrése
    private NyeresegKalkulator kalkulator;

    @BeforeEach
    void setUp() {
        kalkulator = new NyeresegKalkulator();
    }

    @Test
    @DisplayName("Long pozíció nyereséggel (EUR_USD, 10000 egység)")
    void testLongNyereseg() {
        assertEquals(75.0, kalkulator.nyeresegLong(10000, 1.0850, 1.0925), DELTA);
    }

    @Test
    @DisplayName("Long pozíció veszteséggel (USD_CHF, 3000 egység)")
    void testLongVeszteseg() {
        assertEquals(-13.5, kalkulator.nyeresegLong(3000, 0.8920, 0.8875), DELTA);
    }

    @Test
    @DisplayName("Short pozíció nyereséggel: esik az ár (GBP_USD, 5000 egység)")
    void testShortNyereseg() {
        assertEquals(35.0, kalkulator.nyeresegShort(5000, 1.2720, 1.2650), DELTA);
    }

    @Test
    @DisplayName("Short pozíció veszteséggel: emelkedik az ár (USD_JPY, 10000 egység)")
    void testShortVeszteseg() {
        assertEquals(-25000.0, kalkulator.nyeresegShort(10000, 150.00, 152.50), DELTA);
    }

    @Test
    @DisplayName("Árelmozdulás pipben, nem JPY-pár (1 pip = 0.0001)")
    void testPipNemJpy() {
        assertEquals(75.0, kalkulator.pipKulonbseg(1.0850, 1.0925, false), DELTA);
    }

    @Test
    @DisplayName("Árelmozdulás pipben, JPY-pár (1 pip = 0.01)")
    void testPipJpy() {
        assertEquals(170.0, kalkulator.pipKulonbseg(149.50, 151.20, true), DELTA);
    }

    @Test
    @DisplayName("Százalékos változás végtelen tizedes törttel")
    void testSzazalekosValtozas() {
        assertEquals(-0.454545, kalkulator.szazalekosValtozas(1.1000, 1.0950), DELTA);
    }

    @Test
    @DisplayName("0 egység kivételt dob")
    void testNullaEgyseg() {
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> kalkulator.nyeresegLong(0, 1.1000, 1.1050));
        assertEquals("Az egység csak pozitív egész szám lehet!", e.getMessage());
    }

    @Test
    @DisplayName("Negatív egység kivételt dob")
    void testNegativEgyseg() {
        assertThrows(IllegalArgumentException.class,
                () -> kalkulator.nyeresegShort(-100, 1.1000, 1.1050));
    }

    @Test
    @DisplayName("0 vagy negatív ár kivételt dob")
    void testNemPozitivAr() {
        assertThrows(IllegalArgumentException.class, () -> kalkulator.pipKulonbseg(0, 1.1050, false));
        assertThrows(IllegalArgumentException.class, () -> kalkulator.szazalekosValtozas(1.1000, -1.0));
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // Határérték-tesztek (jegyzet 65. o.)

    @Test
    @DisplayName("Változatlan ár: a nyereség 0 Long és Short esetén is")
    void testValtozatlanAr() {
        assertEquals(0.0, kalkulator.nyeresegLong(1000, 1.1000, 1.1000), DELTA);
        assertEquals(0.0, kalkulator.nyeresegShort(1000, 1.1000, 1.1000), DELTA);
    }

    @Test
    @DisplayName("Long és Short ugyanarra az ármozgásra pontosan ellentétes eredményt ad")
    void testLongShortEllentetes() {
        double longPl = kalkulator.nyeresegLong(100000, 1.1000, 1.0950);
        double shortPl = kalkulator.nyeresegShort(100000, 1.1000, 1.0950);
        assertEquals(-500.0, longPl, DELTA);
        assertEquals(500.0, shortPl, DELTA);
        assertEquals(0.0, longPl + shortPl, DELTA);
    }

    @Test
    @DisplayName("Legkisebb érvényes egység (1) és tört pip (1.2 pip)")
    void testLegkisebbErtekek() {
        assertEquals(0.00012, kalkulator.nyeresegLong(1, 1.10000, 1.10012), DELTA);
        assertEquals(1.2, kalkulator.pipKulonbseg(1.10000, 1.10012, false), DELTA);
    }
}