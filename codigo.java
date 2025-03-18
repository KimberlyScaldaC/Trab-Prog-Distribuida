import java.util.*;



public class Cliente extends Thread {
  private static Banco banco = new Banco();
  private Conta conta = null;
  private Double valor = 100;
  
  public Cliente(int numConta, String nome, Conta conta){
    super(nome);
    this.conta = conta;
  }
  
  public void run(){
    Double total = 0;
    while(banco.saque(conta.valor)){
      total += valor;
    }
    
    System.out.println("Num Conta:"numConta+"-"getName()+"Sacou R$"+total);
  }
}

public class Banco{
  public Boolean saque(Conta conta, Double valor){
    Double saldo = conta.getSaldo();
    if(saldo<valor){
      System.out.println("Saldo insuficiente");
      return false;
    }
    
    Double novoSaldo = saldo - valor;
    System.out.println(Thread.currentThead().getName()+"sacou R$"+valor+" - Saldo apos saque: R$"+novoSaldo);
    conta.setSaldo(novoSaldo);
    return true;
  }
}

public class Conta{
  public Double saldo = 0;
  public Conta(Double saldo){
    this.saldo = saldo;
    System.out.println("Conta criada. Saldo inicial: R$"+ saldo);
  }
  public Double getSaldo(){
    return saldo;
  }
  public void setSaldo(Double saldo){
    this.saldo = saldo;
  }
}

public class Familia {
    public static void main(String[] args) {
      //CRIA CONTA JUNTO DA Familia
      final Conta conta = new Conta(1000);
      numConta = 12345;
      
      //CRIA FAMILIARES E LHES INFORMA A CONTA CONJUNTA
      Cliente pai = new Cliente(numConta+"pai ",conta);
      Cliente mae = new Cliente(numConta+"mae ",conta);
      Cliente filho = new Cliente(numConta+"filho ",conta);
      
      //INICIA AS THREADS
      pai.star();
      mae.star();
      filho.star();
  }
}
