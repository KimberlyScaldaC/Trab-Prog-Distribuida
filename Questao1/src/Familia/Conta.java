/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
//package Familia;

/**
 *
 * @author Kimberly Scaldaferro Colodeti
 */
public class Conta {

    public int saldo;
    public final String titular = "Kim";
    public final int numConta = 12345;

    private int totalGastadora = 0;
    private int totalEsperta = 0;
    private int totalEconomica = 0;
    private int quantGastadora = 0;
    private int quantEsperta = 0;
    private int quantEconomica = 0;

    public Conta(int saldo) {
        this.saldo = saldo;
        System.out.println("---------- Conta Criada ----------");
        System.out.println("---- Titular: " + titular);
        System.out.println("---- Numero Da Conta: " + numConta);
        System.out.println("---- Saldo Da Conta: R$ " + saldo + ",00");
        System.out.println(" ");
        System.out.println("------------- Acoes na Conta -------------");
    }

    public synchronized Boolean saque(int valorSaque, String nomeThread) {

        if (saldo < valorSaque) {
            System.out.println("Saldo insuficiente R$ " + getSaldo() + ",00");

            System.out.println("Thread: " + nomeThread + " - Tentou sacar R$ " + valorSaque + ",00");

            return false;
        } else {

            saldo -= valorSaque;

            System.out.println("Thread: " + nomeThread + " - sacou R$ " + valorSaque
                    + ",00 - Saldo apos saque: R$ " + saldo + ",00");

            return true;
        }

    }

    public synchronized int getSaldo() {
        return saldo;
    }

    public int getTotalGastadora() {
        return totalGastadora;
    }

    public int getQuantGastadora() {
        return quantGastadora;
    }

    public int setTotalGastadora(int totalGastadora) {
        this.quantGastadora += 1;
        return this.totalGastadora = totalGastadora;
    }

    public int getTotalEsperta() {
        return totalEsperta;
    }

    public int getQuantEsperta() {
        return quantEsperta;
    }

    public int setTotalEsperta(int totalEsperta) {
        this.quantEsperta += 1;
        return this.totalEsperta = totalEsperta;
    }

    public int getTotalEconomica() {
        return totalEconomica;
    }

    public int getQuantEconomica() {
        return quantEconomica;
    }

    public int setTotalEconomica(int totalEconomica) {
        this.quantEconomica += 1;
        return this.totalEconomica = totalEconomica;
    }

}
