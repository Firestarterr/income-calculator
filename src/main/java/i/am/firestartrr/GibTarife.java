package i.am.firestartrr;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class GibTarife {
    public static List<GelirVergisiDilimi> init() {
        List<GelirVergisiDilimi> gelirVergisiDilimiList = new ArrayList<>();
        gelirVergisiDilimiList.add(new GelirVergisiDilimi(70000d, 0d, 0d, 0.15d));
        gelirVergisiDilimiList.add(new GelirVergisiDilimi(150000d, 70000d, 10500d, 0.20d));
        gelirVergisiDilimiList.add(new GelirVergisiDilimi(370000d, 150000d, 26500d, 0.27d));
        gelirVergisiDilimiList.add(new GelirVergisiDilimi(1900000d, 370000d, 85900d, 0.35d));
        gelirVergisiDilimiList.add(new GelirVergisiDilimi(999999999d, 1900000d, 621400d, 0.40d));
        return gelirVergisiDilimiList;
    }

    @Data
    @AllArgsConstructor
    public static class GelirVergisiDilimi {
        private double nin;
        private double si_icin;
        private double miktar;
        private double oran;
    }
}
