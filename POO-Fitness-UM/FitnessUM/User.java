/**
 * Classe que armezena toda a informa��oo referente a um dado utilizador.
 * 
 * @author Jos� Francisco
 * @version 01/05/2014
 * @version 03/06/2014
 * 
 * @authos jdc
 * @version 18/05/2014
 */

import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import scores.Score;
import atividades.Atividades;
import atividades.AtvBase;

public class User implements Serializable {

    // Vari�veis de instância
    private String email;
    private String password;
    private String nome;
    private String genero;
    private int altura;
    private int peso;
    private String desportofav;
    private GregorianCalendar datanasc;
    private int idade;
    private HashSet<String> friends;                    //conjunto dos emails dos amigos do utilizador
    private HashSet<String> requests;                   //conjunto dos emails que enviaram pedido de amizade a este utilizador
    private HashSet<String> sent;                       //cojunto dos emails dos utilizadores aos quais este mesmo enviou pedidos de amizade
    private Atividades atividades;                      //conjunto das atividades de um utilizador
    private Score scores;
    private HashMap<String,EventoSimples> convites;    //conjunto dos eventos a que est�o associados este utilizador

    
    //Construtores
    public User() {
        this.email = "";
        this.password = "";
        this.nome = "";
        this.genero = "";
        this.altura = 0;
        this.peso = 0;
        this.datanasc = new GregorianCalendar();
        this.idade = 0;
        this.desportofav = "";
        this.friends = new HashSet<String>();
        this.requests = new HashSet<String>();
        this.sent = new HashSet<String>();
        this.atividades = new Atividades();
        this.convites = new HashMap<>();
        this.scores = new Score();
    }

    // Construtor parcialmente parametrizado usado para fins de registo
    public User(String email, String password, String nome, String genero,int altura, int peso, GregorianCalendar datanasc, String fav) {
        this.email = email;
        this.password = password;
        this.nome = nome;
        this.genero = genero;
        this.altura = altura;
        this.peso = peso;
        this.datanasc = datanasc;
        this.idade = CalcularIdade(datanasc);
        this.desportofav = fav;
        this.friends = new HashSet<String>();
        this.requests = new HashSet<String>();
        this.sent = new HashSet<String>();
        this.atividades = new Atividades();
        this.scores = new Score();
        this.convites = new HashMap<>();
    }

    // Construtor Totalmente Parametrizado
    public User(String email, String password, String nome, String genero,int altura, int peso, GregorianCalendar datanasc, String fav,
                int idade, HashSet<String> friends, HashSet<String> requests,HashSet<String> sent, Atividades atividades, Score scores,
                HashMap<String,EventoSimples> ev) {
        this.email = new String(email);
        this.password = new String(password);
        this.nome = new String(nome);
        this.genero = new String(genero);
        this.altura = altura;
        this.peso = peso;
        this.datanasc = new GregorianCalendar(
        datanasc.get(Calendar.DAY_OF_MONTH),
        datanasc.get(Calendar.MONTH), datanasc.get(Calendar.YEAR));
        this.idade = idade;
        this.desportofav = fav;

        this.friends = new HashSet<String>();
        for (String s : friends)
            this.friends.add(s);

        this.requests = new HashSet<String>();
        for (String s : requests)
            this.requests.add(s);

        this.sent = new HashSet<String>();
        for (String s : sent)
            this.sent.add(s);

        this.atividades = atividades.clone();

        this.scores = scores.clone();
        
        this.convites = new HashMap<>();
        for(EventoSimples e : ev.values())
            this.convites.put(e.getNome(),e.clone());
    }
    

    public User(User user) {
        this(user.getEmail(), user.getPassword(), user.getNome(),user.getGenero(), user.getAltura(), user.getPeso(),
                (GregorianCalendar) user.getDataNasc(), user.getFavorito(),user.getIdade(), user.getFriends(), user.getRequests(),
                user.getSent(), user.getAtividades(), user.getScores(), user.getConvites());
    }

    
    
    // Metodos de inst�ncia

    // gets
    public String getEmail() {return this.email;}
    public String getPassword() {return this.password;}
    public String getNome() {return this.nome;}
    public String getGenero() {return this.genero;}
    public int getAltura() {return this.altura;}
    public int getPeso() { return this.peso;}
    public Object getDataNasc() {return this.datanasc;}
    public String getFavorito() {return this.desportofav;}
    public int getIdade() {return this.idade;}

    
    
    //equals, clone e toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        
        if (o==null || this.getClass() != o.getClass()) return false;
        else {
            User user = (User) o;
            return (user.getEmail().equals(this.email));
        }
    }

    
    @Override
    public User clone() {
        return new User(this);
    }

    
    /**
     * toString definido com fins administrativos da API
     */
    @Override
    public String toString() {
        return ("User:" + this.email + "\nPassword:" + this.password + "\n");
    }    
    
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[] { email });
    }

    /*@Override
    public int compareTo(User user) {
        return (this.email).compareTo(user.getEmail());
    }*/
    

    
    /**
     * M�todo que devolve String com dados pessoais do utilizador
     */
    public String meusDados() {
        return ("\n####### PERFIL DE: "+this.nome+"#######\nEmail: "+this.getEmail()
                + "\nData de nascimento: "
                + this.datanasc.get(Calendar.DAY_OF_MONTH) + "/"
                + this.datanasc.get(Calendar.MONTH) + "/"
                + this.datanasc.get(Calendar.YEAR) + "\nIdade: " + this.idade
                + "\nAltura: " + this.altura + "\nPeso: " + this.peso
                + "\nG�nero: " + this.genero + "\nDesporto favorito: "
                + this.desportofav + "\nAmigos: " + this.friends.size());
    }
    
    
    /**
     * M�todo que dada a data de nascimento permite calcular idade
     */
    public int CalcularIdade(GregorianCalendar datanasc) {
        int dia = datanasc.get(Calendar.DAY_OF_MONTH);
        int mes = datanasc.get(Calendar.MONTH);
        int ano = datanasc.get(Calendar.YEAR);

        GregorianCalendar atual = new GregorianCalendar();// data de hoje

        if (atual.get(Calendar.MONTH) < mes|| (atual.get(Calendar.MONTH) == mes && atual.get(Calendar.DAY_OF_MONTH) < dia))// se ainda "não fez anos"
            return (atual.get(Calendar.YEAR) - ano) - 1;
        else return (atual.get(Calendar.YEAR) - ano);
    }
    
    
    
    
    
    //--------------------------------------------------------------------------------------------------------------------------------------//
    //-------------------------------------------------------------AMIGOS-------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------------------------//
    
    
    /**
     * M�todo get de uma c�pia de um Set do conjunto de emails dos amigos
     */
    public HashSet<String> getFriends() {
        HashSet<String> aux = new HashSet<String>();

        for (String u : this.friends)
            aux.add(u);

        return aux;
    }

    
    /**
     * M�todo get devolve uma c�pia de Set do conjunto de emails  que enviaram pedidos de amizades
     */
    public HashSet<String> getRequests() {
        HashSet<String> aux = new HashSet<String>();

        for (String u : this.requests)
            aux.add(u);

        return aux;
    }

    /**
     * M�todo que devolve Set de emails dos utilizadores a que este enviou pedidos de amizade
     */
    public HashSet<String> getSent() {
        HashSet<String> aux = new HashSet<String>();

        for (String u : this.sent)
            aux.add(u);

        return aux;
    }
    

    /**
     * M�todo que adiciona um amigo
     */
    public boolean addFriend(String friend) {return this.friends.add(friend);}

    
    /**
     * M�todo que adiciona um pedido de amizade � lista de pedidos do utilizador
     */
    public boolean addRequest(String request) {return this.requests.add(request);}

    
    /**
     * M�todo que adiciona email � lista de pedidos enviados
     */
    public boolean addSent(String sent) {return this.sent.add(sent);}

    /**
     * M�todo que permite remover amizade
     */
    public boolean removeFriend(String friend){return this.friends.remove(friend);}

    
    /**
     * M�todo que remove email da lista de pedidos de amizade (ap�s utilizador aceitar/rejeitar esse pedido)
     */
    public boolean removeRequest(String request) {
        return this.requests.remove(request);
    }

    
    /**
     * M�todo que remove utilizador da lista de pedidos enviados
     */
    public boolean removeSent(String sent) {return this.sent.remove(sent);}

    
    /**
     * M�todo que retorna o n� de amigos do utilizador
     */
    public int getNrFriends() {return this.friends.size();}

    /**
     * M�todo que retorna o nº de pedidos de amizade pendentes do utilizador
     */
    public int getNrRequests() {return this.requests.size();}

    /**
     * M�todo que dado um email verifica se o utilizador do dado email � amigo
     */
    public boolean isFriend(String friend) {return this.friends.contains(friend);}

    
    /**
     * M�todo que verifica se o utilizador do dado email enviou pedido de amizade
     */
    public boolean isRequested(String friend){return this.requests.contains(friend);}

    /**
     * M�todo que devolve String com lista dos amigos
     */
    public String friendsList() {
        StringBuffer s = new StringBuffer();
        for (String friend : this.friends)
            s.append("Email:" + friend + "\n");
        return s.toString();
    }

    
    /**
     * M�todo que devolve String com lista de pedidos de amizade
     */
    public String requestsList() {
        StringBuffer s = new StringBuffer();
        for (String req : this.requests)
            s.append("Email:" + req + "\n");
        return s.toString();
    }

    
    
    
    
    //--------------------------------------------------------------------------------------------------------------------------------------//
    //---------------------------------------------------------ATIVIDADES-------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------------------------//    
    
    
    
    /**
     * M�todo que devolve c�pia de uma Set das atividades do utilizador (c�pia ao n�vel da classe Atividades)
     */
    public TreeSet<AtvBase> getSetAtividades() {return this.atividades.getSetAtividades();}
    
     /**
     * M�todo que devolve c�pia do registo de atividades do utilizador
     */
    public Atividades getAtividades() {return this.atividades.clone();}
    
    /**
     * M�todo que permite adicionar uma atividade (GestorDeAtividadesUI)
     */
    public void addAtividade(AtvBase a) {this.atividades.addAtividade(a);}

    /**
     * M�todo que permite remover atividade (GestorDeAtividadesUI)
     */
    public void removeAtividade(AtvBase a) {this.atividades.removeAtividade(a);}

    /**
     * M�todo que devolve String das atividades que o utilizador pratica
     */  
    public String listarAtividades() {return this.atividades.listaAtividades();}
    
    /**
     * M�todo que devolve Set das atividades que o utilizador pratica
     */  
    public HashSet<String> getNomesAtividades() {
        HashSet<String> aux = new HashSet<>();

        for (AtvBase a : this.atividades.getSetAtividades())
            aux.add(a.getNome());

        return aux;
    }

    
    
    
    
    //--------------------------------------------------------------------------------------------------------------------------------------//
    //-------------------------------------------------------------SCORES-------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------------------------//    

    /**
     * M�todo que devolve c�pia do registo de recordes pessoais
     */
    public Score getScores() {return this.scores.clone();}
    
    /**
     * M�todo que permite adicionar score (ConsultarScoresUI)
     */
    public void addScore(AtvBase a) {this.scores.addScore(a);}
    
    /**
     * M�todo que devolve melhor tempo/rating/durac�o em minutos da atividade (em conformidade)
     */
    public double getBest(AtvBase a) {return this.scores.getMelhor(a);}
    
    /**
     * M�todo que devolve pior tempo/rating/durac�o em minutos da atividade (em conformidade)
     */
    public double getWorst(AtvBase a) {return this.scores.getPior(a);}
    
    /**
     * M�todo que devolve tempo/rating/durac�o em minutos m�dia(o) da atividade (em conformidade)
     */    
    public double getMedia(AtvBase a) {return this.scores.getMed(a);}
    
    /**
     * M�todo que devolve uma String com o formato definido na classe scores
     */
    public String imprimirUmScore(AtvBase a) {return this.scores.scoreDadoNomeAtividade(a);}   
    
    
    
    
    
    //--------------------------------------------------------------------------------------------------------------------------------------//
    //------------------------------------------------------------EVENTOS-------------------------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------------------------------------//
    
    
    /**
     * M�todo que permite adicionar convite para evento (EventoSimples) na lista de convites
     */
    public void addConvite(EventoSimples evento) {
        if (!this.convites.containsKey(evento.getNome())) {
            this.convites.put(evento.getNome(), evento.clone());
        }
    }


    /**
     * M�todo que permite que utilizador aceite convite para evento dado o nome do mesmo
     */
    public void aceitarConvitePeloNome(String e) {
        if (this.convites.containsKey(e)) {
            this.convites.get(e).aceitarConvite();
        }
    }

    /**
     * M�todo que permite remover convite para evento da lista de convites para eventos
     */
    public void removerConvite(String nomeevento) {
        if (this.convites.containsKey(nomeevento)) {
            this.convites.remove(nomeevento);
        }
    }

    
    /**
     * M�todo que devolve String formatada com convites para eventos do utilizador
     */
    public String convitesEventos() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n### CONVITES PARA EVENTOS ###\n");
        for (EventoSimples e : this.convites.values()) {
            if(e.getStatus()==false)
               sb.append(" - " + e.getNome() + "\n");
        }
        sb.append("\n");

        return sb.toString();
    }

    
    /**
     * M�todo que testa se uma data A � anterior a uma data B
     */
    public static boolean antesDaData(GregorianCalendar data1,GregorianCalendar data2) {
       if(data2.compareTo(data1) <=0) return true;
        else return false;
    }

    
    /**
     * M�todo que permite consultar o status do utilizador para com o evento (inscrito/n�o inscrito)
     */
    public boolean getStatusDoEvento(String evento) {
        if (this.convites.containsKey(evento)) {
            return this.convites.get(evento).getStatus();
        } else
            return false;
    }

    /**
     * M�todo que devolve lista dos nomes dos eventos nos quais o utilizador est� inscrito
     */
    public String getNomesEmQueEstouInscrito() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n##### EVENTOS EM QUE ESTOU INSCRITO ####\n");
        for (String e : this.convites.keySet()) {
            if (this.convites.get(e).getStatus() == true)
                sb.append(" - " + e + "\n");
        }

        return sb.toString();
    }

    
    /**
     * M�todo que devolve c�pia dos convites para eventos (passando c�pias de objetos EventoSimples do utilizador
     */
    public HashMap<String,EventoSimples> getConvites() {
        HashMap<String, EventoSimples> aux = new HashMap<>();

        for (EventoSimples ev : this.convites.values())
            aux.put(ev.getNome(), ev.clone());

        return aux;
    }

    
    /**
     * M�todo para consulta dos detalhes de um evento com "permiss�es" user (devolve null caso n�o esteja inscrito no evento)
     */
    public String getDetalheDoEvento(String e){
        if(this.convites.containsKey(e)){
            return this.convites.get(e).toString();
        }
        else return ("\nN�o est� inscrito em nenhum evento com esse nome\n");
    }    
    
    
    /**
     * M�todo que permite ao utilizador consultar resultado PUBLICO do evento (j� tendo este sido simulado)
     */
    public void disporResultado(String r, String ev){this.convites.get(ev).setResultado(r);}

}