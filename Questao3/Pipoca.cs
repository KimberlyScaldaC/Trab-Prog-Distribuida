using System;
using System.Threading.Tasks;

public class Pipoca {
    public async Task<string> GetPipocaAsync() {
        await Task.Delay(2000); 
        return "Pipoca Pronta";
    }
}