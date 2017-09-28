package T1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.LineNumberInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

import javax.naming.CommunicationException;

public class App {
	private static int numeroDeProcessos, tamFatiaDeTempo;
	private static Processo[] listaProcessos;
	private static String sjfSaida = "", rrpSaida = "";
	private static long TE,TR, TT;
	
	
	public static void sjf() {
		/* AQUI ESTÃ� O CLONE
		* Depois que eu setto o processo para pronto = true 
		* altera tanto no aux quanto na listaProcesso.
		* Imagino que ao invÃ©s de usar um aux eu uso listaProcessos msm e 
		* dps de executar o sjf eu setto tudo para false. 
		*/
		Processo processoDaVez = null;
		int contTempoDeCadaExecucao = 1;
		int tc = 2;
		int foramExecutados = 0;
		int aux2 = 0;
		TE = TR = TT = 0;
		
		for(int i=0; i<listaProcessos.length; i++) {			
			if(listaProcessos[i].isPronto() == false && listaProcessos[i].getTempoDeChegada() <= contTempoDeCadaExecucao) {	
				if(processoDaVez == null) {processoDaVez = listaProcessos[i];}
				if(listaProcessos[i].getTempoDeExecucao() < processoDaVez.getTempoDeExecucao() || 
						(listaProcessos[i].getTempoDeExecucao() == processoDaVez.getTempoDeExecucao() 
							&& listaProcessos[i].getPrioridade() <= processoDaVez.getPrioridade())) {
					processoDaVez = listaProcessos[i];
					aux2 = i;
				} 
			}			
			if(i == listaProcessos.length -1) {
				if(processoDaVez == null) {
					sjfSaida+= "-";
					contTempoDeCadaExecucao++;
				} else { 
					for(int j=0; j<processoDaVez.getTempoDeExecucao(); j++) {
						sjfSaida += aux2+1;
						contTempoDeCadaExecucao++;
					}
					listaProcessos[aux2].setPronto(true);
					foramExecutados++;
					if(foramExecutados != numeroDeProcessos) {
						sjfSaida+= "TC";
						contTempoDeCadaExecucao+=tc;
					}
				}
				TE = contTempoDeCadaExecucao;
				processoDaVez = null;
				i = -1;
			} 			
			if(foramExecutados == numeroDeProcessos) {
				TR = TE;
				return;
			}
		}
	}
	
	public static void rrp() {
		//AQUI ESTÃ� O CLONE
		////Depois que eu setto o processo para pronto = true 
		//altera tanto no aux quanto na listaProcesso.
		Processo processoDaVez = null;
		int contTempoDeCadaExecucao = 1;
		int tc = 2;
		int foramExecutados = 0;
		int aux2 = 0;
		TE = TR = TT = 0;
		
		for(int i=0; i<listaProcessos.length; i++) {			
			if(listaProcessos[i].isPronto() == false && listaProcessos[i].getTempoDeChegada() <= contTempoDeCadaExecucao) {	
				if(processoDaVez == null) {processoDaVez = listaProcessos[i];}
				if(listaProcessos[i].getPrioridade() < processoDaVez.getPrioridade()) {
					processoDaVez = listaProcessos[i];
					aux2 = i;
				} 
			}			
			if(i == listaProcessos.length -1) {
				if(processoDaVez == null) {
					rrpSaida+= "-";
					contTempoDeCadaExecucao++;
				} else { 
					for(int j=0; j<tamFatiaDeTempo; j++) {
						rrpSaida += aux2+1;
						contTempoDeCadaExecucao++;
					}
					processoDaVez.setTempoDeExecucao(processoDaVez.getTempoDeExecucao() - tamFatiaDeTempo);
					if(processoDaVez.getTempoDeExecucao() == 0) { 
						listaProcessos[aux2].setPronto(true); 
						foramExecutados++;
					}
					if(foramExecutados != numeroDeProcessos) {
						rrpSaida+= "TC";
						contTempoDeCadaExecucao+=tc;
					}
				}
				TE = contTempoDeCadaExecucao;
				processoDaVez = null;
				i = -1;
			} 			
			if(foramExecutados == numeroDeProcessos) {
				TR = TE;
				return;
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
					listaProcessos = new Processo [numeroDeProcessos];
					System.out.println(numeroDeProcessos);
					cont++;
				} else if(cont == 1) {
					tamFatiaDeTempo = Integer.parseInt(sc.next());
					System.out.println(tamFatiaDeTempo);
					cont++;
				} else {
					if(cont >= numeroDeProcessos + 2) break;
					processo = new Processo(Integer.parseInt(sc.next()), 
							Integer.parseInt(sc.next()), Integer.parseInt(sc.next()), false);
					listaProcessos[cont-2] = processo;
					cont++;
				}
			}
		}
		catch (IOException e) {
			System.err.format("Erro de E/S: %s%n", e);
		}
	}

	public static void main(String[] args) {
		reader("trab-so1-teste1.txt");
		for(int i=0; i < listaProcessos.length; i++) {
			System.out.println(listaProcessos[i].toString());
		}
		System.out.println("\n\n\n");
		sjf();
		System.out.println(sjfSaida);
		System.out.println("\n" + "TE = " + TE);
		System.out.println("TR = " + TR);
		System.out.println("\n\n");
		
		for(int i=0; i < listaProcessos.length; i++) {
			listaProcessos[i].setPronto(false);
		}
		
		rrp();
		System.out.println(rrpSaida);
	}

}