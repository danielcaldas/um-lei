/**
 * Classe que gere o todo da apliaca��o em 2 Maps, um contendo os utilizadores e outro os eventos criados pela administra��o (o "cora��o" da API).
 * 
 * @author Jos� Francisco
 * @version 05/06/2014
 * @version 29/04/2014
 * 
 * @authos jdc
 * @version 18/05/2014
 */

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;
import java.util.TreeMap;
import exceptions.*;
import atividades.*;
import scores.*;


public class Users implements Serializable {
    private HashMap<String,User> users;          //Map de todos os utilizadores <Email,Objeto Utilizador>
    private HashMap<String,EventoAdmin> eventos; //Map de eventos admin  <Nome,Objecto Evento>

    // Construtores
    public Users() {
        this.users = new HashMap<>();
        this.eventos = new HashMap<>();
    }

    
    public Users(HashMap<String,User> users, HashMap<String,EventoAdmin> ev){
        this.users = new HashMap<>();
        
        for(User u : users.values())
            this.users.put(u.getNome(),u.clone());
            
        this.eventos = new HashMap<>();
        
        for(EventoAdmin ea : ev.values())
            this.eventos.put(ea.getNome(),ea.clone());
            
    }
    
    
    public Users(Users users){
        this.users = users.getUsers();
        this.eventos = users.getEventos();
    }
    
    
    
    
    //M�todos de instância

    //equals, clone e toString    
    @Override
    public boolean equals(Object o){
        if(this==o) return true;
        
        else if(o==null || this.getClass() != o.getClass()) return false;
        
        else{
            
            Users users = (Users) o;
            
            if(this.users.size() != users.usersSize()) return false;
            
            Iterator<Map.Entry<String,User>> itu1 = this.users.entrySet().iterator();
            Iterator<Map.Entry<String,User>> itu2 = users.getUsers().entrySet().iterator();
            
            while(itu1.hasNext() && itu2.hasNext()){
                if(!itu1.next().equals(itu2.next())) return false;
            }
            
            return true;
        }
    }
    
    
    @Override
    public Users clone(){
        return new Users(this);
    }
    
    
    @Override
    public String toString() {
        Collection<User> users = this.users.values();
        StringBuffer s = new StringBuffer();
        for (User user : users)
            s.append(user.toString());
        return s.toString();
    }
    
    
    
    
    /**
     * M�todo que permite registar utilizador passados os par�metros do seu registo nome, idade, altura etc. ...
     */
    public void registerUser(String email, String password, String nome,String genero, int altura, int peso, GregorianCalendar datanasc,String fav) {
        this.users.put(email, new User(email, password, nome, genero, altura,peso, datanasc, fav));
    }
    
    /**
     * M�todo alternativo para o registo de utilizador, aqui recebemos como par�metro o objecto utilizador 
     */
    public void registerUser2(User u) {
        this.users.put(u.getEmail(), u);
    }
    
    
    /**
     * M�todo que devolve c�pia do Map de utilizadores
     */
    public HashMap<String,User> getUsers(){
        HashMap<String,User> aux = new HashMap<>();
        
        for(User u : this.users.values())
            aux.put(u.getEmail(),u.clone());
        
        return aux;
    }
    
    
    /**
     * M�todo que devolve c�ia do Map de eventos
     */
    public HashMap<String,EventoAdmin> getEventos(){
        HashMap<String,EventoAdmin> aux = new HashMap<>();
        
        for(EventoAdmin a : this.eventos.values())
            aux.put(a.getNome(),a.clone());
            
        return aux;
    }

    
    /**
     * M�todo que devolve c�pia do objeto utilizador dado o seu email
     */
    public User getUser(String email) {
        User u = users.get(email);
        if(u!=null)
           return u.clone();
        else return null;
    }

 
    //--------------------------------------------------------------------------------------------------------------------------------------//
    //-------------------------------------------------------------ATIVIDADES---------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------------------------// 
    
    /**
     * M�todo que dado um email devolve o nome do utilizador associado
     */
    public String getNomeDe(String email) {
        if(this.users.containsKey(email)){
            return this.users.get(email).getNome();
        }
        else return null;
    }
    /*Outros m�todos que buscam campos b�sicos do utilizador*/
    public String getGeneroDe(String email){return this.users.get(email).getGenero();}
    public int getAlturaDe(String email){return this.users.get(email).getAltura();}
    public int getPesoDe(String email){return this.users.get(email).getPeso();}
    public int getIdadeDe(String email){return this.users.get(email).getIdade();}
    public Atividades getAtividadesDe(String email){return this.users.get(email).getAtividades();} //faz clone n�vel a baixo
    public TreeSet<AtvBase> getSetAtividadesDe(String email){return this.users.get(email).getAtividades().getSetAtividades();} //faz clone n�vel a baixo
    
    
    public void addAtividadeDe(String email, AtvBase a){ this.users.get(email).addAtividade(a); }
    public void addScoreDe(String email, AtvBase a){ this.users.get(email).addScore(a); }

    public String listarAtividadesDe(String mail){return this.users.get(mail).listarAtividades();}
    //--------------------------------------------------------------------------------------------------------------------------------------//
    //-------------------------------------------------------------ATIVIDADES-FIM-----------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------------------------//
    
    

    //--------------------------------------------------------------------------------------------------------------------------------------//
    //-------------------------------------------------------------SCORES-------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------------------------//
    
    /**
     * Imprime score de utilizador atrav�s de mail e inst�ncia de atividade
     */
    public String imprimirUmScoreDe(String mail,AtvBase a){return this.users.get(mail).imprimirUmScore(a);}
    
    public double getBestDe(String mail, AtvBase a){return this.users.get(mail).getBest(a);}
    public double getWorstDe(String mail, AtvBase a){return this.users.get(mail).getWorst(a);}
    public double getMedDe(String mail, AtvBase a){return this.users.get(mail).getMedia(a);}
    
    //--------------------------------------------------------------------------------------------------------------------------------------//
    //-------------------------------------------------------------SCORES-FIM-------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------------------------//
    
    /**
     * M�todo que dado um email e uma password retorna o utilizador associado no caso de os campos estarem corretos (caso contr�rio retorna null)
     */
    public User login(String email, String password) {
        User user = this.users.get(email);
        if (user != null && user.getPassword().equals(password))
            return user;
        else
            return null;
    }


    /**
     * M�todo que dado um email verifica se existe um utilizador associado
     */
    public boolean containsUser(String email) {return this.users.containsKey(email);}

    
    /**
     * M�todo que devolve o numero de utilizadores registados
     */
    public int usersSize(){return this.users.size();}
    
    
    /**
     * M�todo que retorna o número de eventos criados pela administra��o
     */
    public int eventosSize(){return this.eventos.size();}
    
    
    /**
     * M�todo que permite remover um utilizador da "base de dados" dado o seu mail (caso exista)
     */
    public boolean removeUser(String email) {

        if (this.users.containsKey(email)) {
            this.users.remove(email);

            // Remover user de possíveis eventos em que possa estar inscrito
            Iterator<Map.Entry<String, EventoAdmin>> it = this.eventos.entrySet().iterator();
            while (it.hasNext()) {
                it.next().getValue().removerUserInscrito(email);
            }

            return true;
        } else return false;
    }    
    
    
    
    
    
    //--------------------------------------------------------------------------------------------------------------------------------------//
    //-------------------------------------------------------------AMIGOS-------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------------------------//
    
    
    
    /**
     * M�todo que permite adicionar um amigo dado o seu email e a refer�ncia do utilizador que adiciona o amigo
     */
    public void addFriend(String este, String email) throws UserNaoExisteException,AmigoExisteException, ProprioUserException, ConviteEnviadoException {
        if (!this.users.containsKey(este))
            throw new UserNaoExisteException(este);
        if (email.equals(este))
            throw new ProprioUserException(email);
        if (this.users.get(este).isFriend(email))
            throw new AmigoExisteException(email);
        User friend = this.users.get(email);
        if (friend == null)
            throw new UserNaoExisteException(email);
        if (!this.users.get(este).addSent(email))
            throw new ConviteEnviadoException(email);
        friend.addRequest(este);
    }

    
    
    /**
     * M�todo que permite rejeitar um pedido de amizade dado o seu email e a refer�ncia do utilizador que adiciona o amigo
     */
    public void rejectRequest(String este, String email)throws UserNaoExisteException, NaoEnviouPedidoException, ProprioUserException {
        if (!this.users.containsKey(este))
            throw new UserNaoExisteException(este);
        if (email.equals(este))
            throw new ProprioUserException(email);
        User friend = this.users.get(email);
        if (friend == null)
            throw new UserNaoExisteException(email);
        if (!this.users.get(este).isRequested(email))
            throw new NaoEnviouPedidoException(email);
        this.users.get(este).removeRequest(email);
        friend.removeSent(este);
    }

    
    
    /**
     * M�todo que permite aceitar um pedido de amizade dado o seu email
     */
    public void acceptFriend(String este, String email) throws UserNaoExisteException, NaoEnviouPedidoException, ProprioUserException {
        if (!this.users.containsKey(este))
            throw new UserNaoExisteException(este);
        if (email.equals(este))
            throw new ProprioUserException(email);
        User friend = this.users.get(email);
        if (friend == null)
            throw new UserNaoExisteException(email);
        if (!this.users.get(este).isRequested(email))
            throw new NaoEnviouPedidoException(email);
        this.users.get(este).removeRequest(email);
        this.users.get(este).addFriend(email);
        friend.removeSent(este);
        friend.addFriend(este);
    }

    
    /**
     * M�todo que permite remover um amigo dado o seu email
     */
    public void removeFriend(String este, String email)
            throws UserNaoExisteException, NaoAmigoException, ProprioUserException {
        if (!this.users.containsKey(este))
            throw new UserNaoExisteException(este);
        if (email.equals(este))
            throw new ProprioUserException(email);
        User friend = this.users.get(email);
        if (friend == null)
            throw new UserNaoExisteException(email);
        if (this.users.get(este).isFriend(email)) {
            this.users.get(este).removeFriend(email);
            friend.removeFriend(este);
        } else
            throw new NaoAmigoException(email);
    }

    
    /**
     * M�todo que permite a um dado utilizador aceder aos seus emails
     */
    public User getFriend(String este, String friend) throws UserNaoExisteException, NaoAmigoException, ProprioUserException {
        if (!this.users.containsKey(este))
            throw new UserNaoExisteException(este);
        if (friend.equals(este))
            throw new ProprioUserException(friend);
        User ret = this.users.get(friend);
        if (ret == null)
            throw new UserNaoExisteException(friend);
        if (this.users.get(este).isFriend(friend))
            return ret.clone();
        else
            throw new NaoAmigoException();
    }

    
    /**
     * M�todo para a "op��o encontrar pessoas" permite que se encontre um utilizador pelo seu nome, e n�o pelo email
     */
    public HashSet<String> findUser(String nome) {
        HashSet<String> n = new HashSet<>();
        for (User u : this.users.values()) {
             String s = u.getNome();
             if(s.toLowerCase().contains(nome.toLowerCase()))
                n.add(u.getEmail());
        }
        return n;
    }
    
    
    /**
     * M�todo que dada a string do utilizador devolve o n� de pedidos de amizade pendentes do mesmo
     */
    public int getNrRequestsDe(String email){return this.users.get(email).getNrRequests();}
    
    
    /**
     * Devolve o n�mero de amigos de um utilizador com dado email
     */
    public int getNrFriendsDe(String email){return this.users.get(email).getNrFriends();}
    
    /**
     * Devolve a lista de amigos de um utilizador dado o seu mail
     */
    public String friendsListDe(String email){return this.users.get(email).friendsList();}
    
    
    /**
     * M�todo que dado o mail devolve os dados
     */
    public String getDadosDe(String email){ return this.users.get(email).meusDados();}
    
    
    /**
     * M�todo que devolve lista de pedidos de um utilizador dado o seu mail
     */
    public String requestsListDe(String email){ return this.users.get(email).requestsList();}
    
    
    //--------------------------------------------------------------------------------------------------------------------------------------//
    //------------------------------------------------------------EVENTOS-------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------------------------//
    
    
    
    
    
    /**
     * Criado um evento em AdministradorUI este m�todo permite adicionar evento
     * rec�m-criado ao map de eventos
     */
    public void addEvento(EventoAdmin ead) {
        if (!this.eventos.containsKey(ead.getNome())) {
            this.eventos.put(ead.getNome(), ead);
        }
    }

    
    /**
     * M�todo que a partir de um eventoadmin rec�m-criado cria um evento simples
     * e efetua o convite a todos os users que praticam a(s) atividade(s) relacionada(s)
     */
    public void enviaConvitesDeEvento(EventoAdmin ead) {

        
        //EventoSimples do qual criamos cópias para enviar ao utilizadores potenciais participantes
        EventoSimples es = new EventoSimples(ead.getNome(),
                (GregorianCalendar) ead.getData(),
                (GregorianCalendar) ead.getDataLimiteInscricoes(),
                ead.getLimiteInsc(), ead.getDist());
                
        boolean found;
        //Convidamos todos os users mesmo sabendo do limite de utilizadores,
        //pois temos de pensar que nem todos podem aceitar
        for (User user : this.users.values()) {
            found = false;
            if (user.getNomesAtividades().contains(ead.getAtividadeAssoc())) {
                    found = true;
            }
            if (found == true) {
                user.addConvite(es.clone());
            }
        }
    }
    
    
    
    /**
     * M�todo que imprime todos os eventos criados pela administra��o (AdministradoUI)
     */
    public String listarEventos() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n##### EVENTOS DA ADMINISTRA��O #####\n");
        for (String nome : this.eventos.keySet()) {
            sb.append(" - " + nome + "\n");
        }
        sb.append("\n");

        return sb.toString();
    }

    
    /**
     * M�todo que dado o nome de um evento devolve uma String com os seus
     * detalhes (n�vel Administrador)
     */
    public String detalhesDoEvento(String nomeevento){
        if (this.eventos.containsKey(nomeevento)) {
            return this.eventos.get(nomeevento).toStringDetalhes(this);
        } else
            return ("\nEvento inexistente.\n");
    }

    
    
    
    /**
     * M�todo que a quando a aceita��oo do convite por parte do utilizador o "inscreve oficialmente", passos de execução:
     * 1 - Verifica se evento se encontra na data limite de inscri��o (se ainda � v�lida a inscri��o);
     * 2 - Verifica se evento ainda tem vagas dispon�veis;
     * 3 - Caso tudo se verifique adicionamos o utilizador caso contr�rio devolvemos String com informa��o do porqu� da opera��o ter "falhado";
     * 4 - No final removemos a atividade da lista de convites do utilizador;
     */
    public String inscreveUserEmEvento(String mail, String nomeevento) {

        GregorianCalendar hoje = new GregorianCalendar();
        String resposta = "";

        // alguns m�todos utilizados...
        // public void aceitarConvitePeloNome(String e) USER
        // public HashSet<String> getInscritos() EVENTOADMIN
        // private void removerConvite(String nomeevento) USER

        if (this.eventos.containsKey(nomeevento)) {
            if (antesDaData(hoje,(GregorianCalendar) this.eventos.get(nomeevento).getDataLimiteInscricoes()) == true) { // Data Limite ?
                if (this.eventos.get(nomeevento).getLimiteInsc() > this.eventos.get(nomeevento).getInscritos().size()) { // Limite max de insc?
                    this.eventos.get(nomeevento).inscreverUser(mail);
                    resposta = ("\nEst� inscrito no evento: " + nomeevento + "\n");
                }
                else resposta = ("\nInfelizmente o n� limite de inscri��es foi ultrapassado :(\n");
            }
            else resposta = ("\nInfelizmente a data limite de inscri��es j� foi ultrapassada :(\n");
        }
        else resposta = ("\nEvento inexistente.\n");

        return resposta;
    }

    
    
    /**
     * M�todo que testa se uma data A � anterior a uma data B
     */
    public static boolean antesDaData(GregorianCalendar data1,GregorianCalendar data2) {
       if(data1.compareTo(data2) <=0) return true;
        else return false;
    }

    
    /**
     * M�todo que testa se existe um evento dado o seu nome.
     */
    public boolean existeEventoDeNome(String nome){
        if(this.eventos.containsKey(nome)){
            return true;
        }
        else return false;
    }
    
    
    /**
     * M�todo que recebe sinal para simular evento (encapsulameto) e devolve resposta do m�todo Simular da classe EventoAdmin
     */
    public String SimularEventoDeNome(String nome,String tempomet){
        
        String resultado = this.eventos.get(nome).SimulaEvento(this,tempomet);        
        
        //No final do evento vamos enviar o resultado PÚBLICO a todos os participantes
        for(String mail : this.eventos.get(nome).getInsc()){
                this.users.get(mail).disporResultado(resultado,nome);
        }
        
        
        return resultado;        
    }
    
    
    
    /**
     * M�todo que devolve tabelas de classifica��o do eventos km a km (para consulta da simula��o AdministradorUI)
     */
    public TreeMap<Integer,String> getTabelasDoEvento(String nome){
        if(this.eventos.containsKey(nome)){
            if(this.eventos.get(nome).getJaSimulou()==true){
                return this.eventos.get(nome).getTabelas();
            }
        }
        return null;
    }
    
    
    /**
     * M�todo que dado um email e um nome de um evento devolve a string com o detalhe p�blico do evento
     */
    public String getDetalheDoEventoDe(String mail,String nome){ return this.users.get(mail).getDetalheDoEvento(nome);}
    
    /**
     * M�todo que devolve String com nomes dos eventos em que o utilizador com o dado mail est� inscrito
     */
    public String getNomesEmQueEstaInscrito(String mail){ return this.users.get(mail).getNomesEmQueEstouInscrito();}
    
    /**
     * M�todo que devolve String com convites para eventos de um dado utilizador cujo mail � sabido
     */
    public String convitesEventosDe(String mail){return this.users.get(mail).convitesEventos();}
    
 
    /**
     * Permite que dado um mail se aceite um convite feito ao utilizador associado ao mesmo
     */
    public void aceitarConvitePeloNomeDe(String email,String nome){this.users.get(email).aceitarConvitePeloNome(nome);}
    
    /**
     * Remover convite de evento
     */
    public void removerConviteDe(String mail, String nome){this.users.get(mail).removerConvite(nome);}
}
