package project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SzukajURL {
    public static void main(String[] args) {

        List<String> listaStron = new ArrayList<>();
        iloscStron(listaStron);
        try {
            urlRead(listaStron);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void iloscStron(List<String> listaStron){
        String Marka,Miasto;
        Integer IloscStron;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj Marke pojazdu");
        Marka = scanner.next().toString().toLowerCase();
        System.out.println("Podaj Szukane Miasto");
        Miasto = scanner.next().toString().toLowerCase();
        System.out.println("Podaj ilosc stron do wyszukania");
        IloscStron = scanner.nextInt();
        for(int i=1;i<10;i++) {
            listaStron.add("https://www.olx.pl/motoryzacja/samochody/"+Marka+"/"+Miasto+"/"+"/?page="+i);
        }
        listaStron.stream().forEach(System.out::println);
    }
    public static void urlRead(List<String> listaStron) throws IOException {
    Long Start = System.currentTimeMillis();
        for (int j = 1; j < listaStron.size(); j++) {
            URL oracle = new URL(listaStron.get(j).toString());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(oracle.openStream()));

            //Przechwytywanie calego tekstu
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                stringBuilder.append(inputLine);
                stringBuilder.append(System.lineSeparator());
            }
            in.close();

            Set<String> listaOgłoszen = new LinkedHashSet<>();
            String linki = stringBuilder.toString();

            //szukanie ogloszen z otomoto
            for (int i = 0; i < linki.length(); i++) {
                i = linki.indexOf("https://www.otomoto.pl/oferta/", i);
                if (i < 0) break;
                String substring = linki.substring(i);
                String s = substring.split(".html")[0];
                String s2 = s.replace(s,s+".html");
                listaOgłoszen.add(s2);
            }
            //szukanie ogloszen z olx
            for (int i = 0; i < linki.length(); i++) {
                i = linki.indexOf("https://www.olx.pl/d/oferta/", i);
                if (i < 0) break;
                String substring = linki.substring(i);
                String s = substring.split(".html")[0];
                s.replace(s,s+".html");
                String s2 = s.replace(s,s+".html");
                listaOgłoszen.add(s2);
            }
            //sortowanie listy ogłoszen i cen
            List<String> listaOgloszenICen = new ArrayList<>();
            for (int i = 0; i < linki.length(); i++) {
                i = linki.indexOf("<strong>", i);
                if (i < 0) break;
                String substring = linki.substring(i);
                String s = substring.split("</strong>")[0];
                listaOgloszenICen.add(s);
            }

            List<String> listaCen = new ArrayList();
            List<String> ListaOpisow = new ArrayList();

            for (int i = 1; i < (listaOgloszenICen.size() - 3); i++) {
                if (i % 2 != 0) {
                    ListaOpisow.add(listaOgloszenICen.get(i).substring(8).toString());
                } else {
                    listaCen.add(listaOgloszenICen.get(i).substring(8).toString());

                }
            }
        DisplayAnswer(listaOgłoszen,listaCen,ListaOpisow);
        }
        Long End = System.currentTimeMillis();
        System.out.println(End-Start);
    }
    public static void DisplayAnswer(Set<String> listaOgłoszen,List<String> listaCen,List<String> ListaOpisow){
        List<String> NowaListaOgloszen = new ArrayList<>(listaOgłoszen);
        System.out.println("\n\n");
        for (int i = 0; i < listaOgłoszen.size(); i++) {
            System.out.println(NowaListaOgloszen.get(i)+" || " + listaCen.get(i) + " || " + ListaOpisow.get(i));
        }
        System.out.println("\n\n");
    }
}