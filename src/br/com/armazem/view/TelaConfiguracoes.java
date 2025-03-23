package br.com.armazem.view;

import br.com.armazem.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class TelaConfiguracoes extends JFrame {
    private JComboBox<String> temaComboBox;
    private JCheckBox notificacoesCheckBox;
    private JTextField permissoesTextField;
    private JButton salvarButton;
    private JButton cancelarButton;
    private JButton voltarButton;
    private Usuario usuario;

    public TelaConfiguracoes(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Configurações");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Opções de Tema
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Tema:"), gbc);
        temaComboBox = new JComboBox<>(new String[]{"Claro", "Escuro"});
        gbc.gridx = 1;
        panel.add(temaComboBox, gbc);

        // Opções de Notificações
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Notificações:"), gbc);
        notificacoesCheckBox = new JCheckBox("Ativar Notificações");
        gbc.gridx = 1;
        panel.add(notificacoesCheckBox, gbc);

        // Permissões de Acesso
        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Permissões de Acesso:"), gbc);
        permissoesTextField = new JTextField();
        gbc.gridx = 1;
        panel.add(permissoesTextField, gbc);

        // Botões de Salvar, Cancelar e Voltar
        salvarButton = new JButton("Salvar");
        cancelarButton = new JButton("Cancelar");
        voltarButton = new JButton("Voltar");

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(salvarButton, gbc);
        gbc.gridx = 1;
        panel.add(cancelarButton, gbc);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(voltarButton, gbc);

        add(panel);

        // Ações dos botões
        salvarButton.addActionListener(e -> {
            // Lógica para salvar as configurações
            String temaSelecionado = (String) temaComboBox.getSelectedItem();
            boolean notificacoesAtivadas = notificacoesCheckBox.isSelected();
            String permissoesDeAcesso = permissoesTextField.getText();

            // Exemplo de ação
            JOptionPane.showMessageDialog(this, "Configurações salvas:\nTema: " + temaSelecionado + "\nNotificações: " + (notificacoesAtivadas ? "Ativadas" : "Desativadas") + "\nPermissões de Acesso: " + permissoesDeAcesso);
        });

        cancelarButton.addActionListener(e -> {
            // Lógica para cancelar as alterações
            temaComboBox.setSelectedIndex(0);
            notificacoesCheckBox.setSelected(false);
            permissoesTextField.setText("");
        });

        voltarButton.addActionListener(e -> {
            new TelaDashboard(usuario).setVisible(true);
            dispose();
        });
    }
}