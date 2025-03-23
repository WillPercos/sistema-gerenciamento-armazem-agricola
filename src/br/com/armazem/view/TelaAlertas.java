package br.com.armazem.view;

import br.com.armazem.dao.AlertaDAO;
import br.com.armazem.model.Alerta;
import br.com.armazem.model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TelaAlertas extends JFrame {
    private DefaultTableModel model;
    private Usuario usuario;

    public TelaAlertas(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Alertas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Alertas");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(34, 139, 34));
        topPanel.add(lblTitulo);

        panel.add(topPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Tipo de Alerta", "Data e Hora", "Item Relacionado"};
        model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnMarcarResolvido = new JButton("Marcar como Resolvido");
        btnMarcarResolvido.setBackground(new Color(34, 139, 34));
        btnMarcarResolvido.setForeground(Color.WHITE);
        bottomPanel.add(btnMarcarResolvido);

        JButton btnBuscar = new JButton("Buscar Item");
        btnBuscar.setBackground(new Color(34, 139, 34));
        btnBuscar.setForeground(Color.WHITE);
        bottomPanel.add(btnBuscar);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(34, 139, 34));
        btnVoltar.setForeground(Color.WHITE);
        bottomPanel.add(btnVoltar);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        btnVoltar.addActionListener(e -> {
            new TelaDashboard(usuario).setVisible(true);
            dispose();
        });

        btnMarcarResolvido.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int alertaId = (int) model.getValueAt(selectedRow, 0);
                try {
                    AlertaDAO alertaDAO = new AlertaDAO();
                    alertaDAO.marcarResolvido(alertaId);
                    JOptionPane.showMessageDialog(null, "Alerta marcado como resolvido");
                    carregarAlertas(); // Atualiza a tabela após marcar o alerta como resolvido
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao marcar alerta como resolvido: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(null, "Selecione um alerta para marcar como resolvido");
            }
        });

        btnBuscar.addActionListener(e -> {
            String nomeItem = JOptionPane.showInputDialog("Digite o nome do item:");
            if (nomeItem != null && !nomeItem.isEmpty()) {
                try {
                    AlertaDAO alertaDAO = new AlertaDAO();
                    List<Alerta> alertas = alertaDAO.buscarAlertasPorItem(nomeItem);
                    model.setRowCount(0); // Limpa a tabela antes de adicionar os resultados da busca
                    for (Alerta alerta : alertas) {
                        model.addRow(new Object[]{
                                alerta.getId(),
                                alerta.getTipoAlerta(),
                                alerta.getDataHora(),
                                alerta.getItemId()
                        });
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao buscar alertas: " + ex.getMessage());
                }
            }
        });

        add(panel);

        carregarAlertas();
    }

    private void carregarAlertas() {
        try {
            AlertaDAO alertaDAO = new AlertaDAO();
            List<Alerta> alertas = alertaDAO.listarAlertas();
            model.setRowCount(0); // Limpa a tabela antes de adicionar os alertas
            for (Alerta alerta : alertas) {
                model.addRow(new Object[]{
                        alerta.getId(),
                        alerta.getTipoAlerta(),
                        alerta.getDataHora(),
                        alerta.getItemId()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar alertas: " + e.getMessage());
        }
    }
}