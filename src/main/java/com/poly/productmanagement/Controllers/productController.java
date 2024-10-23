package com.poly.productmanagement.Controllers;

import com.poly.productmanagement.entities.Category;
import com.poly.productmanagement.entities.Product;
import com.poly.productmanagement.repository.CategoryRepository;
import com.poly.productmanagement.repository.productRepository;
import com.poly.productmanagement.service.IServiceProduct;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
@RequestMapping("/products")
public class productController {

    private CategoryRepository cr;
    /*public List<Product> getAllProducts() {
        return productRepo.findAll();
    }*/
    IServiceProduct sp;
    @GetMapping("/all")
    public String getAllProducts(Model m) {
        m.addAttribute("products", sp.getProducts());
        return "Home";
    }
@GetMapping("/home")
public String getProductsByMotCle(@RequestParam(name="mc", defaultValue = "") String mc, Model m) {
    m.addAttribute("products", sp.getProductByMC(mc));
    m.addAttribute("mc", mc);
    return "Home";
}

    @GetMapping("/create")
    public String showCreateProductForm(Model model) {
        model.addAttribute("categories", cr.findAll());
        return "AddProduct";
    }

    @PostMapping("/add")
    public String createProduct(@ModelAttribute Product product) {
        sp.addProduct(product);
        return "redirect:/products/home";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        sp.deleteProduct(id);
        return "redirect:/products/home";
    }
}

