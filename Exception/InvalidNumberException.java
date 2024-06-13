/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Exception;

/**
 *
 * @author flaviorgs
 */
    public class InvalidNumberException extends Exception {
        public InvalidNumberException(String message) {
            super(message);
        }
        
        public static int parseNumber(String input) throws InvalidNumberException {
            try {
                    return Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    throw new InvalidNumberException("Erro: Entrada inválida. Retornando ao menu principal.");
                }
            }
    }
