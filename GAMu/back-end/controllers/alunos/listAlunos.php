<?php

	include '../../models/Aluno.php';

	$request = $_REQUEST["request"];

	if(Aluno::getTotalAlunos()>0) {
		switch ($request) {
			case 'LIST':
				$tp = Aluno::criarTabelaDeAlunosPaginada();
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
		echo "<div class='callout callout-info' style='margin: 3em;'><h4>:( !</h4><p>Infelizmente ainda não registaste nenhum aluno.</p></div>";
	}
	
?>
