package hu.beadando;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

// React menü: induláskor feltölti az adatbázist a resources/data mappában lévő fájlokból
@Configuration
class AdatBetolto {
    private static final Logger log = LoggerFactory.getLogger(AdatBetolto.class);

    @Bean
    CommandLineRunner initDatabase(MegyeRepository megyeRepo, FoglalkozasRepository foglalkozasRepo) {
        return args -> {
            // csak üres táblát töltünk fel, különben minden indításkor felülírnánk a módosításokat
            if (megyeRepo.count() == 0) {
                for (String[] s : sorok("data/megyek.txt")) {
                    megyeRepo.save(new Megye(Integer.parseInt(s[0]), Integer.parseInt(s[1]), s[2]));
                }
                log.info("Megyék betöltve: {} sor", megyeRepo.count());
            }
            if (foglalkozasRepo.count() == 0) {
                for (String[] s : sorok("data/foglalkozasok.txt")) {
                    foglalkozasRepo.save(new Foglalkozas(Integer.parseInt(s[0]), Integer.parseInt(s[1]),
                            Integer.parseInt(s[2]), Integer.parseInt(s[3]), Integer.parseInt(s[4])));
                }
                log.info("Foglalkozások betöltve: {} sor", foglalkozasRepo.count());
            }
        };
    }

    // Beolvassa a tabulátorral tagolt fájlt; az első (fejléc) sort kihagyja
    private List<String[]> sorok(String fajl) throws IOException {
        List<String[]> eredmeny = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new ClassPathResource(fajl).getInputStream(), StandardCharsets.UTF_8))) {
            br.readLine();
            String sor;
            while ((sor = br.readLine()) != null) {
                if (!sor.isBlank()) {
                    eredmeny.add(sor.trim().split("\t"));
                }
            }
        }
        return eredmeny;
    }
}
