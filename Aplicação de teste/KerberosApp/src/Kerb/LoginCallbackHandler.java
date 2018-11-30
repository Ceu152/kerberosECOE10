package Kerb;

import java.io.IOException;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;

//Classe para resolver o login com usuario e senha com a API do Java JAAS
//Esse é um código praticamente padrão de callbackgandler
public class LoginCallbackHandler implements CallbackHandler {

  public LoginCallbackHandler() { 
    super();
  }
  
   public LoginCallbackHandler( String user, String senha) { 
    super();
    this.usuario = user;
    this.senha = senha;
  }
  
  public LoginCallbackHandler( String seha) { 
    super();
    this.senha = seha;
  }
  
  private String senha;
  private String usuario;

  @Override
  public void handle( Callback[] callbacks)
      throws IOException, UnsupportedCallbackException {

    for ( int i=0; i<callbacks.length; i++) {
      if ( callbacks[i] instanceof NameCallback && usuario != null) {
        NameCallback nc = (NameCallback) callbacks[i];
        nc.setName( usuario);
      } 
      else if ( callbacks[i] instanceof PasswordCallback) {
        PasswordCallback pc = (PasswordCallback) callbacks[i];
        pc.setPassword( senha.toCharArray());
      } 
      else {
        throw new UnsupportedCallbackException(
        callbacks[i], "Callback não reconhecido");
      }
    }
  }
  
  
}
  
