package T1_1;

public class Processo {	
	private int tempoDeChegada, tempoDeExecucao, prioridade;
	private boolean pronto, indicaInicioDaExecucao;
	private int identificador;

	public Processo(int tempoDeChegada, int tempoDeExecucao, int prioridade, boolean pronto, int identificador, boolean indicaInicioDaExecucao) {
		this.tempoDeChegada = tempoDeChegada;
		this.tempoDeExecucao = tempoDeExecucao;
		this.prioridade = prioridade;
		this.pronto = pronto;
		this.identificador = identificador;
		this.indicaInicioDaExecucao = indicaInicioDaExecucao;
	}

	public int getTempoDeChegada() {
		return tempoDeChegada;
	}

	public int getTempoDeExecucao() {
		return tempoDeExecucao;
	}

	public void setTempoDeExecucao(int tempoDeExecucao) {
		this.tempoDeExecucao = tempoDeExecucao;
	}

	public int getPrioridade() {
		return prioridade;
	}

	public boolean isPronto() {
		return pronto;
	}

	public void setPronto(boolean pronto) {
		this.pronto = pronto;
	}

	public int getIdentificador() {
		return identificador;
	}
	
	public boolean isIndicaInicioDaExecucao() {
		return indicaInicioDaExecucao;
	}

	public void setIndicaInicioDaExecucao(boolean indicaInicioDaExecucao) {
		this.indicaInicioDaExecucao = indicaInicioDaExecucao;
	}

	@Override
	public String toString() {
		return tempoDeChegada + " " + tempoDeExecucao + " " + prioridade;
	}
}
