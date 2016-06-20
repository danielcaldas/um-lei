/**
 * Classe para gest�o pessoal de eventos, essencialmente inscrever ou rejeitar o evento.
 * 
 * @author jdc
 * @version 03/06/2014
 */
public class GestorDeEventosUI {
    
    public static void OsEventos(Users users, String mail) {
        int user_key = -1;

        while (user_key != 0) {

            printMainMenu();
            user_key = Input.lerInt();
            switch (user_key) {

            case 1:
                System.out.print('\u000C');
                gereEventos(users,mail);
                System.out.println(users.convitesEventosDe(mail));
                break;

            case 2:
                
                String detalhe="";
                while(!detalhe.equals("sair")){
                    System.out.println(users.getNomesEmQueEstaInscrito(mail));
                    System.out.print("Insira o nome do evento para consultar detalhes ou sair> ");
                    detalhe = Input.lerString();
                    System.out.print('\u000C');
                    System.out.println(users.getDetalheDoEventoDe(mail,detalhe));
                }
                               
                break;

            case 0:
                System.out.print('\u000C');
                break;
            default:
                System.out.print('\u000C');
                System.out.println("Opera��o inexistente! Por favor introduza uma op��o v�lida\n");
                break;
            }
        }
    }

    /**
     * M�todo que imprime o menu
     */
    public static void printMainMenu() {
        System.out.println("\n######## OS MEUS EVENTOS #########\n");
        System.out.println("1 - Convites para eventos");
        System.out.println("2 - Eventos em que estou inscrito");
        System.out.println("0 - Sair\n");
        System.out.print("> ");
    }

    
    
    /**
     * M�todo para gest�oo pessoal de um evento, consultar detalhes e inscrever no evento.
     */
    public static void gereEventos(Users users, String mail) {
        String acao = "";
        String resposta = "";

        while (!acao.equals("sair")) {

            System.out.println(users.convitesEventosDe(mail));
            System.out.print("Para se inscrever no evento insira o nome do evento, para sair escreva sair\n");
            System.out.print("\n> ");
            acao = Input.lerString();
           

            if (acao.equals("sair")) {
                return;
            }

            else {
                int key=-1;
                //imprime o detalhe do evento
                System.out.print('\u000C');
                System.out.println(users.getDetalheDoEventoDe(mail,acao));
                System.out.print("\nPara confirmar a inscri��o insira 1 caso contr�rio insira 0: ");
                
                key = Input.lerInt();
                while(key!=1 && key!=0){
                    System.out.println("Op��o inv�lida. Por favor tente de novo: ");
                    key = Input.lerInt();
                }
                
                System.out.print('\u000C');
                
                if(key==1){
                    users.aceitarConvitePeloNomeDe(mail,acao);
                    resposta = users.inscreveUserEmEvento(mail,acao);

                    if (resposta.equals("\nInfelizmente a data limite de inscri��es j� foi ultrapassada :(\n")|| resposta.equals("Infelizmente n�o existem mais vagas dispon�veis para este evento :(\n")) {
                        users.removerConviteDe(mail,acao);
                        System.out.println(resposta);
                    } else {
                        System.out.println(resposta);
                    }
                }
                else{System.out.println("\nInscri��o cancelada.\n");}

            }
        }

    }

}
