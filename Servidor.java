package streaming;

import java.io.*;
import java.net.*;//biblioteca para manipular datagramas em rede
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Servidor 
{
	
	public static void main(String[] args)throws IOException
	{
		//Criando o socket do servidor UDP na porta 
        DatagramSocket server_socket = new DatagramSocket(9876);
        
        //impressão na tela para avisar que o servidor está pront para testar o status de streaming
        System.out.println("Servidor pronto para teste de streaming.");
        
        //loop infinito para sempre receber requisições de usuários
        
        while (true) 
        {
        	
            //Buffer para receber dados do Cliente
            byte[] rcvData = new byte[1024];
            
            //Criando um datagrama para receber o pacote do cliente
            DatagramPacket rcvPacket = new DatagramPacket(rcvData, rcvData.length);
            
            //Recebendo o pacote 
            server_socket.receive(rcvPacket);
            
            //Aqui cria-se uma nova thread a cada mensagem recebida
            new Thread(new interacoesCliente(server_socket, rcvPacket)).start();
        }
    }
}



//Classe que lida com a interação com cada cliente em uma thread separada e implementando Runnable da Thread
class interacoesCliente implements Runnable 
{
	//Definindo socket e janela de recebimento de pacotes para cada thread
    private DatagramSocket server_socket;
    private DatagramPacket rcvPacket;
    
 
    public interacoesCliente(DatagramSocket server_socket, DatagramPacket rcvPacket) 
    {
        this.server_socket = server_socket;
        this.rcvPacket = rcvPacket;
    }
    
 
    
    ///funcao para checar validade do email
    public static boolean checa_email( String mensagemCliente, boolean check_email)
    {
    	 String regexEmail = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    	 
    	 Pattern pattern = Pattern.compile(regexEmail);
    	 Matcher matcher = pattern.matcher(mensagemCliente);
    	
    	 check_email = matcher.matches();
    	 
    	return check_email;
    }
    
    ///funcao para checar validade da senha
    public static boolean check_senha( String mensagemCliente, boolean check_senha)
    {
    	 String regexSenha = "^\\d{4}$";
    	 
    	 Pattern pattern = Pattern.compile(regexSenha);
    	 Matcher matcher = pattern.matcher(mensagemCliente);
    	
    	 check_senha = matcher.matches();
    	 
    	return check_senha;
    }
    
    
    
    @Override
    public void run() 
    {
        try 
        {
            //Pegando a mensagem do cliente sem espaços vazios do buffer (trim())
            String mensagemCliente = new String(rcvPacket.getData()).trim();
          
            //impressão sinalizando qual foi a mensagem do cliente
            System.out.println("Mensagem do cliente: " + mensagemCliente);
            
            //Pegando o endereço IP e a porta do cliente
            InetAddress endereco_IP = rcvPacket.getAddress();
            int portaCliente = rcvPacket.getPort();
            
            //checando se eh email
            boolean check_email = false;
            boolean check_senha = false;
            
            
            if(mensagemCliente.equals("check"))
            {
         	   String resposta = "Streaming Checkado";
                
                byte[] sndData = resposta.getBytes();
     	            
     	       //Criando um datagrama de resposta para o cliente
     	       DatagramPacket sndPacket = new DatagramPacket(sndData, sndData.length, endereco_IP, portaCliente);
     	            
     	       //enviando datagrama ao cliente
     	       server_socket.send(sndPacket);
            }
                        
            else if(checa_email( mensagemCliente, check_email))
            {            	
            	String resposta = "E-mail valido! Agora digite sua senha:";
                
                byte[] sndData = resposta.getBytes();
     	            
     	       //Criando um datagrama de resposta para o cliente
     	       DatagramPacket sndPacket = new DatagramPacket(sndData, sndData.length, endereco_IP, portaCliente);
     	            
     	       //enviando datagrama ao cliente
     	       server_socket.send(sndPacket);
            }
            
            else if(check_senha(mensagemCliente, check_senha))
            {
            	String resposta = "Senha valida! Boas vindas!";
                
                byte[] sndData = resposta.getBytes();
     	            
     	       //Criando um datagrama de resposta para o cliente
     	       DatagramPacket sndPacket = new DatagramPacket(sndData, sndData.length, endereco_IP, portaCliente);
     	            
     	       //enviando datagrama ao cliente
     	       server_socket.send(sndPacket);
            }

          
           else
           {
        	   String resposta = "Entrada incorreta!";
               
               byte[] sndData = resposta.getBytes();
    	            
    	       //Criando um datagrama de resposta para o cliente
    	       DatagramPacket sndPacket = new DatagramPacket(sndData, sndData.length, endereco_IP, portaCliente);
    	            
    	       //enviando datagrama ao cliente
    	       server_socket.send(sndPacket);
           }
          
           
	           
           
            
           
           
            
        } 
        //lançando Exception caso necessário
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}