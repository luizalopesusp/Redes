package streaming;

import java.util.Scanner;
import java.io.*;
import java.net.*;//biblioteca para manipular datagramas em rede
import java.lang.*;

public class Cliente 
{

	public static void  envia_ao_servidor(String mensagem,DatagramSocket cliente_socket,InetAddress endereco_IP) throws IOException
	{
		  //Colcoando bytes da mensagem no buffer para envio ao servidor
		   byte[] sndData = mensagem.getBytes();
	
		   //Criando um datagrama para enviar ao servidor
	       DatagramPacket sndPacket = new DatagramPacket(sndData, sndData.length, endereco_IP, 9876);
	
	       //Enviando o datagrama ao servidor
	       cliente_socket.send(sndPacket); 
	}
	
	public static void pega_resposta_servidor (DatagramSocket cliente_socket) throws IOException
	{
		   /////PEGANDO RESPOSTA DO SERVIDOR
	    
		  //Criando buffer de recepção de dados
			 byte[] rcvData = new byte[1024];
			 
			//Criando um datagrama para receber a resposta do servidor
		    DatagramPacket rcvPacket = new DatagramPacket(rcvData, rcvData.length);
	
		    //Recebendo a resposta do servidor
		    cliente_socket.receive(rcvPacket);
	
		    //Convertendo a resposta recebida para string e exibindo
		    String resposta_servidor = new String(rcvPacket.getData()).trim();//utilizando método trim() para garantir que não virá espaços vazios do buffer
		    System.out.println("Resposta do servidor: " + resposta_servidor);
	}
	
	public static String pega_resposta_negativa (DatagramSocket cliente_socket, String resposta_servidor) throws IOException
	{
		   /////PEGANDO RESPOSTA DO SERVIDOR
	    
		  //Criando buffer de recepção de dados
			 byte[] rcvData = new byte[1024];
			 
			//Criando um datagrama para receber a resposta do servidor
		    DatagramPacket rcvPacket = new DatagramPacket(rcvData, rcvData.length);
	
		    //Recebendo a resposta do servidor
		    cliente_socket.receive(rcvPacket);
	
		    //Convertendo a resposta recebida para string e exibindo
		    resposta_servidor = new String(rcvPacket.getData()).trim();//utilizando método trim() para garantir que não virá espaços vazios do buffer
		    System.out.println("Resposta do servidor: " + resposta_servidor);
		    
		    return resposta_servidor;
	}
	

//funcao principal
	public static void main(String[] args)throws IOException
	{
	    //Criando o socket do cliente UDP
	    DatagramSocket cliente_socket = new DatagramSocket();
	    
	    //definindo variavel a bool true
		boolean status_udp = true;
		
		//loop infinito para sempre receber dados do servidor
		while(status_udp) 
		{
		
		    //Pegando o endereço IP do servidor
		    InetAddress endereco_IP = InetAddress.getByName("localhost");
		
		 //////////////////////AQUI COMEÇA A INTERAÇÃO DE STREAMING
		  	    		
		    //Criando scanner para ler entrada do usuário
		    Scanner inFromUser = new Scanner(System.in);
		    //faz leitura da mensgem na linha de comando
		    String mensagem = inFromUser.nextLine();
		    
		    
		   while(!mensagem.equals("check")) 
		   {
			   System.out.println("Entrada incorreta. Digite 'check' para iniciar streaming.");
			   
			   inFromUser = new Scanner(System.in);
			   
			   mensagem = inFromUser.nextLine();
			   
		   }
		   
		   //enviando check pro servidor
		   envia_ao_servidor( mensagem,cliente_socket,endereco_IP) ;
		   
		   pega_resposta_servidor (cliente_socket);
		   
		   
		   //////AUTENTICANDO LOGIN
		   
		   System.out.println("Iniciando Login...");
		   System.out.println("---------------------------------------");
		   System.out.println("Digite seu e-mail:");
		   
		   inFromUser = new Scanner(System.in);
		   mensagem = inFromUser.nextLine();
		   
		   envia_ao_servidor( mensagem,cliente_socket,endereco_IP);
	  
		   
		   String resposta_servidor = "null";//inicializando
		   		   
		   
		   while(pega_resposta_negativa (cliente_socket,resposta_servidor).equals("Entrada incorreta!"))
		   {
			   inFromUser = new Scanner(System.in);
			   mensagem = inFromUser.nextLine();
			   
			   envia_ao_servidor( mensagem,cliente_socket,endereco_IP);
			   
			   pega_resposta_negativa (cliente_socket,resposta_servidor);

			   
		   }
		   
		   //pegando a senha
		   inFromUser = new Scanner(System.in);
		   mensagem = inFromUser.nextLine();
		   
		   envia_ao_servidor( mensagem,cliente_socket,endereco_IP);
		   
		   while(pega_resposta_negativa (cliente_socket,resposta_servidor).equals("Entrada incorreta!"))
		   {
			   inFromUser = new Scanner(System.in);
			   mensagem = inFromUser.nextLine();
			   
			   envia_ao_servidor( mensagem,cliente_socket,endereco_IP);
			   
			   pega_resposta_negativa (cliente_socket,resposta_servidor);

			   
		   }
		   
		   
	
		  

		    
		
		    
			///////////////////CHECK PARA FINALIZAÇÃO
				    
			//imprimindo string na tela
			System.out.println("Deseja encerrar sessao? Digite 'S' para Sim e 'N' para Nao");
			
			//Lendo a mensagem do usuário
			String mensagem_finalizacao = inFromUser.nextLine();
			
			if(mensagem_finalizacao.equals("S")) 
			{
				status_udp = false;
			}
		    
		}
		
		//Fechando o socket do cliente
		cliente_socket.close();

    
 }
}