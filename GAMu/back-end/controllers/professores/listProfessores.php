<?php

	include '../../models/Professor.php';

	$request = $_REQUEST["request"];
	// $request = "LIST";

	if(Professor::getTotalProfessores()>0) {
		switch ($request) {
			case 'LIST':
				$tp = Professor::criarTabelaDeProfessoresPaginada();
				$primeira_tabela = file_get_contents(".paginas/pagina_1.html");
				echo $primeira_tabela;
				break;

			default:
				$page = $_REQUEST["pagina"];
				$tabela = file_get_contents(".paginas/pagina_".$page.".html");
				echo $tabela;
				break;
		}
		
	} else {
		echo "<div class='callout callout-info' style='margin: 3em;'><h4>:( !</h4><p>Infelizmente ainda não registaste nenhum professor.</p></div>";
	}
	
?>
