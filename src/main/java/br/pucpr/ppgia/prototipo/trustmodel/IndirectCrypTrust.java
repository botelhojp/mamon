package br.pucpr.ppgia.prototipo.trustmodel;

import java.util.List;

import br.pucpr.ppgia.prototipo.agents.ClientAgent;
import br.pucpr.ppgia.prototipo.agents.ServerAgent;
import br.pucpr.ppgia.prototipo.trustmodel.vo.Rating;

public class IndirectCrypTrust extends IndirectTrust {

	public IndirectCrypTrust(ClientAgent agent) {
		super(agent);
	}

	@Override
	protected List<Rating> getRatings(ServerAgent server, String term){
		return server.getRatingsCryp(term);
	}
}
