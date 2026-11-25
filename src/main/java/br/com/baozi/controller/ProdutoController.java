package br.com.baozi.controller;
import br.com.baozi.model.Produto;
import br.com.baozi.repository.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    private final ProdutoRepository repository;
    private final PedidoRepository pedidos;
    public ProdutoController(ProdutoRepository repository, PedidoRepository pedidos) { this.repository = repository; this.pedidos = pedidos; }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Produto criar(@RequestBody Produto obj) {
        if (obj.getNome() == null || obj.getNome().isBlank()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Nome obrigatorio");
        obj.setId(null);
        return repository.save(obj);
    }
    @GetMapping
    public List<Produto> listar() { return repository.findAll(); }
    @GetMapping("/{id}")
    public Produto buscar(@PathVariable Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        buscar(id);
        if (pedidos.findAll().stream().anyMatch(p -> id.equals(p.getProdutoId()))) throw new ResponseStatusException(HttpStatus.CONFLICT, "Existe pedido vinculado");
        repository.deleteById(id);
    }
}
