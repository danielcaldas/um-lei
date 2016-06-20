/**
 * Classe a que apenas o administrador da API tem acesso, onde podemos remover utilizadores, e criar eventos.
 * 
 * @author jdc 
 * @version 18/05/2014
 */

import java.io.Serializable;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;

public class AdministradorUI implements Serializable {

    public static Users admin(Users users) {

        int user_key = -1;

        String pass;
        String email;

        while (user_key != 0) {

            System.out
                    .println("\n########### PAINEL DA ADMINISTRA��O ###########");
            System.out.println("1 - Criar evento");
            System.out.println("2 - Detalhes de um evento");
            System.out.println("3 - Simular evento");
            System.out.println("4 - Apagar um utilizador");
            System.out.println("5 - Apagar todos os utilizadores");
            System.out.println("0 - Sair");
            System.out.print("> ");

            user_key = Input.lerInt();

            switch (user_key) {

            case 1:
                System.out.print('\u000C');
                users = CriarUmEvento(users);
                System.out.print('\u000C');

                break;

            case 2:
                System.out.print('\u000C');
                DetalhesEventos(users);
                System.out.print('\u000C');
                break;

            case 3:
                SimulaEventos(users);
                break;

            case 4:
                System.out.print('\u000C');
                System.out.print("password: ");
                pass = Input.lerString();

                if (pass.equals("admin")) {

                    System.out.print('\u000C');
                    System.out.print("Email do utilizador: ");
                    email = Input.lerString();

                    System.out.print('\u000C');
                    if (users.removeUser(email) == true) {
                        System.out.println("\nUser: " + email + " eliminado\n");
                    } else {
                        System.out.println("\nERRO! Utilizador n�o existe\n");
                    }

                    break;
                } else {
                    System.out.println("\nAcess Denied!\n");
                }

                break;

            case 5:
                System.out.print('\u000C');
                System.out.print("password: ");
                pass = Input.lerString();
                if (pass.equals("perigo")) {
                    users = new Users();
                    System.out.print('\u000C');
                    System.out.println("\nall Deleted\n");
                } else {
                    System.out.println("\nAcesso Negado!\n");
                }

                break;

            default:
                System.out.println("\nOpera��o inexistente! Introduzir opera��o de 0 a 5\n");
                break;
            }
        }

        return users;
    }

    /**
     * M�todo para criar um Evento
     */
    public static Users CriarUmEvento(Users users) {
        String nome;
        int dia, mes, ano;
        double dist;
        GregorianCalendar data;
        GregorianCalendar datalimite;
        int limiteinscricoes;
        String atividade;
        HashSet<String> atividades = new HashSet<String>();

        // Campos básicos
        System.out.println("###### CRIAR EVENTO #####\n\n");
        System.out.print("Nome do evento: ");
        nome = Input.lerString();
        System.out.println("Data do evento (dia mes ano)");

        System.out.print("Dia: ");
        dia = Input.lerInt();
        System.out.print("M�s: ");
        mes = Input.lerInt();
        System.out.print("Ano: ");
        ano = Input.lerInt();
        data = new GregorianCalendar(ano, (mes - 1), dia);

        System.out.println("Data de limite de inscri��es (dia mes ano)");
        System.out.print("Dia: ");
        dia = Input.lerInt();
        System.out.print("M�s: ");
        mes = Input.lerInt();
        System.out.print("Ano: ");
        ano = Input.lerInt();
        datalimite = new GregorianCalendar(ano, (mes - 1), dia);

        System.out.print("Limite m�ximo de inscri��es: ");
        limiteinscricoes = Input.lerInt();

        System.out.print("Atividade associada: ");
        atividade = Input.lerString();

        System.out.print("Dist�ncia da prova: ");
        dist = Input.lerInt();

        // Criar objecto evento
        EventoAdmin evento = new EventoAdmin(nome, atividade, data,datalimite, limiteinscricoes, dist);

        // Convidar utilizadores cujas as atividades sejam compat�veis com o
        // evento
        users.enviaConvitesDeEvento(evento);

        // Adicionar evento � "base de dados"
        users.addEvento(evento);

        return users;
    }

    /*
     * M�todo que permite aceder aos detalhes dos eventos criados pela
     * administra��o at� � data
     */
    public static void DetalhesEventos(Users users) {

        String nome = "";
        // boolean first = true;

        while (!nome.equals("sair")) {

            System.out.println(users.listarEventos());
            System.out.print("Insira o nome do evento ou sair (para sair): ");
            nome = Input.lerString();
            System.out.print('\u000C');
            System.out.println(users.detalhesDoEvento(nome));
            // first = false;
        }

    }

    /**
     * M�todo que faz liga��o com o objecto que evento que queremos simular.
     * @param users, todos os utilizadores
     */
    public static void SimulaEventos(Users users) {

        String user_key = "";
        String resultado = "";
        String nome = "";

        System.out.println(users.listarEventos());
        System.out.print("Insira o nome do evento para simular! (para sair): ");
        user_key = Input.lerString();
        // System.out.print('\u000C');

        if (users.existeEventoDeNome(user_key)) { // então simulamos
            String tempomet; // o administrador passa info ao sistema acerca das condições metereológicas
            System.out.print("Descri��o do tempo: ");
            tempomet = Input.lerString();
            nome = user_key;
            resultado = users.SimularEventoDeNome(user_key,tempomet);

            
            //MÉTODO PARA MOSTRAR RESULTADO DA PROVA KM A KM
            TreeMap<Integer,String> tabelas = users.getTabelasDoEvento(nome);
            
            if(tabelas!=null){visualizarSimulacaoDaProva(tabelas);}
        }
        else{
             System.out.print('\u000C');
             System.out.println("\nEvento inexistente!\n");
        }

    }
    
    
    
    
    /**
     * M�todo que permite vizualizar decorrer do evento em cada km, ap�s a sua simula��o
     * @param TreeMap<Integer,String>, tabela de classifica��es
     * 
     */
    public static void visualizarSimulacaoDaProva(TreeMap<Integer,String> tabelas){
        
        int kms=1;
        int user_key=-1;
        
        Iterator<Map.Entry<Integer,String>> it = tabelas.entrySet().iterator();
        
        
        do{
              System.out.print('\u000C');
              String t = it.next().getValue();
              
              System.out.println(t);
              System.out.println("1 - Avan�ar   0 - Sair   > ");
              
              user_key = Input.lerInt();
        }
        while(it.hasNext() && user_key!=0);
    }
            
                
        
    
    
  
    
}
