package hu.beadando;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class ArfolyamFeldolgozo {

    public static List<Arfolyam> parse(String xml) throws Exception {
        List<Arfolyam> points = new ArrayList<>();
        if (xml == null || xml.isBlank()) {
            return points;   // ha nincs adat
        }

        // XML beolvasása
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));


        NodeList days = doc.getElementsByTagName("Day");
        for (int i = 0; i < days.getLength(); i++) {
            Element day = (Element) days.item(i);
            String date = day.getAttribute("date");

            NodeList rates = day.getElementsByTagName("Rate");
            if (rates.getLength() == 0) {
                continue;
            }
            Element rate = (Element) rates.item(0);
            int unit = Integer.parseInt(rate.getAttribute("unit"));
            // az MNB tizedesvessző, Java tizedespont
            double value = Double.parseDouble(rate.getTextContent().trim().replace(',', '.'));

            points.add(new Arfolyam(date, unit, value));
        }

        points.sort(Comparator.comparing(Arfolyam::getDatum));
        return points;
    }
}
