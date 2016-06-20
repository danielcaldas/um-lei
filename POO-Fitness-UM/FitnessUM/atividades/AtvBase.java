package atividades;

/**SuperClasse abstrata que define o que ser� comum a todo o tipo de atividades.
 *
 * @author jdc
 * @version 20/05/2014
 */

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public abstract class AtvBase implements Serializable {
    
    // Vari�veis de inst�ncia comuns a todo o tipo de atividades
    private Time duracao; // dura��o
    private int calorias; // calorias gastas/queimadas (em Kcal)
    private double hidratacao; // quantidade de �gua perdida (em Litros)
    private GregorianCalendar data; // data de realiza��o da atividade

    // Construtores
    public AtvBase() {
        this.duracao = new Time();
        this.hidratacao = 0;
        this.calorias = 0;
        this.data = new GregorianCalendar();
    }

    public AtvBase(Time duracao, int idade, int peso, int altura,String genero, GregorianCalendar data, double hidratacao) {
        this.duracao = duracao;
        this.hidratacao = hidratacao;
        this.data = data;
        this.calorias = calcularCalorias(idade, altura, genero, duracao, peso);
    }

    public AtvBase(AtvBase atv) {
        this.duracao = atv.getDuracao();
        this.hidratacao = atv.getH2O();
        this.calorias = atv.getKcal();
        this.data = (GregorianCalendar) atv.getData();
    }

    // M�todos de inst�ncia

    // gets
    public Time getDuracao() {return this.duracao.clone();}
    public double getH2O() {return this.hidratacao;}
    public int getKcal() {return this.calorias;}
    public Object getData() {return this.data.clone();}
    public abstract  String getNome();
    
    // equals, clone e toString
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        else if (o == null || this.getClass() != o.getClass())
            return false;

        else {
            AtvBase atv = (AtvBase) o;
            return (this.duracao.equals(atv.getDuracao())
                    && this.hidratacao == atv.getH2O()
                    && this.calorias == atv.getKcal() && this.data.equals(atv
                    .getData()));
        }
    }

    @Override
    public abstract AtvBase clone();

    @Override
    public String toString() {
        return ("\n#########REGISTO DE ATIVIDADE - " + this.getNome()
                + "#########\n" + "Desporto/Atividade: " + this.getNome()+ "\n"
                + "Dura��o: " + this.duracao + "\n" + "Hidrata��o: "
                + this.hidratacao + " L\n" + "Calorias gastas: "
                + this.calorias + " kcal\n" + "Data: "
                + this.data.get(Calendar.DAY_OF_MONTH) + "/"
                + (this.data.get(Calendar.MONTH) + 1) + "/"
                + this.data.get(Calendar.YEAR) + "\n");
    }

    /**
     * M�todo que devolve uma String que � um cabe�alho da atividade
     */
    public String tituloAtividade() {
        return ("Desporto/Atividade: " + this.getNome() + "  Data: "
                + this.data.get(Calendar.DAY_OF_MONTH) + "/"
                + (this.data.get(Calendar.MONTH) + 1) + "/"
                + this.data.get(Calendar.YEAR) + "\n");
    }

    
    /**
     * M�todo que devolve a dura��o da atividade em minutos (int)
     */
    public int duracaoEmMinutos() {
        return (int) this.getDuracao().duracaoEmMinutos();
    }


    /**
     * M�todo abstractto do cálculo de calorias a ser concretizado nas classes que instanciam atividades
     */
    public abstract int calcularCalorias(int idade, int altura, String genero,Time duracao, int peso);

}
