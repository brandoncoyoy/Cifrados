/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cifradorsa;

import java.util.ArrayList;

/**
 *
 * @author brandon
 */
public class CifradoMD5 {
    public CifradoMD5()
    {
        
    }
    private String Completar(String cadena,int bits)
    {
        String nuevo=cadena;
        while(nuevo.length()<bits)
            nuevo="0"+nuevo;
        return nuevo;
    }
    private String Rellenar(String cadena)   
    {
        //Cambiar a forma binaria
        String c = "";
        for(int i=0; i<cadena.length();i++)
        {
            c+=Completar(Integer.toBinaryString( cadena.charAt(i)),8);
        }
        //Completar para que sea multiplo de 512
        int longitud = c.length();
        c+="1";
        while((c.length()+64)%512!=0)
            c+="0";
        c+=Completar(Integer.toBinaryString((int)(longitud%Math.pow(2, 64))),64);
        return c;
    }
    private ArrayList<Integer> Bloques(String cadena)
    {
        String c = Rellenar(cadena);
        ArrayList<Integer> bloques = new ArrayList<Integer>();
        for(int i=0;i<c.length();i+=32)
        {
            char[] sub = new char[32];
            c.getChars(i, i+32, sub, 0);
            Integer n = StringBinaryInteger(sub);
            bloques.add(n);
        }
        return bloques;
    }
    private int StringBinaryInteger(char[] cadena)    
    {
        char[] v = new char[cadena.length-1];
        for(int i=1;i<cadena.length;i++)
            v[i-1] = cadena[i];
        int valor = Integer.parseInt(String.valueOf(v),2);
        if(cadena[0]=='1')
        {
            int signo = 1073741824;
            valor = valor|Integer.rotateLeft(signo, 1);
        }
        return valor;
    }
    private int Rotar(int n,int c)
    {
        return Integer.rotateLeft(n, c);
    }
    public String MD5(String cadena)
    {
        //Declaracion de valores de preronda
        int[] s= new int[]{7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22,  7, 12, 17, 22
        , 5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20,  5,  9, 14, 20
        ,4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23,  4, 11, 16, 23 
        ,6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21,  6, 10, 15, 21 };
        //Declaracion de constantes
        int[] K = new int[]{0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee
        ,0xf57c0faf, 0x4787c62a, 0xa8304613, 0xfd469501
        ,0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be
        ,0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821
        ,0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa
        ,0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8
        ,0x21e1cde6, 0xc33707d6, 0xf4d50d87, 0x455a14ed
        ,0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a
        ,0xfffa3942, 0x8771f681, 0x6d9d6122, 0xfde5380c
        ,0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70
        ,0x289b7ec6, 0xeaa127fa, 0xd4ef3085, 0x04881d05
        ,0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665
        ,0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039
        ,0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1
        ,0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1
        ,0xf7537e82, 0xbd3af235, 0x2ad7d2bb, 0xeb86d391};
        //Obtencio de bloques de 32 bits
        ArrayList<Integer> bloques = Bloques(cadena);
        ArrayList<Integer> grupo = new ArrayList<Integer>();
        for(int j=0;j<16;j++)
            grupo.add(0);
        //Iniciar variables
        int a0 = 0x67452301;
        int b0 = 0xefcdab89;
        int c0 = 0x98badcfe;
        int d0 = 0x10325476;
        for(int i=0; i<bloques.size()/16;i++)
        {
           for(int j=0;j<16;j++)
               grupo.set(j, bloques.get(j+16*i));
           int A =a0;
           int B =b0;
           int C =c0;
           int D =d0;
           int F=0;
           int g=0;
           for(int j=0;j<64;j++)
           {
               if(j<=15)
               {
                   F = D^(B&(C^D));
                   g =j;
               }
               else if(j<=31)
               {
                   F = C^(D&(B^C));
                   g = (5*j+1)%16;
               }
               else if(j<=47)
               {
                   F = B^C^D;
                   g = (3*j+5)%16;
               }
               else if(j<=63)
               {
                   F = C^(B|~D);
                   g = (7*j)%16;
               }
               int t=D;
               D =C;
               C =B;
               B = B+Rotar(A+F+K[j]+grupo.get(g),s[j]);
               A =t;
           }
           a0 = a0|A;
           b0 = b0|B;
           c0 = c0|C;
           d0 = d0|D;
        }
        return Integer.toHexString(a0)+" "+Integer.toHexString(b0)+" "+Integer.toHexString(c0)+" "+Integer.toHexString(d0);
    }
    
}
