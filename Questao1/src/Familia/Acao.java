
package Familia;

/**
 *
 * @author  Kimberly Scaldaferro Colodeti
 * QUESTAO 1
 */
public class Acao extends Thread {

  private Conta conta; // Instância da conta compartilhada entre as threads
  private int valorSaque; // Valor a ser sacado por cada thread
  private int tempoThread; // Tempo de espera entre as tentativas de saque (em milissegundos)


  // Construtor da classe Acao, responsável por inicializar os parâmetros da thread.
  public Acao(String nomeThread, int tempoThread, int valorSaque, Conta conta) {
    super(nomeThread);
    this.tempoThread = tempoThread;
    this.valorSaque = valorSaque;
    this.conta = conta;
  }

  /**
   * Método que imprime um resumo final dos saques realizados pelos diferentes
   * tipos de usuários (threads).
   */
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
    System.out.println("------------- Acoes na Conta -------------");");
  }

  /**
   * Método que contém a lógica principal da thread. Ele verifica se o saldo da
   * conta é suficiente para realizar o saque, faz o saque ou o depósito (se
   * necessário), e realiza a impressão do resumo final quando a conta atingir um
   * saldo de 0.
   */
  public void run() {

    // Loop infinito para que a thread continue tentando realizar a ação
    while (true) {
      int total = 0;
      try {
        // A thread aguarda um tempo de espera antes de tentar realizar a próxima ação
        Thread.sleep(tempoThread);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      // Acesso à conta sincronizado para evitar condições de corrida
      synchronized (conta) {

        // Se o saldo da conta for 0, a thread "Patrocinadora" faz um depósito
        if (conta.getSaldo() == 0) {

          // Verifica se a thread é a "Patrocinadora"
          if (getName().equals("APatrocinadora")) {
            // Realiza o depósito na conta
            conta.Deposito(valorSaque, getName());
            total = valorSaque + conta.getTotalPatrocinadora(); // Atualiza o total da Patrocinadora
            conta.setTotalPatrocinadora(total);
            imprimirResumo(); // Imprime o resumo final após o depósito
            conta.notifyAll(); // Notifica todas as threads que estavam esperando
          }
        }

        // Verifica se pode sacar
        if (conta.getSaldo() >= valorSaque) {
          // Realiza o saque e atualiza o total de saques de acordo com o tipo de usuário
          // (thread)
          if (getName() == "AGastadora") {
            total = valorSaque + conta.getTotalGastadora();
            conta.setTotalGastadora(total);
            // System.out.println(" ENTROU AGastadora -> " + conta.getTotalGastadora());
          } else if (getName() == "AEsperta") {
            total = valorSaque + conta.getTotalEsperta();
            conta.setTotalEsperta(total);
            // System.out.println(" ENTROU AEsperta -> " + conta.getTotalEsperta());
          } else if (getName() == "AEconomica") {
            total = valorSaque + conta.getTotalEconomica();
            conta.setTotalEconomica(total);
            // System.out.println(" ENTROU AEconomica -> " + conta.getTotalEconomica());
          }
        }

        // Se a thread não for a "Patrocinadora", ela tenta realizar um saque
        if (getName() != "APatrocinadora") {
          conta.saque(valorSaque, getName());
        }
      }

    }

  }

}
