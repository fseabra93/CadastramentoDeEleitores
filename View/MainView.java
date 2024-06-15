/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;
import View.MainView;


import Exception.InvalidNumberException;
import Entity.Eleitor;
import Entity.Pessoa;
import Entity.Endereco;
import Service.EleitorService;
import Service.PessoaService;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.stream.Collectors;


/**
 *
 * @author flaviorgs
 */
public class MainView implements View {
    private EleitorService eleitorService;
    private Scanner scanner;

    public MainView() {
        this.eleitorService = new EleitorService();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void startView() {
        while (true) {
            System.out.println("\n===== Sistema de Cadastro de Eleitores =====");
            System.out.println("1. Cadastrar Eleitor");
            System.out.println("2. Buscar Eleitor");
            System.out.println("3. Listar Todos Eleitores");
            System.out.println("4. Atualizar Eleitor");
            System.out.println("5. Remover Eleitor");
            System.out.println("6. Filtrar Eleitores");
            System.out.println("7. Ordenar Eleitores");
            System.out.println("8. Sair");
            System.out.print("Escolha uma opção: ");

            int escolha = scanner.nextInt();
            scanner.nextLine(); // consumir nova linha

            switch (escolha) {
                case 1:
                    cadastrarEleitor();
                    break;
                case 2:
                    System.out.println("Digite:\n(1) para buscar pelo ID"
                            + "\n(2) para buscar pelo pelo Título"
                            + "\n(3) para buscar pelo CPF");
                    String input = scanner.nextLine();
                    try {
                        int num_buscar = InvalidNumberException.parseNumber(input);
                        if (num_buscar == 1){
                            buscarEleitorPorId();
                        }
                        else if (num_buscar == 2){
                            buscarPorTitulo();
                        } else if (num_buscar == 3){
                            buscarPorCpf();
                        }
                        else {
                            System.out.println("Opção inválida.\n");
                            startView();
                        }
                    } catch (InvalidNumberException e) {
                        System.err.println(e.getMessage());
                    }                     
                    
                    break;
                case 3:
                    listarTodosEleitores();
                    break;
                case 4:
                    atualizarEleitor();
                    break;
                case 5:
                    removerEleitor();
                    break;
                case 6:
                    filtrarEleitores();
                    break;
                case 7:
                    ordenarEleitores();
                    break;
                case 8:
                    System.out.println("Encerrando o sistema...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private void cadastrarEleitor() {
        System.out.println("\nEntrou no módulo Cadastrar Eleitor");
        System.out.println("Digite a data de nascimento do próximo eleitor no formato dd/mm/aaaa:");
        String dn = scanner.nextLine();
        //corrigir se a pessoa não digitar um 0 no início dos dias e/ou dos meses 1 a 9
        List<Character> listaCharDN = PessoaService.consertarInsercaoData(dn);
        dn = listaCharDN.stream()
              .map(String::valueOf)
              .collect(Collectors.joining());
        
        int idade_calc = PessoaService.checarIdade(dn);
        
        //Se a pessoa tiver menos de 16 anos não pode ser cadastrada
        if (idade_calc < 16){
                System.out.println("Pessoa com idade abaixo do limite mínimo para votar.\n");
                    startView();
            } else {
                System.out.print("Nome Completo: ");
                String nome = scanner.nextLine();

                //pega automaticamente o sobrenome do nome completo digitado apenas para efeito de busca futura
                String sobrenome = PessoaService.getSobrenome(nome);
                
                System.out.print("Digite o CPF de " + nome + ": ");
                String cpf_digitado = scanner.nextLine();

                
                //Se a pessoa esquecer de digitar algum dígito do CPF inserir zeros pra não quebrar o programa
                if (cpf_digitado.length() < 11){
                    int tam = cpf_digitado.length();
                    for (int i = tam; i < 11; i++){
                        cpf_digitado = cpf_digitado + "0";
                    }
                }
                //Separa o CPF em uma lista de char para testar validade
                List<Character> elementos_CPF = PessoaService.padronizaCPF(cpf_digitado);
                //Reconstrói CPF no formato correto para inserir no cadastro caso passe no teste de validade
                String cpf_reconstruido = PessoaService.reconstroiCPF(elementos_CPF);
                //Testa se CPF é válido
                boolean cpf_valido = PessoaService.checarCPF(elementos_CPF);
                if (!cpf_valido){
                    System.out.println("O CPF fornecido é inválido.\n");
                    startView();
                } else {
                    System.out.println("Cadastrar endereço de "+nome);
                    System.out.print("Rua: ");
                    String rua_digitada = scanner.nextLine();
                    System.out.print("Número: ");
                    String num_digitado = scanner.nextLine();
                    System.out.print("Bairro: ");
                    String bairro_digitado = scanner.nextLine();
                    System.out.print("Cidade (Natal, Macaíba ou Parnamirim): ");
                    String cidade_digitada = scanner.nextLine();
                    
                    //String sobrenome = scanner.nextLine();
                    String tituloEleitoral = EleitorService.geradorDeTitulo();
                    int secaoEleitoral = EleitorService.geradorDeSecao();
                    String cidade_para_selecionar_zona = EleitorService.converterParaMinusculasSemAcento(cidade_digitada);
                    int zonaEleitoral = EleitorService.selecionaZona(cidade_para_selecionar_zona);
                    
                    //padronizar cidade para inserir no cadastro
                    String cidade_para_cadastrar = EleitorService.padronizaCidade(cidade_para_selecionar_zona);
  
                    System.out.println("\nFoi gerado para " + nome + ":" +
                            "\nTítulo Eleitoral: " + tituloEleitoral +
                            "\nSeção: " + secaoEleitoral +
                            "\nZona: " + zonaEleitoral);
                                        
                    //inserir os dados fornecidos
                    Endereco endereco = new Endereco();
                    Eleitor eleitor = new Eleitor();
                    Pessoa pessoa = new Pessoa();
                    
                    endereco.setRua(rua_digitada);
                    endereco.setNumero_compl(num_digitado);
                    endereco.setBairro(bairro_digitado);
                    endereco.setCidade(cidade_para_cadastrar);
                    endereco.setEstado("RN");
                    
                    pessoa.setNome(nome);
                    pessoa.setSobrenome(sobrenome);
                    pessoa.setDataNascimento(dn);
                    pessoa.setCpf(cpf_reconstruido);
                    pessoa.setEndereco(endereco);
                              
                    eleitor.setPessoa(pessoa);
                    eleitor.setZonaEleitoral(zonaEleitoral);
                    eleitor.setSecaoEleitoral(secaoEleitoral);
                    eleitor.setTituloEleitoral(tituloEleitoral);
                    eleitor.setMultas(0);
                    eleitor.setSituacao(true);

                    eleitorService.cadastrarEleitor(eleitor);
                    System.out.println("Eleitor cadastrado com sucesso!"); 
                        if (idade_calc > 18){

                            //calcular a multa por alistamento tardio
                            int ano_nasc = PessoaService.extractYearFromDate(dn);
                            List<Integer>anosMultas = EleitorService.pegaAnosMultas(ano_nasc);

                            float valor_multa = (float) 3.51 * anosMultas.size();

                            if (valor_multa > 0){
                                DecimalFormat df = new DecimalFormat("#.##");
                                String multaFormatada = df.format(valor_multa);

                                System.out.println("Foi gerada uma multa de R$ "+ multaFormatada + " por alistamento tardio por não ter votado nas eleições de: ");
                                System.out.println(anosMultas);
                                System.out.println("O título de " + nome + " permanecerá INATIVO e será ativado automaticamente após a quitação da multa."); 
                                eleitor.setSituacao(false);
                                eleitor.setMultas(valor_multa);
                            }

                        }

                    } //fecha o else do CPF       
                

            }
    }

    private void buscarEleitorPorId() {
        System.out.print("ID do Eleitor: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consumir nova linha

        Eleitor eleitor = eleitorService.buscarEleitorPorId(id);
        if (eleitor != null) {
            System.out.println("Eleitor encontrado:");
                    imprimeEleitor(eleitor);

            if (eleitor.isSituacao() == false){
                System.out.println("O eleitor encontra-se INATIVO devido a existência de uma multa no valor de R$" + eleitor.getMultas());
                System.out.println("A situação mudará para ATIVO após a quitação da multa.");
                System.out.println("Deseja quitar a multa agora? (1)Sim; (2)Não");
                String input = scanner.nextLine();
                try {
                    int quitar = InvalidNumberException.parseNumber(input);
                    if (quitar == 1){
                        EleitorService.quitarMulta(eleitor);
                    }
                } catch (InvalidNumberException e) {
                    System.err.println(e.getMessage());
                }             
            }
        } else {
            System.out.println("Eleitor não encontrado.");
        }
    }

    private void listarTodosEleitores() {
        List<Eleitor> eleitores = eleitorService.listarTodosEleitores();
        if (eleitores.isEmpty()) {
            System.out.println("Nenhum eleitor cadastrado.");
        } else {
            for (Eleitor eleitor : eleitores) {
                System.out.print("ID: " + eleitor.getId() + ", Nome: " + eleitor.getPessoa().getNome());
                if (eleitor.isSituacao() == true){
                    System.out.println(", Situação: Quite");
                } else {
                    System.out.println(", Situação: INATIVO");
                    
                }
                
            }
            System.out.println("Deseja buscar algum eleitor peo ID?  (1)Sim; (2)Não");
            String input = scanner.nextLine();
            try {
                int buscar = InvalidNumberException.parseNumber(input);
                if (buscar == 1){
                    buscarEleitorPorId();
                }
            } catch (InvalidNumberException e) {
                System.err.println(e.getMessage());
            }                   
        }
    }

    private void atualizarEleitor() {
        listarTodosEleitores();
        System.out.print("Digite o ID do Eleitor que deseja atualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consumir nova linha

        Eleitor eleitor = eleitorService.buscarEleitorPorId(id);
        if (eleitor == null) {
            System.out.println("Eleitor não encontrado.");
            return;
        } else {
 
            System.out.println("\n===== Atualizar Eleitor =====");
            System.out.println("Que campo do eleitor " + eleitor.getPessoa().getNome() +" você deseja atializar?");
            System.out.println("1. Nome");
            System.out.println("2. Endereço");
            System.out.println("Obs. Caso a atualização do endereço seja para outro município, o campo Zona Eleitoral será atualizado automaticamente.");
            String input = scanner.nextLine();
            int escolha = 0;
            
            try {
                escolha = InvalidNumberException.parseNumber(input);
                if (escolha == 1){
                }
            } catch (InvalidNumberException e) {
                System.err.println(e.getMessage());
            } 
            switch (escolha) {
                    case 1:
                        System.out.print("Nome atual (" + eleitor.getPessoa().getNome() + "): ");
                        System.out.println("Digite nome completo para atualização:");
                        String nome = scanner.nextLine();
                        String sobrenome = PessoaService.getSobrenome(nome);
                        eleitor.getPessoa().setNome(nome);
                        eleitor.getPessoa().setSobrenome(sobrenome);
                        break;
                    case 2:
                        System.out.println("Atualizar endereço de "+ eleitor.getPessoa().getNome());
                        System.out.print("Rua: ");
                        String rua_digitada = scanner.nextLine();
                        eleitor.getPessoa().setRua(rua_digitada);
                        System.out.print("Número: ");
                        String num_digitado = scanner.nextLine();
                        eleitor.getPessoa().setNumero_compl(num_digitado);
                        System.out.print("Bairro: ");
                        String bairro_digitado = scanner.nextLine();
                        eleitor.getPessoa().setBairro(bairro_digitado);
                        System.out.print("Cidade (Natal, Macaíba ou Parnamirim): ");
                        String cidade_digitada = scanner.nextLine();
                        String cidade_para_selecionar_zona = EleitorService.converterParaMinusculasSemAcento(cidade_digitada);
                        int zonaEleitoral = EleitorService.selecionaZona(cidade_para_selecionar_zona);                   
                        //padronizar cidade para inserir no cadastro
                        String cidade_para_cadastrar = EleitorService.padronizaCidade(cidade_para_selecionar_zona);
                        eleitor.getPessoa().setCidade(cidade_para_cadastrar);
                        eleitor.setZonaEleitoral(zonaEleitoral);

                        int secaoEleitoral = EleitorService.geradorDeSecao();
                        eleitor.setSecaoEleitoral(secaoEleitoral);
                        break;
                    default:
                        System.out.println("Opção inválida.");
                        startView();
                }
        }
        System.out.println("Eleitor atualizado com sucesso!");
        imprimeEleitor(eleitor);
    }

    private void removerEleitor() {
        System.out.println("A remoção de um eleitor deve ser feita pelo ID, escolha uma opção:");
        System.out.println("Digite (1) se vc sabe o ID do eleitor que deseja remover");
        System.out.println("Digite (2) se vc sabe o CPF do eleitor que deseja remover. Anote o ID do eleitora que será exibido na tela.");
        System.out.println("Digite (3) se vc sabe o número do Título do eleitor que deseja remover. Anote o ID do eleitora que será exibido na tela.");
        System.out.println("Caso não saiba nenhuma dessas opções, digite (4) para retornar ao MENU inicial e depois a opção de 'Listar' os eleitores");
        String input = scanner.nextLine();
        try {
                int opcao = InvalidNumberException.parseNumber(input);
                if (opcao == 1){
                        System.out.print("ID do Eleitor: ");
                        int id = scanner.nextInt();
                        scanner.nextLine(); // Consumir nova linha
                        eleitorService.removerEleitor(id);
                        System.out.println("Eleitor removido com sucesso!");
                } else if (opcao == 2){
                    buscarPorCpf();                    
                } else if (opcao == 3){
                    buscarPorTitulo();                    
                } else if (opcao == 4){
                    startView();
                } else {
                    System.out.println("Opção inválida.");
                    startView();
                }
        } catch (InvalidNumberException e) {
               System.err.println(e.getMessage());
        }  
        

    }

    private void filtrarEleitores() {
        System.out.println("===== Filtrar Eleitores =====");
        System.out.println("1. Por Sobrenome");
        System.out.println("2. Por Zona Eleitoral");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha

        switch (opcao) {
            case 1:
                System.out.print("Sobrenome: ");
                String sobrenome = scanner.nextLine();
                List<Eleitor> eleitoresPorNome = eleitorService.filtrarEleitores(
                    eleitor -> eleitor.getPessoa().getSobrenome().equalsIgnoreCase(sobrenome));
                mostrarEleitores(eleitoresPorNome);
                break;
            case 2:
                System.out.print("Zona Eleitoral (10, 20 ou 30): ");
                int zonaEleitoral = scanner.nextInt();
                scanner.nextLine(); // Consumir nova linha
                List<Eleitor> eleitoresPorZona = eleitorService.filtrarEleitores(
                    eleitor -> eleitor.getZonaEleitoral() == zonaEleitoral);
                mostrarEleitores(eleitoresPorZona);
                break;

            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
    }
    
    private void buscarPorTitulo(){
            System.out.print("Digite o número do Título (somente numeros): ");
            String titulo_buscar = scanner.nextLine();
            
                   // Inserir espaços após a 4ª e a 9ª posições
            StringBuilder sb = new StringBuilder(titulo_buscar);
            sb.insert(4, " ");
            sb.insert(9, " ");
            
            String titulo_formatado = sb.toString();

            List<Eleitor> eleitoresPorNome = eleitorService.filtrarEleitores(
                    eleitor -> eleitor.getTituloEleitoral().equalsIgnoreCase(titulo_formatado));
                mostrarEleitores(eleitoresPorNome);
            
    }
    
    private void buscarPorCpf(){
            System.out.print("Digite o número do CPF (somente numeros): ");
            String cpf_buscar = scanner.nextLine();
            
                            //Se a pessoa esquecer de digitar algum dígito do CPF inserir zeros pra não quebrar o programa
            if (cpf_buscar.length() < 11){
                int tam = cpf_buscar.length();
                for (int i = tam; i < 11; i++){
                    cpf_buscar = cpf_buscar + "0";
                }
            }
                //Separa o CPF em uma lista de char para testar validade
            List<Character> elementos_CPF = PessoaService.padronizaCPF(cpf_buscar);
                //Reconstrói CPF no formato correto para inserir no cadastro caso passe no teste de validade
            String cpf_reconstruido = PessoaService.reconstroiCPF(elementos_CPF);

            List<Eleitor> eleitoresPorNome = eleitorService.filtrarEleitores(
                    eleitor -> eleitor.getPessoa().getCpf().equalsIgnoreCase(cpf_reconstruido));
                mostrarEleitores(eleitoresPorNome);
            
    }

    private void ordenarEleitores() {
        System.out.println("===== Ordenar Eleitores =====");
        System.out.println("1. Por Nome");
        System.out.println("2. Por Zona Eleitoral");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha

        List<Eleitor> eleitoresOrdenados;
        switch (opcao) {
            case 1:
                eleitoresOrdenados = eleitorService.ordenarEleitores(
                    (e1, e2) -> e1.getPessoa().getNome().compareToIgnoreCase(e2.getPessoa().getNome()));
                mostrarEleitores(eleitoresOrdenados);
                break;
            case 2:
                eleitoresOrdenados = eleitorService.ordenarEleitores(
                    (e1, e2) -> Integer.compare(e1.getZonaEleitoral(), e2.getZonaEleitoral()));
                mostrarEleitores(eleitoresOrdenados);
                break;
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
    }

    private void mostrarEleitores(List<Eleitor> eleitores) {
        if (eleitores.isEmpty()) {
            System.out.println("Nenhum eleitor encontrado.");
        } else {
            for (Eleitor eleitor : eleitores) {
                System.out.println("\nID: " + eleitor.getId() +
                        "\nNome: " + eleitor.getPessoa().getNome() + 
                        "\nCPF: " + eleitor.getPessoa().getCpf() + 
                        "\nZona Eleitoral: " + eleitor.getZonaEleitoral() +
                        "\nSeção Eleitoral: " + eleitor.getSecaoEleitoral() +
                        "\nTítulo Eleitoral: " + eleitor.getTituloEleitoral());
                        String condicao = (eleitor.isSituacao()) ? "Situação: Quite" : "Situação: INATIVO";
                        System.out.println(condicao);
            }
        }
    }

    
    public static void imprimeEleitor(Eleitor eleitor){
        System.out.println("\nNome: " + eleitor.getPessoa().getNome());
        System.out.println("Título: "+ eleitor.getTituloEleitoral());
        System.out.println("Zona Eleitoral: " + eleitor.getZonaEleitoral());
        System.out.println("Seção: " + eleitor.getSecaoEleitoral());
        String condicao = (eleitor.isSituacao()) ? "Situação: Quite" : "Situação: INATIVO";
        
    }
}
