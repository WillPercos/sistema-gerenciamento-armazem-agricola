package br.com.armazem.view;

import br.com.armazem.dao.ItemDAO;
import br.com.armazem.dao.TransacaoDAO;
import br.com.armazem.model.Item;
import br.com.armazem.model.Transacao;
import br.com.armazem.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.table.JTableHeader;

public class TelaTransacoes extends JFrame {
    private DefaultTableModel model;
    private Usuario usuario;

    public TelaTransacoes(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Transações");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Transações", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(new Color(34, 139, 34)); // Cor verde
        panel.add(label, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Data e Hora", "Tipo", "Funcionário", "Itens"};
        model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        // Definir a cor de fundo do cabeçalho da tabela
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(34, 139, 34)); // Cor verde
        header.setForeground(Color.WHITE); // Texto branco

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();

        JButton addButton = new JButton("Adicionar Nova Transação");
        addButton.setBackground(new Color(144, 238, 144)); // Cor verde claro
        buttonPanel.add(addButton);

        JButton searchButton = new JButton("Buscar Transação");
        searchButton.setBackground(new Color(144, 238, 144)); // Cor verde claro
        buttonPanel.add(searchButton);

        // Botão para voltar ao dashboard
        JButton backButton = new JButton("Voltar ao Dashboard");
        backButton.setBackground(new Color(144, 238, 144)); // Cor verde claro
        backButton.addActionListener(e -> {
            new TelaDashboard(usuario).setVisible(true);
            dispose();
        });
        buttonPanel.add(backButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
    System.out.println("Botão 'Adicionar Nova Transação' clicado");
    JDialog dialog = new JDialog(TelaTransacoes.this, "Adicionar Nova Transação", true);
    dialog.setSize(500, 400);
    dialog.setLocationRelativeTo(TelaTransacoes.this);

    JPanel dialogPanel = new JPanel(new GridBagLayout());
    dialogPanel.setBackground(Color.WHITE);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    gbc.gridx = 0;
    gbc.gridy = 0;
    dialogPanel.add(new JLabel("Data e Hora:"), gbc);
    gbc.gridx = 1;
    JTextField txtDataHora = new JTextField(20);
    dialogPanel.add(txtDataHora, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    dialogPanel.add(new JLabel("Tipo:"), gbc);
    gbc.gridx = 1;
    JTextField txtTipo = new JTextField(20);
    dialogPanel.add(txtTipo, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    dialogPanel.add(new JLabel("Funcionário:"), gbc);
    gbc.gridx = 1;
    JTextField txtFuncionario = new JTextField(20);
    dialogPanel.add(txtFuncionario, gbc);

    gbc.gridx = 0;
    gbc.gridy = 3;
    dialogPanel.add(new JLabel("Itens:"), gbc);
    gbc.gridx = 1;
    JComboBox<String> comboItens = new JComboBox<>();
    dialogPanel.add(comboItens, gbc);

    // Carregar itens do banco de dados
    Map<String, Integer> itemMap = new HashMap<>();
    try {
        ItemDAO itemDAO = new ItemDAO();
        List<Item> itens = itemDAO.listarItens();
        for (Item item : itens) {
            comboItens.addItem(item.getNome());
            itemMap.put(item.getNome(), item.getId());
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Erro ao carregar itens: " + ex.getMessage());
        System.err.println("Erro ao carregar itens: " + ex.getMessage());
    }

    gbc.gridx = 0;
    gbc.gridy = 4;
    dialogPanel.add(new JLabel("Quantidade:"), gbc);
    gbc.gridx = 1;
    JTextField txtQuantidade = new JTextField(10);
    dialogPanel.add(txtQuantidade, gbc);

    gbc.gridx = 0;
    gbc.gridy = 5;
    gbc.gridwidth = 2;
    JButton btnAdicionarItem = new JButton("Adicionar Item");
    dialogPanel.add(btnAdicionarItem, gbc);

    DefaultListModel<String> listModel = new DefaultListModel<>();
    JList<String> listItens = new JList<>(listModel);
    gbc.gridx = 0;
    gbc.gridy = 6;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    dialogPanel.add(new JScrollPane(listItens), gbc);

    btnAdicionarItem.addActionListener(event -> {
        String itemNome = (String) comboItens.getSelectedItem();
        String quantidade = txtQuantidade.getText();
        if (itemNome != null && !quantidade.isEmpty()) {
            listModel.addElement(itemNome + ":" + quantidade);
        }
    });

    gbc.gridx = 0;
    gbc.gridy = 7;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.NONE;
    gbc.weightx = 0;
    gbc.weighty = 0;
    JButton btnSalvar = new JButton("Salvar");
    btnSalvar.setBackground(new Color(34, 139, 34));
    btnSalvar.setForeground(Color.WHITE);
    dialogPanel.add(btnSalvar, gbc);

    // Adicionar o listener do botão Salvar antes de exibir o dialog
    btnSalvar.addActionListener(event -> {
        System.out.println("Botão 'Salvar' clicado");
        try {
            // Validação de entrada
            if (txtDataHora.getText().isEmpty() || txtTipo.getText().isEmpty() || txtFuncionario.getText().isEmpty() || listModel.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Todos os campos devem ser preenchidos.");
                return;
            }

            int funcionarioId;
            try {
                funcionarioId = Integer.parseInt(txtFuncionario.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "ID do Funcionário deve ser um número válido.");
                return;
            }

            // Construir a string de itens no formato "item_id:quantidade"
            StringBuilder itensBuilder = new StringBuilder();
            for (int i = 0; i < listModel.size(); i++) {
                String[] itemData = listModel.get(i).split(":");
                String itemNome = itemData[0];
                String quantidade = itemData[1];
                int itemId = itemMap.get(itemNome);
                itensBuilder.append(itemId).append(":").append(quantidade);
                if (i < listModel.size() - 1) {
                    itensBuilder.append(",");
                }
            }

            System.out.println("Tentando adicionar transação...");
            TransacaoDAO transacaoDAO = new TransacaoDAO();
            Transacao transacao = new Transacao(
                0, // ID será gerado automaticamente
                txtDataHora.getText(),
                txtTipo.getText(),
                funcionarioId,
                itensBuilder.toString()
            );
            System.out.println("Dados da transação capturados: " + transacao);
            transacaoDAO.adicionarTransacao(transacao);
            JOptionPane.showMessageDialog(null, "Transação adicionada com sucesso!");
            System.out.println("Transação adicionada com sucesso!");
            dialog.dispose();
            carregarTransacoes(); // Atualiza a tabela após adicionar a transação
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro ao adicionar transação: " + ex.getMessage());
            System.err.println("Erro ao adicionar transação: " + ex.getMessage());
        }
    });

    dialog.add(dialogPanel);

    // Só mostra o dialog depois que o botão Salvar estiver configurado
    dialog.setVisible(true);
});

        searchButton.addActionListener(e -> {
            String tipoTransacao = JOptionPane.showInputDialog("Digite o tipo de transação:");
            if (tipoTransacao != null && !tipoTransacao.isEmpty()) {
                try {
                    TransacaoDAO transacaoDAO = new TransacaoDAO();
                    List<Transacao> transacoes = transacaoDAO.buscarTransacoesPorTipo(tipoTransacao);
                    model.setRowCount(0); // Limpa a tabela antes de adicionar os resultados da busca
                    for (Transacao transacao : transacoes) {
                        model.addRow(new Object[]{
                                transacao.getId(),
                                transacao.getDataHora(),
                                transacao.getTipo(),
                                transacao.getFuncionarioId(),
                                transacao.getItens()
                        });
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao buscar transações: " + ex.getMessage());
                }
            }
        });

        add(panel);

        carregarTransacoes();
    }

    private void carregarTransacoes() {
        try {
            TransacaoDAO transacaoDAO = new TransacaoDAO();
            List<Transacao> transacoes = transacaoDAO.listarTransacoes();
            model.setRowCount(0); // Limpa a tabela antes de adicionar as transações
            for (Transacao transacao : transacoes) {
                model.addRow(new Object[]{
                        transacao.getId(),
                        transacao.getDataHora(),
                        transacao.getTipo(),
                        transacao.getFuncionarioId(),
                        transacao.getItens()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar transações: " + e.getMessage());
        }
    }
}