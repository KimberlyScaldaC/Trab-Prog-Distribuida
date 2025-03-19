/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Familia;

/**
 *
 * @author  Kimberly Scaldaferro Colodeti
 */
public class Conta {
    
    public int saldo = 0;
    public String titular = "Kim";
    public int numConta = 12345;
    
    public Conta(int saldo){
      this.saldo = saldo;
      System.out.println("---------- Conta Criada ----------");
      System.out.println("---- Titular: "+titular);
      System.out.println("---- Numero Da Conta: "+numConta);
      System.out.println("---- Saldo Da Conta: R$ "+saldo+",00");
      System.out.println(" ");
      System.out.println("------------- Acoes na Conta -------------");
    }
    
    public String getTitular(){
      return titular;
    }
    
    public int getNumConta(){
      return numConta;
    }
    
    public int getSaldo(){
      return saldo;
    }
    public void setSaldo(int saldo){
      this.saldo = saldo;
    }
    
    public synchronized  Boolean saque(Conta conta, int valorSaque){
       
        if(saldo < valorSaque){
          System.out.println("Saldo insuficiente R$" + getSaldo());
          return false;
        }else{
            int novoSaldo = saldo - valorSaque;

            System.out.println("Thread: "+Thread.currentThread().getName() + " - sacou R$ " + valorSaque + " - Saldo apos saque: R$" + novoSaldo);
            conta.setSaldo(novoSaldo);
            return true;
        }
   
    }
}
