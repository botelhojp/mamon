package br.pucpr.ppgia.prototipo.vo;

import java.util.Date;

public class Arquivo  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long idFile;
	private String nome;
    private Date data;
    
	public Arquivo() {
		super();
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Arquivo(String nome, Date data) {
		super();
		this.nome = nome;
		this.data = data;
	}
	public Long getIdFile() {
		return idFile;
	}
	public void setIdFile(Long idFile) {
		this.idFile = idFile;
	}
}


