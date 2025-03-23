package br.com.armazem.dao;

import br.com.armazem.model.Funcionario;
import br.com.armazem.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FuncionarioDAO {
    public void adicionarFuncionario(Funcionario funcionario) throws SQLException {
        String sql = "INSERT INTO Funcionario (Nome, Sobrenome, cargo, email) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getSobrenome());
            stmt.setString(3, funcionario.getCargo());
            stmt.setString(4, funcionario.getEmail());
            stmt.executeUpdate();
            System.out.println("Funcionario adicionado com sucesso: " + funcionario);
        } catch (SQLException e) {
            System.err.println("Erro ao adicionar funcionario: " + e.getMessage());
            throw e;
        }
    }

    public List<Funcionario> listarFuncionarios() throws SQLException {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM Funcionario";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Funcionario funcionario = new Funcionario(
                        rs.getInt("id"),
                        rs.getString("Nome"),
                        rs.getString("Sobrenome"),
                        rs.getString("cargo"),
                        rs.getString("email"),
                        "" // Telefone será buscado separadamente
                );
                funcionarios.add(funcionario);
            }
            System.out.println("Funcionarios listados com sucesso");
        } catch (SQLException e) {
            System.err.println("Erro ao listar funcionarios: " + e.getMessage());
            throw e;
        }
        return funcionarios;
    }

    public Funcionario buscarFuncionarioPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Funcionario WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Funcionario funcionario = new Funcionario(
                            rs.getInt("id"),
                            rs.getString("Nome"),
                            rs.getString("Sobrenome"),
                            rs.getString("cargo"),
                            rs.getString("email"),
                            buscarTelefonePorFuncionarioId(id) // Buscar telefone separadamente
                    );
                    System.out.println("Funcionario encontrado: " + funcionario);
                    return funcionario;
                } else {
                    System.out.println("Funcionario com ID " + id + " não encontrado");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar funcionario por ID: " + e.getMessage());
            throw e;
        }
        return null;
    }

    public void atualizarFuncionario(Funcionario funcionario) throws SQLException {
        String sql = "UPDATE Funcionario SET Nome = ?, Sobrenome = ?, cargo = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getNome());
            stmt.setString(2, funcionario.getSobrenome());
            stmt.setString(3, funcionario.getCargo());
            stmt.setString(4, funcionario.getEmail());
            stmt.setInt(5, funcionario.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Funcionario atualizado com sucesso: " + funcionario);
                atualizarTelefoneFuncionario(funcionario.getId(), funcionario.getTelefone());
            } else {
                System.out.println("Nenhum funcionario encontrado com ID " + funcionario.getId());
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar funcionario: " + e.getMessage());
            throw e;
        }
    }

    public void atualizarTelefoneFuncionario(int funcionarioId, String telefone) throws SQLException {
        String sql = "UPDATE Telefone_Funcionario SET telefone_celular = ? WHERE funcionario_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, telefone);
            stmt.setInt(2, funcionarioId);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Telefone do funcionário atualizado com sucesso: ID " + funcionarioId);
            } else {
                System.out.println("Nenhum telefone encontrado para o funcionário com ID " + funcionarioId);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao atualizar telefone do funcionário: " + e.getMessage());
            throw e;
        }
    }

    private String buscarTelefonePorFuncionarioId(int funcionarioId) throws SQLException {
        String sql = "SELECT telefone_celular FROM Telefone_Funcionario WHERE funcionario_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, funcionarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("telefone_celular");
                } else {
                    System.out.println("Nenhum telefone encontrado para o funcionário com ID " + funcionarioId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar telefone do funcionário: " + e.getMessage());
            throw e;
        }
        return "";
    }

    public void deletarFuncionario(int id) throws SQLException {
        String sql = "DELETE FROM Funcionario WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Funcionario deletado com sucesso: ID " + id);
            } else {
                System.out.println("Nenhum funcionario encontrado com ID " + id);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao deletar funcionario: " + e.getMessage());
            throw e;
        }
    }
}