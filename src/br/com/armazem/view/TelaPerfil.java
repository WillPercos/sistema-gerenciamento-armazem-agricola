package br.com.armazem.view;

import br.com.armazem.dao.FuncionarioDAO;
import br.com.armazem.model.Funcionario;
import br.com.armazem.model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class TelaPerfil extends JFrame {
    private JTextField txtNome;
    private JTextField txtSobrenome;
    private JTextField txtCargo;
    private JTextField txtEmail;
    private JTextField txtTelefone;
    private Usuario usuario;

    public TelaPerfil(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Perfil");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Título
        JLabel lblTitulo = new JLabel("Perfil", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(34, 139, 34)); // Cor verde
        lblTitulo.setForeground(Color.WHITE); // Texto branco
        panel.add(lblTitulo, BorderLayout.NORTH);

        // Painel de informações do perfil
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(5, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblNome = new JLabel("Nome:");
        txtNome = new JTextField();
        JLabel lblSobrenome = new JLabel("Sobrenome:");
        txtSobrenome = new JTextField();
        JLabel lblCargo = new JLabel("Cargo:");
        txtCargo = new JTextField();
        JLabel lblEmail = new JLabel("Email:");
        txtEmail = new JTextField();
        JLabel lblTelefone = new JLabel("Telefone:");
        txtTelefone = new JTextField();

        infoPanel.add(lblNome);
        infoPanel.add(txtNome);
        infoPanel.add(lblSobrenome);
        infoPanel.add(txtSobrenome);
        infoPanel.add(lblCargo);
        infoPanel.add(txtCargo);
        infoPanel.add(lblEmail);
        infoPanel.add(txtEmail);
        infoPanel.add(lblTelefone);
        infoPanel.add(txtTelefone);

        panel.add(infoPanel, BorderLayout.CENTER);

        // Painel de botões
        JPanel buttonPanel = new JPanel();
        JButton btnAtualizar = new JButton("Atualizar");
        btnAtualizar.setBackground(new Color(34, 139, 34)); // Cor verde
        btnAtualizar.setForeground(Color.WHITE); // Texto branco
        JButton btnSalvar = new JButton("Salvar");
        btnSalvar.setBackground(new Color(34, 139, 34)); // Cor verde
        btnSalvar.setForeground(Color.WHITE); // Texto branco

        // Botão de voltar ao dashboard
        JButton btnVoltar = new JButton("Voltar");
        btnVoltar.setBackground(new Color(34, 139, 34)); // Cor verde
        btnVoltar.setForeground(Color.WHITE); // Texto branco
        btnVoltar.addActionListener(e -> {
            new TelaDashboard(usuario).setVisible(true);
            dispose();
        });

        buttonPanel.add(btnAtualizar);
        buttonPanel.add(btnSalvar);
        buttonPanel.add(btnVoltar);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnAtualizar.addActionListener(e -> {
            // Lógica para preencher os campos com os dados atuais do usuário
            try {
                FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
                Funcionario funcionario = funcionarioDAO.buscarFuncionarioPorId(usuario.getFuncionarioId());
                if (funcionario != null) {
                    txtNome.setText(funcionario.getNome());
                    txtSobrenome.setText(funcionario.getSobrenome());
                    txtCargo.setText(funcionario.getCargo());
                    txtEmail.setText(funcionario.getEmail());
                    txtTelefone.setText(funcionario.getTelefone());
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro ao buscar dados do funcionário: " + ex.getMessage());
            }
        });

      btnSalvar.addActionListener(e -> {
    // Lógica para salvar os dados atualizados do usuário
    System.out.println("Botão 'Salvar' clicado");
    try {
        // Capturar os dados do formulário
        String nome = txtNome.getText();
        String sobrenome = txtSobrenome.getText();
        String cargo = txtCargo.getText();
        String email = txtEmail.getText();
        String telefone = txtTelefone.getText();

        // Exibir os dados capturados no console para depuração
        System.out.println("Dados capturados:");
        System.out.println("Nome: " + nome);
        System.out.println("Sobrenome: " + sobrenome);
        System.out.println("Cargo: " + cargo);
        System.out.println("Email: " + email);
        System.out.println("Telefone: " + telefone);

        // Criar objeto Funcionario com os dados capturados
        Funcionario funcionario = new Funcionario(
                usuario.getFuncionarioId(),
                nome,
                sobrenome,
                cargo,
                email,
                telefone
        );

        // Atualizar os dados do funcionário no banco de dados
        FuncionarioDAO funcionarioDAO = new FuncionarioDAO();
        funcionarioDAO.atualizarFuncionario(funcionario);

        // Exibir mensagem de sucesso
        JOptionPane.showMessageDialog(null, "Dados atualizados com sucesso!");
        System.out.println("Dados atualizados com sucesso!");
    } catch (SQLException ex) {
        // Exibir mensagem de erro e imprimir stack trace para depuração
        JOptionPane.showMessageDialog(null, "Erro ao atualizar dados do funcionário: " + ex.getMessage());
        System.err.println("Erro ao atualizar dados do funcionário: " + ex.getMessage());
        ex.printStackTrace();
    }
});

        add(panel);
    }
}
