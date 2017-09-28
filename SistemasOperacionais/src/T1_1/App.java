package T1_1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;


public class App {
	
	//http://tutorials.jenkov.com/java-collections/queue.html
	
	private static int numeroDeProcessos, tamFatiaDeTempo;
	private static Processo[] listaProcessos;
	private static Queue<Processo> filaProcessos = new LinkedList();
	private static String sjfSaida = "", rrpSaida = "";
	private static float TE,TR, TT;
	
	
	/********************************************************************************************	   		 
	 * 								    SHORTEST JOB FIRST 										*	
	 ********************************************************************************************/			   		 	 		
	public static void sjf() {
		Processo processoDaVez = null;		//Processo que será analisado		
		int contTempoDeCadaExecucao = 0;	//Relógio do sistema	
		int clock = 0; 						//Auxiliar do relógio	
		int foramExecutados = 0; 			//Número de processos que foram executados
		TE = TR = TT = 0;  					//Medicoes de tempo
		
		//Laco em que será realizado a execucao de todos os processos
		for(int i=0; i<listaProcessos.length; i++) {
			//Verifica se o processo já foi executado e, se não, verifica se já chegou na fila de processos
			if(listaProcessos[i].isPronto() == false && listaProcessos[i].getTempoDeChegada() <= contTempoDeCadaExecucao) {	
				//Se não estiver processo sendo analisado, o processo que será analisado será o proximo da fila
				if(processoDaVez == null) {processoDaVez = listaProcessos[i];}
				// Verificacoes seguindo os requisitos de sjf para um processo executar
				if(listaProcessos[i].getTempoDeExecucao() < processoDaVez.getTempoDeExecucao() || 
						(listaProcessos[i].getTempoDeExecucao() == processoDaVez.getTempoDeExecucao() 
							&& listaProcessos[i].getPrioridade() < processoDaVez.getPrioridade())) {
						//Caso o processo analisado não cumpra algumas das condicoes, "ele vai para o final da fila"
						processoDaVez = listaProcessos[i];
				} 
			}
			// Após analisar todos os processo
			if(i == listaProcessos.length -1) {
				//Caso nenhum processo tenha chego na fila
				if(processoDaVez == null) {
					clock = 0;
					sjfSaida+= "-";
					contTempoDeCadaExecucao++;
				} else { 
					//Calcula o tempo de execucao do SJF
					TE += contTempoDeCadaExecucao - (processoDaVez.getTempoDeChegada());
					//Cria pedaco do grafico relacionado a ao processo selecionado
					for(int j=0; j<processoDaVez.getTempoDeExecucao(); j++) {
						sjfSaida += processoDaVez.getIdentificador();
						contTempoDeCadaExecucao++;
						clock++;
					}
					//Soma ao tempo de Turnaround o tempo em que o processo selecionado parou de executar
					TT += clock;
					//Registra que o processo foi executado
					listaProcessos[processoDaVez.getIdentificador()-1].setPronto(true);
					//Acrescenta um ao numero de executados
					foramExecutados++;
					//Caso nao seja a ultima execucao, registrar troca de contexto
					if(foramExecutados != numeroDeProcessos) {
						sjfSaida+= "TC";
						contTempoDeCadaExecucao+=2;
						clock+=2;
					}
				}
				//Esvazia a váriavel para realizar nova analise
				processoDaVez = null;
				//Faz com que o laço se repita
				i = -1;
			} 			
			//Ao executar todos os processos, finalizar o calculo dos tempos e encerrar metodo
			if(foramExecutados == numeroDeProcessos) {
				TE = TE/numeroDeProcessos;
				TR = TE;
				TT = TT/numeroDeProcessos;
				return;
			}
		}
	}

	/********************************************************************************************	   		 
	 * 									  ROUND ROBIN 											*	
	 ********************************************************************************************/	 				
	public static void rrp() {
		Processo processoDaVez = null;					// Processo que será analisado
		int prioridade = 1;								// Maior prioridade
		int contTempoDeCadaExecucao = 0;				// Relogio do sistema
		int clock = 0; 									// Auxiliar do relogio
		int foramExecutados = 0;						// Número de processos que foram executados
		int contaPostergados = 0;						// Processos que estao com baixa prioridade
		int repeticoesNulas = 0;						// Numero de casos qeu nao podem executar
		float vetor[] = new float[numeroDeProcessos];	// Vetor auxiliar para o calculo do TE
		int processoPassado = 0;						// Variável auxiliar para calculo do TE
		TE = TR = TT = 0;								// Medicoes de tempo
		
		//Laco em que será realizado a execucao de todos os processos
		while(true) {
			//Se todos os processo com alta prioridade já executaram baixa-se um nível de prioridade
			if(contaPostergados == filaProcessos.size()) {
				prioridade++;
				contaPostergados = 0;
				//Liberar variavel utilzada para verficar se já foi registrado como postergado
				for(int i=0; i<filaProcessos.size(); i++) {
					filaProcessos.peek().setPronto(true);
					filaProcessos.add(filaProcessos.peek());
					filaProcessos.remove();
				}
			}	
			//Verifica se o processo ja esta pronto para executar
			if(filaProcessos.peek().getTempoDeChegada() <= contTempoDeCadaExecucao) {
				//Torna ele o processo que sera analisado
				if(processoDaVez == null) {
					processoDaVez = filaProcessos.peek(); 
				}		
				//Verifica se a prioridade de é maior do que a requerida
				if(processoDaVez.getPrioridade() > prioridade && repeticoesNulas <= 0) {
					//se for, verifica se ele ja foi registrado como postergado
					if(processoDaVez.isPronto()) {
						//Se nao, registra.
						contaPostergados++;
						processoDaVez.setPronto(false);
					}
					//Se sim, envia o processo pra final da fila
					filaProcessos.add(processoDaVez);
					filaProcessos.remove();
					processoDaVez = null;	
				}  
				else { //Se a prioridade for menor ou igual que a requerida
					if(processoDaVez.isIndicaInicioDaExecucao() == false) {
						TR += clock -1;
						processoDaVez.setIndicaInicioDaExecucao(true);
						
					}
					if(processoPassado != processoDaVez.getIdentificador()) {
						TE += clock -1 - vetor[filaProcessos.peek().getIdentificador()-1]; 
						vetor[filaProcessos.peek().getIdentificador()-1] = clock + 5;
					}
					for(int j=0; j<tamFatiaDeTempo && processoDaVez.getTempoDeExecucao() > 0 ; j++) {	
						rrpSaida += filaProcessos.peek().getIdentificador();
						filaProcessos.peek().setTempoDeExecucao(processoDaVez.getTempoDeExecucao() - 1);
						contTempoDeCadaExecucao++;
						clock++;
						repeticoesNulas = 0 - (filaProcessos.size());
					} 
					
					if(processoDaVez.getTempoDeExecucao() == 0) { 
						TT += clock -1;
						filaProcessos.remove();
						foramExecutados++;
					} else {
						filaProcessos.add(processoDaVez);
						filaProcessos.remove();
					}	
					if(foramExecutados != numeroDeProcessos) {
						processoPassado = processoDaVez.getIdentificador();
						rrpSaida+= "TC";
						contTempoDeCadaExecucao+=2;
						clock+=2;
					}					
					processoDaVez = null;
				}
			} else {
				//Garante que não vai executar nada depois de passar por todos
				if(repeticoesNulas >= filaProcessos.size()) {
					clock = 0;
					rrpSaida+= "-";
					contTempoDeCadaExecucao++;
				} else { 
					repeticoesNulas++;				
					filaProcessos.add(filaProcessos.peek());
					filaProcessos.remove();
					processoDaVez = null;
				}

			}
			if(foramExecutados == numeroDeProcessos) {
				TR = TR/numeroDeProcessos;
				TE = TE/numeroDeProcessos;
				TT = TT/numeroDeProcessos;
				break;
			}
		}
	}
	
	
	public static void reader(String arquivo) {
		int cont = 0;
		Processo processo;
		Path path1 = Paths.get(arquivo);
		try (BufferedReader reader = Files.newBufferedReader(path1, Charset.defaultCharset())) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				Scanner sc = new Scanner(line).useDelimiter(" ");
				if(cont == 0) {
					numeroDeProcessos = Integer.parseInt(sc.next());
					listaProcessos = new Processo[numeroDeProcessos];
					System.out.println(numeroDeProcessos);
					cont++;
				} else if(cont == 1) {
					tamFatiaDeTempo = Integer.parseInt(sc.next());
					System.out.println(tamFatiaDeTempo);
					cont++;
				} else {
					if(cont >= numeroDeProcessos + 2) break;
					processo = new Processo(Integer.parseInt(sc.next()), 
							Integer.parseInt(sc.next()), Integer.parseInt(sc.next()), false, cont - 1, false);
					filaProcessos.add(processo);
					listaProcessos[cont - 2] = processo;
					cont++;
				}
			}
		}
		catch (IOException e) {
			System.err.format("Erro de E/S: %s%n", e);
		}
	}

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Informe o número do teste: ");
		String nTeste = in.nextLine();
		String tituloTeste = "trab-so1-teste" + nTeste + ".txt";
		
		System.out.println("Caso de teste " + tituloTeste);
		reader(tituloTeste);
		for(int i=0; i < filaProcessos.size(); i++) {
			System.out.println(filaProcessos.peek().toString());
			filaProcessos.add(filaProcessos.peek());
			filaProcessos.remove();
		}
		
		System.out.println("\nSJF:");
		sjf();
		System.out.println(sjfSaida);
		System.out.println("\n" + "TE = " + TE);
		System.out.println("TR = " + TR);
		System.out.println("TT = " + TT);
		
		System.out.println("\nRRP:");
		rrp();
		System.out.println(rrpSaida);
		System.out.println("\nTR = " + TR);
		System.out.println("TE = " + TE);
		System.out.println("TT = " + TT);
		System.out.println("\n");
	}

}