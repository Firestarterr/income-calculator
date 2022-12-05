package i.am.firestartrr;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static i.am.firestartrr.GibTarife.GelirVergisiDilimi;

public class YillikGelirGiderHesapTahminiWithData implements Constants {


    public static void main(String[] args) throws IOException, URISyntaxException {
        DecimalFormat paraFormatter = new DecimalFormat("#,###,###,##0.00");
        //inputs
        System.out.println("----- Aylik Hesap -----");
        List<AylikFatura> aylikFaturalar = OncekiFaturalar.init();
        List<AylikGider> aylikGiderler = OncekiGiderler.init();
        List<GelirVergisiDilimi> dilimler = GibTarife.init();
        double toplamGelir = 0d;
        double dusmedenToplamGelir = 0d;
        double toplamKdv = 0d;
        double dusmedenToplamKdv = 0d;
        double toplamGelirVergisi = 0d;
        double dusmedenToplamGelirVergisi = 0d;
        double vergisiOdenmemisGelir = 0d;
        double dusmedenVergisiOdenmemisGelir = 0d;
        double yillikKazanc = 0d;
        for (int i = 1; i < 13; i++) {
            AylikFatura oncekiFatura = aylikFaturalar.get(i - 1);
            AylikGider oncekiGider = aylikGiderler.get(i - 1);

            double gelir = oncekiFatura.calculateGelir(oncekiGider.getToplamKdvsiz());
            yillikKazanc += oncekiFatura.getAylikKazanc();
            double aylikKalanKdv = oncekiFatura.getKdv() - oncekiGider.getToplamKdv();

            toplamGelir += gelir;
            dusmedenToplamGelir += oncekiFatura.calculateGelir(0d);
            System.out.println(i + " ay Ele Gecen: " + paraFormatter.format(oncekiFatura.getAylikKazanc()) + "\tGelir"
                    + " Kdv: " + paraFormatter.format(oncekiFatura.getKdv()));
            System.out.println(i + " ay Gideri: " + paraFormatter.format(oncekiGider.getToplamKdvli()) + "\tGider " + "Kdv: " + paraFormatter.format(oncekiGider.getToplamKdv()));
            System.out.println(i + " ay geliri: " + paraFormatter.format(gelir) + "\tÖdenecek Kdv: " + paraFormatter.format(aylikKalanKdv));
            toplamKdv += aylikKalanKdv;
            dusmedenToplamKdv += oncekiFatura.getKdv();

            vergisiOdenmemisGelir += gelir;
            dusmedenVergisiOdenmemisGelir += oncekiFatura.getAylikKazanc();
            switch (i) {
                case 3:
                case 6:
                case 9:
                case 12:
                    double geciciGelirVergisi = vergisiOdenmemisGelir * dilimler.get(0).getOran();
                    double dusmedenGeciciGelirVergisi = dusmedenVergisiOdenmemisGelir * dilimler.get(0).getOran();
                    toplamGelirVergisi += geciciGelirVergisi;
                    dusmedenToplamGelirVergisi += dusmedenGeciciGelirVergisi;
                    System.out.println("*" + i + " ay gelir vergisi:" + paraFormatter.format(geciciGelirVergisi));
                    vergisiOdenmemisGelir = 0d;
                    dusmedenVergisiOdenmemisGelir = 0d;
            }
            System.out.println("********");
        }


        System.out.println("----- Sene Sonu Gider Hesabı -----");
        System.out.println("%0 Gider: " + paraFormatter.format((Double) aylikGiderler.stream().mapToDouble(AylikGider::getKdvsiz0).sum()) + "\t%0 Kdv: " + paraFormatter.format(0d));
        System.out.println("%1 Gider: " + paraFormatter.format((Double) aylikGiderler.stream().mapToDouble(AylikGider::getKdvli1).sum()) + "\t%1 Kdv: " + paraFormatter.format((Double) aylikGiderler.stream().mapToDouble(AylikGider::getToplam1kdv).sum()));
        System.out.println("%8 Gider: " + paraFormatter.format((Double) aylikGiderler.stream().mapToDouble(AylikGider::getKdvli8).sum()) + "\t%8 Kdv: " + paraFormatter.format((Double) aylikGiderler.stream().mapToDouble(AylikGider::getToplam8kdv).sum()));
        System.out.println("%18 Gider: " + paraFormatter.format((Double) aylikGiderler.stream().mapToDouble(AylikGider::getKdvli18).sum()) + "\t%18 Kdv: " + paraFormatter.format((Double) aylikGiderler.stream().mapToDouble(AylikGider::getToplam18kdv).sum()));
        System.out.println("Toplam Gider: " + paraFormatter.format((Double) aylikGiderler.stream().mapToDouble(AylikGider::getToplamKdvli).sum()) + "\tToplam Kdv: " + paraFormatter.format((Double) aylikGiderler.stream().mapToDouble(AylikGider::getToplamKdv).sum()));

        System.out.println("----- Sene Sonu Hesabı -----");
        System.out.println("Yıllık Brüt: " + paraFormatter.format(yillikKazanc));

        double yillikGelirVergisi = 0d;
        double dusmedenYillikGelirVergisi = 0d;
        for (GelirVergisiDilimi dilim : dilimler) {
            if (toplamGelir < dilim.getNin() && toplamGelir > dilim.getSi_icin()) {
                double kalanGelir = toplamGelir - dilim.getSi_icin();
                yillikGelirVergisi += dilim.getMiktar();
                yillikGelirVergisi += kalanGelir * dilim.getOran();
                System.out.println("Toplam Gelir: " + paraFormatter.format(toplamGelir));
                System.out.println("Yıllık Gelir Vergisi (%" + dilim.getOran() * 100 + "):" + paraFormatter.format(yillikGelirVergisi));
                System.out.println("Seneye ödenecek olan taksitler " + paraFormatter.format((yillikGelirVergisi - toplamGelirVergisi) / 2) + " x2");
                toplamGelirVergisi += (yillikGelirVergisi - toplamGelirVergisi);
            }
            if (yillikKazanc < dilim.getNin() && yillikKazanc > dilim.getSi_icin()) {
                double kalanGelir = yillikKazanc - dilim.getSi_icin();
                dusmedenYillikGelirVergisi += dilim.getMiktar();
                dusmedenYillikGelirVergisi += kalanGelir * dilim.getOran();
                System.out.println("Toplam Gelir Hiç Düşmezsen: " + paraFormatter.format(dusmedenToplamGelir));
                System.out.println("Düşmeden Yıllık Gelir Vergisi (%" + dilim.getOran() * 100 + "):" + paraFormatter.format(dusmedenYillikGelirVergisi));
                System.out.println("Düşmeden Seneye ödenecek olan taksitler " + paraFormatter.format((dusmedenYillikGelirVergisi - dusmedenToplamGelirVergisi) / 2) + " x2");
                dusmedenToplamGelirVergisi += (dusmedenYillikGelirVergisi - dusmedenToplamGelirVergisi);
            }
        }

        System.out.println("----- Hesap -----");
        System.out.println("Toplam Kazanılan: " + paraFormatter.format(yillikKazanc));
        System.out.println("Toplam Kdv: " + paraFormatter.format(toplamKdv) + "\t" + " Hiç Düşmezsen: " + paraFormatter.format(dusmedenToplamKdv));
        System.out.println("Toplam Gelir Vergisi: " + paraFormatter.format(toplamGelirVergisi) + "\t" + " Hiç " +
                "Düşmezsen: " + paraFormatter.format(dusmedenToplamGelirVergisi));
        double eldeKalanPara = yillikKazanc - toplamKdv - toplamGelirVergisi;
        System.out.println("Elde Kalan Para Yillik: " + paraFormatter.format(eldeKalanPara));
        System.out.println("Elde Kalan Para Aylik: " + paraFormatter.format(eldeKalanPara / 12));
    }

    @Data
    @AllArgsConstructor
    public static class AylikFatura {
        private double kdvliFaturaToplamTutar;
        private double kdvsizFaturaToplamTutar;

        public double getKdv() {
            return getKdvliFaturaToplamTutar() * kdv18 / (100 + kdv18);
        }

        public double getKdvliFaturaKdvDahilToplamTutar() {
            return getKdvliFaturaToplamTutar();
        }

        public double getAylikKazanc() {
            return getKdvliFaturaKdvDahilToplamTutar() + getKdvsizFaturaToplamTutar();
        }

        public double calculateGelir(double gider) {
            double gelir = getKdvliFaturaKdvDahilToplamTutar() - gider;
            if (gelir > 0) {
                return gelir + getKdvsizFaturaToplamTutar() / 2;
            } else {
                return (getKdvsizFaturaToplamTutar() + gelir) / 2;
            }
        }
    }

    public static class OncekiFaturalar {
        public static List<AylikFatura> init() throws IOException, URISyntaxException {
            List<AylikFatura> oncekiFaturalar = new ArrayList<>();
            EnparaLiveJsoup enparaLive = new EnparaLiveJsoup();
            enparaLive.init();
            enparaLive.print();
            double dovizKuru = (enparaLive.getUsdBuy() + enparaLive.getUsdSell()) / 2;

            Path path =
                    Paths.get(Objects.requireNonNull(YillikGelirGiderHesapTahminiWithData.class.getClassLoader().getResource("oncekiFaturalar.txt")).toURI());
            try (Stream<String> stream = Files.lines(path)) {
                for (String line : stream.collect(Collectors.toList())) {
                    String[] split = line.split("\t");
                    oncekiFaturalar.add(new AylikFatura(Double.parseDouble(split[1].replace("₺", "").replace(",", "")), Double.parseDouble(split[3].replace("₺", "").replace(",", ""))));
                }
            }
            while (oncekiFaturalar.size() < 12) {
                oncekiFaturalar.add(new AylikFatura((aylikTahminiFatura * (kdv18 + 100) / 100),
                        aylikTahminiKdvsizFatura * dovizKuru));
            }
            return oncekiFaturalar;
        }
    }

    @Data
    @AllArgsConstructor
    public static class AylikGider {
        private double toplamTutar;
        private double toplam1kdv;
        private double toplam8kdv;
        private double toplam18kdv;
        //        private double gidereTabii;

        public double getKdvsiz0() {
            return getToplamTutar() - getKdvli1() - getKdvli8() - getKdvli18();
        }

        public double getKdvsiz1() {
            return getToplam1kdv() * 100 / kdv1;
        }

        public double getKdvli1() {
            return getToplam1kdv() * (kdv1 + 100) / kdv1;
        }

        public double getKdvsiz8() {
            return getToplam8kdv() * 100 / kdv8;
        }

        public double getKdvli8() {
            return getToplam8kdv() * (kdv8 + 100) / kdv8;
        }

        public double getKdvsiz18() {
            return getToplam18kdv() * 100 / kdv18;
        }

        public double getKdvli18() {
            return getToplam18kdv() * (kdv18 + 100) / kdv18;
        }

        public double getToplamKdv() {
            return getToplam1kdv() + getToplam8kdv() + getToplam18kdv();
        }

        public double getToplamKdvsiz() {
            return getKdvsiz0() + getKdvsiz1() + getKdvsiz8() + getKdvsiz18();
        }

        public double getToplamKdvli() {
            return toplamTutar;
        }

    }

    public static class OncekiGiderler {
        public static List<AylikGider> init() throws IOException, URISyntaxException {
            List<AylikGider> oncekiGiderler = new ArrayList<>();

            Path path =
                    Paths.get(Objects.requireNonNull(YillikGelirGiderHesapTahminiWithData.class.getClassLoader().getResource("oncekiGiderler.txt")).toURI());
            try (Stream<String> stream = Files.lines(path)) {
                for (String line : stream.collect(Collectors.toList())) {
                    String[] split = line.split("\t");
                    oncekiGiderler.add(new AylikGider(Double.parseDouble(split[0]), Double.parseDouble(split[3]),
                            Double.parseDouble(split[1]), Double.parseDouble(split[2])));
                }
            }
            while (oncekiGiderler.size() < 12) {
                oncekiGiderler.add(new AylikGider((aylikTahmini0Gider + aylikTahmini1Gider + aylikTahmini8Gider + aylikTahmini18Gider), (aylikTahmini1Gider * kdv1 / (100 + kdv1)), (aylikTahmini8Gider * kdv8 / (100 + kdv8)), (aylikTahmini18Gider * kdv18 / (100 + kdv18))));
            }
            return oncekiGiderler;
        }
    }

}
