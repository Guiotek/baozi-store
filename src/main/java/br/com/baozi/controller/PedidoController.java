package br.com.baozi.controller;
import br.com.baozi.model.Pedido;
import br.com.baozi.repository.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoRepository repository;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;
    public PedidoController(PedidoRepository repository, ClienteRepository clientes, ProdutoRepository produtos) { this.repository = repository; this.clientes = clientes; this.produtos = produtos; }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido criar(@RequestBody Pedido obj) {
        if (obj.getClienteId() == null || obj.getProdutoId() == null || obj.getQuantidade() == null || obj.getQuantidade() <= 0 || !clientes.existsById(obj.getClienteId()) || !produtos.existsById(obj.getProdutoId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pedido invalido");
        obj.setId(null);
        return repository.save(obj);
    }
    @GetMapping
    public List<Pedido> listar() { return repository.findAll(); }
    @GetMapping("/{id}")
    public Pedido buscar(@PathVariable Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)); }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        buscar(id);
        repository.deleteById(id);
    }
}
