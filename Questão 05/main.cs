using System;
using System.Collections.Generic;
using System.Threading;

// Classe que representa um Veículo
class Veiculo
{
    public int Id { get; } // Identificador único do veículo
    public string Rota { get; } // Rota que o veículo vai percorrer
    private Cruzamento cruzamento; // Instância de Cruzamento, onde o veículo irá passar

    // Construtor que inicializa as propriedades do veículo
    public Veiculo(int id, string rota, Cruzamento cruzamento)
    {
        Id = id;
        Rota = rota;
        this.cruzamento = cruzamento;
    }

    // Método que simula o veículo percorrendo rotas e passando pelo cruzamento
    public void Dirigir()
    {
        Console.WriteLine($"Veículo {Id} iniciou seu percurso");

        // O veículo vai percorrer 3 rotas (Rota 1, 2 e 3)
        for (int rota = 1; rota <= 3; rota++)
        {
            Console.WriteLine($"Veículo {Id} iniciando Rota {rota}");
            
            // O veículo entra no cruzamento na Rota específica
            cruzamento.Entrar(Id, $"Rota {rota}");

            // Simula o tempo que o veículo leva para atravessar o cruzamento (8800 ms)
            Thread.Sleep(8800);

            // O veículo sai do cruzamento após completar a rota
            cruzamento.Sair(Id, $"Rota {rota}");
        }
    }
}

// Classe que representa um Cruzamento
class Cruzamento
{
    private object lockObj = new object(); // Objeto de bloqueio para garantir sincronização
    private bool cruzamentoOcupado = false; // Flag para indicar se o cruzamento está ocupado
    private Queue<int> filaEspera = new Queue<int>(); // Fila de espera para veículos aguardando para passar

    // Método que simula a entrada de um veículo no cruzamento
    public void Entrar(int veiculoId, string rota)
    {
        // Bloqueia o código para garantir que somente um veículo passe de cada vez
        lock (lockObj)
        {
            // Coloca o veículo na fila de espera
            filaEspera.Enqueue(veiculoId);

            // O veículo espera até que o cruzamento não esteja ocupado e seja sua vez
            while (cruzamentoOcupado || filaEspera.Peek() != veiculoId)
            {
                Console.WriteLine($"🚦 Veículo {veiculoId} aguardando no cruzamento ({rota})...");
                Monitor.Wait(lockObj); // Espera até que seja sua vez de passar
            }

            // O veículo pode passar, então o remove da fila de espera e ocupa o cruzamento
            filaEspera.Dequeue();
            cruzamentoOcupado = true;
            Console.WriteLine($"🚗 Veículo {veiculoId} cruzando ({rota})...");
        }
    }

    // Método que simula a saída de um veículo do cruzamento
    public void Sair(int veiculoId, string rota)
    {
        // Libera o cruzamento para o próximo veículo
        lock (lockObj)
        {
            cruzamentoOcupado = false;
            Console.WriteLine($"✅ Veículo {veiculoId} liberou o cruzamento ({rota})...");
            Monitor.PulseAll(lockObj); // Desperta todos os veículos na fila
        }
    }
}

// Classe que gerencia a simulação
class Simulacao
{
    private static int totalVeiculos = 0; // Contador para o número total de veículos
    private static int veiculosProcessados = 0; // Contador para o número de veículos processados
    private static readonly object lockRelatorio = new object(); // Objeto de bloqueio para sincronizar o relatório

    // Método para registrar que um veículo foi processado
    public static void RegistrarVeiculoProcessado()
    {
        lock (lockRelatorio)
        {
            veiculosProcessados++; // Incrementa o número de veículos processados
        }
    }

    // Método principal que inicia a simulação
    public static void Main()
    {
        // Pergunta ao usuário quantos veículos deseja simular
        Console.Write("Quantos veículos deseja simular? ");
        int quantidade = int.Parse(Console.ReadLine()); // Lê o número de veículos

        Cruzamento cruzamento = new Cruzamento(); // Cria uma instância de cruzamento
        List<Thread> threads = new List<Thread>(); // Lista para armazenar as threads dos veículos

        for (int i = 0; i < quantidade; i++)
        {
            // A cada iteração, cria um novo veículo
            totalVeiculos++;
            string rota = $"Rota {new Random().Next(1, 3)}"; // Aleatoriamente define a rota do veículo
            Veiculo veiculo = new Veiculo(i + 1, rota, cruzamento); // Cria o veículo com um id, rota e cruzamento

            // Cria e inicia uma nova thread para o veículo
            Thread thread = new Thread(new ThreadStart(veiculo.Dirigir));
            threads.Add(thread);
            thread.Start();

            // Intervalo entre a criação das threads (simula a chegada dos veículos ao cruzamento)
            Thread.Sleep(5650);
        }

        // Espera todas as threads terminarem antes de continuar
        foreach (Thread thread in threads)
        {
            thread.Join();
        }

        // Exibe o relatório final após todos os veículos terminarem a simulação
        Console.WriteLine("\n=== Relatório de Tráfego ===");
        Console.WriteLine($"Total de veículos simulados: {totalVeiculos}");
        Console.WriteLine($"Total de veículos processados: {veiculosProcessados}");
    }
}
