/*
*	Autores: Daniel Caldas, Jos� Cortez, Marcelo Gon�alves, Ricardo Silva
*	Data: 2015.05.29
*	Vers�o: 4.0v
*
*/

#ifndef _circulo_h
#define _circulo_h

#include "formas.h"
#include <vector>
#include <string>
#include <GL/glew.h>
#include <GL/glut.h>

class Circulo : public Forma {
private:
	float raio;
	int nlados;

public:
	Circulo(){}
	void gerarCirculo(float raio, int nlados, string filename);
	void draw();
	virtual void drawVBO() override;
	void criarVBO(string filename) override;
private:
	void write3DtoFile(string filename);
};

#endif
