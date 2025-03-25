using System;
using System.Threading.Tasks;

public class Pedido {
    private Pipoca pipoca;
    private Refrigerante refrigerante;

    public Pedido() {
        this.pipoca = new Pipoca();
        this.refrigerante = new Refrigerante();
    }

    public async Task<string> FazerPedidoAsync() {
        Task<string> tarefaPipoca = pipoca.GetPipocaAsync();
        Task<string> tarefaRefrigerante = refrigerante.GetRefrigeranteAsync();

        await Task.WhenAll(tarefaPipoca, tarefaRefrigerante);

        return $"Pedido pronto: {tarefaPipoca.Result} e {tarefaRefrigerante.Result}";
    }
}