using System;
using System.Threading.Tasks;

class Program {
    static async Task Main(string[] args) {
        Pedido pedido = new Pedido();
        string resultado = await pedido.FazerPedidoAsync();
        Console.WriteLine(resultado);
    }
}