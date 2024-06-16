/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.GenericDAO;
import Entity.Eleitor;
import Exception.EleitorNotFoundException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import View.View;

/**
 *
 * @author flaviorgs
 */
public class EleitorService {
    private GenericDAO<Eleitor> eleitorDAO;

    public EleitorService() {
        this.eleitorDAO = new GenericDAO<>(Eleitor.class);
    }

    public void cadastrarEleitor(Eleitor eleitor) {
        eleitorDAO.save(eleitor);
    }

    public Eleitor buscarEleitorPorId(int id) {
        Eleitor eleitor = eleitorDAO.findById(id);
        if (eleitor == null) {
            throw new EleitorNotFoundException("Eleitor com ID " + id + " não encontrado.");
            
        }
        return eleitor;
    }

    public List<Eleitor> listarTodosEleitores() {
        return eleitorDAO.findAll();
    }

    public void atualizarEleitor(Eleitor eleitor) {
        eleitorDAO.update(eleitor);
    }

    public void removerEleitor(int id) {
        eleitorDAO.delete(id);
    }

    public List<Eleitor> filtrarEleitores(Predicate<Eleitor> predicate) {
        return listarTodosEleitores().stream().filter(predicate).collect(Collectors.toList());
    }

    public List<Eleitor> ordenarEleitores(java.util.Comparator<Eleitor> comparator) {
        return listarTodosEleitores().stream().sorted(comparator).collect(Collectors.toList());
    }
    
    public static String geradorDeTitulo(){
       
        Supplier<Double> fabricaDeNumeros = () -> Math.random();
        int parte1num = (int)(fabricaDeNumeros.get()*10000);
        String parte1str = String.valueOf(parte1num);
        int parte2num = (int)(fabricaDeNumeros.get()*10000);
        String parte2str = String.valueOf(parte2num);
        int parte3num = (int)(fabricaDeNumeros.get()*10000);
        String parte3str = String.valueOf(parte3num);
        
        List<String> titulo_completo = new ArrayList<>();
        titulo_completo.add(parte1str);
        titulo_completo.add(parte2str);
        titulo_completo.add(parte3str);
        
        //colocar zeros se a parte gerada do título de eleitor for menor que 1000
        
        int cont = 0;
        for (String parte : titulo_completo){
            int tam = parte.length();
            if (tam < 4){
                for (int i = 0; i < (4-tam); i++){
                    parte = "0" + parte;
                }
                titulo_completo.set(cont, parte);  
            }
            cont++;
        }

        String titulo = Stream.of(titulo_completo.get(0), titulo_completo.get(1), titulo_completo.get(2))
                .collect(Collectors.joining(" "));
        
        
        return titulo;
        
    }
    
    public static int geradorDeSecao(){
       
        Supplier<Double> fabricaDeNumeros = () -> Math.random();
        int secao = (int)(fabricaDeNumeros.get()*100 +1);
        
        return secao;
    }
    
    public static int selecionaZona(String cidade){
        
        Map<String, Integer> mapCidades = new HashMap<>();

        mapCidades.put("natal", 10);
        mapCidades.put("parnamirim", 20);
        mapCidades.put("macaiba", 30);
    
        return mapCidades.get(cidade);
               
    }
    
    public static String converterParaMinusculasSemAcento(String str) {
        // Primeiro, normaliza a string para decompor os caracteres acentuados
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        // Remove os caracteres não-ASCII, que incluem os diacríticos (acentos)
        String withoutAccents = normalized.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        // Converte a string para minúsculas
        return withoutAccents.toLowerCase();
    }
    
    public static String padronizaCidade(String cidade){
        
        Map<String, String> mapCidades = new HashMap<>();

        mapCidades.put("natal", "Natal");
        mapCidades.put("parnamirim", "Parnamirim");
        mapCidades.put("macaiba", "Macaíba");
    
        return mapCidades.get(cidade);
               
    }
    
    public static List<Integer> pegaAnosMultas(int anoNasc){
        
        List<Integer>anos_eleitorais = new ArrayList<>();
        // Obtém o ano atual
        Calendar calendar = Calendar.getInstance();
        int anoAtual = calendar.get(Calendar.YEAR);
        int ano = anoNasc + 18; 
        while(ano < anoAtual){
            if (ano % 2 == 0){
                anos_eleitorais.add(ano);
            }
            ano++;
        }
        
        return anos_eleitorais;
         
    }
    
    public static void quitarMulta(Eleitor eleitor){
        eleitor.setMultas(0);
        eleitor.setSituacao(true);
        if (eleitor.getMultas() == 0 && eleitor.isSituacao() == true){
            System.out.println("Eleitor " + eleitor.getPessoa().getNome() + " agora está quite com a Justiça Eleitoral.");
        }
        eleitor.limparAnosSemVotar();
        
    } 
    

    

    
    
}
