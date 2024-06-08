/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author flaviorgs
 */
public class PessoaService {
    
        public static List<Character> consertarInsercaoData(String str){
        List<Character> elementosDN = str.chars() 
              .mapToObj(c -> (char) c) 
              .collect(Collectors.toList()); 
        
        char barra = '/';
        char zero = '0';
        
        if (elementosDN.get(1) == barra){
            elementosDN.add(0, zero);
        }
        
        if (elementosDN.get(4) == barra){
            elementosDN.add(3, zero);
        }
        
        return elementosDN;
    
    }
        
    public static int checarIdade(String dataString){
        LocalDate dataNascimento = LocalDate.parse(dataString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate dataAtual = LocalDate.now();
        Period periodo = Period.between(dataNascimento, dataAtual);
        int idade = periodo.getYears();
        
        return idade;
        

    }
    
    public static String getSobrenome(String nomeCompleto) {
        // Usa streams para dividir o nome completo e pegar o último elemento
        List<String> nomes = Arrays.stream(nomeCompleto.split("\\s+"))
                                   .collect(Collectors.toList());
        
        if (nomes.size() > 1) {
            return nomes.get(nomes.size() - 1);
        } else {
            return ""; // Retorna uma string vazia se não houver sobrenome
        }
    }
    
         public static List<Character> padronizaCPF(String cpf_padronizar){
        List<Character> elementos = cpf_padronizar.chars() 
              .mapToObj(c -> (char) c) 
              .collect(Collectors.toList()); 

        elementos.removeIf(c -> !Character.isDigit(c));

        return elementos;
              
    }
    
    public static String reconstroiCPF(List<Character>lista){
        String cpf_primeiraParte = lista.stream()
                .map(String::valueOf)
                .limit(3)
                .collect(Collectors.joining());
        
        String cpf_segundaParte = lista.stream()
                .map(String::valueOf)
                .limit(6)
                .skip(3)
                .collect(Collectors.joining());
        
        String cpf_terceiraParte = lista.stream()
                .map(String::valueOf)
                .limit(9)
                .skip(6)
                .collect(Collectors.joining());
        
        String cpf_verificadores = lista.stream()
                .map(String::valueOf)
                .skip(9)
                .collect(Collectors.joining());
        
        String concatenacao1 = Stream.of(cpf_primeiraParte, cpf_segundaParte, cpf_terceiraParte)
                .collect(Collectors.joining("."));
        
        String concatenacao_final = Stream.of(concatenacao1, cpf_verificadores)
                .collect(Collectors.joining("-"));
        

        return concatenacao_final;
        
    }
    
    public static boolean checarCPF(List<Character> cpf_checar){
        
        List<Character> elementos_principais = cpf_checar.stream()
                        .limit(9)
                        .collect(Collectors.toList());
        
          
        List<Character> elementos_verificadores = cpf_checar.stream()
                        .skip(9)
                        .collect(Collectors.toList());
        
        int primeiroVerificador = Character.getNumericValue(elementos_verificadores.get(0));
        int segundoVerificador = Character.getNumericValue(elementos_verificadores.get(1));
        
        boolean prim_digit_cpf = checa_primeiro_digito(elementos_principais, primeiroVerificador);
        boolean seg_digit_cpf = checa_segundo_digito(elementos_principais, primeiroVerificador, segundoVerificador);
        
        if (prim_digit_cpf && seg_digit_cpf){
            return true;
        } else {
            return false;
        }


    }
    

    
    public static boolean checa_primeiro_digito(List<Character>lista, int verificador){
        int mult = 10;
        List<Integer>lista_multiplica = new ArrayList();
        
        for (char valor : lista ){
            lista_multiplica.add(mult*(Character.getNumericValue(valor)));
            mult--;
        }
        
        int soma = lista_multiplica.stream()
                .reduce(0, Integer::sum);
        
        int resto = soma % 11;
        
        if ((resto == 0 || resto == 1) && verificador == 0){
            return true;
        } else if ((resto != 0 && resto != 1) && (11 - resto == verificador) ){
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean checa_segundo_digito(List<Character>lista, int num , int verificador){
        int mult = 11;
        List<Integer>lista_multiplica = new ArrayList();
        
        for (char valor : lista ){
            lista_multiplica.add(mult*(Character.getNumericValue(valor)));
            mult--;
        }
        
        lista_multiplica.add(num*2);
        
        int soma = lista_multiplica.stream()
                .reduce(0, Integer::sum);
        
        int resto = soma % 11;
                
        if ((resto == 0 || resto == 1) && verificador == 0){
            return true;
        } else if ((resto != 0 && resto != 1) && (11 - resto == verificador) ){
            return true;
        } else {
            return false;
        }
        
    }
    

    
    
    
    
}
