package com.ps23244.controller.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ps23244.dao.CategoryDAO;
import com.ps23244.entity.Category;

@Controller
@RequestMapping("category")
public class CategoryList {

	public List<Category> Category = new ArrayList<Category>();
	@Autowired
	CategoryDAO dao;

	
	
	@RequestMapping("/list")
	public String index(Model model, @RequestParam("p") Optional<Integer> page) {
		int currentPage = page.orElse(0);
		Pageable pageable = PageRequest.of(currentPage, 5);
		Page<Category> categoryPage = dao.findAll(pageable);
		model.addAttribute("page", categoryPage);
		return "admin/categorylist";
	}

	@GetMapping("/controller")
	public String showCategoryForm(Model model) {
		Category category = new Category();
		model.addAttribute("item", category);
		return "admin/categorycontroller";
	}

	@GetMapping("/edit/{id}")
	public String editCategory(Model model, @PathVariable("id") String id) {
		Category category = dao.findById(id).orElse(null);
		if (category == null) {
			return "redirect:/category/list";
		}
		model.addAttribute("item", category);
		List<Category> items = dao.findAll();
		model.addAttribute("items", items);
		return "admin/categorycontroller";
	}

	@PostMapping("/create")
	public String createCategory(@Valid @ModelAttribute("item") Category category, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			List<Category> items = dao.findAll();
			model.addAttribute("items", items);
			return "admin/categorycontroller";
		}
		dao.save(category);
		return "redirect:/category/list";
	}

	@PostMapping("/update")
	public String updateCategory(@Valid @ModelAttribute("item") Category category, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			List<Category> items = dao.findAll();
			model.addAttribute("items", items);
			return "admin/categorycontroller";
		}
		dao.save(category);
		return "redirect:/category/edit/" + category.getId();
	}

	@GetMapping("/delete/{id}")
	public String deleteCategory(@PathVariable("id") String id) {
		dao.deleteById(id);
		return "redirect:/category/list";
	}
}
