package br.pucpr.ppgia.prototipo.agents;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import br.pucpr.ppgia.prototipo.core.Environment;
import br.pucpr.ppgia.prototipo.strategy.Action;
import br.pucpr.ppgia.prototipo.strategy.BadStrategy;
import br.pucpr.ppgia.prototipo.strategy.GoodStrategy;
import br.pucpr.ppgia.prototipo.strategy.IStrategy;
import br.pucpr.ppgia.prototipo.strategy.MaliciousStrategy;
import br.pucpr.ppgia.prototipo.strategy.PerfectStrategy;
import br.pucpr.ppgia.prototipo.strategy.TerribleStrategy;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Rating;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Response;
import br.pucpr.ppgia.prototipo.util.Config;
import br.pucpr.ppgia.prototipo.util.Util;
import br.pucpr.ppgia.prototipo.vo.Acao;

/**
 * Classe de agentes clientes, consumidores de serviçoes
 * 
 * @author vanderson
 */
public class ServerAgent extends CronosAgent {

	private static final long CHANGE_DAY = Config.getChangeDay();
	private static long idResponse = 0;

	private IStrategy strategy;
	private List<Response> responseService = new ArrayList<Response>();

	protected static Logger logger = Logger.getLogger(ServerAgent.class);

	/**
	 * Construtor que recebe o nome do agente o tipo de estratégia utilizada
	 * para processamento das ações
	 * 
	 * @param name
	 *            Nome do agente
	 * @param strategy_type
	 *            Tipo de estratégia de investimento
	 * @param acaoList
	 */
	public ServerAgent(String name) {
		super(name);
		ratings = new Hashtable<String, List<Rating>>();
	}

	/**
	 * A cada dias, modifica o desempenho do agente provedor por meio da
	 * alteração de estratégia.
	 */
	@Override
	public void runDay(Date today) {
		clearRatings();
		long iDay = Environment.getInstance().getDayToInt(today);
		changeStrategy(iDay);
		makeResponses();
	}

	/**
	 * Faz analise finaceira do dia e quarda numa lista que será retorna aos
	 * agentes clientes que solicitarem este servico
	 */
	private void makeResponses() {
		responseService.clear();
		Date today = Environment.getInstance().getToday();
		for (Acao acao : Environment.getInstance().getAcoes()) {
			idResponse++;
			Action value;
			try {
				value = strategy.getRecomentation(acao);
			} catch (NullPointerException e) {
				logger.info("NullPointerException");
				value = Action.NOTHING;
			}
			Response response = new Response(idResponse, this, acao
					.getNomeres(), value, today);
			responseService.add(response);
		}
	}

	/**
	 * Recebe uma solicitação e retorna uma resposta. Otimizado para executar
	 * apenas uma vez ao dia
	 */
	@Override
	public List<Response> getService() {
		return responseService;
	}

	/**
	 * Remove todos os ratings expirados
	 */
	private void clearRatings() {
		Enumeration<String> keys = this.ratings.keys();
		List<Rating> listToRemove = new ArrayList<Rating>();
		while (keys.hasMoreElements()) {
			String term = (String) keys.nextElement();
			List<Rating> listRating = ratings.get(term);
			for (Rating rating : listRating) {
				if (rating.isExpired()) {
					listToRemove.add(rating);
				}
			}
			for (Rating rating2 : listToRemove) {
				listRating.remove(rating2);
			}
		}
	}

	private void changeStrategy(long iDay) {
		if (iDay > 0 && iDay % CHANGE_DAY == 0 && Config.isServerChange()) { // Hora
																				// de
																				// Trocar
																				// as
																				// estrategias

			if (strategy.getClass().equals(PerfectStrategy.class))
				strategy = new TerribleStrategy(strategy.getAgent());

			else if (strategy.getClass().equals(TerribleStrategy.class))
				strategy = new PerfectStrategy(strategy.getAgent());

			else if (strategy.getClass().equals(GoodStrategy.class))
				strategy = new BadStrategy(strategy.getAgent());

			else if (strategy.getClass().equals(BadStrategy.class))
				strategy = new BadStrategy(strategy.getAgent());
		}
	}

	/**
	 * Recebe um testemunho de um agente cliente
	 * 
	 * @param testemunha
	 *            Agente testemunhador
	 * @param utilidade
	 *            Valor da utilidade
	 */
	public void receiveUtility(Rating rating) {
		String term = rating.getTerm();
		if (ratings.containsKey(getKey(this, term))) {
			ratings.get(getKey(this, term)).add(rating);
		} else {
			ArrayList<Rating> lista = new ArrayList<Rating>();
			lista.add(rating);
			ratings.put(getKey(this, term), lista);
		}
	}

	/**
	 * Retorna uma lista de testemunhos ordenados
	 * 
	 * @return
	 */
	public List<Rating> getRatings(String term) {

		List<Rating> lista = getSelectedRatings(term, true);

		if (strategy.getClass().equals(MaliciousStrategy.class)&& lista != null && !lista.isEmpty()) {
			double maior = Double.MIN_NORMAL;
			boolean achou = false;
			Rating ratingMaior = null;
			for (Rating rating : lista) {
				if (rating.getValor() > maior) {
					maior = rating.getValor();
					ratingMaior = rating;
					achou = true;
				}
			}
			if (achou) {
				List<Rating> melhorLista = new ArrayList<Rating>();
				Rating falsoRating = new Rating(ratingMaior.getAgent(),10d, ratingMaior.getResponseDate(), ratingMaior.getTerm(), ratingMaior.getResponseDate());
				//melhorLista.add(ratingMaior);
				melhorLista.add(falsoRating);
				return melhorLista;
			}else{
				return null;
			}
			// Rating melhor = lista.get(0);//TODO Tentar fazer isso!
			// lista.add(melhor);
			// return lista2;
		}
		return lista;
	}

	/**
	 * Retorna uma lista de testemunhos desodenados
	 * 
	 * @return
	 */
	public List<Rating> getRatingsCryp(String term) {
		List<Rating> lista = getSelectedRatings(term, false);
		return lista;
	}

	/**
	 * Retorna a lista de ratings. Para otimizar este processamento há duas
	 * listas que são guardada uma lista ordenada e outra não ordenada. O método
	 * recebe uma das listas e retorna ele caso ela ja tenha sido processada.
	 * Caso contrário a lista é recalculada
	 * 
	 * @param listCache
	 *            Lista ordanda ou não ordenada
	 * @param isOrder
	 *            diz se é ordenada.
	 * @return
	 */
	private List<Rating> getSelectedRatings(String term, boolean isOrder) {
		List<Rating> listRating = ratings.get(getKey(this, term));
		if (listRating != null && !listRating.isEmpty()) {
			if (isOrder) {
				Util.ordenaLista(listRating);
			}
			int num_melhores = listRating.size() / 5;
			ArrayList<Rating> selected = new ArrayList<Rating>();
			for (int i = 0; i < num_melhores; i++) {
				selected.add(listRating.get(listRating.size() - i - 1));
			}
			return selected;
		}
		return null;
	}

	@Override
	public String showState() {
		int count = 0;
		Enumeration<List<Rating>> ratingsList = ratings.elements();
		while (ratingsList.hasMoreElements()) {
			List<Rating> rats = (List<Rating>) ratingsList.nextElement();
			count += rats.size();
		}
		return this.getName() + " ratings[" + count + "]";
	}

	public void setStratety(IStrategy strategia) {
		strategy = strategia;
	}

}
