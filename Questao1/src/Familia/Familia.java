/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Familia;



/**
 *
 * @author  Kimberly Scaldaferro Colodeti
 */
public class Familia {
    
    public static void main(String[] args) {
        
      //CRIA CONTA JUNTO DA Familia
      final Conta conta = new Conta(75);
      
      
      //CRIA FAMILIARES E LHES INFORMA A CONTA CONJUNTA
      Acao AGastadora = new Acao("AGastadora ",3000, 10, conta);//10
      Acao AEsperta = new Acao("AEsperta ",6000, 50, conta);//50
      Acao AEconomica = new Acao("AEconomica ",12000, 5, conta);//5
      Acao APatrocinadora = new Acao("APatrocinadora", 0, 100, conta);
        
      // INICIA AS THREADS
      AGastadora.start();
      AEsperta.start();
      AEconomica.start();
      APatrocinadora.start();
      
  }
}
