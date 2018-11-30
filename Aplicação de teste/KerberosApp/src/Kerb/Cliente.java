package Kerb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.security.PrivilegedAction;
import java.util.Properties;
import javax.security.auth.Subject;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import org.ietf.jgss.GSSContext;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.GSSManager;
import org.ietf.jgss.GSSName;
import org.ietf.jgss.Oid;
import sun.misc.BASE64Encoder;

/**
 *
 * @author CaioVitor
 */
public class Cliente {

    private static Oid kerberos5Oid;
    private Subject subject;
    private byte[] serviceTicket;

    public static void main(String[] args) {
        try {

            //Pega as propriedades de configuração do arquivo
            Properties props = new Properties();

            //Abre o arquivo
            props.load(new FileInputStream("client.properties"));

            System.setProperty("sun.security.krb5.debug", "true");
            System.setProperty("java.security.krb5.realm", props.getProperty("realm")); //msi
            System.setProperty("java.security.krb5.kdc", props.getProperty("kdc")); //localhost
            System.setProperty("java.security.auth.login.config", "./jaas.conf"); //Arquivo de configuração de parâmetros
            System.setProperty("javax.security.auth.useSubjectCredsOnly", "true");

            String usuario = props.getProperty("client.principal.name");
            String senha = props.getProperty("client.password");

            //Usa o kerberos versão 5
            kerberos5Oid = new Oid("1.2.840.113554.1.2.2");//Versão do objeto
            Cliente client = new Cliente();
            client.login(usuario, senha);
            // Pede o ticket de serviço
            client.iniciaEAbreContextoDeSeguranca(props.getProperty("service.principal.name"));
            // Escreve o ticket no disco para enviar ao servidor
            codificarTicketDeServicoProDisco(client.serviceTicket, "./security.token.txt");
            System.out.println("Ticket de serviço codificado com sucesso");
        } catch (LoginException e) {
            e.printStackTrace();
            System.err.println("Erro enquanto realizava login com o JAAS");
            System.exit(-1);
        } catch (GSSException e) {
            e.printStackTrace();
            System.err.println("Erro durante a inicialização do contexto de segurança");
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Erro de IO");
            System.exit(-1);
        }
    }

    public Cliente() {
        super();
    }

    //Autentica com o Key distribution center
    private void login(String user, String pass) throws LoginException {
        LoginContext loginCtx = new LoginContext("Cliente", //Pega a configuração "Cliente" no arquivo jaas.conf
                new LoginCallbackHandler(user, pass));
        loginCtx.login();
        this.subject = loginCtx.getSubject();
    }

//Inicia um contexto seguro com o serviço
    private void iniciaEAbreContextoDeSeguranca(String servicePrincipalName)
            throws GSSException {

        GSSManager manager = GSSManager.getInstance();
        
        GSSName serverName = manager.createName(servicePrincipalName,
                GSSName.NT_HOSTBASED_SERVICE);
        final GSSContext context = manager.createContext(serverName, kerberos5Oid, null,
                GSSContext.DEFAULT_LIFETIME);
        this.serviceTicket = Subject.doAs(subject, (PrivilegedAction<byte[]>) () -> {
            try {
                byte[] token = new byte[0];
                // This is a one pass context initialisation.
                context.requestMutualAuth(false);
                context.requestCredDeleg(false);
                return context.initSecContext(token, 0, token.length);
            } catch (GSSException e) {
                e.printStackTrace();
                return null;
            }
        });

    }

    private static void codificarTicketDeServicoProDisco(byte[] ticket, String filepath)
            throws IOException {
        BASE64Encoder encoder = new BASE64Encoder();
        FileWriter writer = new FileWriter(new File(filepath));
        String encodedToken = encoder.encode(ticket);
        writer.write(encodedToken);
        writer.close();
    }
}
