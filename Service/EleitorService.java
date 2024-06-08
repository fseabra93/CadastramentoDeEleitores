/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import DAO.GenericDAO;
import Entity.Eleitor;
import Exception.EleitorNotFoundException;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 * @author flaviorgs
 */
public class EleitorService {
    private GenericDAO<Eleitor> eleitorDAO;

    public EleitorService() {
        this.eleitorDAO = new GenericDAO<>(Eleitor.class);
    }

    public void cadastrarEleitor(Eleitor eleitor) {
        eleitorDAO.save(eleitor);
    }

    public Eleitor buscarEleitorPorId(int id) {
        Eleitor eleitor = eleitorDAO.findById(id);
        if (eleitor == null) {
            throw new EleitorNotFoundException("Eleitor com ID " + id + " não encontrado.");
        }
        return eleitor;
    }

    public List<Eleitor> listarTodosEleitores() {
        return eleitorDAO.findAll();
    }

    public void atualizarEleitor(Eleitor eleitor) {
        eleitorDAO.update(eleitor);
    }

    public void removerEleitor(int id) {
        eleitorDAO.delete(id);
    }

    public List<Eleitor> filtrarEleitores(Predicate<Eleitor> predicate) {
        return listarTodosEleitores().stream().filter(predicate).collect(Collectors.toList());
    }

    public List<Eleitor> ordenarEleitores(java.util.Comparator<Eleitor> comparator) {
        return listarTodosEleitores().stream().sorted(comparator).collect(Collectors.toList());
    }
}
