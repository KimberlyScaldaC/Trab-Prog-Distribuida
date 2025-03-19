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

// Primeiro de tudo, construímos uma classe com todas as informações necessárias para cadastrar um contato.
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
	arrArquivos := []string{"dados.csv"}
	limpaConsole()

	var wg sync.WaitGroup
	for _, arquivo := range arrArquivos {
		// Adiciona 1 ao contador do WaitGroup para cada goroutine
		wg.Add(1)

		// Inicia a goroutine
		go buscaCompleto(arquivo, &wg)
	}

	// Espera todas as goroutines terminarem antes de continuar
	wg.Wait()

	fmt.Println("Todos os arquivos foram processados.")
	// menu()
}

func buscaCompleto(nomeArquivo string, wg *sync.WaitGroup) {
	defer wg.Done()
	// Abre o arquivo CSV
	file, err := os.Open(nomeArquivo)
	if err != nil {
		log.Fatal("Erro ao abrir o arquivo:", err)
	}
	defer file.Close()

	// Cria um novo leitor CSV
	reader := csv.NewReader(file)

	// Lê todas as linhas do arquivo
	records, err := reader.ReadAll()
	if err != nil {
		log.Fatal("Erro ao ler o arquivo:", err)
	}

	// Processa cada linha do CSV
	for _, record := range records {
		// Cada 'record' é uma linha do CSV
		if len(record) == 4 {
			// Converte o valor 'Ativo' para booleano
			flag, err := strconv.ParseBool(record[3])
			if err != nil {
				log.Printf("Erro ao converter ativo para bool: %v\n", err)
				continue
			}

			aluno := Aluno{
				Matricula: record[0], // A matrícula continua como string
				Nome:      record[1],
				Curso:     record[2],
				Flag:      flag,
			}

			// Imprime as informações do aluno
			if !aluno.Flag {
				fmt.Printf("Matrícula: %s\n", aluno.Matricula)
				fmt.Printf("Nome: %s\n", aluno.Nome)
				fmt.Printf("Curso: %s\n", aluno.Curso)
				fmt.Printf("Status: completo\n")
				fmt.Println("----------------------")
			}

		} else {
			fmt.Println("Linha com formato inválido:", record)
		}
	}
}
