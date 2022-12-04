package i.am.firestartrr;

import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class EnparaLiveJsoup {

    private double usdSell;
    private double usdBuy;
    private double eurSell;
    private double eurBuy;
    private double gauSell;
    private double gauBuy;
    private double parSell;
    private double parBuy;

    public void init() {
        BufferedReader br = null;
        System.out.println("------ Enpara Live ------");
        try {
            Document doc = Jsoup.connect("https://www.qnbfinansbank.enpara.com/hesaplar/doviz-ve-altin-kurlari").get();
            Elements elements = doc.getElementsByClass("enpara-gold-exchange-rates__table-item");
            elements.forEach(element -> {
                Elements spans = element.getElementsByTag("span");
                double spanSell =
                        Double.parseDouble(spans.get(1).text().replace(" TL", "").replaceAll("\\.", "").replace(",",
                                "."));
                double spanBuy =
                        Double.parseDouble(spans.get(2).text().replace(" TL", "").replaceAll("\\.", "").replace(",",
                                "."));
                if (!element.getElementsContainingText("USD ($)").isEmpty()) {
                    usdSell = spanSell;
                    usdBuy = spanBuy;
                }
                if (!element.getElementsContainingOwnText("EUR (€)").isEmpty()) {
                    eurSell = spanSell;
                    eurBuy = spanBuy;
                }
                if (!element.getElementsContainingOwnText("Altın (gram)").isEmpty()) {
                    gauSell = spanSell;
                    gauBuy = spanBuy;
                }
                if (!element.getElementsContainingOwnText("EUR/USD Parite").isEmpty()) {
                    parSell = spanSell;
                    parBuy = spanBuy;
                }
            });
            System.out.println("-------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void print() {
        System.out.println("-------------------------");
        prettyPrintValues(usdSell, usdBuy, "usd  ");
        prettyPrintValues(eurSell, eurBuy, "eur  ");
        prettyPrintValues(parSell, parBuy, "par   ");
        prettyPrintValues(gauSell, gauBuy, "gau ");
        System.out.println("-------------------------");
    }

    private void prettyPrintValues(Double sell, Double buy, String kurAdi) {
        double diff = buy - sell;
        double diffOverBuy = diff / buy;
        System.out.println(kurAdi + BigDecimal.valueOf(buy).setScale(6, RoundingMode.HALF_UP) + " / " + kurAdi + BigDecimal.valueOf(sell).setScale(6, RoundingMode.HALF_UP) + "\t\t\t : \t makas: " + BigDecimal.valueOf(diff).setScale(6, RoundingMode.HALF_UP) + "\t makas bölü alım: " + BigDecimal.valueOf(diffOverBuy).setScale(6, RoundingMode.HALF_UP));
    }
}