using System;
using System.Threading;
using System.Threading.Tasks;
using System.Collections.Concurrent;
using System.Linq;

class Program
{
    static int[] vetor = new int[1000000];  // Um vetor de 1 milhão de elementos.

    static void Main()
    {
        Random rand = new Random();  // Instância de um gerador de números aleatórios.

        // Preenche o vetor com números aleatórios entre 0 e 99.
        for (int i = 0; i < vetor.Length; i++)
        {
            vetor[i] = rand.Next(0, 100);  // Gera um número aleatório entre 0 e 99.
        }

        // Console.WriteLine("\n------ Numeros dentro do Vetor ------ \n");
        // for (int i = 0; i < vetor.Length; i++)
        // {
        //     Console.WriteLine("Vetor[" + i + "] = " + vetor[i]);
        // }

        Console.WriteLine("\n------------ Resultado ----------- \n");
        // Abordagem Sequencial: Soma todos os elementos do vetor de maneira simples e direta.
        int somaSequencial = SomaSequencial(vetor);
        Console.WriteLine($"---> Soma Sequencial: {somaSequencial}");

        // Abordagem Paralela: Soma os elementos do vetor usando múltiplas threads.
        int somaParalela = SomaParalela(vetor, 10);  // Usando 10 threads para somar os elementos.
        Console.WriteLine($"---> Soma Paralela: {somaParalela}");
    }

    // Método que calcula a soma de todos os elementos do vetor de forma sequencial.
    static int SomaSequencial(int[] vetor)
    {
        int soma = 0;  // Variável para armazenar o resultado final da soma.

        // Laço que percorre todos os elementos do vetor e realiza a soma.
        foreach (int num in vetor)
        {
            soma += num;  // Adiciona o valor do elemento ao total.
        }

        return soma;  // Retorna o resultado da soma.
    }

    // Método que realiza a soma dos elementos do vetor de forma paralela utilizando múltiplas threads.
    static int SomaParalela(int[] vetor, int numThreads)
    {
        int tamanho = vetor.Length;  // Obtém o tamanho do vetor.

        // Cria um array para armazenar as threads que serão usadas.
        Thread[] threads = new Thread[numThreads];

        // Um container thread-safe para armazenar os resultados das threads.
        ConcurrentBag<int> resultados = new ConcurrentBag<int>();

        // Laço que cria e inicializa as threads.
        for (int i = 0; i < numThreads; i++)
        {
            // Calcula o intervalo de índice para cada thread. A primeira thread pega os primeiros elementos, a última pega os últimos.
            int inicio = i * (tamanho / numThreads);  // A posição inicial para a thread i.
            int fim = (i == numThreads - 1) ? tamanho : (i + 1) * (tamanho / numThreads);  // A posição final para a thread i.

            // Cria uma nova instância da classe SomaThread, passando o intervalo para somar e o resultado compartilhado.
            SomaThread somaThread = new SomaThread(vetor, inicio, fim, resultados);

            // Cria e inicia a thread que executará o método CalcularSoma da classe SomaThread.
            threads[i] = new Thread(new ThreadStart(somaThread.CalcularSoma));
            threads[i].Start();
        }

        // Aguarda a conclusão de todas as threads. O método Join garante que o programa espere todas as threads terminarem.
        foreach (Thread t in threads)
        {
            t.Join();
        }

        // Soma todos os resultados parciais armazenados no ConcurrentBag e retorna o total.
        return resultados.ToArray().Sum();
    }
}

// Classe responsável por realizar a soma de um intervalo do vetor. Cada instância dessa classe calcula a soma de um pedaço do vetor.
class SomaThread
{
    private int[] vetor;          // O vetor de números que será somado.
    private int inicio, fim;      // A faixa de índices do vetor que a thread vai processar.
    private ConcurrentBag<int> resultados;  // Um container thread-safe para armazenar resultados parciais.

    public SomaThread(int[] vetor, int inicio, int fim, ConcurrentBag<int> resultados)
    {
        this.vetor = vetor;     // Inicializa o vetor com os dados a serem somados.
        this.inicio = inicio;   // A posição inicial no vetor para esta thread.
        this.fim = fim;         // A posição final no vetor para esta thread.
        this.resultados = resultados;  // O objeto thread-safe onde os resultados parciais serão armazenados.
    }

    // Método que realiza a soma dos elementos do vetor para o intervalo [inicio, fim).
    public void CalcularSoma()
    {
        int somaParcial = 0;  // Variável para armazenar a soma dos elementos no intervalo da thread.

        // Laço que percorre os elementos do vetor no intervalo [inicio, fim)
        for (int i = inicio; i < fim; i++)
        {
            somaParcial += vetor[i];  // Soma o valor do vetor no índice i.
        }

        // Adiciona o resultado parcial ao objeto ConcurrentBag para ser usado posteriormente.
        resultados.Add(somaParcial);
    }
}
