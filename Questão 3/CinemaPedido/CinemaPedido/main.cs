using System; //Importa funcionalidades básicas do .NET, como entrada e saída
using System.Threading.Tasks; //Permite o uso de programação assíncrona com Task, que facilita a execução concorrente.

class CinemaPedido //classe contém os métodos responsáveis por preparar os pedidos
{
    public static async Task<string> GetPipoca() //Declara um método assíncrono que retorna uma Task<string> (uma promessa de retornar uma string no futuro).
    {
        await Task.Delay(2000); // Simula um tempo de espera de 2 segundos
        return "Pipoca Pronta";
    }

    public static async Task<string> GetRefrigerante()
    {
        await Task.Delay(1500); //Simula um tempo de espera de 1.5 segundos
        return "Refrigerante Pronto";
    }

    public static async Task<string> LanchePronto()
    {
        var pipocaTask = GetPipoca(); //Inicia a preparação da pipoca de forma assíncrona.
        var refrigeranteTask = GetRefrigerante(); //Inicia a preparação do refrigerante ao mesmo tempo.

        await Task.WhenAll(pipocaTask, refrigeranteTask); //Aguarda ambos os processos finalizarem antes de continuar

        return $"Lanche pronto: {await pipocaTask} e {await refrigeranteTask}";//Retorna a string indicando que ambos os itens estão prontos.
    }
}

class Program
{
    public static async Task Main(string[] args)
    {
        Console.WriteLine("Pedido realizado. Preparando..."); //Exibe uma mensagem inicial.
        string resultado = await CinemaPedido.LanchePronto();//Chama LanchePronto() e aguarda o resultado.
        Console.WriteLine(resultado); //Exibe o resultado final: "Lanche pronto: Pipoca Pronta e Refrigerante Pronto"
    }
}
