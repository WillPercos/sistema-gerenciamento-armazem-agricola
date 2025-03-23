package br.com.armazem.view;

import br.com.armazem.model.Usuario;

import javax.swing.*;
import java.awt.*;

public class TelaDashboard extends JFrame {
    private Usuario usuario;

    public TelaDashboard(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Dashboard");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Menu de navegação
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setPreferredSize(new Dimension(200, getHeight()));
        menuPanel.setBackground(new Color(60, 63, 65));

        JLabel lblMenu = new JLabel("Menu de Navegação");
        lblMenu.setFont(new Font("Arial", Font.BOLD, 16));
        lblMenu.setForeground(Color.WHITE);
        lblMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(lblMenu);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] menuItems = {"Configurações", "Transações", "Inventário", "Relatórios", "Alertas", "Perfil"};
        for (String item : menuItems) {
            JButton button = new JButton(item);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(180, 40));
            button.setBackground(new Color(75, 110, 175));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);
            menuPanel.add(button);
            menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));

            // Adiciona ação aos botões do menu
            button.addActionListener(e -> {
                switch (item) {
                    case "Configurações":
                        if (usuario.getAreaAcesso().equals("Gerenciamento")) {
                            new TelaConfiguracoes(usuario).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Acesso negado.");
                        }
                        break;
                    case "Transações":
                        if (usuario.getAreaAcesso().equals("Gerenciamento") || usuario.getAreaAcesso().equals("Compras")) {
                            new TelaTransacoes(usuario).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Acesso negado.");
                        }
                        break;
                    case "Inventário":
                        if (usuario.getAreaAcesso().equals("Gerenciamento") || usuario.getAreaAcesso().equals("Armazém")) {
                            new TelaInventario(usuario).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Acesso negado.");
                        }
                        break;
                    case "Relatórios":
                        if (usuario.getAreaAcesso().equals("Gerenciamento")) {
                            new TelaRelatorios(usuario).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Acesso negado.");
                        }
                        break;
                    case "Alertas":
                        if (usuario.getAreaAcesso().equals("Gerenciamento") || usuario.getAreaAcesso().equals("Armazém")) {
                            new TelaAlertas(usuario).setVisible(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "Acesso negado.");
                        }
                        break;
                    case "Perfil":
                        new TelaPerfil(usuario).setVisible(true);
                        break;
                }
                dispose();
            });
        }

        // Painel de visão geral
        JPanel overviewPanel = new JPanel(new GridBagLayout());
        overviewPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblVisaoGeral = new JLabel("Visão Geral");
        lblVisaoGeral.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        overviewPanel.add(lblVisaoGeral, gbc);

        // Adicionando ícones e gráficos
        ImageIcon estoqueIcon = new ImageIcon("path/to/estoque_icon.png");
        JLabel lblItensEstoque = new JLabel("Itens em Estoque: 8", estoqueIcon, JLabel.LEFT);
        lblItensEstoque.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        overviewPanel.add(lblItensEstoque, gbc);

        ImageIcon alertasIcon = new ImageIcon("path/to/alertas_icon.png");
        JLabel lblAlertasRecentes = new JLabel("Alertas Recentes: 2", alertasIcon, JLabel.LEFT);
        lblAlertasRecentes.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 2;
        overviewPanel.add(lblAlertasRecentes, gbc);

        ImageIcon transacoesIcon = new ImageIcon("path/to/transacoes_icon.png");
        JLabel lblTransacoesRecentes = new JLabel("Transações Recentes: 7", transacoesIcon, JLabel.LEFT);
        lblTransacoesRecentes.setFont(new Font("Arial", Font.PLAIN, 18));
        gbc.gridx = 0;
        gbc.gridy = 3;
        overviewPanel.add(lblTransacoesRecentes, gbc);

        // Adiciona os painéis ao painel principal
        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(overviewPanel, BorderLayout.CENTER);

        // Adiciona o painel principal ao frame
        add(mainPanel);
    }
}