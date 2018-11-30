package Kerb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSCredential;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import sun.misc.BASE64Decoder;

/**
 *
 * @author CaioVitor
 */
public class Servidor {
    
    
private static Oid kerberos5Oid;
  private Subject sujeito;
  
  public static void main( String[] args) {
    try {
     //Pega as propriedades de configuração do arquivo
      Properties props = new Properties();
      
      props.load( new FileInputStream( "server.properties"));
      
      System.setProperty( "sun.security.krb5.debug", "true");
      System.setProperty( "java.security.krb5.realm", props.getProperty( "realm"));
      System.setProperty( "java.security.krb5.kdc", props.getProperty( "kdc"));
      System.setProperty( "java.security.auth.login.config", "./jaas.conf");
      System.setProperty( "javax.security.auth.useSubjectCredsOnly", "true");
      String password = props.getProperty( "service.password");
      
      
      //Usa o kerberos versão 5
      kerberos5Oid = new Oid( "1.2.840.113554.1.2.2");//Versão do objeto
      
      Servidor server = new Servidor();
      server.login( password);
      
      byte ticketServico[] = carregarTokenDoDiscoEDecodificar();

      String clientName = server.aceitarContextoSeguroEPegaNome( ticketServico);
      System.out.println( "\nContexto seguro iniciado!");
      System.out.println( "\nOlá cliente: " + clientName + "!");
    }
    catch ( LoginException e) {
      e.printStackTrace();
      System.err.println( "Erro enquanto realizava login com o JAAS");
      System.exit( -1);
    }
    catch ( GSSException e) {
      e.printStackTrace();
      System.err.println( "Erro durante a inicialização do contexto de segurança");
      System.exit( -1);
    }
    catch ( IOException e) {
      e.printStackTrace();
      System.err.println( "Erro de IO");
      System.exit( -1);
    }
  }

  private static byte[] carregarTokenDoDiscoEDecodificar() throws IOException {
    //Abre o token de segurança
    
    BufferedReader in = new BufferedReader( new FileReader( "security.token.txt"));
    System.out.println( new File( "security.token").getAbsolutePath());
    String str;
    StringBuilder buffer = new StringBuilder();
    while ((str = in.readLine()) != null) {
       buffer.append(str).append("\n");
    }
    in.close();
    System.out.println( "Token lido:" +buffer.toString());
    BASE64Decoder decoder = new BASE64Decoder();
    return decoder.decodeBuffer( buffer.toString());
  }

  
  
  // Faz login no KDC usando a API JAAS
  private void login( String password) throws LoginException {
    LoginContext loginCtx = null;
    // "Servidor" se refere ao identificador no arquivo de configuração
    loginCtx = new LoginContext( "Servidor", 
        new LoginCallbackHandler( password));
    loginCtx.login();
    this.sujeito = loginCtx.getSubject();
  }
  

  private String aceitarContextoSeguroEPegaNome( final byte[] serviceTicket) 
      throws GSSException {
    kerberos5Oid = new Oid( "1.2.840.113554.1.2.2");

    return Subject.doAs(sujeito, (PrivilegedAction<String>) () -> {
        try {
            //
            GSSManager manager = GSSManager.getInstance();
            GSSContext context = manager.createContext( (GSSCredential) null);
            context.acceptSecContext( serviceTicket, 0, serviceTicket.length);
            
            //Retorna o nome da fonte da requisição do contexto seguro
            return context.getSrcName().toString();
        }
        catch ( Exception e) {
            e.printStackTrace();
            return null;
        }
    });
  }
}
