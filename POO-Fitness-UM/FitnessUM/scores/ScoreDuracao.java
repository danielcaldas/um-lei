package scores;

/**
 *Class que agrupa scores de atividades cujo record pessoal � obtido atrav�s da dura��o da atividade.
 * 
 * @author jdc
 * @version 27/05/2014
 */

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import atividades.Atividade;
import atividades.AtvBase;

public class ScoreDuracao implements Serializable {
	// Vari�veis de inst�ncia
	private Map<String, TreeSet<Atividade>> scores;

	// Construtores
	public ScoreDuracao() {
		this.scores = new HashMap<>();
		TreeSet<Atividade> aux = new TreeSet<Atividade>(
				new ScoreTimeComparator());
		this.scores.put("", aux);
	}

	public ScoreDuracao(Map<String, TreeSet<Atividade>> scores) {
		this.scores = new HashMap<>();

		Iterator<Map.Entry<String, TreeSet<Atividade>>> it = scores.entrySet()
				.iterator();
		while (it.hasNext()) {
			Map.Entry<String, TreeSet<Atividade>> entry = it.next();

			TreeSet<Atividade> aux = new TreeSet<>(new ScoreTimeComparator());

			Iterator<Atividade> ittree = entry.getValue().iterator();

			while (ittree.hasNext()) {
				Atividade atv = ittree.next();
				aux.add(atv.clone());
			}

			this.scores.put(entry.getKey(),aux);
		}
	}

	public ScoreDuracao(ScoreDuracao s) {
		this.scores = new HashMap<>();

		Iterator<Map.Entry<String, TreeSet<Atividade>>> it = this.scores
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, TreeSet<Atividade>> entry = it.next();

			TreeSet<Atividade> aux = new TreeSet<>(new ScoreTimeComparator());

			Iterator<Atividade> ittree = entry.getValue().iterator();

			while (ittree.hasNext()) {
				Atividade atv = ittree.next();
				aux.add(atv.clone());
			}

			this.scores.put(entry.getKey(), aux);
		}
	}

	// M�todos de inst�ncia

	// gets

	/**
	 * M�todo que retorma Set de atividades que se comparam por dura��o
	 */
	public TreeSet<Atividade> getAtividadesScore(String nomeatividade) {
		TreeSet<Atividade> aux = new TreeSet<Atividade>(
				new ScoreTimeComparator());

		if (!this.scores.containsKey(nomeatividade)) {
			return aux;
		} else {

			for (Atividade atv : this.scores.get(nomeatividade))
				// iterar sobre os scores da atividade passada como parâmetro
				aux.add(atv.clone());

			return aux;
		}
	}

	// equals, clone e toString
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		else if (o == null || this.getClass() != o.getClass())
			return false;

		else {
			ScoreDuracao sc = (ScoreDuracao) o;

			if (sc.getNumAtividades() != this.getNumAtividades())
				return false;

			else {
				Iterator<Map.Entry<String, TreeSet<Atividade>>> itthis = this.scores
						.entrySet().iterator();
				while (itthis.hasNext()) {
					Map.Entry<String, TreeSet<Atividade>> entry = itthis.next();

					TreeSet<Atividade> aux = getAtividadesScore(entry.getKey());

					Iterator<Atividade> it1 = entry.getValue().iterator();
					Iterator<Atividade> it2 = aux.iterator();

					while (it1.hasNext()) {
						if (!it2.hasNext())
							return false;
						else if (it1.next().getScore() != it2.next().getScore())
							return false;
					}
				}

				return true;
			}
		}
	}

	@Override
	public ScoreDuracao clone() {
		return new ScoreDuracao(this);
	}

	/**
	 * Devolve string com score board do utilizador
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		Iterator<Map.Entry<String, TreeSet<Atividade>>> it = this.scores
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, TreeSet<Atividade>> entry = it.next();
			String nome = entry.getKey();
			TreeSet<Atividade> aux = entry.getValue();

			if (!nome.equals("")) {
				sb.append("\n###### RECORD PESSOAL: " + nome + " ######\n");
			}

			int flag = 0;
			for (Atividade atv : aux) {
				if (flag != 0)
					break;
				else
					flag++;

				DecimalFormat f = new DecimalFormat("##.00");

				sb.append("Dura��o: " + atv.getDuracao() + "\n");
			}
		}

		return sb.toString();
	}

	/**
	 * M�todo que devolve o score board da atividade passada como par�metro
	 */
	public String scoreDadoNomeAtividade(String a) {
		String score = "VAZIO\n";
		if (this.scores.containsKey(a)) {
			TreeSet<Atividade> aux = this.scores.get(a);

			DecimalFormat f = new DecimalFormat("##.00");

			for (Atividade atv : aux) {
				score = ("\n###### RECORD PESSOAL: " + atv.getNome()
						+ " ######\n" + "Dura��o: " + atv.getDuracao() + "\n");
				break;
			}
			return score;
		} else
			return score;
	}

	/**
	 * M�todo que adiciona atividade aos scores de 
	 */
	public void addAtividadeScore(AtvBase a) {
		Atividade atv = (Atividade) a;

		if (atv == null)
			return;

		if (this.scores.containsKey(atv.getNome())) {
			TreeSet<Atividade> aux = this.scores.get(atv.getNome());
			aux.add(atv);
			this.scores.put(atv.getNome(), aux); // repor Set modificado na
													// �rvore
		} else {
			TreeSet<Atividade> aux = new TreeSet<Atividade>(
					new ScoreTimeComparator());
			aux.add(atv);
			this.scores.put(atv.getNome(), aux); // colocar uma nova
													// correspond�ncia na �rvore
		}
	}

	/**
	 * A maior dura��o
	 */
	public double getMelhor(String a) {

		double score = 0;

		if (this.scores.containsKey(a)) {
			TreeSet<Atividade> aux = this.scores.get(a);

			for (Atividade atv : aux) {
				score = atv.getScore().duracaoEmMinutos();
				break;
			}
			return score;
		} else
			return score;
	}

	
	/**
	 * A menor dura��o
	 */
	public double getPior(String a) {

		double score = 0;

		if (this.scores.containsKey(a)) {
			TreeSet<Atividade> aux = this.scores.get(a);

			for (Atividade atv : aux) {
				score = atv.getScore().duracaoEmMinutos();
			}
			return score; // score fica com o Ultimo valor no set, i.e o pior
							// tempo
		} else
			return score;
	}

	
	/**
	 * A dura��o m�dia
	 */
	public double getMed(String a) {

		double media = 0;

		if (this.scores.containsKey(a)) {
			TreeSet<Atividade> aux = this.scores.get(a);

			for (Atividade atv : aux) {
				media += atv.getScore().duracaoEmMinutos(); 
			}

			media = (media / (double) aux.size());

			return media;
		}

		return media;
	}

	/**
	 * Devolve o n� de atividades no set
	 */
	public int getNumAtividades() {
		return this.scores.size();
	}
}
