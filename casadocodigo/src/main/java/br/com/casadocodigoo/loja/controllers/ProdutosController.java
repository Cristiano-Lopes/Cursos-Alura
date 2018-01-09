package br.com.casadocodigoo.loja.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.casadocodigoo.loja.daos.ProdutoDAO;
import br.com.casadocodigoo.loja.infra.FileSaver;
import br.com.casadocodigoo.loja.models.Produto;
import br.com.casadocodigoo.loja.models.TipoPreco;
import br.com.casadocodigoo.loja.validation.ProdutoValidation;

@Controller
@RequestMapping("/produtos")
public class ProdutosController {

	@Autowired
	private ProdutoDAO produtoDAO;

	@Autowired
	private FileSaver fileSaver;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(new ProdutoValidation());
	}

	@RequestMapping("/form")
	public ModelAndView form(Produto produto) {
		// mando um obj do model para o view
		ModelAndView modelAndView = new ModelAndView("produtos/form"); // indico a pagina
		modelAndView.addObject("tipos", TipoPreco.values());
		// mando o obj tipo preco para ser exibido pelo jsp

		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView gravar(MultipartFile sumario, @Valid Produto produto, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) { // se aconteceu algum erro volta para o formulario
			return form(produto);
		}

		String path = fileSaver.write("arquivos-sumario", sumario);
		produto.setSumarioPath(path);

		produtoDAO.gravar(produto);

		redirectAttributes.addFlashAttribute("sucesso", "Produto cadastrado com sucesso !");

		return new ModelAndView("redirect:/produtos"); // faço isso para nao ficar em cache e gravar novamente ao F5;
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView listar() {
		List<Produto> produtos = produtoDAO.listar();
		ModelAndView modelAndView = new ModelAndView("produtos/lista");
		modelAndView.addObject("produtos", produtos);
		return modelAndView;
	}

	@RequestMapping("/detalhe")
	public ModelAndView detalhe(Integer id) {
		ModelAndView modelAndView = new ModelAndView("produto/detalhe");
		Produto produto = produtoDAO.find(id);
		modelAndView.addObject("produto", produto);

		return modelAndView;
	}

}