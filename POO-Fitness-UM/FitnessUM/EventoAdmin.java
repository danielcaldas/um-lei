 /**
 * Classe para criar e gerir evento at� � data da sua simula��o, incluindo tamb�m os m�todos que executam simula��es e apresentam resultados.
 *
 * @author jdc
 * @version 4/06/2014
 */

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.HashSet;
import atividades.*;
import java.text.DecimalFormat;
import java.lang.String;
import java.lang.Math;
import java.util.Random;
import java.util.TreeSet;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Map;


public class EventoAdmin extends EventoSuper implements Serializable {
    // Vari�veis de instância
    private String atividade;           // atividades de FitnessUM que se enquadram no contexto do evento
    private HashSet<String> inscritos;
    private String ficha;
    public boolean jasimulado;
    TreeMap<Integer,String> tabelas;    //tabelas classificaticas da prova para cada km
    private String resultadopublico;    //a tabela classificativa disponível a todos os participantes do evento
    

    // Construtores
    public EventoAdmin() {
        super();
        this.inscritos = new HashSet<>();
        this.atividade = "";
        this.ficha="";
        this.jasimulado=false;
        tabelas = new TreeMap<>();
    }

    /* Construtor preferido */
    public EventoAdmin(String nome, String atividade,GregorianCalendar data, GregorianCalendar datalim, int limite,double dist) {
        super(nome, data, datalim, limite, dist);

        this.inscritos = new HashSet<>();
        this.atividade = atividade;        
        this.jasimulado=false;        
        this.tabelas = new TreeMap<>();
        this.resultadopublico="";
    }

    public EventoAdmin(EventoAdmin evento) {
        super(evento.getNome(), (GregorianCalendar) evento.getData(),(GregorianCalendar) evento.getDataLimiteInscricoes(), evento.getLimiteInsc(), evento.getDist());;
        this.inscritos = new HashSet<>();

        this.atividade = evento.getAtividadeAssoc();

        for (String u : evento.getInscritos())
            this.inscritos.add(u);
            
        this.ficha = evento.getFicha();
        this.jasimulado=evento.getJaSimulou();
        this.tabelas = evento.getTabelas();
        this.resultadopublico = evento.getResultadoPub();
    }

    
    // M�todos de inst�ncia
    // gets & sets

    /**
     * Devolve uma c�pia de um set dos utilizadores inscritos no evento
     */
    public HashSet<String> getInscritos() {
        HashSet<String> aux = new HashSet<>();

        for (String u : this.inscritos)
            aux.add(u);

        return aux;
    }

    
    public String getAtividadeAssoc(){return this.atividade;}    
    public String getFicha(){return this.ficha;}
    public boolean getJaSimulou(){return this.jasimulado;}
    public String getResultadoPub(){return this.resultadopublico;}
    
    public HashSet<String> getInsc(){
        HashSet<String> aux = new HashSet<>();
        
        for(String mail : this.inscritos)
            aux.add(mail);
        return aux;
    }
    
    
    
    
    /**
     * M�todo que devolve um Map das tabelas classificativas descriminadas por km
     */
    public TreeMap<Integer,String> getTabelas(){
        TreeMap<Integer,String> aux = new TreeMap<>();
        
        Iterator<Map.Entry<Integer,String>> it = this.tabelas.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Integer,String> entry = it.next();
            aux.put(entry.getKey(),entry.getValue());
        }
        
        return aux;
    }
    
    //clone
    @Override
    public EventoAdmin clone() {
        return new EventoAdmin(this);
    }
    

    // toString devolve o evento detalhado
    public String toStringDetalhes(Users users) {
        StringBuilder sb = new StringBuilder();

        if(this.jasimulado==false){
            sb.append(super.toString());
            sb.append("\nAtividade associada: "+this.getAtividadeAssoc()+"\n");

            sb.append("\n*LISTA DE UTILIZADORES INSCRITOS:*\n\n");

            for (String u : this.inscritos) {
                sb.append("Nome: " + users.getNomeDe(u) + "   (Email: " + u + ")\n");
            }
        }
        
        else{
                sb.append("\nEvento j� simulado, resultado:\n");
                sb.append(this.ficha);
                sb.append(this.resultadopublico);
        }
        
        return sb.toString();
    }

  
    /**
     * M�todo que inscreve um utilizador dado o seu email
     */
    public void inscreverUser(String email) {
        this.inscritos.add(email);
    }

    
    /**
     * M�todo que remove um utilizador inscrito dado o seu mail
     */
    public boolean removerUserInscrito(String email) {
        return this.inscritos.remove(email);
    }
    
    
    
    
    
    /**
     * M�todo que permite simular um Evento dados os utilizadores inscritos e a descri��o do tempo metereol�gico, algoritmo:
     * 1 - Recolha de dados: Cria��o de inst�ncia da atividade associada, criar ficha do evento;
     * 2 - Determinar fator de influ�ncia do tempo metereol�gico;
     * 3 - Inicializar tabela classificativa;
     * 4 - Simula��o km a km com participantes din�micos guardando tabela classificativa km a km.
     */    
     public String SimulaEvento(Users users, String tempomet){
         
        DecimalFormat f = new DecimalFormat("##.00");
        String resultado="";
        
        //INICIALIZA��O/RECOLHA DE DADOS
        Simulavel cr;
        //Criar inst�ncia da atividade associada ao evento atrav�s do seu nome
        try {
             String path = ("atividades." + this.getAtividadeAssoc());
             cr = (Simulavel) Class.forName(path).newInstance();
        } catch (Exception e) {
                return null;
        }
        
        //Incerteza para ritmos de simula��o em fun��o da atividade
        double incerteza = cr.getIncerteza();
        
        //Constru�r ficha
        this.ficha = this.constroiFicha(users,cr,tempomet); 
        
        //Calcular fator de influ�ncia do tempo metereol�gico
        double fatortempomet = fatorTempoMet(tempomet);        
        
        //Criar tabela classificativa base antes do in�cio da prova
        TreeSet<Participante> tabelaClass  = new TreeSet<>(new ParticipanteComparator());
        
        for(String mail : this.inscritos){
            double med = users.getMedDe(mail,(AtvBase)cr);
            Random random = new Random();
            double ritmoinicial = (med-incerteza) + ((med+incerteza)-(med-incerteza)) * random.nextDouble();
            ritmoinicial = (ritmoinicial*fatortempomet); //fator do estado do tempo
            Participante p = new Participante(mail,users.getNomeDe(mail),users.getGeneroDe(mail),users.getIdadeDe(mail),ritmoinicial);
            tabelaClass.add(p);
        }       
        
        //----------------------------------------------------------------------------------------------------------------------------
        
        
        //Come�o da prova
        StringBuilder sb = new StringBuilder();
        TreeSet<Participante> lap;
        int kms = 1;
        int fimprova = (int)this.getDist();
        
        while(kms<=fimprova){
            //refresh da tabela classificativa
            lap = new TreeSet<>(new ParticipanteComparator());
            
                //atualizar ritmo, gerador de desistências baseado em probabilidades
                for(Participante p : tabelaClass){

                    if(p.getRitmo()>3){//caso o participante n�o tenha desistido temos de averiguar se ele desiste
                    
                        double ritmoatualiza=0;
                    
                        Random r = new Random();
                        int d;
                        int prob = Math.abs( 100 - p.getProbDesist());
                        if(prob>0) d = r.nextInt(prob); //booleano gerado com base na probabilidade de desist�ncia de cada participante
                            else d=0;
                    
                        boolean desistiu = false;
                        if(d==1) desistiu=true;
                    
                        if(desistiu==true){
                            p.setRitmo(-1);
                            p.setTime(100,100,100);
                        }
                    
                        else{
                            p.atualizaProbDesist(kms); //atualizar probabilidade de desist�ncia em fun��o do km
                        
                            Random refresh = new Random();
                            ritmoatualiza = (p.getRitmo()-incerteza) + ( ((p.getRitmo()+incerteza)-(p.getRitmo()-incerteza)) ) * refresh.nextDouble();
                            ritmoatualiza = (ritmoatualiza*fatortempomet); //Atualizar o ritmo
                        
                            p.setRitmo(ritmoatualiza); //set do valor do novo ritmo
                            p.incRitmos(ritmoatualiza);
                        }
                    
                        //Tempo que vai levar para o participante percorrer este kil�metro mediante o novo ritmo calculado
                        //CALCULO DO TEMPO POR UMA REGRA TRES SIMPLES
                        //  ritmoatualiza ------> 60 minutos
                        //       1 km     ------> ? quantos minutos => � este tempo que temos de adicionar ao tempo de prova do utilizador
                    
                        double tempokm = (double) (60/ritmoatualiza);
                        p.addTempoDoKm(tempokm); //o tempo que demora a percorrer o kil�metro            
                    }
                    else{ p.setRitmo(-1); p.setTime(100,100,100);}
                
                    lap.add(p); //adicionar participante com ritmo atualizado � tabela
                }
            

                //Atualiza a tabela classificativa
                tabelaClass = new TreeSet<>(new ParticipanteComparator());
                for(Participante x : lap){
                    tabelaClass.add(x.clone());
                    x.toString();
                }     
            
                sb = new StringBuilder();
                sb.append("- ").append(this.getNome()).append(" - ").append("Tabela Classificativa ao Kil�metro: ").append(kms+"\n");
            
                //imprimir informa��o da volta
                int posicao=1;
                for(Participante p : tabelaClass){
                    if(p.getRitmo()>2){
                        sb.append(posicao+"� Lugar: "+p.toString());
                        posicao++;
                    }
                    else{ sb.append(p.toString()); }
                }
                
                //guardar tabelas
                this.tabelas.put(kms,sb.toString());            
            
                kms++; //inc ao kilometro da prova            
        }
        
        
        //Resultado publico
        int pos=1;
        StringBuilder sbpub = new StringBuilder();
        for(Participante p : tabelaClass){
            if(p.getRitmo()!=-1){
                sbpub.append(pos+"� Lugar:  "+p.getNome()+"  Tempo: "+p.getTimeTime().toString()+"    Ritmo m�dio: "+ f.format((p.getSumRitmos()/(int)this.getDist()))+" km/h\n");
            }
            else{sbpub.append(p.getNome()+" (DESISTIU)\n");}
            pos++;
        }
        
        this.resultadopublico = sbpub.toString();
        this.jasimulado=true;
        resultado = sb.toString();
        
        
        return resultadopublico;
     }
    
    
     
    
    
     
     
    /**
     * M�todo auxiliar de simulação que constroí uma ficha de dados acerca do Evento
     */
     private String constroiFicha(Users users, Simulavel cr, String tempomet){
        StringBuilder sbficha = new StringBuilder(); 
        DecimalFormat f = new DecimalFormat("##.00"); //formator o double nas strings 
        
        sbficha.append("\n######## FICHA DE EVENTO ########\n");
        sbficha.append("Nome do evento: ").append(this.getNome()).append("\n");
        sbficha.append("Data de realiza��o do evento: ");
        sbficha.append( ((GregorianCalendar)this.getData()).get(Calendar.DAY_OF_MONTH));
        sbficha.append("/");
        sbficha.append((((GregorianCalendar)this.getData()).get(Calendar.MONTH)) +1 );
        sbficha.append("/");
        sbficha.append((((GregorianCalendar)this.getData()).get(Calendar.YEAR)));
        sbficha.append("\n");
        sbficha.append("N�mero de participantes: ").append(this.inscritos.size()).append("\n");
        sbficha.append("Descri��o do tempo metereol�gico no dia do evento: ").append(tempomet).append("\n\n");
        sbficha.append("-Lista de participantes e scores m�dios na atividade-\n");
        
        for(String mail : this.inscritos){
            sbficha.append("Nome: "+users.getNomeDe(mail)).append(" Tempo m�dio: ").append(f.format(users.getMedDe(mail,(AtvBase)cr))).append(" km/h\n");
        }
        sbficha.append("\n\n");
        
        return sbficha.toString();
    }
    
    
    
    
    /**
     * M�todo que calcula o fator de influ�ncia das condi��es metereol�gicas na prova
     */
    private double fatorTempoMet(String tempomet){
        if(tempomet.toLowerCase().contains("intenso") && tempomet.toLowerCase().contains("calor")) return 1.01; //afeta��o de 10%
            else if(tempomet.toLowerCase().contains("intensa") && tempomet.toLowerCase().contains("chuva")) return 1.009; //afeta��o de 9%
            else if(tempomet.toLowerCase().contains("chuva")) return 1.005; //todos os concorrentes v?m os seus tempos diminuidos em 5%
            else if(tempomet.toLowerCase().contains("calor")) return 1.004; //afeta��o de 4%
            else return 1; //o tempo meterol�gico n�o afeta os participantes
    }
}
