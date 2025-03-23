package br.com.armazem.model;

import br.com.armazem.view.TelaLogin;

public class Main {
    public static void main(String[] args) {
        /*Usar para testar
        login: joaos  
        senha: senha123
        */
        // Inicia a tela de login
        TelaLogin telaLogin = new TelaLogin();
        telaLogin.setVisible(true);
    }
}

