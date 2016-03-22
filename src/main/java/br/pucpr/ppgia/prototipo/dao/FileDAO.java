package br.pucpr.ppgia.prototipo.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import br.pucpr.ppgia.prototipo.vo.Arquivo;
/**
 * Classe de acesso aos arquivos imortados na base de dados
 * @author Vanderson Bot�lho
 *
 */
public class FileDAO {
	

	public FileDAO()  {		
	}


	/**
	 * Insere um arquivo na base
	 * @param file
	 */
	public void insert(Arquivo file) {
		Session session = HibernateUtil.getCurrentSession();
		Arquivo fileBusca = findByName(file.getNome());		
		if (fileBusca != null) {
			session.close();
			throw new RuntimeException("Arquivo Duplicada");
		}
		session.save(file);
	}

	/**
	 * Atualiza um arquivo na base
	 * @param file
	 * @throws Exception
	 */
	public void update(Arquivo file) throws Exception {
		Session session = HibernateUtil.getCurrentSession();
		Transaction t = session.beginTransaction();
		session.update(file);
		session.flush();
		t.commit();
	}

	/**
	 * Atualizar ou insere um arquivo na base
	 * @param file
	 * @throws Exception
	 */
	public void saveOrUpdate(Arquivo file) throws Exception {
		Session session = HibernateUtil.getCurrentSession();
		Transaction t = session.beginTransaction();
		session.saveOrUpdate(file);
		session.flush();
		t.commit();
	}

	/**
	 * Remove um arquivo na base
	 * @param file
	 * @throws Exception
	 */
	public void delete(Arquivo file) throws Exception {
		Session session = HibernateUtil.getCurrentSession();
		Transaction t = session.beginTransaction();
		session.delete(file);
		session.flush();
		t.commit();
	}

	/**
	 * Retorna toda a lista de arquivos importados na base
	 * @return
	 */
	public List<Arquivo> list() {
		return this.list(null, null);
	}

	@SuppressWarnings("unchecked")
	public List<Arquivo> list(Long numPagina, Long qtdPagina) {
		Session session = HibernateUtil.getCurrentSession();
		List<Arquivo> coll = new ArrayList<Arquivo>();		
		Criteria q = session.createCriteria(Arquivo.class);
		if (qtdPagina != null && numPagina != null) {
			q.setMaxResults(qtdPagina.intValue());
			q.setFirstResult((numPagina.intValue() - 1)
					* qtdPagina.intValue());
		}
		coll = q.list();	
		return coll;
	}

	@SuppressWarnings("unchecked")
	public Arquivo findByName(String string) {
		Session session = HibernateUtil.getCurrentSession();
		List<Arquivo> lista = session.createCriteria(Arquivo.class).add(
				Restrictions.eq("nome", string)).list();		
		if (lista != null && lista.size() > 0)
			return lista.get(0);
		return null;
	}
}
