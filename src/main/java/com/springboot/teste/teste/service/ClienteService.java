package com.springboot.teste.teste.service;

import java.util.List;

import com.springboot.teste.teste.entity.Cliente;

public interface ClienteService {
	public List<Cliente> listar();
	public Cliente procurarId(int id);
	public void salvar(Cliente cliente);
	public void deletar(int id);

}
