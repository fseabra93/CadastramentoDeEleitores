/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package View;



import Entity.Eleitor;
import Entity.Pessoa;
import Service.EleitorService;
import java.util.List;
import java.util.Scanner;


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
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Sobrenome: ");
        String sobrenome = scanner.nextLine();
        System.out.print("Zona Eleitoral: ");
        int zonaEleitoral = scanner.nextInt();
        System.out.print("Seção Eleitoral: ");
        int secaoEleitoral = scanner.nextInt();
        scanner.nextLine(); // consumir nova linha
        System.out.print("Título Eleitoral: ");
        String tituloEleitoral = scanner.nextLine();

        Eleitor eleitor = new Eleitor();
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(nome);
        pessoa.setSobrenome(sobrenome);
        eleitor.setPessoa(pessoa);
        eleitor.setZonaEleitoral(zonaEleitoral);
        eleitor.setSecaoEleitoral(secaoEleitoral);
        eleitor.setTituloEleitoral(tituloEleitoral);

        eleitorService.cadastrarEleitor(eleitor);
        System.out.println("Eleitor cadastrado com sucesso!");
    }

    private void buscarEleitorPorId() {
        System.out.print("ID do Eleitor: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // consumir nova linha

        Eleitor eleitor = eleitorService.buscarEleitorPorId(id);
        if (eleitor != null) {
            System.out.println("Eleitor encontrado: " + eleitor.getPessoa().getNome() + " " + eleitor.getPessoa().getSobrenome());
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
                System.out.println("ID: " + eleitor.getId() + ", Nome: " + eleitor.getPessoa().getNome() + " " + eleitor.getPessoa().getSobrenome());
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
                        ", Nome: " + eleitor.getPessoa().getNome() + " " + eleitor.getPessoa().getSobrenome() +
                        ", Zona Eleitoral: " + eleitor.getZonaEleitoral() +
                        ", Seção Eleitoral: " + eleitor.getSecaoEleitoral() +
                        ", Título Eleitoral: " + eleitor.getTituloEleitoral());
            }
        }
    }
}
