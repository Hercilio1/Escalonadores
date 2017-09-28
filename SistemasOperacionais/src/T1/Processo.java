package T1;

public class Processo {	
	private int tempoDeChegada, tempoDeExecucao, prioridade;
	private boolean pronto;

	public Processo(int tempoDeChegada, int tempoDeExecucao, int prioridade, boolean pronto) {
		super();
		this.tempoDeChegada = tempoDeChegada;
		this.tempoDeExecucao = tempoDeExecucao;
		this.prioridade = prioridade;
		this.pronto = pronto;
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

	@Override
	public String toString() {
		return tempoDeChegada + " " + tempoDeExecucao + " " + prioridade + " " + pronto;
	}
}
