package com.springboot.teste.teste.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="cliente")
public class Cliente {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	@Column(name="nome")
	private String nome;
	@Column(name="idade")
	private int idade;
	@Column(name="cidade")
	private String cidade;
	@Column(name="min_temp")
	private float minTemp;
	@Column(name="max_temp")
	private float maxTemp;
	
	public Cliente() {
	}

	public Cliente(int id, String nome, int idade, String cidade, float minTemp, float maxTemp) {
		super();
		this.id = id;
		this.nome = nome;
		this.idade = idade;
		this.cidade = cidade;
		this.minTemp = minTemp;
		this.maxTemp = maxTemp;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getIdade() {
		return idade;
	}

	public void setIdade(int idade) {
		this.idade = idade;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public float getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(float minTemp) {
		this.minTemp = minTemp;
	}

	public float getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(float maxTemp) {
		this.maxTemp = maxTemp;
	}
	
}
