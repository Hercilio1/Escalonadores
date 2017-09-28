package Aula1;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class App {
	
	private static ArrayList<Integer> array;

	public static void main(String[] args) {
		reader("arquivo.txt");
		calcula();
	}
	
	public static void calcula() {
		String oprFinal = "r";
		while(oprFinal.equals("r")) {
			Scanner in = new Scanner(System.in);
			System.out.println("Infome a operação qual operacão vc deseja (+/*): ");
			String opr = in.nextLine();
			if(opr.equals("*")) System.out.println(multiplica(opr));
			else if(opr.equals("+")) System.out.println(soma(opr));
			else System.out.println(opr + " é uma operação inválida");
			System.out.println("- Se deseja fazer com outra operação digite 'r'.");
			System.out.println("- Se deseja encerrar o programa digite qualquer outro caracter.");
			oprFinal = in.nextLine();
		}
		System.out.println("Operação finalizada!!!");
	}
	
	public static String multiplica(String opr){
		String result = "";
		int mult = 1;
		for(int i = 0; i < array.size(); i++) {
			result += array.get(i);
			if(array.size() > i + 1) result += " * ";
			else result += " = ";
			mult = mult * array.get(i);
		}
		return result += mult;		
	}
	
	public static String soma(String opr){
		String result = "";
		int soma = 0;
		for(int i = 0; i < array.size(); i++) {
			result += array.get(i);
			if(array.size() > i + 1) result += " + ";
			else result += " = ";
			soma += array.get(i);
		}
		return result += soma;		
	}
	
	public static void reader(String arquivo) {
		array = new ArrayList<Integer>();
		Path path1 = Paths.get(arquivo);
		try (BufferedReader reader = Files.newBufferedReader(path1, Charset.defaultCharset())) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				array.add(Integer.parseInt(line));
			}
		}
		catch (IOException e) {
			System.err.format("Erro de E/S: %s%n", e);
		}
	}

}
