package Controlador;

import java.math.BigInteger;
import java.util.Arrays;

public class Probabilitat {

    public static BigInteger variacionsAmbRepeticio(int m,int n){
        return BigInteger.valueOf(((int) Math.pow(m, n)));
    }

    public static BigInteger variacions(int m,int n){
        return optimitzate(m,m-n);
    }

    public static BigInteger combinacionsAmbRepeticio(int m,int n){
        return combinacions(m+n-1,n);
    }

    public static BigInteger combinacions(int m,int n){
        return variacions(m,n).divide(fact(BigInteger.valueOf(n)));
    }

    public static BigInteger permutacions(int m){
        return variacions(m,m);
    }

    public static BigInteger permutacionsAmbRepeticio(int m, int[] n){
        Arrays.sort(n);
        BigInteger aux  = BigInteger.valueOf(1);
        for(int i = 0; i < n.length - 1;i++){
            aux = aux.multiply(fact(BigInteger.valueOf(n[i])));
        }
        return variacions(m,n[n.length - 1]).divide(aux);
    }

    //Calcula el factorial d'un integer
    private static BigInteger fact(BigInteger n){
        return n.equals(BigInteger.valueOf(1)) ?  BigInteger.valueOf(1) : fact(n.subtract(BigInteger.valueOf(1))).multiply(n);
    }

    //Calcula el factorial simplificat. Si es necesita dividir 10!/8! aquesta fucio obté (10,8) i només calcula 10*9
    private static BigInteger optimitzate(int top,int bottom){
        BigInteger aux = BigInteger.valueOf(1);
        for(int i = top; i > bottom; i--){
            aux = aux.multiply(BigInteger.valueOf(i));
        }
        return aux;
    }

}
