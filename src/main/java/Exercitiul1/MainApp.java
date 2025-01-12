package Exercitiul1;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class MainApp {
    public static void main(String[] args) {
        try {
            // Configurarea ObjectMapper pentru a lucra cu LocalDate
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            // Citirea din fișierul JSON
            List<Angajat> angajati = Arrays.asList(mapper.readValue(new File("src/main/resources/angajati.json"), Angajat[].class));

            // Cerința 1: Afișarea listei de angajați folosind referințe la metode
            angajati.forEach(System.out::println);

            // Cerința 2: Afișarea angajaților cu salariul peste 2500
            angajati.stream()
                    .filter(angajat -> angajat.getSalariu() > 2500)
                    .forEach(System.out::println);

            // Cerința 3: Angajații din luna aprilie a anului trecut cu funcție de conducere
            int currentYear = LocalDate.now().getYear();
            List<Angajat> conducereAprilie = angajati.stream()
                    .filter(angajat -> angajat.getPost().toLowerCase().contains("sef") ||
                            angajat.getPost().toLowerCase().contains("director"))
                    .filter(angajat -> angajat.getDataAngajarii().getYear() == currentYear - 1 &&
                            angajat.getDataAngajarii().getMonthValue() == 4)
                    .collect(Collectors.toList());
            conducereAprilie.forEach(System.out::println);

            // Cerința 4: Angajații fără funcție de conducere, ordonați descrescător după salariu
            angajati.stream()
                    .filter(angajat -> !angajat.getPost().toLowerCase().contains("sef") &&
                            !angajat.getPost().toLowerCase().contains("director"))
                    .sorted(Comparator.comparing(Angajat::getSalariu).reversed())
                    .forEach(System.out::println);

            // Cerința 5: Numele angajaților în majuscule
            angajati.stream()
                    .map(angajat -> angajat.getNume().toUpperCase())
                    .collect(Collectors.toList())
                    .forEach(System.out::println);

            // Cerința 6: Salariile mai mici de 3000 RON
            angajati.stream()
                    .filter(angajat -> angajat.getSalariu() < 3000)
                    .map(Angajat::getSalariu)
                    .forEach(System.out::println);

            // Cerința 7: Primul angajat al firmei (minimul după data angajării)
            Optional<Angajat> primulAngajat = angajati.stream()
                    .min(Comparator.comparing(Angajat::getDataAngajarii));
            primulAngajat.ifPresentOrElse(System.out::println, () -> System.out.println("Nu există angajați"));

            // Cerința 8: Statistici despre salarii
            DoubleSummaryStatistics statisticiSalarii = angajati.stream()
                    .collect(Collectors.summarizingDouble(Angajat::getSalariu));
            System.out.println("Salariu mediu: " + statisticiSalarii.getAverage());
            System.out.println("Salariu minim: " + statisticiSalarii.getMin());
            System.out.println("Salariu maxim: " + statisticiSalarii.getMax());

            // Cerința 9: Existența unui angajat cu numele "Ion"
            boolean areIon = angajati.stream()
                    .anyMatch(angajat -> angajat.getNume().equalsIgnoreCase("Ion"));
            System.out.println(areIon ? "Firma are cel puțin un Ion angajat" : "Firma nu are nici un Ion angajat");

            // Cerința 10: Numărul de angajați angajați vara anului trecut
            long countVara = angajati.stream()
                    .filter(angajat -> angajat.getDataAngajarii().getYear() == currentYear - 1 &&
                            (angajat.getDataAngajarii().getMonthValue() == 6 ||
                                    angajat.getDataAngajarii().getMonthValue() == 7 ||
                                    angajat.getDataAngajarii().getMonthValue() == 8))
                    .count();
            System.out.println("Numărul de angajați angajați vara anului trecut: " + countVara);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
