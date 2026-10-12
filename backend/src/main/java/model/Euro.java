package model;
import java.text.NumberFormat;
import java.util.Locale;


public class Euro {
    int euro;
    int cent;
    @Override
    public String toString(){
        return (euro + ":" + cent + "$");
    }   
    public static String formatEuro(double euro) {
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.GERMANY);
    return nf.format(euro);
}
}
