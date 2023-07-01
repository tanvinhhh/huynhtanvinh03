package com.ps23244.controller.admin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ps23244.dao.AccountDAO;
import com.ps23244.entity.Account;




@Controller
@RequestMapping("account")
public class AccountList {
	@Autowired
	AccountDAO dao;
	
	
	@RequestMapping("/list")
	public String index(Model model , @RequestParam("p") Optional<Integer> p) {
		Account item = new Account();
		model.addAttribute("item", item);
		
		Pageable pageable = PageRequest.of(p.orElse(0), 5);
		Page<Account> page = dao.findAll(pageable);
		model.addAttribute("page", page);
		return "admin/accountlist";
	}
	
	@RequestMapping("/controller")
	public String control(Model model) {
		Account item = new Account();
		model.addAttribute("item", item);
		return "admin/accountcontroller";
	}
	
	
	@RequestMapping("/edit/{username}")
	public String edit(Model model, @PathVariable("username") String username) {
		Account item = dao.findById(username).get();
		model.addAttribute("item", item);
		List<Account> items = dao.findAll();
		model.addAttribute("items", items);
		return "admin/accountcontroller";
	}

	@RequestMapping("/create")
	public String create(@Valid @ModelAttribute("item") Account item, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			List<Account> items = dao.findAll();
			model.addAttribute("items", items);
			return "admin/accountcontroller";
		}
		dao.save(item);
		return "redirect:/account/list";
	}

	@RequestMapping("/update")
	public String update(@Valid @ModelAttribute("item") Account item, BindingResult bindingResult,
			Model model) {
		if (bindingResult.hasErrors()) {
			List<Account> items = dao.findAll();
			model.addAttribute("items", items);
			return "admin/accountcontroller";
		}
		dao.save(item);
		return "redirect:/account/edit/" + item.getUsername();
	}

	@RequestMapping("/delete/{username}")
	public String create(@PathVariable("username") String username) {
		dao.deleteById(username);
		return "redirect:/account/list";
	}
	
	@ModelAttribute("activateds")
	public Map<Boolean, String>getActivateds(){
		Map<Boolean, String> map = new HashMap<>();
		map.put(true, "Active");
		map.put(false, "Unactive");
		return map;
	}
	
	@ModelAttribute("admins")
	public Map<Boolean, String>getAdmins(){
		Map<Boolean, String> map = new HashMap<>();
		map.put(true, "Admin");
		map.put(false, "Customer");
		return map;
	}
}
