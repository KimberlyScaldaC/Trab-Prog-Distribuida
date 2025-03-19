/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Familia;

/**
 *
 * @author Kimberly Scaldaferro Colodeti
 */
public class Acao extends Thread {
    
  private Conta conta = null;
  private int valorSaque = 0;
  private int tempo = 0;
  public String NomeThread;
  
  public Acao(String NomeThread,int tempo, int valorSaque , Conta conta){
    super(NomeThread);
    this.tempo = tempo;
    this.valorSaque = valorSaque;
    this.conta = conta;
  }
  
  public String getNomeThread(){
    return NomeThread;
  }
  
  @SuppressWarnings("null")
  public void run(){
    int total = 0;
//    while(conta.saque(conta,valorSaque)){;
//      total += valorSaque;
//    }
    // Enquanto houver saldo suficiente
    while (true) {
      try {
 
        // Espera tempo milissegundos
        Thread.sleep(tempo);
        
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      
      // Verifica se pode sacar
      conta.saque(conta, valorSaque);
      
      
      //System.out.println(NomeThread+" - sacou R$" + valorSaque + " - Soma total sacado R$"+total);
      
     
          
    }
   
    
    
  }
}
