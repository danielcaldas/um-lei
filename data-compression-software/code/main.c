#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "comprimir.h"

int main ()
{
   char op;
   int i;


         printf("\n##########################COMPRESSOR DE DADOS###############################\n");
         printf("                                                         daniel caldas a67691\n\n");
         printf("Escolher uma das opções:\n\n");
         printf(" (1)   Comprimir um ficheiro\n");
         printf(" (2)   Descomprimir um ficheiro\n\n");
         printf(" >");
         i=scanf("%c", &op);
         if(op=='1') comprimir();
         else if(op=='2') descomprimir();
 
         return 0;
}