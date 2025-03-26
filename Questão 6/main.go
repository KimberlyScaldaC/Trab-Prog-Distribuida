package main

import (
	"encoding/csv"
	"fmt"
	"os"
	"strconv"
	"sync"
)

func main() {
	// Em um array, colocamos os nomes de todos os arquivos que queremos usar
	// futuramente seria inteligente encontrar uma forma de pegar esses nomes automaticamente, caso usemos uma base de dados maior
	estados := []string{
		"norte.csv",
		"nordeste.csv",
		"sul.csv",
		"sudeste.csv",
		"centro-oeste.csv",
	}

	// Instância de um objeto da classe WaitGroup, ferramenta importante para implementar sincronismo
	var wg sync.WaitGroup

	var mutex sync.Mutex

	// Essa é uma variável que será usada por todas as outras GoRoutines, instanciamos ela como um "chan" ou Channel,
	// que funciona de forma parecida aos ponteiros, mas com capacidade de interagir com ferramentas que previnekm concorrência
	mediaGeral := make(chan float64, len(estados))

	// Um loop para executar uma (1) GoRoutine para cada arquivo CSV
	for _, arquivo := range estados {
		wg.Add(1) // Adiciona +1 no contador do WaitGroup, para dizer quantas GoRoutines estão em execução
		go mediaTemperatura(arquivo, &wg, mediaGeral, &mutex)
	}

	// O código não irá continuar até que todas as GoRoutines sejam executadas, ou melhor, até o contador zerar
	wg.Wait()

	// Quando terminar a execução das GoRoutines, o canal da variável será fechado
	close(mediaGeral)

	// Variáveis usadas para calcular a média geral de temperatura no país
	var somaMedias float64
	var numRegioes int

	// Para cada valor em média, vai acrescentar em 1, isso é uma forma mais prática de dizer quantas regiões/arquivos lemos
	// isso será útil para implementar esse código para países que tenham mais ou menos regiões que o Brasil
	for media := range mediaGeral {
		if media > 0 {
			somaMedias += media
			numRegioes++
		}
	}

	// Calcula a média do Brasil
	if numRegioes > 0 {
		mediaGeral := somaMedias / float64(numRegioes)
		fmt.Printf("Média geral das temperaturas do Brasil: %.2f°C\n", mediaGeral)
	} else {
		fmt.Println("Não foi possível calcular a média geral.")
	}
}

// Função para calcular a temperatura média de cada região
func mediaTemperatura(arquivo string, wg *sync.WaitGroup, mediaGeral chan<- float64, mutex *sync.Mutex) {
	defer wg.Done() // Diminui o contador em 1 quando a função terminar de executar

	// Abre o arquivo e retorna se ocorrer algum erro de leitura (geralmente o erro está atrelado à escrita do nome lá no array da main)
	file, err := os.Open(arquivo)
	if err != nil {
		fmt.Println("Erro ao abrir o arquivo:", err)
		return
	}
	defer file.Close()

	// Cria um leitor para navegar no arquivo e ignorar a primeira linha, que é o cabeçalho
	reader := csv.NewReader(file)

	// Faz uma leitura completa de todas as linhas do arquivo e armazena elas em "linhas"
	linhas, err := reader.ReadAll()
	if err != nil {
		fmt.Println("Erro ao ler o arquivo CSV:", err)
		return
	}

	// Cria as variáveis para calcular a média da temperatura da região baseada em cada estado
	var somaTemperatura float64
	var numEstados int

	// Loop para navegar entre os elementos de linhas
	for i, linha := range linhas {
		if i == 0 {
			continue
		}

		// Obtém a temperatura da linha, que está armazenada na segunda posição, ou índice 1
		temperatura, err := strconv.ParseFloat(linha[1], 64)
		if err != nil {
			fmt.Println("Erro ao converter a temperatura:", err)
			continue
		}

		// Adiciona a temperatura à soma das temperaturas e incrementa a contagem de estados
		somaTemperatura += temperatura
		numEstados++
	}

	// Switch case apenas para embelezar a saída do código com o nome certinho de cada estado
	// porém, ao implementar esse sistema para outros países, esse switch case terá que ser adaptado
	// seria bom, futuramente, conseguir pegar o nome do arquivo e retirar só o ".csv", já ia embelezar muito
	// usamos o numero de estados no if else para termos um parâmetro que a GoRoutine foi executada em conformidade
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
		mutex.Lock() // Trava a variável para ser manipulada sem concorrência
		mediaGeral <- media // Adiciona a média da região na variável que colocamos no canal
		mutex.Unlock() // Destrava para outras GoRoutines usarem
	} else {
		fmt.Printf("Nenhum dado válido encontrado no arquivo '%s'.\n", arquivo)
	}
}
