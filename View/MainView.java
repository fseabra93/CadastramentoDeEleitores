/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;
import View.MainView;



import Entity.Eleitor;
import Entity.Pessoa;
import Entity.Endereco;
import Service.EleitorService;
import Service.PessoaService;
import java.text.DecimalFormat;
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
            System.out.println("===== Sistema de Cadastro de Eleitores =====");
            System.out.println("1. Cadastrar Eleitor");
            System.out.println("2. Buscar Eleitor por ID");
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
                    buscarEleitorPorId();
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
                    

                    

                    System.out.println("Foi gerado para " + nome + ":" +
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
            System.out.println("Eleitor encontrado: " + eleitor.getPessoa().getNome() +
                    "\nTítulo: "+ eleitor.getTituloEleitoral());
            if (eleitor.isSituacao() == false){
                System.out.println("O eleitor encontra-se INATIVO devido a existência de uma multa no valor de R$" + eleitor.getMultas());
                System.out.println("A situação mudará para ATIVO após a quitação da multa.");
                System.out.println("Deseja quitar a multa agora? (1)Sim; (2)Não");
                int quitar = scanner.nextInt();
                scanner.nextLine(); // consumir nova linha
                if (quitar == 1){
                    EleitorService.quitarMulta(eleitor);
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
                System.out.println("ID: " + eleitor.getId() + ", Nome: " + eleitor.getPessoa().getNome());// + " " + eleitor.getPessoa().getSobrenome());
            }
        }
    }

    private void atualizarEleitor() {
        System.out.print("ID do Eleitor: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consumir nova linha

        Eleitor eleitor = eleitorService.buscarEleitorPorId(id);
        if (eleitor == null) {
            System.out.println("Eleitor não encontrado.");
            return;
        }

        System.out.print("Nome (" + eleitor.getPessoa().getNome() + "): ");
        String nome = scanner.nextLine();
        System.out.print("Sobrenome (" + eleitor.getPessoa().getSobrenome() + "): ");
        String sobrenome = scanner.nextLine();
        System.out.print("Zona Eleitoral (" + eleitor.getZonaEleitoral() + "): ");
        int zonaEleitoral = scanner.nextInt();
        scanner.nextLine();  // Consumir nova linha
        System.out.print("Seção Eleitoral (" + eleitor.getSecaoEleitoral() + "): ");
        int secaoEleitoral = scanner.nextInt();
        scanner.nextLine();  // Consumir nova linha
        System.out.print("Título Eleitoral (" + eleitor.getTituloEleitoral() + "): ");
        String tituloEleitoral = scanner.nextLine();

        if (!nome.isEmpty()) eleitor.getPessoa().setNome(nome);
        if (!sobrenome.isEmpty()) eleitor.getPessoa().setSobrenome(sobrenome);
        eleitor.setZonaEleitoral(zonaEleitoral);
        eleitor.setSecaoEleitoral(secaoEleitoral);
        if (!tituloEleitoral.isEmpty()) eleitor.setTituloEleitoral(tituloEleitoral);

        eleitorService.atualizarEleitor(eleitor);
        System.out.println("Eleitor atualizado com sucesso!");
    }

    private void removerEleitor() {
        System.out.print("ID do Eleitor: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha

        eleitorService.removerEleitor(id);
        System.out.println("Eleitor removido com sucesso!");
    }

    private void filtrarEleitores() {
        System.out.println("===== Filtrar Eleitores =====");
        System.out.println("1. Por Nome");
        System.out.println("2. Por Zona Eleitoral");
        System.out.print("Escolha uma opção: ");
        int opcao = scanner.nextInt();
        scanner.nextLine(); // Consumir nova linha

        switch (opcao) {
            case 1:
                System.out.print("Nome: ");
                String nome = scanner.nextLine();
                List<Eleitor> eleitoresPorNome = eleitorService.filtrarEleitores(
                    eleitor -> eleitor.getPessoa().getNome().equalsIgnoreCase(nome));
                mostrarEleitores(eleitoresPorNome);
                break;
            case 2:
                System.out.print("Zona Eleitoral: ");
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
                System.out.println("ID: " + eleitor.getId() +
                        "\nNome: " + eleitor.getPessoa().getNome() + 
                        "\nCPF: " + eleitor.getPessoa().getCpf() + 
                        "\nZona Eleitoral: " + eleitor.getZonaEleitoral() +
                        "\nSeção Eleitoral: " + eleitor.getSecaoEleitoral() +
                        "\nTítulo Eleitoral: " + eleitor.getTituloEleitoral());
            }
        }
    }
}
