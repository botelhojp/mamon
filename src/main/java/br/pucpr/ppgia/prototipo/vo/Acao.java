package br.pucpr.ppgia.prototipo.vo;

import java.util.HashSet;
import java.util.Set;

public class Acao  implements java.io.Serializable {

	private static final long serialVersionUID = 8457914838257793588L;
	private Long idAcao;
    private String nomeres;
    private String nomefull;
    private Boolean ibovespa;
    private Set<Cotacao> cotacaos = new HashSet<Cotacao>(0);

    public Acao() {
    }

    public Acao(Long idAcao) {
        this.idAcao = idAcao;
    }
    
    public Acao(String nomeres) {
        this.nomeres = nomeres;
    }
    
    public Acao(Long idAcao, String nomeres, Boolean ibovespa, Set<Cotacao> cotacaos) {
       this.idAcao = idAcao;
       this.nomeres = nomeres;
       this.ibovespa = ibovespa;
       this.cotacaos = cotacaos;
    }
    
   
    // Property accessors
    public Long getIdAcao() {
        return this.idAcao;
    }
    
    public void setIdAcao(Long idAcao) {
        this.idAcao = idAcao;
    }
    public String getNomeres() {
        return this.nomeres;
    }
    
    public void setNomeres(String nomeres) {
        this.nomeres = nomeres;
    }
    public Boolean getIbovespa() {
        return this.ibovespa;
    }
    
    public void setIbovespa(Boolean ibovespa) {
        this.ibovespa = ibovespa;
    }
    public Set<Cotacao> getCotacaos() {
        return this.cotacaos;
    }
    
    public void setCotacaos(Set<Cotacao> cotacaos) {
        this.cotacaos = cotacaos;
    } 
	

	public String getNomefull() {
		return nomefull;
	}

	public void setNomefull(String nomefull) {
		this.nomefull = nomefull;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((nomefull == null) ? 0 : nomefull.hashCode());
		result = prime * result + ((nomeres == null) ? 0 : nomeres.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Acao other = (Acao) obj;
		if (nomefull == null) {
			if (other.nomefull != null)
				return false;
		} else if (!nomefull.equals(other.nomefull))
			return false;
		if (nomeres == null) {
			if (other.nomeres != null)
				return false;
		} else if (!nomeres.equals(other.nomeres))
			return false;
		return true;
	}
	
	
}


