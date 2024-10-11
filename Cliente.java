package streaming;

import java.util.Scanner;
import java.io.*;
import java.net.*;//biblioteca para manipular datagramas em rede

public class Cliente 
{
	
//Funcao para enviar pacotes ao servidor
public static void envia_ao_servidor(String mensagem,DatagramSocket cliente_socket,InetAddress endereco_IP) throws IOException
{
    //Colcoando bytes da mensagem no buffer para envio ao servidor
    byte[] sndData = mensagem.getBytes();

    //Criando um datagrama para enviar ao servidor
    DatagramPacket sndPacket = new DatagramPacket(sndData, sndData.length, endereco_IP, 9876);

    //Enviando o datagrama ao servidor
    cliente_socket.send(sndPacket);
}

//Funcao para receber dados do servidor
public static void pega_resposta_servidor(DatagramSocket cliente_socket) throws IOException
{
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
	    
	    
	    switch (mensagem)
	    {
	    	case "check":
	    		envia_ao_servidor( mensagem, cliente_socket, endereco_IP); 
	    		pega_resposta_servidor(cliente_socket);
	    	    
	    		
	    }
	    
	    
	   // String resposta_servidor = "ativo";//inicializando variavel
	    
	    ////////CONFIRMANDO STREAMING
	    //imprimindo string na tela
	   
	
	    //Lendo a mensagem do usuário
	    
	    //?? POR QUE QUANDO DÁ STREAMING ATIVO A resposta_servidor fica null???
	    
	    
	    
		//////////////////////CHECK AUTENTICACAO
	    
	

	    
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