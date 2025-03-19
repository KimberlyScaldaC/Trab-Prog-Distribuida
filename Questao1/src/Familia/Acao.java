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
    private String nomeThread;
    private int totalGastadora;
    private int totalEsperta;
    private int totalEconomica;

    public Acao(String nomeThread,int tempo, int valorSaque , Conta conta){
        super(nomeThread);
        this.nomeThread = nomeThread;
        this.tempo = tempo;
        this.valorSaque = valorSaque;
        this.conta = conta;
    }

    public String getNomeThread(){
        return nomeThread;
    }
    
    public int getValorSaque(){
        return valorSaque;
    }
  
    public void run(){
        int total = 0;
//    while(conta.saque(conta,valorSaque)){;
//      total += valorSaque;
//    }
    // Enquanto houver saldo suficiente
    while (true) {
        
        
        if(conta.getSaldo() == 0){
            System.out.println(" ");
            System.out.println(" ");
            System.out.println("---------- Resultado ----------");
            System.out.println(" ");
            System.out.println("Thread: AGastadora - Sacou Total R$ " + totalGastadora + ",00");
            System.out.println("Thread: AEsperta   - Sacou Total R$ " + totalEsperta+ ",00");
            System.out.println("Thread: AEconomica - Sacou Total R$ " + totalEconomica + ",00");
            System.out.println(" ");
            System.exit(0);
        }

        try {
          // Espera tempo milissegundos
          Thread.sleep(tempo);

        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        
        // Verifica se pode sacar
        conta.saque(conta, valorSaque, nomeThread);
            
        
        if(true){
            if(valorSaque == 10){
                System.out.println("Entrou 10");
                totalGastadora = valorSaque + totalGastadora;
                System.out.println("Entrou 10 -> "+totalGastadora);
            }
            if(valorSaque == 50){
                System.out.println("Entrou 50");
                totalEsperta = valorSaque + totalEsperta;
                System.out.println("Entrou 50 -> "+totalEsperta);
            }
            if(valorSaque == 5){
                System.out.println("Entrou 5");
                totalEconomica = valorSaque + totalEconomica;
            }
        }
        //System.out.println(NomeThread+" - sacou R$" + valorSaque + " - Soma total sacado R$"+total);
            
    }  
  }
}
