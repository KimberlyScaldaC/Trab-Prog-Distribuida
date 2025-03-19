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

  public Acao(String nomeThread, int tempo, int valorSaque, Conta conta) {
    super(nomeThread);
    this.tempo = tempo;
    this.valorSaque = valorSaque;
    this.conta = conta;
  }

  public void imprimirResumo() {

    System.out.println("\n\n---------- RESUMO FINAL ----------");
    System.out.println("\nAGastadora -> sacou um total de R$ " + conta.getTotalGastadora() + ",00");
    System.out.println("           -> Quantidade de Saques: " + conta.getQuantGastadora());
    System.out.println("\nAEsperta  -> sacou um total de R$ " + conta.getTotalEsperta() + ",00");
    System.out.println("           -> Quantidade de Saques: " + conta.getQuantEsperta());
    System.out.println("\nAEconomica -> sacou um total de R$ " + conta.getTotalEconomica() + ",00");
    System.out.println("           -> Quantidade de Saques: " + conta.getQuantEconomica());
    System.out.println("\nAPatrocinadora -> sacou um total de R$ " + conta.getTotalPatrocinadora() + ",00");
    System.out.println("           -> Quantidade de Saques: " + conta.getQuantPatrocinadora());
    System.out.println("\n----------------------------------\n");
    // System.exit(0);
  }

  public void run() {

    while (true) {
      int total = 0;
      try {
        Thread.sleep(tempo);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      synchronized (conta) { // Lock na conta
        // Se o saldo for 0, imprime o resumo e faz o depósito
        if (conta.getSaldo() == 0) {
          if (getName() == "APatrocinadora") {
            conta.Deposito(valorSaque, getName()); // Deposita o valor
            total = valorSaque + conta.getTotalPatrocinadora();
            conta.setTotalPatrocinadora(total);
            imprimirResumo(); // Imprime o resumo
            conta.notifyAll(); // Libera todas as threads bloqueadas

          }
          // break; // Encerra a thread
        }

        // Verifica se pode sacar
        if (conta.getSaldo() >= valorSaque) {
          if (getName() == "AGastadora") {
            total = valorSaque + conta.getTotalGastadora();
            conta.setTotalGastadora(total);
            // System.out.println(" ENTROU 10 -> " + conta.getTotalGastadora());
          }
          if (getName() == "AEsperta") {
            total = valorSaque + conta.getTotalEsperta();
            conta.setTotalEsperta(total);
            // System.out.println(" ENTROU 10 -> " + conta.getTotalEsperta());
          }
          if (getName() == "AEconomica") {
            total = valorSaque + conta.getTotalEconomica();
            conta.setTotalEconomica(total);
            // System.out.println(" ENTROU 10 -> " + conta.getTotalEconomica());
          }
        }
        if (getName() != "APatrocinadora") {
          conta.saque(valorSaque, getName());
        }
      }

      // conta.saque(valorSaque, getName());

    }

  }

}
