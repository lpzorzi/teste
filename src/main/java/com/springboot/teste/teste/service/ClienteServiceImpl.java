package com.springboot.teste.teste.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.teste.teste.dao.ClienteDAO;
import com.springboot.teste.teste.entity.Cliente;

@Service
public class ClienteServiceImpl implements ClienteService {

	private ClienteDAO clienteDAO;
	
	@Autowired
	public ClienteServiceImpl (ClienteDAO clienteDAO) {
		this.clienteDAO = clienteDAO;
	}
	
	@Override
	@Transactional
	public List<Cliente> listar() {
		return clienteDAO.listar();
	}

	@Override
	@Transactional
	public Cliente procurarId(int id) {
		return clienteDAO.procurarId(id);
	}

	@Override
	@Transactional
	public void salvar(Cliente cliente) {
		clienteDAO.salvar(cliente);
	}

	@Override
	@Transactional
	public void deletar(int id) {
		clienteDAO.deletar(id);
	}

}
