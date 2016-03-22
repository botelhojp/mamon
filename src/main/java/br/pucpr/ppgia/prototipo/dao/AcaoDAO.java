package br.pucpr.ppgia.prototipo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.pucpr.ppgia.prototipo.vo.Acao;
import br.pucpr.ppgia.prototipo.vo.Cotacao;
/**
 * Classe da acesso as informações das açõed
 * @author Vanderson Botêlho
 */
public class AcaoDAO {
	
	private static final int CACHE_SIZE = 2000;
	Hashtable<String, Cotacao> cache;	
	private static AcaoDAO acaoDAO = new AcaoDAO();
	
	/**
	 * Construtor
	 */
	private AcaoDAO()  {
		cache = new Hashtable<String, Cotacao>();
	}

	/**
	 * Retorna instância única da classe
	 * @return
	 */
	public static AcaoDAO getInstance(){
		return acaoDAO;
	}
	
	/**
	 * Insere uma ação na base de dados
	 * @param acao ação
	 * @throws Exception
	 */
	public void insert(Acao acao) {
		Session session = HibernateUtil.getCurrentSession();	
		Acao acaoBusca = findByName(acao.getNomeres());		
		if (acaoBusca != null) {
			session.close();
			throw new RuntimeException("Acao Duplicada");
		}
		session.save(acao);
		session.flush();	
	}

	/**
	 * Atualiza uma ação na base de dados
	 * @param acao
	 */
	public void update(Acao acao){
		Session session = HibernateUtil.getCurrentSession();
		session.update(acao);
		session.flush();
	}

	/**
	 * Atualiza ou insere uma ação na base de dados
	 * @param acao
	 * @throws Exception
	 */
	public void saveOrUpdate(Acao acao) throws Exception {
		Session session = HibernateUtil.getCurrentSession();
		session.saveOrUpdate(acao);
		session.flush();
	}

	/**
	 * Remove uma ação na base de dados
	 * @param acao
	 * @throws Exception
	 */
	public void delete(Acao acao) throws Exception {
		Session session = HibernateUtil.getCurrentSession();
		session.delete(acao);
		session.flush();
	}

	/**
	 * Retorna uma lista com todas as açõe na base de dados
	 * @return
	 */
	public List<Acao> list() {
		return this.list(null, null);
	}

	/**
	 * Retorna uma lista paginada, ou seja, apenas uma página
	 * @param numPagina Número da página
	 * @param qtdPagina Quantidade de páginas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Acao> list(Long numPagina, Long qtdPagina) {
		Session session = HibernateUtil.getCurrentSession();
		List<Acao> coll = new ArrayList<Acao>();		
		Criteria q = session.createCriteria(Acao.class);
		if (qtdPagina != null && numPagina != null) {
			q.setMaxResults(qtdPagina.intValue());
			q.setFirstResult((numPagina.intValue() - 1)
					* qtdPagina.intValue());
		}
		coll = q.list();	
		return coll;
	}

	/**
	 * Procura uma ação dado seu nome resumido
	 * @param string
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Acao findByName(String string) {
		Session session = HibernateUtil.getCurrentSession();
		List<Acao> lista = session.createCriteria(Acao.class).add(
				Restrictions.eq("nomeres", string)).list();		
		if (lista != null && lista.size() > 0)
			return lista.get(0);
		return null;
	}


	/**
	 * Retorna uma coleção e cotações de forma paginada
	 * @param qtdPagina
	 * @param numPagina
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Cotacao> getCotacoes(Long qtdPagina, Long numPagina) {
		Session session = HibernateUtil.getCurrentSession();
		List<Cotacao> coll = new ArrayList<Cotacao>();		
		Criteria q = session.createCriteria(Cotacao.class);
		if (qtdPagina > 0 && numPagina > 0) {
			q.setMaxResults(qtdPagina.intValue());
			q.setFirstResult((numPagina.intValue() - 1)
					* qtdPagina.intValue());
		}
		coll = q.list();		
		return coll;
	}
	
	/**
	 * Retorna todas as cotações para um determinada ação
	 * @param acao
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Cotacao> getCotacoes(Acao acao){
		Session session = HibernateUtil.getCurrentSession();		
		Acao objAcao = findByName(acao.getNomeres());		
		return session.createCriteria(Cotacao.class)
			.addOrder(Order.asc("datapre"))
			.add(Restrictions.eq("acao", objAcao))
			.list();
	}	

	/**
	 * Retorna a cotação de uma ação para um determinado dia
	 * @param acao
	 * @param data
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Cotacao getCotacao(Acao acao, Date data){
		Cotacao cotacao = null;
		String chave = acao.toString() + data.toString();
		if (cache.containsKey(chave)){
			return cache.get(chave);
		}		
		Session session = HibernateUtil.getCurrentSession();		
		Acao objAcao = findByName(acao.getNomeres());		
		List<Cotacao> lista = (List<Cotacao>) session.createCriteria(Cotacao.class)
												.addOrder(Order.asc("datapre"))
												.add(Restrictions.eq("acao", objAcao))
												.add(Restrictions.eq("datapre", data))
												.list();
		if (lista != null && lista.size() == 1){
			cotacao = lista.get(0);
			cache.put(chave, cotacao);
			if (cache.size() > CACHE_SIZE){
				cache.clear();
			}		
		}
		return cotacao;		
	}

	
}
