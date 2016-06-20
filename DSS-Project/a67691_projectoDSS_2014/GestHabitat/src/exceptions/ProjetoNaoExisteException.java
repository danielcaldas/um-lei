
package exceptions;


/**Excepção para tratar casos em que um projeto não existe.
 * 
 * @author Jorge Caldas, José Cortez, Marcelo Gonçalves, Ricardo Silva
 * @version 2015.01.06
 */
public class ProjetoNaoExisteException extends Exception {
    public ProjetoNaoExisteException(int id){
        super("Projeto com id: "+id+" não existe");
    }
}
