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
	// wg.Add(1)
	// go buscaCompleto("dados.csv", &wg)
	// wg.Wait()

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

// // A fução menu é constituída de um case, para reconhecer a escolha de opção do usuário.
// func menu() {
// 	var opc int
// 	fmt.Println("Digite 0 para incluir contatos.")
// 	fmt.Println("Digite 1 para mostrar contatos.")
// 	fmt.Println("Digite 2 para limpar todos os contatos.")
// 	fmt.Println("Digite 3 para sair.")
// 	fmt.Scan(&opc)
// 	switch opc {
// 	case 0:
// 		regist()
// 	case 1:
// 		buscar()
// 	case 2:
// 		limpar()
// 	case 3:
// 		fmt.Println("Muito obrigado por testar nosso código. Volte sempre!")
// 	default:
// 		fmt.Println("Valor não reconhecido pelo sistema, tente novamente.")
// 	}
// }

// // Para incluir contatos seria importante que o usuário conseguisse registrar contatos como bem entendesse, porém esse código é para fins educacionais, então deixamos um input pronto para demonstrar na apresentação do trabalho.
// func regist() {
// 	limpaConsole()

// 	arquivo, err := os.Create("contatos.csv")
// 	if err != nil {
// 		panic(err)
// 	}
// 	defer arquivo.Close()

// 	pessoas := [][]string{
// 		{"Vitor","OCVitin","(27)","99603-8438"},
// 		{"Armando","Algo","(27)","12345-6789"},
// 		{"Dudu","Duds","(27)","54321-9876"},
// 		{"Octávio","OCOctávio","(27)","96969-6969"},
// 		{"Isaac","The Binding of Isaac","(27)","99666-6969"},
//   }

//   err = csv.NewWriter(arquivo).WriteAll(pessoas)
//   if err != nil {
//     panic(err)
//   }
// 	fmt.Println("")
// 	menu()
// }

// // Depois de registrar os contatos, fizemos uma sequência para buscar as informações dos dados salvos no arquivo CSV. Os objetos são instanciados e registrados em um Array, a busca é feita pela posição nele.
// func buscar() {
// 	limpaConsole()
// 	arquivo, err := os.Open("contatos.csv")
// 	if err != nil {
// 		panic(err)
// 	}
// 	defer arquivo.Close()

// 	dados, err := csv.NewReader(arquivo).ReadAll()
// 	if err != nil {
// 		panic(err)
// 	}

// 	pessoas := make([]Contato, len(dados))
// 	for i, record := range dados {
// 		contato := Contato{
// 			Nome:    record[0],
// 			Apelido: record[1],
// 			DDD:     record[2],
// 			Numero:  record[3]}
// 		pessoas[i] = contato
// 	}
// 	var busc int
// 	fmt.Println("Escolha a posição do contato para fazer a busca [0 - n].")
// 	fmt.Scan(&busc)

// 	// Abaixo está uma condicional que atua como tratamento de erro, avisando o usuário se o Array está vazio, ou se a posição que ela pediu em específico está vazia.
// 	if len(pessoas) == 0 {
// 		fmt.Println("Não há contatos na lista...")
// 	} else if busc >= len(pessoas) {
// 		fmt.Println("Não há contatos nessa posição...")
// 	} else {
// 		fmt.Println(pessoas[busc])
// 	}
// 	fmt.Println("")
// 	menu()
// }

// // A função limpar se assemelha muito com o de registrar contatos, pois os dois sobrescrevem o arquivo CSV com dados novos, mas nesse caso ele sobrescreve com nenhum dano, limpando completamente o arquivo.
// func limpar() {
// 	limpaConsole()
// 	arquivo, err := os.Create("contatos.csv")
// 	if err != nil {
// 		panic(err)
// 	}
// 	defer arquivo.Close()

// 	fmt.Println("Limpando sua lista de contatos...")
// 	pessoas := [][]string{{""}}

// 	err = csv.NewWriter(arquivo).WriteAll(pessoas)
// 	if err != nil {
// 		panic(err)
// 	}
// 	fmt.Println("")
// 	menu()
// }
