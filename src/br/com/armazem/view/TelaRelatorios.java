package br.com.armazem.view;

import br.com.armazem.dao.RelatorioDAO;
import br.com.armazem.model.Relatorio;
import br.com.armazem.model.Usuario;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TelaRelatorios extends JFrame {
    private DefaultTableModel model;
    private Usuario usuario;

    public TelaRelatorios(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Relatórios");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);

        JLabel lblTitulo = new JLabel("Relatórios");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(34, 139, 34));
        topPanel.add(lblTitulo);

        JButton btnGerar = new JButton("Gerar Relatório");
        btnGerar.setBackground(new Color(75, 110, 175));
        btnGerar.setForeground(Color.WHITE);
        topPanel.add(btnGerar);

        JButton btnExportar = new JButton("Exportar");
        btnExportar.setBackground(new Color(75, 110, 175));
        btnExportar.setForeground(Color.WHITE);
        topPanel.add(btnExportar);

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel graphPanel = new JPanel();
        graphPanel.setBackground(Color.WHITE);
        graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.Y_AXIS));

        JLabel lblGrafico = new JLabel("Gráfico de Rotatividade");
        lblGrafico.setFont(new Font("Arial", Font.BOLD, 18));
        lblGrafico.setAlignmentX(Component.CENTER_ALIGNMENT);
        graphPanel.add(lblGrafico);

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart barChart = ChartFactory.createBarChart(
                "Rotatividade dos Produtos",
                "Produto",
                "Percentual",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(600, 300));
        graphPanel.add(chartPanel);

        panel.add(graphPanel, BorderLayout.CENTER);

        String[] columnNames = {"ID", "Total Transações", "Total Saídas", "Total Entradas", "Data Geração", "Item ID"};
        model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.SOUTH);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(Color.WHITE);

        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(75, 110, 175));
        btnVoltar.setForeground(Color.WHITE);
        bottomPanel.add(btnVoltar);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        btnVoltar.addActionListener(e -> {
            new TelaDashboard(usuario).setVisible(true);
            dispose();
        });

        btnGerar.addActionListener(e -> {
            try {
                RelatorioDAO relatorioDAO = new RelatorioDAO();
                relatorioDAO.gerarRelatorio();
                JOptionPane.showMessageDialog(null, "Relatório Gerado");
                carregarRelatorios(); // Atualiza a tabela após gerar o relatório
                atualizarGrafico(dataset); // Atualiza o gráfico após gerar o relatório
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao gerar relatório: " + ex.getMessage());
            }
        });

        btnExportar.addActionListener(e -> {
            // Lógica para exportar relatório
            JOptionPane.showMessageDialog(null, "Relatório Exportado");
        });

        add(panel);

        carregarRelatorios();
        atualizarGrafico(dataset);
    }

    private void carregarRelatorios() {
        try {
            RelatorioDAO relatorioDAO = new RelatorioDAO();
            List<Relatorio> relatorios = relatorioDAO.listarRelatorios();
            model.setRowCount(0); // Limpa a tabela antes de adicionar os relatórios
            for (Relatorio relatorio : relatorios) {
                model.addRow(new Object[]{
                        relatorio.getId(),
                        relatorio.getTotalTransacoes(),
                        relatorio.getTotalSaidas(),
                        relatorio.getTotalEntradas(),
                        relatorio.getDataGeracao(),
                        relatorio.getItemId()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar relatórios: " + e.getMessage());
        }
    }

    private void atualizarGrafico(DefaultCategoryDataset dataset) {
        try {
            RelatorioDAO relatorioDAO = new RelatorioDAO();
            List<Relatorio> relatorios = relatorioDAO.listarRelatorios();
            dataset.clear(); // Limpa o dataset antes de adicionar novos dados
            for (Relatorio relatorio : relatorios) {
                dataset.addValue(relatorio.getTotalTransacoes(), "Total Transações", "Item " + relatorio.getItemId());
                dataset.addValue(relatorio.getTotalSaidas(), "Total Saídas", "Item " + relatorio.getItemId());
                dataset.addValue(relatorio.getTotalEntradas(), "Total Entradas", "Item " + relatorio.getItemId());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar gráfico: " + e.getMessage());
        }
    }
}