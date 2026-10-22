package hu.beadando;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

// React menü
@RestController
@RequestMapping("/api")
class FoglalkozasRestController {
    private final FoglalkozasRepository foglalkozasRepo;
    private final MegyeRepository megyeRepo;

    FoglalkozasRestController(FoglalkozasRepository foglalkozasRepo, MegyeRepository megyeRepo) {
        this.foglalkozasRepo = foglalkozasRepo;
        this.megyeRepo = megyeRepo;
    }

    // lenyíló listához és a megyenevek kiírásához
    @GetMapping("/megyek")
    Iterable<Megye> megyek() {
        return megyeRepo.findAll();
    }

    // Read: az összes sor
    @GetMapping("/foglalkozasok")
    Iterable<Foglalkozas> olvasMind() {
        return foglalkozasRepo.findAll();
    }

    // Read: egy sor
    @GetMapping("/foglalkozasok/{fkod}")
    Foglalkozas olvasEgy(@PathVariable Integer fkod) {
        return foglalkozasRepo.findById(fkod)
                .orElseThrow(() -> nemTalalhato(fkod));
    }

    // Create új sor
    @PostMapping("/foglalkozasok")
    @ResponseStatus(HttpStatus.CREATED)
    Foglalkozas uj(@RequestBody Foglalkozas uj) {
        ellenoriz(uj);
        int maxKod = 100;
        for (Foglalkozas f : foglalkozasRepo.findAll()) {
            maxKod = Math.max(maxKod, f.getFkod());
        }
        uj.setFkod(maxKod + 1);
        return foglalkozasRepo.save(uj);
    }

    // Update
    @PutMapping("/foglalkozasok/{fkod}")
    Foglalkozas modosit(@RequestBody Foglalkozas adat, @PathVariable Integer fkod) {
        ellenoriz(adat);
        Foglalkozas f = foglalkozasRepo.findById(fkod)
                .orElseThrow(() -> nemTalalhato(fkod));
        f.setMegyekod(adat.getMegyekod());
        f.setMezogazdasag(adat.getMezogazdasag());
        f.setIpar(adat.getIpar());
        f.setSzolgaltatas(adat.getSzolgaltatas());
        return foglalkozasRepo.save(f);
    }

    // Delete
    @DeleteMapping("/foglalkozasok/{fkod}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void torol(@PathVariable Integer fkod) {
        if (!foglalkozasRepo.existsById(fkod)) {
            throw nemTalalhato(fkod);
        }
        foglalkozasRepo.deleteById(fkod);
    }

    // ellenőrzés
    private void ellenoriz(Foglalkozas f) {
        if (f.getMegyekod() == null || !megyeRepo.existsById(f.getMegyekod())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nincs ilyen megye!");
        }
        if (f.getMezogazdasag() < 0 || f.getIpar() < 0 || f.getSzolgaltatas() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A létszám nem lehet negatív!");
        }
    }

    private ResponseStatusException nemTalalhato(Integer fkod) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, "Nincs ilyen sor: " + fkod);
    }
}
