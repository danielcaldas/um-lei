package atividades;

/**Classe para gerir conjunto de diferentes tipos de atividades.
 *
 * @author jdc
 * @version 02/05/2014
 */

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

public class Atividades implements Serializable {
    // Vari�veis de inst�ncia
    private TreeSet<AtvBase> atividades; // porque precisamos de ordem cronol�gica
    
    //Construtores
    public Atividades() {
        this.atividades = new TreeSet<>(new AtividadeComparator());
    }

    public Atividades(TreeSet<AtvBase> atividades) {
        this.atividades = new TreeSet<>(new AtividadeComparator());

        Iterator<AtvBase> it = atividades.iterator();
        while (it.hasNext()) {
            AtvBase a = it.next();
            this.atividades.add(a);
        }
    }

    public Atividades(Atividades atvs) {
        this.atividades = new TreeSet<>(new AtividadeComparator());

        for (AtvBase a : atvs.getSetAtividades())
            this.atividades.add(a.clone());
    }

    // M�todos de inst�ncia

    
    // gets
 
    /**
     * M�todo que devolbe set de c�pias de registos de atividades
     */
    public TreeSet<AtvBase> getSetAtividades() {
        TreeSet<AtvBase> aux = new TreeSet<>(new AtividadeComparator());

        for (AtvBase a : this.atividades) {
            aux.add(a.clone());
        }

        return aux;
    }



    /**
     * M�todo que devolve conjunto das 10 atividades mais recentes, Util para consultas r�pidas por parte do utilizador
     * e �nica maneira que os amigos t�m se aceder �s suas atividades
     */
    public Atividades get10MaisRecentes() { // permite ao amigos acederem
        Atividades copia = new Atividades();
        int counter = 0;
        Iterator<AtvBase> it = this.atividades.iterator();
        while (it.hasNext() && counter != 10) {
            copia.addAtividade(it.next().clone());
            counter++;
        }

        return copia;
    }



    /**
     * M�todo que devolve tamanho da lista de atividades registadas
     */
    public int getSizeLista() {
        return this.atividades.size();
    }

    
    // equals, clone e toString
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        else if (o==null || this.getClass() != o.getClass())
            return false;

        else {
            Atividades atvs = (Atividades) o;

            if (this.atividades.size() != atvs.getSetAtividades().size())
                return false;

            else {
                TreeSet<AtvBase> atvset = atvs.getSetAtividades();

                Iterator<AtvBase> it = this.atividades.iterator();
                Iterator<AtvBase> ita = atvset.iterator();

                while (it.hasNext() && ita.hasNext()) {
                    if (!it.next().equals(ita.next()))
                        return false;
                }

                return true;
            }
        }
    }

    @Override
    public Atividades clone() {
        return new Atividades(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;

        for (AtvBase atv : this.atividades) {
            sb.append(" ").append(i).append(" - ")
                    .append(atv.tituloAtividade());
            i++;
        }

        return sb.toString();
    }

    
    /**
     * M�todo que permite adicionar uma atividades
     */
    public void addAtividade(AtvBase atv) {
        this.atividades.add(atv);
    }

    
    /**
     * M�todo para remover uma atividade
     */
    public void removeAtividade(AtvBase a) {
        this.atividades.remove(a);
    }

    /**
     * M�todo que dada a posição da atividade no TreeSet de atividades, devolve a respectiva atividade
     */
    public AtvBase atividadeDadoIndice(int indice) {
        Iterator<AtvBase> it = this.atividades.iterator();
        AtvBase atv = null;
        int i = 0;

        while (it.hasNext() && i != indice) {
            atv = it.next();
            i++;
        }

        return atv;
    }

    
    /**
     * Devolve uma String que cont�m os nomes das atividades que o utilizador pratica
     */
    public String listaAtividades() {
        StringBuilder sb = new StringBuilder();
        HashSet<String> atvs = new HashSet<String>();

        sb.append("### LISTA DAS MINHAS ATIVIDADES ###\n");
        for (AtvBase atividade : this.atividades)
            atvs.add(atividade.getNome());

        for (String a : atvs)
            sb.append(" - " + a + "\n");
        sb.append("\n");

        return sb.toString();
    }

    
    /**
     * M�todo que dada uma data devolve um objeto Atividades (conjunto de atividades desse dia)
     */
    public Atividades atividadesDadoData(GregorianCalendar data) {
        Iterator<AtvBase> it = this.atividades.iterator();
        Atividades atvs = new Atividades();

        while (it.hasNext()) {
            AtvBase a = it.next();
            GregorianCalendar d = (GregorianCalendar) a.getData();

            if (d.equals(data))
                atvs.addAtividade(a);
        }
        return atvs;
    }

    
    /**
     * M�todo que devolve set de inteiros que representam os dias do mes em que o utilizador registou pelo menos 1 atividade
     */
    public HashSet<Integer> existeAtividadesDiasDoMes(int mes, int ano) {
        Iterator<AtvBase> it = this.atividades.iterator();
        HashSet<Integer> dias = new HashSet<>();

        while (it.hasNext()) {
            AtvBase a = it.next();
            GregorianCalendar d = (GregorianCalendar) a.getData();
            if (d.get(Calendar.YEAR) == ano && d.get(Calendar.MONTH) == mes)
                dias.add(d.get(Calendar.DAY_OF_MONTH));
        }

        return dias;
    }

    
    /**
     * M�todo que devolve a estatística mensal para um dado mes de um dado ano, esta estat�stica comtempla os seguintes par�metros:
     * 1 - Calorias gastas nesse mes; (todas as atividades contam)
     * 2 - Distancia percorrida nesse mes; (apenas contam atividades que contemplam dist�ncia)
     * 3 - Dura��o; (todas as atividades contam)
     */
    public String estatisticaMensal(GregorianCalendar data,TreeSet<AtvBase> atividades) {

        DecimalFormat f = new DecimalFormat("##.00");
        int totalminutos = 0;       // total de minutos de exercício físico
        int totalcalorias = 0;      // total de calorias gastas em todos os
                                    // exercícios
        double totaldistancia = 0;  // distancia total percorrida
        int dias = 0;
        AtvBase a;
        HashSet<String> nomesdomes = new HashSet<>();

        Iterator<AtvBase> it = atividades.iterator();

        while (it.hasNext()) {
            a = it.next();
            GregorianCalendar d = (GregorianCalendar) a.getData();

            if (d.get(Calendar.MONTH) == data.get(Calendar.MONTH)) {
                if (a instanceof Distancia) {
                    AtvComDistancia atv = (AtvComDistancia) a;

                    nomesdomes.add(atv.getNome());

                    dias++;
                    totalminutos += atv.duracaoEmMinutos();
                    totalcalorias += atv.getKcal();
                    totaldistancia += atv.getDist();
                } else if (a instanceof Atividade || a instanceof Rating) {
                    nomesdomes.add(a.getNome());
                    dias++;
                    totalminutos += a.duracaoEmMinutos();
                    totalcalorias += a.getKcal();
                }
            }
        }

        int diasdomes = data.getActualMaximum(Calendar.DAY_OF_MONTH); // obter nº de dias do mes

        double kmpordia = (totaldistancia) / (double) diasdomes;
        int minutospordia = totalminutos / diasdomes;
        int caloriaspordia = totalcalorias / diasdomes;

        int totalsemanas = diasdomes / 7;
        int horasporsemana = (totalminutos / 60) / totalsemanas;
        int caloriasporsemana = totalcalorias / totalsemanas;


        StringBuilder sb = new StringBuilder();

        sb.append("\n--Atividades que pratiquei este mes--\n");
        for (String n : nomesdomes) {
            sb.append(" - " + n + "\n");
        }
        sb.append("\nVisualize o seu calend�rio de atividades para mais consultas!\n");

        // Finalmente retornamos uma String com o relatório
        return ("\n####### Estat�stica " + numParaMes(data.get(Calendar.MONTH))
                + " #######"
                + "\nN� de vezes que praticou exerc�cio (no m�s): " + dias
                + "\n\nDura��o/Dia: " + minutospordia + " minutos"
                + "\nCalorias/Dia: " + caloriaspordia + " Kcal" + "\nKm/Dia: "
                + f.format(kmpordia) + " km\n" + "\nDura��o/Semana: "
                + horasporsemana + " hora(s)" + "\nCalorias/Semana: "
                + caloriasporsemana + " Kcal" + "\nKm/Semana: "
                + f.format(totaldistancia) + " km\n" + sb.toString());
    }

    
    
    /**
     * M�todo que devolve a estat�stica anual para um dado ano, esta estat�stica comtempla os seguintes par�metros:
     * 1 - Calorias gastas nesse mes; (todas as atividades contam)
     * 2 - Distancia percorrida nesse mes; (apenas contam atividades que contemplam dist�ncia)
     * 3 - Dura��o; (todas as atividades contam)
     */
    public String estatisticaAnual(GregorianCalendar data,TreeSet<AtvBase> atividades) {

        int totalminutos = 0;       // total de minutos de exercício físico
        int totalcalorias = 0;      // total de calorias gastas em todos os
                                    // exercícios
        double totaldistancia = 0;  // distancia total percorrida
        int dias = 0;
        AtvBase a;

        Iterator<AtvBase> it = atividades.iterator();

        while (it.hasNext()) {
            a = it.next();
            GregorianCalendar d = (GregorianCalendar) a.getData();

            if (d.get(Calendar.YEAR) == data.get(Calendar.YEAR)) {
                if (a instanceof Distancia) {
                    AtvComDistancia atv = (AtvComDistancia) a;

                    dias++;
                    totalminutos += atv.duracaoEmMinutos();
                    totalcalorias += atv.getKcal();
                    totaldistancia += atv.getDist();
                } else if (a instanceof Atividade) {
                    dias++;
                    totalminutos += a.duracaoEmMinutos();
                    totalcalorias += a.getKcal();
                }
            }
        }

        int diasano;

        if (data.get(Calendar.YEAR) % 4 == 0)
            diasano = 366; // ano bisexto
        else
            diasano = 365;

        DecimalFormat f = new DecimalFormat("##.00");

        // Finalmente retornamos uma String com o relat�rio
        return ("\n####### Estat�stica " + data.get(Calendar.YEAR) + " #######"
                + "\nN� de vezes que praticou exerc�cio (no ano): " + dias
                + "/" + diasano + "\n\nDura��o total: " + (totalminutos / 60)
                + " minutos" + "\nCalorias total: " + totalcalorias + " Kcal"
                + "\nKm total: " + f.format(totaldistancia) + " km\n" + "\nDura��o/Mes: "
                + ((totalminutos / 60) / 12) + " hora(s)" + "\nCalorias/Mes: "
                + (totalcalorias / 12) + " Kcal" + "\nKm/Semana: "
                + (f.format(totaldistancia / 12.0)) + " km\n");
    }

    
    
    /**
     * M�todo que dado um inteiro devolve uma String com o nome do M�s correspondente.
     */
    public String numParaMes(int x) {
        switch (x) {
        case 0:
            return "Janeiro";
        case 1:
            return "Fevereiro";
        case 2:
            return "Mar�o";
        case 3:
            return "Abril";
        case 4:
            return "Maio";
        case 5:
            return "Junho";
        case 6:
            return "Julho";
        case 7:
            return "Agosto";
        case 8:
            return "Setembro";
        case 9:
            return "Outubro";
        case 10:
            return "Novembro";
        case 11:
            return "Dezembro";
        default:
            return "M�S INV�LIDO!";
        }
    }

}
