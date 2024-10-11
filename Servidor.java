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
        DatagramSocket server_socket = new DatagramSocket(8765);
        
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
    
    //funcao comum para envio de todas os tipos de mensagens
    public void envio_ao_cliente(String resposta, DatagramSocket server_socket,InetAddress endereco_IP,int portaCliente) throws IOException
    {

    	//Definindo buffer de envio de dados ao Cliente e atribuindo a resposta do servidor ao cliente
        byte[] sndData = resposta.getBytes();
        
        //Criando um datagrama de resposta para o cliente
        DatagramPacket sndPacket = new DatagramPacket(sndData, sndData.length, endereco_IP, portaCliente);
        
        //enviando datagrama ao cliente
        server_socket.send(sndPacket);

    }
    
    public static String isValidEmailAddressRegex(String email) {
        String isEmailIdValid = "invalido";
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = "valido";
            }
        }
        return isEmailIdValid;
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
            
            ///////////CHECANDO SE A ENTRADA É UM EMAIL E SE É VALIDO
	            String check_email = mensagemCliente; 
	            
	            isValidEmailAddressRegex(check_email);
	            
	            //se a funcao retornar valido, a mensagem_cliente continua a mesma e passa pelas condições
	            if(check_email.equals("valido")) 
	            {	            	System.out.println("email eh valido");
	            	mensagemCliente = check_email;
	            }
	         ///////////CHECANDO SE A ENTRADA É UM EMAIL E SE É VALIDO

            
            switch (mensagemCliente)
            {
            	case "check":
            		//Resposta para o cliente
                    String resposta_streaming = "Streaming Ativo!";
                    
                    envio_ao_cliente(resposta_streaming, server_socket, endereco_IP, portaCliente);
                    
            	 
            	case "valido":
            		//Resposta para o cliente
                    String resposta_autenticacao = "E-mail valido! Agora digite a sua senha";
                    
                    envio_ao_cliente(resposta_autenticacao, server_socket, endereco_IP, portaCliente);
                
            	
                    
                    
                default:
                	//Resposta para o cliente
                    String resposta_erro = "Entrada incorreta!";
                    
                    envio_ao_cliente(resposta_erro, server_socket, endereco_IP, portaCliente);

                    
            }
            
            
            
           
            
        } 
        //lançando Exception caso necessário
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }
}