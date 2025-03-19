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
  private int valorSaque;
  private int tempo;

  // private int totalGastadora = 0;
  // private int totalEsperta = 0;
  // private int totalEconomica = 0;

  public Acao(String nomeThread, int tempo, int valorSaque, Conta conta) {
    super(nomeThread);
    this.tempo = tempo;
    this.valorSaque = valorSaque;
    this.conta = conta;
  }

  // public int getTotalGastadora() {
  // return totalGastadora;
  // }

  // public int getTotalEsperta() {
  // return totalEsperta;
  // }

  // public int getTotalEconomica() {
  // return totalEconomica;
  // }

  public void imprimirResumo() {

    System.out.println("\n\n---------- RESUMO FINAL ----------");
    System.out.println("AGastadora sacou um total de R$ " + conta.getTotalGastadora() + ",00");
    System.out.println("AEsperta sacou um total de R$ " + conta.getTotalEsperta() + ",00");
    System.out.println("AEconomica sacou um total de R$ " + conta.getTotalEconomica() + ",00");
    System.out.println("\n----------------------------------");
    System.exit(0);
  }

  public void run() {
    int total = 0;
    while (true) {

      if (conta.getSaldo() == 0) {
        imprimirResumo();

      }

      try {
        // Espera tempo milissegundos
        Thread.sleep(tempo);

      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (conta.getSaldo() >= valorSaque) {
        if (getName() == "AGastadora") {
          total = valorSaque + conta.getTotalGastadora();
          conta.setTotalGastadora(total);
          System.out.println(" ENTROU 10 -> " + conta.getTotalGastadora());
        }
        if (getName() == "AEsperta") {
          total = valorSaque + conta.getTotalEsperta();
          conta.setTotalEsperta(total);
          System.out.println(" ENTROU 10 -> " + conta.getTotalEsperta());
        }
        if (getName() == "AEconomica") {
          total = valorSaque + conta.getTotalEconomica();
          conta.setTotalEconomica(total);
          System.out.println(" ENTROU 10 -> " + conta.getTotalEconomica());
        }
      }

      
      conta.saque(valorSaque, getName());


    }

  }

}
