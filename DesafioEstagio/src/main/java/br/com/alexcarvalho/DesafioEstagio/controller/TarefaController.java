package br.com.alexcarvalho.DesafioEstagio.controller;

import br.com.alexcarvalho.DesafioEstagio.entity.Tarefa;
import br.com.alexcarvalho.DesafioEstagio.repository.TarefaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaRepository tarefaRepository;

    public TarefaController(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    @PostMapping
    public ResponseEntity<Tarefa> cadastrar(@RequestBody Tarefa tarefa) {
        Tarefa tarefaSalva = tarefaRepository.save(tarefa);
        return ResponseEntity.status(HttpStatus.CREATED).body(tarefaSalva);
    }

    @GetMapping
    public ResponseEntity<List<Tarefa>> listar(@RequestParam(required = false) String responsavel) {
        if(responsavel != null){
            return ResponseEntity.ok(tarefaRepository.findByResponsavelContainingIgnoreCase(responsavel));
        }
        //Aqui é caso ele não tenha passado o nome do responsável
        return ResponseEntity.ok(tarefaRepository.findAll());
    }

    @GetMapping("/data")
    public ResponseEntity<List<Tarefa>> buscaPorData(@RequestParam LocalDate data) {
        return ResponseEntity.ok(tarefaRepository.findByDataEntrega(data));
    }

    @GetMapping("/pendentes")
    public ResponseEntity<List<Tarefa>> listarPendentes(@RequestParam(required = false) String responsavel) {
        if(responsavel != null){
            return ResponseEntity.ok(tarefaRepository.findByConcluidaFalseAndResponsavelContainingIgnoreCase(responsavel));
        }
        return ResponseEntity.ok(tarefaRepository.findByConcluidaFalse());
    }
}
