/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rogerio.conversaocoordenadas.Model;

/**
 *
 * @author Rogerio
 */
public class Main {
    
    private static double a = 6378137; //semi-eixo maior
    private static double f = 1/298.257222101;
    private static double b = (a - 1) / f; //semi-eixo menor
    private static double ondulacao_geoidal = -5.03; // Valor de N em metros!Obtido em: https://ww2.ibge.gov.br/mapgeo/mapgeo.htm
    
    public static void main (String[] args) {
        System.out.println("Conversão de Coordenadas\n\n");
       
        /*Coordenadas da Estação PPTE*/
        convert_XYZ_to_LatLong(3687624.3674, -4620818.6827, -2386880.3805);
        convert_LatLong_to_XYZ(Math.toRadians(-22.1198889), Math.toRadians(-51.4085278), 431.049);
    }
    
    public static void convert_LatLong_to_XYZ(double phi, double lamb, double h) {
        double f_CALC = (a - b) / a; // C0MPARAR
        double e2 = 2 * f - f*f;
        double N = a / (Math.sqrt( 1 - e2 * (Math.sin(phi) * Math.sin(phi)))) ; //Grande Normal
        
        double X = (N + h) * Math.cos(phi) * Math.cos(lamb);
        double Y = (N + h) * Math.cos(phi) * Math.sin(lamb);
        double Z = ( (1 - e2) * N + h) * Math.sin(phi);
        
        System.out.println("\n\n ========================================================");
        System.out.println("\nX[m]: " + X);
        System.out.println("\nY[m]: " + Y);
        System.out.println("\nZ[m]: " + Z);
    }
    
    public static void convert_XYZ_to_LatLong(double X, double Y, double Z) {
        double e2 = 2 * f - f*f;
        double e_line = e2 / (1 - e2); // Segunda excentricidade       
            
        double theta = Math.atan( (Z * a) / (b * Math.sqrt(X*X + Y*Y)));
        
        double sin_theta = Math.sin(theta);
        double cos_theta = Math.cos(theta);
        
        double phi = Math.atan( (Z + e_line * b * (sin_theta * sin_theta * sin_theta)) / 
                        Math.sqrt(X*X + Y*Y) * e2 * a * (cos_theta * cos_theta * cos_theta) );
        double lamb = Math.atan(Y/X);
        
        double N = a / (Math.sqrt( 1 - e2 * (Math.sin(phi) * Math.sin(phi)))) ; //Grande Normal;
        double h = (Math.sqrt(X*X + Y*Y)/Math.cos(phi) - N);
        
        double H = calc_Alt_Orto(h);
                
        System.out.println("\nPhi   (Latitude) [rad]: " + phi); // Latitude em radianos
        System.out.println("\nLamb (Longitude) [rad]: " + lamb);// Longitude em radianos
        System.out.println("\n\nPhi  (Latitude)  [deg]: " + Math.toDegrees(phi)); // Latitude em radianos
        System.out.println(  "\nLamb (Longitude) [deg]: " + Math.toDegrees(lamb));// Longitude em radianos
        System.out.println(  "\nAlt Geométrica  (h)[m]: " + h);
        System.out.println(  "\nAlt Ortométrica (H)[m]: " + H);
    }
    
    public static double calc_Alt_Orto(double h) {
        return (h - ondulacao_geoidal);
    }
    
}
