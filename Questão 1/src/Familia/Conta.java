
package Familia;

/**
 *
 * @author  Kimberly Scaldaferro Colodeti
 * QUESTAO 1
 */
public class Conta {

    // Atributos da classe Conta
    public int saldo; // Saldo atual da conta
    public final String titular = "Kim"; // Nome do titular da conta (fixo)
    public final int numConta = 12345; // Número da conta (fixo)

    // Atributos relacionados aos totais de saques de cada tipo de thread
    private int totalGastadora = 0;
    private int totalEsperta = 0;
    private int totalEconomica = 0;
    private int totalPatrocinadora = 0;

    // Atributos relacionados à quantidade de saques de cada tipo de thread
    private int quantGastadora = 0;
    private int quantEsperta = 0;
    private int quantEconomica = 0;
    private int quantPatrocinadora = 0;

    // Construtor da classe Conta. Inicializa a conta com um saldo específico.
    public Conta(int saldo) {
        this.saldo = saldo;
        System.out.println("---------- Conta Criada ----------");
        System.out.println("---- Titular: " + titular);
        System.out.println("---- Numero Da Conta: " + numConta);
        System.out.println("---- Saldo Da Conta: R$ " + saldo + ",00");
        System.out.println(" ");
        System.out.println("------------- Acoes na Conta -------------");
    }

    /**
     * Realiza o saque de um valor da conta, se o saldo for suficiente. O método é
     * sincronizado para garantir que apenas uma thread possa acessar a conta por
     * vez.
     * 
     * -> valorSaque O valor que se deseja sacar.
     * -> nomeThread O nome da thread que está tentando realizar o saque.
     * -> true se o saque for realizado com sucesso, false caso contrário.
     */
    public synchronized Boolean saque(int valorSaque, String nomeThread) {

        if (saldo < valorSaque) {
            System.out.println("\nThread: " + nomeThread + " - Tentou sacar R$ " + valorSaque + ",00");
            System.out.println("Saldo insuficiente R$ " + getSaldo() + ",00\n");
            return false; // Não é possível sacar, saldo insuficiente.
        } else {
            saldo -= valorSaque; // Reduz o valor do saldo da conta
            System.out.println("Thread: " + nomeThread + " - sacou R$ " + valorSaque
                    + ",00 - Saldo apos saque: R$ " + saldo + ",00");
            return true; // Saque realizado com sucesso.
        }

    }

    /**
     * Realiza o depósito de um valor na conta. O método é sincronizado para
     * garantir que apenas
     * uma Thread possa acessar a conta por vez.
     * 
     * -> valorDeposito O valor a ser depositado na conta.
     * -> nomeThread O nome da Thread que está realizando o depósito.
     */
    public synchronized void Deposito(int valorDeposito, String nomeThread) {
        saldo += valorDeposito; // Adiciona o valor do depósito ao saldo da conta
        System.out.println("\nThread: " + nomeThread + " - depositou R$ " + valorDeposito
                + ",00 - Saldo apos deposito: R$ " + saldo + ",00");
    }

    // O método é sincronizado para garantir que a leitura do saldo seja feita de forma segura em ambientes multithread.
    public synchronized int getSaldo() {
        return saldo; // Retorna o saldo atual da conta.
    }

    // Métodos para acessar os totais de saques e quantidades de saques por cada tipo de Thread
    public int getTotalGastadora() {
        return totalGastadora;
    }

    public int getQuantGastadora() {
        return quantGastadora;
    }

    public int getTotalPatrocinadora() {
        return totalPatrocinadora;
    }

    public int getQuantPatrocinadora() {
        return quantPatrocinadora;
    }

    public int getTotalEsperta() {
        return totalEsperta;
    }

    public int getQuantEsperta() {
        return quantEsperta;
    }

    public int getTotalEconomica() {
        return totalEconomica;
    }

    public int getQuantEconomica() {
        return quantEconomica;
    }

    // Atualiza o total de saques da thread e incrementa a quantidade de saques dessa Thread.
    public int setTotalGastadora(int totalGastadora) {
        this.quantGastadora += 1; // Incrementa a quantidade de saques dessa Thread
        return this.totalGastadora = totalGastadora; // Atualiza o total de saques dessa Thread
    }

    public int setTotalPatrocinadora(int totalPatrocinadora) {
        this.quantPatrocinadora += 1;
        return this.totalPatrocinadora = totalPatrocinadora;
    }

    public int setTotalEsperta(int totalEsperta) {
        this.quantEsperta += 1;
        return this.totalEsperta = totalEsperta;
    }

    public int setTotalEconomica(int totalEconomica) {
        this.quantEconomica += 1;
        return this.totalEconomica = totalEconomica;
    }

}
