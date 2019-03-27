package com.springboot.teste.teste.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.teste.teste.entity.Cliente;

@Component
public class ClienteDAOHibernateImpl implements ClienteDAO {
	
	private EntityManager entityManager;

	@Autowired
	public ClienteDAOHibernateImpl (EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public List<Cliente> listar() {
		Session currentSession = entityManager.unwrap(Session.class);
		Query<Cliente> query = currentSession.createQuery("from Cliente", Cliente.class);
		return query.getResultList();
	}

	@Override
	public Cliente procurarId(int id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Cliente cliente = currentSession.get(Cliente.class, id);
		return cliente;
	}

	@Override
	public void salvar(Cliente cliente) {
		Session currentSession = entityManager.unwrap(Session.class);
		currentSession.saveOrUpdate(cliente);
	}

	@Override
	public void deletar(int id) {
		Session currentSession = entityManager.unwrap(Session.class);
		Query query = currentSession.createQuery("DELETE FROM cliente WHERE id=:clienteId");
		query.setParameter("clienteId", id);
		query.executeUpdate();
	}

}
