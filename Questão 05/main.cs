
using System;
using System.Collections.Generic;
using System.Threading;

class Veiculo
{
    public int Id { get; }
    public string Rota { get; }
    private Cruzamento cruzamento;

    public Veiculo(int id, string rota, Cruzamento cruzamento)
    {
        Id = id;
        Rota = rota;
        this.cruzamento = cruzamento;
    }

    public void Dirigir()
    {
        Console.WriteLine($"Veículo {Id} iniciou seu percurso");
        
        // Percorre todas as 3 rotas
        for (int rota = 1; rota <= 3; rota++)
        {
            Console.WriteLine($"Veículo {Id} iniciando Rota {rota}");
            cruzamento.Entrar(Id, $"Rota {rota}");
            Thread.Sleep(8800);
            cruzamento.Sair(Id, $"Rota {rota}");
        }

       
    }
}

class Cruzamento
{
    private object lockObj = new object();
    private bool cruzamentoOcupado = false;
    private Queue<int> filaEspera = new Queue<int>();

    public void Entrar(int veiculoId, string rota)
    {
        // Verifica se o cruzamento está ocupado e travar o veículo
        lock (lockObj)
        {
            // fila de espera no cruzamento
            filaEspera.Enqueue(veiculoId);
            
            while (cruzamentoOcupado || filaEspera.Peek() != veiculoId)
            {
                Console.WriteLine($"🚦 Veículo {veiculoId} aguardando no cruzamento ({rota})...");
                Monitor.Wait(lockObj);
            }

            // definir uma fila de espera no cruzamento

            filaEspera.Dequeue();
            cruzamentoOcupado = true;
            Console.WriteLine($"🚗 Veículo {veiculoId} cruzando ({rota})...");
        }
    }

    public void Sair(int veiculoId, string rota)
    {
        // liberar o cruzamento
        lock (lockObj)
        {
            cruzamentoOcupado = false;
            Console.WriteLine($"✅ Veículo {veiculoId} liberou o cruzamento ({rota})...");
            Monitor.PulseAll(lockObj);
        }
    }
}

class Simulacao
{
    private static int totalVeiculos = 0;
    private static int veiculosProcessados = 0;
    private static readonly object lockRelatorio = new object();

    public static void RegistrarVeiculoProcessado()
    {
        
        // definir relatorio de trafego após todos os veiculos serem processados
        
        lock (lockRelatorio)
        {
            veiculosProcessados++;
        }
    }

    public static void Main()
    {
        // definir quantida de veiculos a serem simulados
        Console.Write("Quantos veículos deseja simular? ");
        int quantidade = int.Parse(Console.ReadLine());

        Cruzamento cruzamento = new Cruzamento();
        List<Thread> threads = new List<Thread>();

        for (int i = 0; i < quantidade; i++)
        {
            // definir rotas de cada veiculo e ordem a serem percorridas
            totalVeiculos++;
            string rota = $"Rota {new Random().Next(1, 3)}";
            Veiculo veiculo = new Veiculo(i + 1, rota, cruzamento);

            Thread thread = new Thread(new ThreadStart(veiculo.Dirigir));
            threads.Add(thread);
            thread.Start();
            Thread.Sleep(5650);
        }

        foreach (Thread thread in threads)
        {
            thread.Join();
        }

        Console.WriteLine("\n=== Relatório de Tráfego ===");
        Console.WriteLine($"Total de veículos simulados: {totalVeiculos}");
        Console.WriteLine($"Total de veículos processados: {veiculosProcessados}");
    }
}
