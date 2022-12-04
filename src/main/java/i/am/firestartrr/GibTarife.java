package i.am.firestartrr;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public class GibTarife {
    public static List<GelirVergisiDilimi> init() {
        List<GelirVergisiDilimi> gelirVergisiDilimiList = new ArrayList<>();
        gelirVergisiDilimiList.add(new GelirVergisiDilimi(32000d, 0d, 0d, 0.15d));
        gelirVergisiDilimiList.add(new GelirVergisiDilimi(70000d, 32000d, 4800d, 0.20d));
        gelirVergisiDilimiList.add(new GelirVergisiDilimi(170000d, 70000d, 12400d, 0.27d));
        gelirVergisiDilimiList.add(new GelirVergisiDilimi(880000d, 170000d, 39400d, 0.35d));
        gelirVergisiDilimiList.add(new GelirVergisiDilimi(999999999d, 880000d, 287900d, 0.40d));
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
