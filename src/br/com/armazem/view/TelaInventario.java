package br.com.armazem.view;

import br.com.armazem.dao.ItemDAO;
import br.com.armazem.model.Item;
import br.com.armazem.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TelaInventario extends JFrame {
    private DefaultTableModel model;
    private Usuario usuario;

    public TelaInventario(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Inventário");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Inventário");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(34, 139, 34));
        topPanel.add(lblTitulo);
        JButton btnAdicionar = new JButton("Adicionar Novo Item");
        btnAdicionar.setBackground(new Color(34, 139, 34));
        btnAdicionar.setForeground(Color.WHITE);
        topPanel.add(btnAdicionar);

        JButton btnBuscar = new JButton("Buscar Item");
        btnBuscar.setBackground(new Color(34, 139, 34));
        btnBuscar.setForeground(Color.WHITE);
        topPanel.add(btnBuscar);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Nome", "Quantidade", "Data Entrada", "Validade"};
        model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(34, 139, 34));
        btnVoltar.setForeground(Color.WHITE);
        bottomPanel.add(btnVoltar);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        btnVoltar.addActionListener(e -> {
            new TelaDashboard(usuario).setVisible(true);
            dispose();
        });

       btnAdicionar.addActionListener(e -> {
    System.out.println("Botão 'Adicionar Novo Item' clicado");
    JDialog dialog = new JDialog(TelaInventario.this, "Adicionar Novo Item", true);
    dialog.setSize(400, 300);
    dialog.setLocationRelativeTo(TelaInventario.this);

    JPanel dialogPanel = new JPanel(new GridBagLayout());
    dialogPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    dialogPanel.add(new JLabel("Nome:"), gbc);
    gbc.gridx = 1;
    JTextField txtNome = new JTextField(20);
    dialogPanel.add(txtNome, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    dialogPanel.add(new JLabel("Quantidade:"), gbc);
    gbc.gridx = 1;
    JTextField txtQuantidade = new JTextField(20);
    dialogPanel.add(txtQuantidade, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    dialogPanel.add(new JLabel("Data Entrada:"), gbc);
    gbc.gridx = 1;
    JTextField txtDataEntrada = new JTextField(20);
    dialogPanel.add(txtDataEntrada, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    dialogPanel.add(new JLabel("Validade:"), gbc);
    gbc.gridx = 1;
    JTextField txtValidade = new JTextField(20);
    dialogPanel.add(txtValidade, gbc);

    gbc.gridx = 0;
    gbc.gridy = 4;
    dialogPanel.add(new JLabel("Lote:"), gbc);
    gbc.gridx = 1;
    JTextField txtLote = new JTextField(20);
    dialogPanel.add(txtLote, gbc);

    gbc.gridx = 0;
    gbc.gridy = 5;
    dialogPanel.add(new JLabel("Ponto de Ressuprimento:"), gbc);
    gbc.gridx = 1;
    JTextField txtPontoRessuprimento = new JTextField(20);
    dialogPanel.add(txtPontoRessuprimento, gbc);

    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 2;
    JButton btnSalvar = new JButton("Salvar");
    btnSalvar.setBackground(new Color(34, 139, 34));
    btnSalvar.setForeground(Color.WHITE);
    dialogPanel.add(btnSalvar, gbc);

    // Adicionar o listener do botão Salvar antes de exibir o dialog
    btnSalvar.addActionListener(event -> {
        System.out.println("Botão 'Salvar' clicado");
        try {
            // Validação de entrada
            if (txtNome.getText().isEmpty() || txtQuantidade.getText().isEmpty() || txtDataEntrada.getText().isEmpty() || txtValidade.getText().isEmpty() || txtLote.getText().isEmpty() || txtPontoRessuprimento.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos.");
                System.out.println("Campos vazios detectados");
                return;
            }

            int quantidade;
            int pontoRessuprimento;
            try {
                quantidade = Integer.parseInt(txtQuantidade.getText());
                pontoRessuprimento = Integer.parseInt(txtPontoRessuprimento.getText());
                System.out.println("Quantidade e Ponto de Ressuprimento convertidos com sucesso");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Quantidade e Ponto de Ressuprimento devem ser números válidos.");
                System.out.println("Erro na conversão de Quantidade ou Ponto de Ressuprimento: " + ex.getMessage());
                return;
            }

            System.out.println("Tentando adicionar item...");
            ItemDAO itemDAO = new ItemDAO();
            Item item = new Item(
                0, // ID será gerado automaticamente
                txtNome.getText(),
                quantidade,
                txtDataEntrada.getText(),
                txtValidade.getText(),
                txtLote.getText(),
                pontoRessuprimento
            );
            System.out.println("Dados do item capturados: " + item);
            itemDAO.adicionarItem(item);
            JOptionPane.showMessageDialog(null, "Item adicionado com sucesso!");
            System.out.println("Item adicionado com sucesso!");
            dialog.dispose();
            carregarItens(); // Atualiza a tabela após adicionar o item
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar item: " + ex.getMessage());
            System.err.println("Erro ao adicionar item: " + ex.getMessage());
        }
    });

    dialog.add(dialogPanel);
    dialog.setVisible(true);
});

        btnBuscar.addActionListener(e -> {
            // Lógica para buscar item
            String nomeItem = JOptionPane.showInputDialog("Digite o nome do item:");
            if (nomeItem != null && !nomeItem.isEmpty()) {
                try {
                    ItemDAO itemDAO = new ItemDAO();
                    List<Item> itens = itemDAO.listarItens();
                    model.setRowCount(0); // Limpa a tabela antes de adicionar os resultados da busca
                    for (Item item : itens) {
                        if (item.getNome().equalsIgnoreCase(nomeItem)) {
                            model.addRow(new Object[]{
                                    item.getId(),
                                    item.getNome(),
                                    item.getQuantidade(),
                                    item.getDataEntrada(),
                                    item.getDataValidade()
                            });
                        }
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao buscar item: " + ex.getMessage());
                }
            }
        });

        add(panel);

        carregarItens();
    }

    private void carregarItens() {
        try {
            ItemDAO itemDAO = new ItemDAO();
            List<Item> itens = itemDAO.listarItens();
            model.setRowCount(0); // Limpa a tabela antes de adicionar os itens
            for (Item item : itens) {
                model.addRow(new Object[]{
                        item.getId(),
                        item.getNome(),
                        item.getQuantidade(),
                        item.getDataEntrada(),
                        item.getDataValidade()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar itens: " + e.getMessage());
        }
    }
}