package br.pucpr.ppgia.prototipo.vo;

import java.util.Date;

import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.dao.AcaoDAO;

public class Cotacao  implements java.io.Serializable {
	
	private static final long serialVersionUID = 5015670764633320404L;
	private Long idCotacao;
    private Acao acao;
    private Date datapre;
    private Float preabe;
    private Float premax;
    private Float premin;
    private Float premed;
    private Float preult;
    


	//Media Movel
    private Float preultmv5;
    private Float preultmv10;    
    private Float preultmv20;
    //Estocastico
    private Float preultESMax;
    private Float preultESMin;
    
    //PREOFC PREÇO DA MELHOR OFERTA DE COMPRA DO PAPEL-MERCADO
    private Float preofc;
    //PREOFV - PREÇO DA MELHOR OFERTA DE VENDA DO PAPEL-MERCADO
    private Float preofv;
    //TOTNEG - NEG. - NÚMERO DE NEGÓCIOS EFETUADOS COM O PAPELMERCADO NO PREGÃO
    private Long totneg;
    //QUATOT - QUANTIDADE TOTAL DE TÍTULOS NEGOCIADOS NESTE PAPELMERCADO
    private Long quatot;
    //VOLTOT - VOLUME TOTAL DE TÍTULOS NEGOCIADOS NESTE PAPEL-MERCADO
    private Long votot;

    
    
    /**
     * Retorna a cotaÃ§Ã£o anterior, ou seja, um dia atras
     * @return Cotacao anterior a cotaÃ§Ã£o corrente
     */
    public Cotacao getAnterior(){
		return getAnterior(1);
    }
    
    /**
     * Retorna a cotacao n anterior
     * @param days NÃºmero de dias
     * @return Cotacao
     */
    public Cotacao getAnterior(int days){
		Date dataAnteior = Environment.getInstance().getAddDay(this.getDatapre(), -days);
		//return AcaoDAO.getInstance().getCotacao(acao, dataAnteior);
		return AcaoDAO.getInstance().getCotacao(acao, dataAnteior);
    }
    
	public Float getPreultmv5() {
		return preultmv5;
	}

	public void setPreultmv5(Float preultmv5) {
		this.preultmv5 = preultmv5;
	}

	public Float getPreultmv10() {
		return preultmv10;
	}

	public void setPreultmv10(Float preultmv10) {
		this.preultmv10 = preultmv10;
	}

    /** default constructor */
    public Cotacao() {
    }

	/** minimal constructor */
    public Cotacao(Long idCotacao) {
        this.idCotacao = idCotacao;
    }
    /** full constructor */
    public Cotacao(Long idCotacao, Acao acao, Date datapre, Float preabe, Float premax, Float premin, Float premed, Float preult, Float preofc, Float preofv, Long totneg, Long quatot, Long votot) {
       this.idCotacao = idCotacao;
       this.acao = acao;
       this.datapre = datapre;
       this.preabe = preabe;
       this.premax = premax;
       this.premin = premin;
       this.premed = premed;
       this.preult = preult;
       this.preofc = preofc;
       this.preofv = preofv;
       this.totneg = totneg;
       this.quatot = quatot;
       this.votot = votot;
    }
    
    public Cotacao(Date datapre) {
        this.datapre = datapre;
     }
    
   
    // Property accessors
    public Long getIdCotacao() {
        return this.idCotacao;
    }
    
    public void setIdCotacao(Long idCotacao) {
        this.idCotacao = idCotacao;
    }
    public Acao getAcao() {
        return this.acao;
    }
    
    public void setAcao(Acao acao) {
        this.acao = acao;
    }
    public Date getDatapre() {
        return this.datapre;
    }
    
    public void setDatapre(Date datapre) {
        this.datapre = datapre;
    }
    public Float getPreabe() {
        return this.preabe;
    }
    
    public void setPreabe(Float preabe) {
        this.preabe = preabe;
    }
    public Float getPremax() {
        return this.premax;
    }
    
    public void setPremax(Float premax) {
        this.premax = premax;
    }
    public Float getPremin() {
        return this.premin;
    }
    
    public void setPremin(Float premin) {
        this.premin = premin;
    }
    public Float getPremed() {
        return this.premed;
    }
    
    public void setPremed(Float premed) {
        this.premed = premed;
    }
    public Float getPreult() {
        return this.preult;
    }
    
    public void setPreult(Float preult) {
        this.preult = preult;
    }
    public Float getPreofc() {
        return this.preofc;
    }
    
    public void setPreofc(Float preofc) {
        this.preofc = preofc;
    }
    public Float getPreofv() {
        return this.preofv;
    }
    
    public void setPreofv(Float preofv) {
        this.preofv = preofv;
    }
    public Long getTotneg() {
        return this.totneg;
    }
    
    public void setTotneg(Long totneg) {
        this.totneg = totneg;
    }
    public Long getQuatot() {
        return this.quatot;
    }
    
    public void setQuatot(Long quatot) {
        this.quatot = quatot;
    }
    public Long getVotot() {
        return this.votot;
    }
    
    public void setVotot(Long votot) {
        this.votot = votot;
    }
    
    public Float getPreultESMax() {
		return preultESMax;
	}

	public void setPreultESMax(Float preultESMax) {
		this.preultESMax = preultESMax;
	}

	public Float getPreultESMin() {
		return preultESMin;
	}

	public void setPreultESMin(Float preultESMin) {
		this.preultESMin = preultESMin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((acao == null) ? 0 : acao.hashCode());
		result = prime * result + ((datapre == null) ? 0 : datapre.hashCode());
		result = prime * result
				+ ((idCotacao == null) ? 0 : idCotacao.hashCode());
		result = prime * result + ((preabe == null) ? 0 : preabe.hashCode());
		result = prime * result + ((premax == null) ? 0 : premax.hashCode());
		result = prime * result + ((premed == null) ? 0 : premed.hashCode());
		result = prime * result + ((premin == null) ? 0 : premin.hashCode());
		result = prime * result + ((preofc == null) ? 0 : preofc.hashCode());
		result = prime * result + ((preofv == null) ? 0 : preofv.hashCode());
		result = prime * result + ((preult == null) ? 0 : preult.hashCode());
		result = prime * result
				+ ((preultmv10 == null) ? 0 : preultmv10.hashCode());
		result = prime * result
				+ ((preultmv5 == null) ? 0 : preultmv5.hashCode());
		result = prime * result + ((quatot == null) ? 0 : quatot.hashCode());
		result = prime * result + ((totneg == null) ? 0 : totneg.hashCode());
		result = prime * result + ((votot == null) ? 0 : votot.hashCode());
		return result;
	}
	
	
	
	public int compareTo(Cotacao c){
		int r = getPreult().compareTo(c.getPreult());
		return r;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Cotacao other = (Cotacao) obj;
		if (acao == null) {
			if (other.acao != null)
				return false;
		} else if (!acao.equals(other.acao))
			return false;
		if (datapre == null) {
			if (other.datapre != null)
				return false;
		} else if (!datapre.equals(other.datapre))
			return false;
		if (idCotacao == null) {
			if (other.idCotacao != null)
				return false;
		} else if (!idCotacao.equals(other.idCotacao))
			return false;
		if (preabe == null) {
			if (other.preabe != null)
				return false;
		} else if (!preabe.equals(other.preabe))
			return false;
		if (premax == null) {
			if (other.premax != null)
				return false;
		} else if (!premax.equals(other.premax))
			return false;
		if (premed == null) {
			if (other.premed != null)
				return false;
		} else if (!premed.equals(other.premed))
			return false;
		if (premin == null) {
			if (other.premin != null)
				return false;
		} else if (!premin.equals(other.premin))
			return false;
		if (preofc == null) {
			if (other.preofc != null)
				return false;
		} else if (!preofc.equals(other.preofc))
			return false;
		if (preofv == null) {
			if (other.preofv != null)
				return false;
		} else if (!preofv.equals(other.preofv))
			return false;
		if (preult == null) {
			if (other.preult != null)
				return false;
		} else if (!preult.equals(other.preult))
			return false;
		if (preultmv10 == null) {
			if (other.preultmv10 != null)
				return false;
		} else if (!preultmv10.equals(other.preultmv10))
			return false;
		if (preultmv5 == null) {
			if (other.preultmv5 != null)
				return false;
		} else if (!preultmv5.equals(other.preultmv5))
			return false;
		if (quatot == null) {
			if (other.quatot != null)
				return false;
		} else if (!quatot.equals(other.quatot))
			return false;
		if (totneg == null) {
			if (other.totneg != null)
				return false;
		} else if (!totneg.equals(other.totneg))
			return false;
		if (votot == null) {
			if (other.votot != null)
				return false;
		} else if (!votot.equals(other.votot))
			return false;
		return true;
	}

	public Float getPreultmv20() {
		return preultmv20;
	}

	public void setPreultmv20(Float preultmv20) {
		this.preultmv20 = preultmv20;
	}

	
    

}


