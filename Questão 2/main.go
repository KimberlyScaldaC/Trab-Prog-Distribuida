package main


import (
	"encoding/csv"
	"fmt"
	"log"
	"os"
	"os/exec"
	"strconv"
	"sync"
)

// Primeiro de tudo, construímos uma estrutura com todas as informações necessárias para cadastrar um contato.
type Aluno struct {
	Matricula string
	Nome      string
	Curso     string
	Flag      bool
}

// Esta é uma função feita para limpar o console, visando melhorar a visibilidade do output
func limpaConsole() {
	c := exec.Command("clear")
	c.Stdout = os.Stdout
	c.Run()
}

// Essa é a nossa função main, que chama diretamente o menu.
func main() {
	// Array com os nomes dos arquivos a serem chamados
	arrArquivos := []string{"administração.csv", "arquitetura.csv", "biologia.csv", "computacao.csv", "direito.csv", "economia.csv", "engenharia.csv", "filosofia.csv",
				"física.csv", "história.csv", "letras.csv", "matemática.csv", "medicina.csv", "psicologia.csv", "química.csv"}
	limpaConsole()

	var wg sync.WaitGroup // Ferramenta da biblioteca do Go para promover sincronismo entre funções
	for _, arquivo := range arrArquivos {
		// Adiciona 1 ao contador do WaitGroup para cada goroutine
		wg.Add(1)

		// Inicia a goroutine
		go buscaCompleto(arquivo, &wg) // Passa o nome do arquivo como parâmetro
	}

	// Espera todas as goroutines terminarem antes de continuar
	wg.Wait()

	fmt.Println("Todos os arquivos foram processados.")
}

// Função que busca os alunos com curso COMPLETO
func buscaCompleto(nomeArquivo string, wg *sync.WaitGroup) {
	defer wg.Done() // Reduz o contador do WaitGroup em 1 quando a função terminar de ser executada

	// Abre o arquivo CSV
	file, err := os.Open(nomeArquivo)
	if err != nil {
		log.Fatal("Erro ao abrir o arquivo:", err)
	}
	defer file.Close()

	// Cria um novo leitor CSV
	reader := csv.NewReader(file)

	// Lê a primeira linha para ignorar os cabeçalhos (Matrícula/Nome/Curso/Cursando)
	_, err = reader.Read()
	if err != nil {
		log.Fatal("Erro ao ler os cabeçalhos do arquivo:", err)
	}

	// Lê todas as linhas restantes
	records, err := reader.ReadAll()
	if err != nil {
		log.Fatal("Erro ao ler o arquivo:", err)
	}

	// Processa cada linha do CSV
	for _, record := range records {
		if len(record) == 4 {
			// Converte o valor 'Cursando' para booleano (sim para cursando ou não para completo)
			flag, err := strconv.ParseBool(record[3])
			if err != nil {
				log.Printf("Erro ao converter 'Cursando' para bool na linha %+v: %v\n", record, err)
				continue
			}

			// Cria uma instância de Aluno conforme os dados oferecidos pela leitura dos arquivos CSV
			aluno := Aluno{
				Matricula: record[0],
				Nome:      record[1],
				Curso:     record[2],
				Flag:      flag,
			}

			// Imprime as informações do aluno linearmente (tentei fazer um dado do aluno para cada linha,
			// mas as funções sendo executadas concorrentemente estava quebrando e deixando o output confuso)
			if !aluno.Flag {
				fmt.Printf("%s %s %s COMPLETO\n", aluno.Matricula, aluno.Nome, aluno.Curso)
			}
		} else {
			fmt.Println("Linha com formato inválido:", record)
		}
	}
}
