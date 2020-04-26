<?php

if($_SERVER['REQUEST_METHOD']=='POST'){

    $email = $_POST['email'];
    $senha = $_POST['senha'];

    require_once 'connect.php';

    //COMANDO SQL PARA VERIFICAR SE EXISTE UM EMAIL IGUAL NO BANCO
    $sql = "SELECT * FROM usuarios WHERE email='$email' ";
    
    //RESPOSTA DA CONSULTA SQL
    $response = mysqli_query($conn, $sql, );

    $result = array();
    $result['login'] = array();

    // VERIFICA SE RETORNOU ALGUM REGISTRO    
    if (mysqli_num_rows($response)=== 1){
        $row = mysql_fetch_assoc($response);

        if (password_verify($senha,$row['senha'] )){ // VERIFICA A SENHA DIGITADA COM A SENHA CADASTRADA DO BANCO
            $index['nome'] = $row['nome'];// INDEX RECEBE O NOME QUE ESTA NO BANCO DE DADOS
            $index['email'] = $row['email']// INDEX RECEBE O EMAIL DO BANCO

            array_push($result['login'],$index); // COLOCA O NOME E EMAIL NO RESULTADO E VAI RETORNAR PARA O APP

            $result['sucess'] = "1";
            $result['message'] = "sucess";
            echo jason_encode($result); // EMPACOTA AS INFORMAÇÕES

            mysqli_close($conn);  // FECHA CONEXÃO COM BANCO
        }else{
            $result['sucess'] = "0";
            $result['message'] = "erro";

            echo jason_encode($result);

            mysqli_close($conn);
        }
        
    }
}


?>