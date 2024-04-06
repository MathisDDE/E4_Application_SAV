package cours;


import java.util.Date;

public class code {
    
    public static void main(String argv[]) {
        Date d = new Date();
        System.out.println("La date est : " + d);
        System.out.println(calcule(2, 56));
        System.out.println(calculeair(230, 3232));
        System.out.println(calculecirconference(5));


        Maclass moninstance1 = new Maclass();
        System.out.println(moninstance1.compteur2);

        Maclass moninstance2 = new Maclass();
        System.out.println(moninstance2.compteur3);

         Maclass moninstance3 = new Maclass();
        System.out.println(moninstance3.nombre1);
    }
        



    // ceci est la methode calculeperimetre
    public static double calcule(double longueur, double largeur) {
        double c = 0;
        c = 2 * (longueur + largeur);

        System.out.println("le resultat est perimetre :" + c);
        return c;
// ceci est la methode calculeairrectnagle
    }

    public static double calculeair(double longueur, double largeur) {
        double air = 0;
        air = longueur * largeur;
        System.out.println("le resultat est air :" + air);
        return air;
    }

    public static double calculecirconference(double rayon) {
        double circonference = 0;
        circonference = Math.PI * 2 * rayon;
        System.out.println("le resultat est Circonference :" + circonference);
        return circonference;


    }
}