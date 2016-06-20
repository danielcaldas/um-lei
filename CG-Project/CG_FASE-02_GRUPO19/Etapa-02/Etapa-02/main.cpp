/*
 *	Autores: Daniel Caldas, Jos� Cortez, Marcelo Gon�alves, Ricardo Silva
 *	Data: 2015.04.08
 *	Vers�o: 2.0v
 *
 */

#include "const.h"

// Vari�vel globa para armazenamento dos modelos da cena a desenhar
vector<Forma*> formas;

// Vari�veis globais para controlo apropriado da c�mera
float px, py, pz;
float alpha, beta, radius;
float height;

// Vari�vel global op��o do menu
int option;

// Vari�veis auxiliares para leitura de ficheiros XML e povoamento das estruturas de dados
vector<TransformsWrapper> transforms_atual;
vector<Grupo> grupos;
Ponto3D color;

/**Fun��o que gera automaticamente ficheiros com os palentas do modelo est�tico do sistema solar*/
vector<string> makeStaticSolarSystem()
{
	string p;
	vector<string> planetas;

	p = "gerador rec 512 512 plano.3d"; planetas.push_back(p);
	p = "gerador esfera 17 30 30 sol.3d"; planetas.push_back(p);
	p = "gerador esfera 1.5 30 30 mercurio.3d"; planetas.push_back(p);
	p = "gerador esfera 1.9 30 30 venus.3d"; planetas.push_back(p);
	p = "gerador esfera 2 30 30 terra.3d"; planetas.push_back(p);
	p = "gerador esfera 0.5 30 30 lua.3d"; planetas.push_back(p);
	p = "gerador esfera 1.6 30 30 marte.3d"; planetas.push_back(p);
	p = "gerador esfera 9 30 30 jupiter.3d"; planetas.push_back(p);
	p = "gerador esfera 8 30 30 saturno.3d"; planetas.push_back(p);
	p = "gerador esfera 4.7 30 30 urano.3d"; planetas.push_back(p);
	p = "gerador esfera 4.5 30 30 neptuno.3d"; planetas.push_back(p);

	return planetas;
}

/*---------------------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------------------*/

/*C�digo referente ao GLUT*/

/*---------------------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------------------*/

/**Ajusta tamanho da janela (esqueleto openGL)*/
void changeSize(int w, int h)
{

	// Prevenir divis�o por 0 quando janela � demasiado pequena
	// (N�o podemos criar uma janela com altura 0)
	if (h == 0)
		h = 1;

	float ratio = w * 1.0 / h;
	glMatrixMode(GL_PROJECTION);
	glLoadIdentity();

	glViewport(0, 0, w, h);
	gluPerspective(45.0f, ratio, 1.0f, 1000.0f);

	glMatrixMode(GL_MODELVIEW);
}

void sphericalToCartesian()
{
	pz = radius * cosf(beta) * cosf(alpha);
	px = radius * cosf(beta) * sinf(alpha);
	py = radius * sinf(beta);
}

/**Desenha a cena (esqueleto openGL)*/
void renderScene(void)
{
	// Limpar buffer
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// Convers�o de coordenadas esf�ricas para coordenadas cartesianas da posi��o da c�mera
	sphericalToCartesian();

	// Atualizar c�mara
	glLoadIdentity();
	gluLookAt(px, py, pz,
		0.0, 0.0, 0.0,
		0.0f, 1.0f, 0.0f);

	glPolygonMode(GL_FRONT, option);

	// Loop para desenhar formas
	for (std::vector<Forma*>::iterator it = formas.begin(); it != formas.end(); ++it){
		(*it)->draw();
	}

	glutSwapBuffers();
}

/**Fun��o que processa a��es do teclado (movimento da c�mera)*/
void keyPressed(unsigned char key, int x, int y)
{
	switch (key){
	case 'a':
		alpha -= 0.5;
		break;
	case 'd':
		alpha += 0.5;
		break;
	case 'w':
		if (beta < (M_PI / 2) - 0.1) beta += 0.3;
		break;
	case 's':
		if (beta > -(M_PI / 2) + 0.1) beta -= 0.3;
		break;
	case'q':
		if (radius>0) radius -= 3;
		break;
	case 'e':
		radius += 3;
		break;
	default:
		break;
	}
	glutPostRedisplay();
}

/**Handler para eventos de escolha do menu*/
void processMenuEvents(int op)
{
	switch (op){
	case 1:
		option = GL_FILL;
		break;
	case 2:
		option = GL_LINE;
		break;
	case 3:
		option = GL_POINT;
		break;
	default:
		break;
	}
	glutPostRedisplay();
}

/**Fun��o que constr�i o menu*/
void menu()
{
	glutCreateMenu(processMenuEvents);

	// Definir entradas do menu
	glutAddMenuEntry("Ver com cores", 1);
	glutAddMenuEntry("Ver apenas linhas", 2);
	glutAddMenuEntry("Ver apenas pontos", 3);

	glutAttachMenu(GLUT_RIGHT_BUTTON);
}

/**Inicializa par�metros relativos � camera*/
void init_camera_parameters()
{
	px = 0;
	py = 3.0;
	pz = 5.0;
	alpha = 0.5;
	beta = (M_PI / 4);
	radius = 2;
}

/**Fun��o que inicializa os par�metros do glut criando a respetiva janela para desenhar a cena*/
int prepare_glut()
{
	init_camera_parameters();
	option = GL_LINE;
	height = 1;

	// Inicializa��o
	char *myargv[1];
	int myargc = 1;
	myargv[0] = "motor3d";
	glutInit(&myargc, myargv);
	glutInitDisplayMode(GLUT_DEPTH | GLUT_DOUBLE | GLUT_RGBA);
	glutInitWindowPosition(100, 100);
	glutInitWindowSize(800, 800);
	glutCreateWindow("Mini Motor3D");

	// Registo de fun��es 
	glutDisplayFunc(renderScene);
	glutIdleFunc(renderScene);
	glutReshapeFunc(changeSize);

	// Intera��es Rato/Teclado
	glutDisplayFunc(renderScene);
	glutReshapeFunc(changeSize);
	glutIdleFunc(renderScene);
	glutKeyboardFunc(keyPressed);

	// Cria��o do menu
	menu();

	// Settings OpenGL
	glEnable(GL_DEPTH_TEST);
	glEnable(GL_CULL_FACE);
	glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

	printf("end.\n\n");
	// Entrar no ciclo do GLUT
	glutMainLoop();

	return -1;
}

/*---------------------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------------------*/

/*Fim do c�digo referente ao GLUT*/

/*---------------------------------------------------------------------------------------*/
/*---------------------------------------------------------------------------------------*/


/**Fun��o chamada a quando da leitura de um ficheiro XML em readFromXML
   translacoes e rotacoes s�o cumulativas pelo que as vari�veis globais translacoes e rotacoes representam
   transforma��es comuns (definidas no grupo pai)
*/
void generateForm(string filename)
{
	ifstream f(filename);
	string form;
	f >> form;

	Forma* s = NULL;
	if (form.compare(FORMA_RECTANGULO) == 0){
		s = new Rectangulo();
	}
	else if (form.compare(FORMA_TRIANGULO) == 0){
		s = new Triangulo();
	}
	else if (form.compare(FORMA_CIRCULO) == 0){
		s = new Circulo();
	}
	else if (form.compare(FORMA_PARALEL) == 0){
		s = new Paralelepipedo();
	}
	else if (form.compare(FORMA_ESFERA) == 0){
		s = new Esfera();

	}
	else if (form.compare(FORMA_CONE) == 0){
		s = new Cone();
	}

	// Set dos novas valores da forma geom�trica
	s->color = color;
	s->read3DfromFile(filename);
	s->setTransformacoes(grupos.at(grupos.size()-1).transfs);

	// Adicionar nova forma
	formas.push_back(s);

}

// debug
void printTransforms(){
	for (int i = 0; i < transforms_atual.size(); i++){
		transforms_atual.at(i).printT();
	}
}

TiXmlElement * ant;
string ficheiro_a_ler;
/**Fun��o que faz parse de um nodo <grupo> do ficheiro XML*/
void readGrupoFromXML(TiXmlElement* element)
{
	TiXmlElement* modelo;

	const char* name = element->Value();

	if ((strcmp(name, "translacao") == 0) || (strcmp(name, "escala") == 0) ){
		Ponto3D p;
		p.x = atof(element->Attribute("X"));
		p.y = atof(element->Attribute("Y"));
		p.z = atof(element->Attribute("Z"));
		
		TransformsWrapper tw;

		if (strcmp(name, "translacao") == 0){
			tw.translacao = p;
			tw.nome = "TRANSLATE";
		}
		else{
			tw.escala = p;
			tw.nome = "SCALE";
		}

		// Atualizar transforma��es atuais
		transforms_atual.push_back(tw);
	}
	else if (strcmp(name, "rotacao") == 0){
		Rotacao3D r;// = (Rotacao3D*)malloc(sizeof(Rotacao3D));
		r.ang = atof(element->Attribute("angulo"));
		r.x = atof(element->Attribute("eixoX"));
		r.y = atof(element->Attribute("eixoY"));
		r.z = atof(element->Attribute("eixoZ"));

		TransformsWrapper tw;
		tw.rotacao= r;
		tw.nome = "ROTATE";

		// Atualizar transforma��es atuais
		transforms_atual.push_back(tw);
	}
	else if (strcmp(name, "modelos") == 0){
		modelo = element->FirstChildElement("modelo");
		while (modelo){
			string filename = modelo->Attribute("ficheiro");
			ficheiro_a_ler = filename;
			color.x = atof(modelo->Attribute("colorX"));
			color.y = atof(modelo->Attribute("colorY"));
			color.z = atof(modelo->Attribute("colorZ"));

			modelo = modelo->NextSiblingElement("modelo");		
		}
	}
	else if(strcmp(name, "grupo") == 0){
		/*Desmarcar para fazer debug dos grupos do XML*/
		//cout << "ID: " << element->Attribute("prof") << "\n";	
		ant = element;
		TiXmlElement * novo = element->FirstChildElement();
		if (novo!=NULL)	readGrupoFromXML(element->FirstChildElement());
	}

	/*Cria��o de novo grupo com transforma��es se:
	 - Estamos no �ltimo grupo portanto pr�ximo "Element" � nulo;
	 - O pr�ximo elemento XML � um grupo, guardamos os dados referentes ao que acabou (ant).
	*/
	if(element->NextSiblingElement()!=NULL && strcmp(element->NextSiblingElement()->Value(), "grupo")== 0){
		Grupo g;

		// Id de profundidade do grupo
		int prof = atoi(ant->Attribute("prof"));
		vector<TransformsWrapper> acumulador;

		// Calcular indice do irm�o subtrair 1 e obtemos o(s) pai(s)
		int indice_do_pai = -1;
		if (grupos.size() > 0){
			int i;
			for (i = grupos.size() - 1; prof <= grupos.at(i).id; i--); // Para no irmao vari�vel i

			if (i <= 0){
				indice_do_pai = 0;
			}
			else{
				indice_do_pai = i;
			}
		}

		// Herdar transforma��es at� ao indice_do_pai (se indice_do_pai==-1 n�o herda nada)
		if (indice_do_pai != -1){
			//for (int k = 0; k <= indice_do_pai; k++){
				// Percorrer e copiar transforma��es de um dado grupo pai
				for (int i = 0; i < grupos.at(indice_do_pai).transfs.size(); i++){
					acumulador.push_back(grupos.at(indice_do_pai).transfs.at(i));
				}
			//}
		}
		// Fazer acumula��es se existirem
		if (acumulador.size()>0){
			g = Grupo(acumulador);
			g.id = prof;

			// Agora podemos atribuir as transforma��es do PR�PRIO grupo
			for (int i = 0; i < transforms_atual.size(); i++){
				g.transfs.push_back(transforms_atual.at(i));
			}
		}
		else{
			// N�o existem transforma��es para acumular (e.g. In�cio do ficheiro ou grupo sem pai(s))
			g = Grupo(transforms_atual);
			g.id = prof;
		}

		// Incluir o novo grupo no vetor de grupos
		grupos.push_back(g);

		// Eliminar todas as transforma��es ATUAIS (renicializar o vetor para um novo grupo)
		transforms_atual = vector <TransformsWrapper>();

		// CRIAR UM MODELO:
		/*1 - Criar modelo*/
		/*1.1 - Associar respetivo array de transforma��es*/
		generateForm(ficheiro_a_ler);
	}

	// Chamada recursiva
	if (element->NextSiblingElement() != NULL){
		readGrupoFromXML(element->NextSiblingElement());
	}
}

/**Parser xml extrai nomes de ficheiros que conteem os tri�ngulos das figuras a desenhar*/
void readFromXML(string filename)
{
	const char * nomeFich = filename.c_str();

	TiXmlDocument doc(nomeFich);
	doc.LoadFile();
	TiXmlHandle docHandle(&doc);

	TiXmlNode * node = docHandle.FirstChild("imagem").ToNode();

	TiXmlElement * element;
	TiXmlElement * aux;

	element = node->FirstChildElement();

	readGrupoFromXML(element);
}

/**Fun��o que testa se um dado ficheiro possu� extens�o .xml*/
bool isXML(string filename)
{
	if (regex_match(filename, regex(".+\.xml"))) return true;
	else{
		cout << ERROR_FORMAT_EXCEPTION;
		return false;
	}
}

/**Verifica se um dado ficheiro existe na diretoria do projeto*/
bool lookUpFile(string filename)
{
	ifstream file(filename);
	if (file){
		return true;
	} else {
		cout << ERROR_FILE_NF;
		return false;
	}
}

/**Fun��o que prepara as figuras para desenhar a partir de uma fonte .xml e invoca o ciclo glut*/
int gerar_cena(vector<string> args)
{
	string filename;
	string form;

	if (args.size() < 2){
		cout << ERROR_NUMBER_ARGS;
		return -1;
	}
	try{
		filename = args.at(1);
		if (isXML(filename) && lookUpFile(filename)){
			printf("a recolher dados para desenhar cena ...\n");
			readFromXML(filename); // Internamente � leitura geramos os modelos enquanto que na 1� etapa
								   // eram gerados nesta mesma fun��o
		
			// Chamada expl�cita para desenhar cena
			prepare_glut();
		}
	}
	catch (invalid_argument ia){
		cout << ERROR_INVALID_ARGS;
	}
	return -1;
}

/** Fun��o que remove os espa�os em branco de uma string*/
string removeSpaces(string input){
	input.erase(remove(input.begin(), input.end(), ' '), input.end());
	return input;
}

/**Comando gerador*/
int gerador(vector<string> args)
{
	string file;

	if (args.size() < 3){
		cout << ERROR_NUMBER_ARGS;
		return -1;
	}
	try{
		if ((args.at(1).compare("rec") == 0) && args.size() == 5){
			file = args.at(4);
			float c = stof(args.at(2));
			float l = stof(args.at(3));

			// Gerar ficheiro com os tri�ngulos
			cout << "a gerar rectangulo ...\n";
			Rectangulo* p = new Rectangulo();
			p->gerarRectangulo(l, c, file);
			cout << "end.\n\n";
		}
		else if ((args.at(1).compare("circ") == 0) && args.size() == 5){
			file = args.at(4);
			float raio = stof(args.at(2));
			float nlados = stof(args.at(3));

			// Gerar ficheiro com os tri�ngulos
			cout << "a gerar circulo ...\n";
			Circulo* c = new Circulo();
			c->gerarCirculo(raio, nlados, file);
			cout << "end.\n\n";
		}
		else if ((args.at(1).compare("tri") == 0) && args.size() == 4){
			file = args.at(3);
			float lado = stof(args.at(2));

			// Gerar ficheiro com os tri�ngulos
			cout << "a gerar triangulo ...\n";
			Triangulo* p = new Triangulo();
			p->gerarTriangulo(lado, file);
			cout << "end.\n\n";
		}
		else if ((args.at(1).compare("paralel") == 0) && args.size() == 6){
			file = args.at(5);
			float l = stof(args.at(2));
			float c = stof(args.at(3));
			float h = stof(args.at(4));

			// Gerar ficheiro com os tri�ngulos
			cout << "a gerar parelelepipedo ...\n";
			Paralelepipedo* p = new Paralelepipedo();
			p->gerarParalelepipedo(l, c, h, file);
			cout << "end.\n\n";
		}
		else if ((args.at(1).compare("esfera") == 0) && args.size() == 6){
			file = args.at(5);
			float raio = stof(args.at(2));
			int fatias = stof(args.at(3));
			int camadas = stoi(args.at(4));

			// Gerar ficheiro com os tri�ngulos
			cout << "a gerar esfera ...\n";
			Esfera* esfera = new Esfera();
			esfera->gerarEsfera(raio, fatias, camadas, file);
			cout << "end.\n\n";
		}
		else if ((args.at(1).compare("cone") == 0) && args.size() == 7){
			file = args.at(6);
			float h = stof(args.at(2));
			float r = stof(args.at(3));
			float nlados = stof(args.at(4));
			float ncamadas = stof(args.at(5));

			// Gerar ficheiro com os tri�ngulos
			cout << "a gerar cone ...\n";
			Cone* c = new Cone();
			c->gerarCone(h, r, nlados, ncamadas, file);
			cout << "end.\n\n";
		}
		else { cout << ERROR_INVALID_ARGS; }
	}
	catch (invalid_argument ia){
		cout << ERROR_INVALID_ARGS;
	}
	return -1;
}

/**Func�o interpretar*/
int interpretar(string linha){
	istringstream ss(linha);
	string token;
	int cont = 0;
	vector<string> playerInfoVector;

	while (std::getline(ss, token, ' ')) {
			token = removeSpaces(token);
			if (token.compare("") != 0){
				playerInfoVector.push_back(token);
				cont++;
			}
	}

	// Comando gerar
	if (playerInfoVector.at(0).compare("gerador") == 0 && cont != 0){
		return gerador(playerInfoVector);
	}
	else if(playerInfoVector.at(0).compare("desenhar") == 0 && cont != 0){
		return gerar_cena(playerInfoVector);
	}
	else if(playerInfoVector.at(0).compare("static_solar") == 0 && cont != 0){
		vector<string> planetas = makeStaticSolarSystem();
		for(int i = 0; i < planetas.size(); i++) interpretar(planetas.at(i));
		playerInfoVector.push_back("static_solar.xml");
		return gerar_cena(playerInfoVector);
	}
	else if (playerInfoVector.at(0).compare("help") == 0 && cont != 0){
		cout << MESSAGE_HELP;
	}
	else if (playerInfoVector.at(0).compare("exit") == 0 && cont != 0){
		return 0;
	}
	else {
		// Como o comando invocado n�o � nenhum dos anteriores, devolve o erro correspondente
		cout << ERROR_COMMAND_NO_EXISTS;
		return -1;
	}
}

/**Fun��o interpretador*/
void interpretador()
{
	int resultado = 0;
	string input = "";
	int ciclo = 1;

	for (printf("$motor3D > "); ciclo && getline(cin, input); printf("$motor3D > ")){
		if (input.compare("") != 0){
			resultado = interpretar(input);
			if (resultado == 0)
				ciclo = 0;
		}
	}
}

/**Fun��o main*/
int main() {
	interpretador();
	return 0;
}