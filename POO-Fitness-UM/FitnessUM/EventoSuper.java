/**
 * SuperClasse para eventos, define os nomes e datas, campos b�sicos comuns aos 2 tipos de eventos.
 * 
 * @author jdc
 * @version 03/06/2014
 */

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class EventoSuper implements Serializable {
    // Variáveis de inst�ncia
    private String nome;                        // nome de evento
    private GregorianCalendar data;             // data de realiza��o do evento
    private GregorianCalendar finalinscricoes;  // data limite das inscri��es
    private int limite;                         // n� limite m�ximo de inscri��es
    private double dist;                        // dist�ncia da prova
    private String resultado;

    // Construtores
    public EventoSuper() {
        this.nome = "";
        this.data = new GregorianCalendar();
        finalinscricoes = new GregorianCalendar();
        limite = 0;
        this.dist = 0;
        this.resultado="";
    }

    /* Construtor preferido */
    public EventoSuper(String nome, GregorianCalendar data,
            GregorianCalendar datalim, int limite, double dist) {

        this.nome = nome;
        this.data = new GregorianCalendar(data.get(Calendar.YEAR),
                data.get(Calendar.MONTH), data.get(Calendar.DAY_OF_MONTH));
        this.finalinscricoes = new GregorianCalendar(
                datalim.get(Calendar.YEAR), datalim.get(Calendar.MONTH),
                datalim.get(Calendar.DAY_OF_MONTH));
        this.limite = limite;
        this.dist = dist;
    }

    public EventoSuper(EventoSuper evento) {
        this.nome = evento.getNome();
        this.data = (GregorianCalendar) evento.getData();
        this.finalinscricoes = (GregorianCalendar) evento
                .getDataLimiteInscricoes();
        this.limite = evento.getLimiteInsc();
        this.dist = evento.getDist();
        this.resultado=evento.getResultado();
    }

    
    // M�todos de inst�ncia

    // gets & sets
    public String getNome() {return this.nome;}
    public Object getData() {return this.data.clone();}
    public Object getDataLimiteInscricoes() {return this.finalinscricoes.clone();}
    public int getLimiteInsc() {return this.limite;}
    public double getDist() {return this.dist;}      
    public String getResultado(){ return this.resultado;}
    
    public void setResultado(String r){this.resultado=r;}

    // equals, clone e toString
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        else if (o == null || o.getClass() != this.getClass())
            return false;

        else {
            EventoSuper evento = (EventoSuper) o;

            // Dois eventos s�o iguais se t�m o mesmo nome, 2 eventos podem ter
            // nomes diferentes mas terem exatamento os mesmo concorrentes
            if (this.nome.equals(evento.getNome()))
                return true;
            else
                return false;
        }
    }

    // � IMPERATIVO: que m�todo clone esteja bem definido para subclasse
    // EventoSimples para que cada utilizador tenha o seu estado Unico e mut�vel
    @Override
    public abstract EventoSuper clone();

    // toString devolve o evento detalhado
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("\n######### " + this.nome + " #########\n");

        sb.append("Data de realiza��o do evento: "
                + (this.data.get(Calendar.DAY_OF_MONTH) + 1) + "/"
                + this.data.get(Calendar.MONTH) + "/"
                + this.data.get(Calendar.YEAR) + "\n");
        sb.append("Data limite das inscri��es: "
                + (this.finalinscricoes.get(Calendar.DAY_OF_MONTH) + 1) + "/"
                + this.finalinscricoes.get(Calendar.MONTH) + "/"
                + this.finalinscricoes.get(Calendar.YEAR) + "\n");
        sb.append("N� limite de inscri��es: " + this.limite + "\n");
        sb.append("Dist�ncia da prova: " + this.dist + " km\n\n");

        return sb.toString();
    }

}