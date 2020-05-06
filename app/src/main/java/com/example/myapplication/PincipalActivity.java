package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class PincipalActivity extends AppCompatActivity {

    //Criar objetos baseados nos arquivos de layout
    CompoundButton Checkbok;
    ToggleButton toggleButton;
    Switch switchbutton;
    SeekBar seekBar;
    Spinner spinner;
    RadioGroup radioGroup;
    TextView txtValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pincipal);

        // conexão dos componentes com a Classe R
        Checkbok = findViewById(R.id.chkHabilitar);
        toggleButton = findViewById(R.id.tgHabilitar);
        switchbutton = findViewById(R.id.swtHabilitar);
        seekBar = findViewById(R.id.skbValor);
        spinner = findViewById(R.id.spnChaves);
        radioGroup = findViewById(R.id.rgOpcoes);
        txtValor = findViewById(R.id.txtValor);

        configuraSpinner();
        configuraSeekbar();
        configuraSwitch();

        //estabelecer alguns valores padrão

        Checkbok.setChecked(true);
        seekBar.setProgress(15); // BARRA DE 0 A 30
        spinner.setSelection(2);
        spinner.setSelection(2);
        radioGroup.check(R.id.rbOpcao1);

    }//Oncreate
    private void configuraSpinner(){
        String[] chaves = new String[] {"Vendas","Manutenção","Lavação"};
        // AQUI passa as chaves para o Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, chaves);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

    }//ConfiguraSpinner

    private void configuraSeekbar(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                 txtValor.setText(String.valueOf(i)); // MOSTRA VALORES NA BARRA, QUANDO A PESSOA ESTIVER FAZENDO PROGRESSO NA BARRA
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }//ConfiguraSeekbar

    private void configuraSwitch(){
        switchbutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               Checkbok.setEnabled(compoundButton.isChecked());
               toggleButton.setEnabled(b);

            }
        });

    }//ConfiguraSwitch

    public void verValores (View v){
        int idRadioSelecionado = radioGroup.getCheckedRadioButtonId();
        RadioButton radio = findViewById(idRadioSelecionado);

        String Habilitado = Checkbok.isChecked() ? "Hablitado" : "Desabilitado";
        String valor = "valor: " + seekBar.getProgress();
        String chave = "chave: " + spinner.getSelectedItem().toString();
        String opcao = "opcao: " + radio.getText();
        StringBuilder mensagem = new StringBuilder();
        mensagem.append(Habilitado).append("\n");
        mensagem.append(valor).append("\n");
        mensagem.append(chave).append("\n");
        mensagem.append(opcao);

        Toast.makeText(this, mensagem.toString(), Toast.LENGTH_SHORT).show();

    }

}//class
