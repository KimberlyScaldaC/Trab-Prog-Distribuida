using System;
using System.Threading.Tasks;

public class Refrigerante {
    public async Task<string> GetRefrigeranteAsync() {
        await Task.Delay(1500); 
        return "Refrigerante Pronto";
    }
}