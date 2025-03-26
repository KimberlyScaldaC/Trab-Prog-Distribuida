package main

import (
	"encoding/csv"
	"fmt"
	"os"
	"strconv"
	"sync"
)

func main() {
	// Lista de arquivos CSV, um para cada região do Brasil (exemplo).
	arquivos := []string{
		"norte.csv",
		"nordeste.csv",
		"sul.csv",
		"sudeste.csv",
		"centro-oeste.csv",
	}

	// WaitGroup para aguardar as goroutines.
	var wg sync.WaitGroup

	// Canal para armazenar as médias regionais.
	resultados := make(chan float64, len(arquivos))

	// Para cada arquivo, chama a função de cálculo em uma goroutine.
	for _, arquivo := range arquivos {
		wg.Add(1) // Incrementa o contador do WaitGroup.
		go calcularMediaTemperatura(arquivo, &wg, resultados)
	}

	// Aguarda todas as goroutines terminarem.
	wg.Wait()

	// Fecha o canal após todas as goroutines terminarem.
	close(resultados)

	// Variáveis para calcular a média geral.
	var somaMedias float64
	var numRegioes int

	// Calcula a soma de todas as médias regionais.
	for media := range resultados {
		if media > 0 {
			somaMedias += media
			numRegioes++
		}
	}

	// Calcula a média geral do país.
	if numRegioes > 0 {
		mediaGeral := somaMedias / float64(numRegioes)
		fmt.Printf("Média geral das temperaturas do Brasil: %.2f°C\n", mediaGeral)
	} else {
		fmt.Println("Não foi possível calcular a média geral.")
	}
}

// Função que lê um arquivo CSV, calcula a média de temperatura e retorna o resultado.
func calcularMediaTemperatura(arquivo string, wg *sync.WaitGroup, resultados chan<- float64) {
	defer wg.Done() // Decrementa o contador do WaitGroup quando a função terminar.

	// Abre o arquivo CSV.
	file, err := os.Open(arquivo)
	if err != nil {
		fmt.Println("Erro ao abrir o arquivo:", err)
		return
	}
	defer file.Close()

	// Cria um leitor CSV.
	reader := csv.NewReader(file)

	// Lê todas as linhas do arquivo CSV.
	linhas, err := reader.ReadAll()
	if err != nil {
		fmt.Println("Erro ao ler o arquivo CSV:", err)
		return
	}

	// Variáveis para calcular a soma das temperaturas e contar os estados.
	var somaTemperatura float64
	var numEstados int

	// Itera pelas linhas do arquivo CSV, ignorando o cabeçalho.
	for i, linha := range linhas {
		if i == 0 {
			// Ignora a primeira linha, que é o cabeçalho.
			continue
		}

		// Obtém a temperatura da linha.
		temperatura, err := strconv.ParseFloat(linha[1], 64)
		if err != nil {
			fmt.Println("Erro ao converter a temperatura:", err)
			continue
		}

		// Adiciona a temperatura à soma.
		somaTemperatura += temperatura
		numEstados++
	}

	// Calcula a média das temperaturas da região.
	if numEstados > 0 {
		media := somaTemperatura / float64(numEstados)
		var nomeRegiao string
		switch arquivo {
		case "norte.csv":
			nomeRegiao = "Norte"
		case "nordeste.csv":
			nomeRegiao = "Nordeste"
		case "sul.csv":
			nomeRegiao = "Sul"
		case "sudeste.csv":
			nomeRegiao = "Sudeste"
		case "centro-oeste.csv":
			nomeRegiao = "Centro-Oeste"
		}
		
		fmt.Printf("Média de temperatura do %s: %.2f°C\n", nomeRegiao, media)
		// Envia a média para o canal para ser usada na média geral.
		resultados <- media
	} else {
		fmt.Printf("Nenhum dado válido encontrado no arquivo '%s'.\n", arquivo)
		resultados <- 0 // Envia 0 se não houver dados válidos.
	}
}